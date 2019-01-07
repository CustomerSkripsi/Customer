package mobi.garden.bottomnavigationtest.Adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.List;

import mobi.garden.bottomnavigationtest.Activity.CartActivity;
import mobi.garden.bottomnavigationtest.Model.ModelPromo;
import mobi.garden.bottomnavigationtest.R;
import mobi.garden.bottomnavigationtest.Session.SessionManagement;

public class CartHomeAdapter extends  RecyclerView.Adapter<CartHomeAdapter.cartViewHolder> {
    Context context;
    List<ModelPromo> CartModel;
    int hargaProduk;
    boolean isStoppedClicked = true;
    SessionManagement session;
    HashMap<String, String> login;
    String memberID, userName;
    DecimalFormat df;

    public CartHomeAdapter(List<ModelPromo> CartModel, Context context) {
        this.CartModel = CartModel;
        this.context = context;
    }

    @NonNull
    @Override
    public CartHomeAdapter.cartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list_row2, parent, false);
        return new CartHomeAdapter.cartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartHomeAdapter.cartViewHolder holder, int position) {
        final ModelPromo carts = CartModel.get(position);
        session = new SessionManagement(context);
        login = session.getMemberDetails();
        userName= login.get(SessionManagement.USERNAME);
        memberID = login.get(SessionManagement.KEY_KODEMEMBER);
      //  Toast.makeText(context, ""+memberID, Toast.LENGTH_SHORT).show();


        holder.tvCartProductName.setText(carts.getNameProduct());
        hargaProduk = carts.getPriceProduct() * carts.getProductQty();
        holder.tvCartProductPrice.setText(String.valueOf(ConvertNominal(hargaProduk)));

        holder.edtQty.setText(String.valueOf(carts.getProductQty()));
        holder.edtQty.setOnEditorActionListener(new DoneOnEditorActionListener(){
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (holder.edtQty.getText().toString().equals("")||Integer.parseInt(holder.edtQty.getText().toString())==0) {
                        holder.edtQty.setText("1");
                        carts.cartProductQty = 1;
                        ubah(CartModel.get(position).ProductID, carts.cartProductQty,memberID);
                    }
                   else if(Integer.parseInt(holder.edtQty.getText().toString())>100){
                       int ProductQty = 100;
                       holder.edtQty.setText(ProductQty+"");
                        Toast.makeText(context, "Maximum pembelian 100", Toast.LENGTH_SHORT).show();
                        holder.tvCartProductPrice.setText("Total : "+String.valueOf(carts.ProductQty*100));
                        ubah(CartModel.get(position).getProductID(), carts.ProductQty, memberID);
                   }
                    else {
                        carts.ProductQty = Integer.parseInt(holder.edtQty.getText().toString());
                        ubah(CartModel.get(position).getProductID(), carts.ProductQty, memberID);
                        holder.tvCartProductPrice.setText(""+String.valueOf(ConvertNominal(carts.PriceProduct*carts.ProductQty)));
                    }
                    InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
        
        holder.btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (carts.getProductQty()==1) {
                    delete(CartModel.get(position).ProductID, CartModel.get(position),memberID);

                }else{
                    holder.edtQty.setText(--carts.ProductQty + "");
                    hargaProduk = carts.getPriceProduct() * carts.getProductQty();
                    holder.tvCartProductPrice.setText(String.valueOf(hargaProduk));
                    if (isStoppedClicked) {
                        isStoppedClicked = false;
                        new CartHomeAdapter.UpdateDataDecrease().execute(carts);
                    }
                    notifyDataSetChanged();
                }
                notifyDataSetChanged();
            }
        });

        holder.btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(carts.getProductQty()>=100){
                    Toast.makeText(context, "Maximum pembelian 100 pack", Toast.LENGTH_SHORT).show();
                }else {
                    holder.edtQty.setText(++carts.ProductQty + "");
                    hargaProduk = carts.getPriceProduct() * carts.getProductQty();
                    holder.tvCartProductPrice.setText(String.valueOf(hargaProduk));
                    if (isStoppedClicked) {
                        isStoppedClicked = false;
                        new CartHomeAdapter.UpdateDataIncrease().execute(carts);
                    }
                }
                notifyDataSetChanged();
            }
        });

//        if(carts.getStockProductQty() < Integer.parseInt(holder.edtQty.getText().toString())){ }

    }

    @Override
    public int getItemCount() {
        return CartModel.size();
    }

    public static class cartViewHolder extends RecyclerView.ViewHolder{
        TextView tvCartProductName, tvCartProductPrice, edtQty;
        ImageView btnDecrease,btnIncrease;
        public cartViewHolder(View itemView) {
            super(itemView);
            tvCartProductName = itemView.findViewById(R.id.tvCartProductName);
            tvCartProductPrice = itemView.findViewById(R.id.tvCartProductPrice);
            edtQty = itemView.findViewById(R.id.edtQty);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
        }
    }

    private class UpdateDataDecrease extends AsyncTask<ModelPromo,ModelPromo,Void >{
        @Override
        protected Void doInBackground(ModelPromo... modelPromos) {
            ModelPromo product = modelPromos[0];
            ubah(product.ProductID, product.ProductQty, memberID);//Integer.parseInt(CustomerID)
            //Log.d("TAGGG", "doInBackground: "+product.ProductQty);
            return null;
        }
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            isStoppedClicked=true;
            //Log.d("TAGG","do in background kelar");
        }
    }

    private class UpdateDataIncrease extends AsyncTask<ModelPromo,ModelPromo,Void >{
        @Override
        protected Void doInBackground(ModelPromo... modelPromos) {
            ModelPromo product = modelPromos[0];
            ubah(product.ProductID, product.ProductQty, memberID);//Integer.parseInt(CustomerID)
            return null;
        }
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            isStoppedClicked=true;
            //Log.d("TAGG","do in background kelar");
        }
    }



    public void ubah(String id, int qty, String CustomerID) {
        JSONObject objAdd = new JSONObject();
        try {
            JSONArray arrData = new JSONArray();
            JSONObject objDetail = new JSONObject();
            objDetail.put("Id_Product", id);
            objDetail.put("Qty", qty);
            objDetail.put("CustomerID",CustomerID);
            arrData.put(objDetail);
            objAdd.put("data", arrData);
            Log.d("asd", "ubah: "+objAdd);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, "http://pharmanet.apodoc.id/customer/UpdateCart.php", objAdd,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("OK")) {
                                CartActivity.refresh_total_cart(CartModel);
//                                CartActivity.refresh_cart(CartModel);
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void delete (final String product_id, final ModelPromo removedProduct, final String CustomerID){
        JSONObject objAdd = new JSONObject();
        try {
            JSONArray arrData = new JSONArray();
            JSONObject objDetail = new JSONObject();
            objDetail.put("ProductID", product_id);
            objDetail.put("CustomerID", memberID);
            arrData.put(objDetail);
            objAdd.put("data", arrData);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, "http://pharmanet.apodoc.id/customer/DeleteCartCustomer.php", objAdd,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("OK")) {
                                CartModel.remove(removedProduct);


                                //CartActivity.refresh_cart(CartModel);
                                //CartActivity.refresh_total_cart(CartModel);
                                CartActivity.refresh_total_cart(CartModel);
                                notifyDataSetChanged();
                        }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(context,"ERROR FROM SERVER" + error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public static String ConvertNominal(double input){
        DecimalFormat df = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setCurrencySymbol("Rp. ");
        dfs.setMonetaryDecimalSeparator(',');
        dfs.setGroupingSeparator('.');
        df.setDecimalFormatSymbols(dfs);
        df.setMaximumFractionDigits(0);
        String hsl = df.format(input);

        return hsl;
    }

    private class DoneOnEditorActionListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            return false;
        }
    }
}



//new CartHomeAdapter.delete().execute(carts);
//                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                    builder.setTitle("Konfirmasi Hapus Product "+carts.getNameProduct()+" dari keranjang");
//                    builder.setMessage("Apakah anda yakin?");
//                    builder.setCancelable(false);
//                    builder.setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                        }
//                    });
//                    builder.setPositiveButton("YA", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Log.d("prdId",carts.ProductID+"");
//                            delete(CartModel.get(position).ProductID, CartModel.get(position),"8181200006");
//                        }
//                    });
//                    AlertDialog alert = builder.create();
//                    alert.show();