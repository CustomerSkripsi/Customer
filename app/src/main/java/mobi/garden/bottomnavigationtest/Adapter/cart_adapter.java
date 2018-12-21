package mobi.garden.bottomnavigationtest.Adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
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

import mobi.garden.bottomnavigationtest.Activity.CartApotekActivity;
import mobi.garden.bottomnavigationtest.Activity.PromoSelengkapnyaActivity;
import mobi.garden.bottomnavigationtest.LoginRegister.UserLocalStore;
import mobi.garden.bottomnavigationtest.Model.obat;
import mobi.garden.bottomnavigationtest.R;
import mobi.garden.bottomnavigationtest.Session.SessionManagement;

public class cart_adapter extends RecyclerView.Adapter<cart_adapter.cartViewHolder>{

    Context context;
    public static List<obat> cartList;

    AlertDialog dialog;
    AlertDialog.Builder builder;

    //int userID;
    //String CustomerID;
    boolean isStoppedClicked = true;
    DecimalFormat df;
    //session_obat session;
    UserLocalStore userLocalStore;


    //login
    SessionManagement session;
    HashMap<String, String> login;
    public static String CustomerID,memberID, userName;
    String ProductName;

    public cart_adapter(Context context, List<obat> cartList, String productName) {
        this.context = context;
        this.cartList = cartList;
        this.memberID = CustomerID;
        ProductName = productName;
        session = new SessionManagement(context);
    }
    public cart_adapter(Context context, List<obat> cartList) {
        this.context = context;
        this.cartList = cartList;
        this.memberID = CustomerID;
        session = new SessionManagement(context);
    }

    public void setCartList(List<obat> cartList) {
        this.cartList = cartList;
    }

    public void setProductList(Context context) {
        this.context = context;
    }

    @Override
    public cartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list_row, parent, false);

        return new cartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(cartViewHolder holder, int position) {

        session = new SessionManagement(context);
        login = session.getMemberDetails();
        userName= login.get(SessionManagement.USERNAME);
        memberID = login.get(SessionManagement.KEY_KODEMEMBER);
        obat ob;
        final obat product = cartList.get(position);

        df = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setCurrencySymbol("Rp. ");
        dfs.setMonetaryDecimalSeparator(',');
        dfs.setGroupingSeparator('.');
        df.setDecimalFormatSymbols(dfs);
        df.setMaximumFractionDigits(0);

        holder.tvCartProductName.setText(product.getProductName());
//        holder.tvOutletStock.setText(product.getOutletProductStockQty() +"");
        holder.tvCartProductPrice.setText(df.format(product.getCartProductPrice()) + "");
        holder.edtQty.setText(product.cartProductQty+ "");
//        userLocalStore  = new UserLocalStore(context);
//        User currUser = userLocalStore.getLoggedInUser();
//        CustomerID = currUser.getUserID();
//


        holder.edtQty.setOnEditorActionListener(new DoneOnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (holder.edtQty.getText().toString().equals("")||Integer.parseInt(holder.edtQty.getText().toString())==0) {
                        holder.edtQty.setText("1");
                        product.cartProductQty = 1;
                        ubah(cartList.get(position).productID, product.cartProductQty,memberID);
                    }
                    else if(Integer.parseInt(holder.edtQty.getText().toString())>product.outletProductStockQty){
                        product.cartProductQty = product.outletProductStockQty;
                        holder.edtQty.setText(product.cartProductQty+"");
                        Toast.makeText(context, "Stock barang hanya "+  product.outletProductStockQty, Toast.LENGTH_SHORT).show();
                        ubah(cartList.get(position).productID, product.cartProductQty, memberID);
                    }
                    else {
                        product.cartProductQty = Integer.parseInt(holder.edtQty.getText().toString());
                        ubah(cartList.get(position).productID, product.cartProductQty, memberID);
                    }
                    InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });

        holder.btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(product.cartProductQty >= product.outletProductStockQty){
                    Toast.makeText(context, "Stock barang hanya "+  product.outletProductStockQty, Toast.LENGTH_SHORT).show();
                }
                else{
                    holder.edtQty.setText(++product.cartProductQty+"");
                    Log.d("qtynya",product.cartProductQty+"");
                    //ubah(cartList.get(position).productID, ++cartList.get(position).cartProductQty, Integer.parseInt(CustomerID));
                    if(isStoppedClicked){
                        isStoppedClicked=false;
                        new UpdateDataIncrease().execute(product);
                    }
                    notifyDataSetChanged();
                }
            }
        });

        holder.btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("qtynya",product.cartProductQty+"");
//                if (product.cartProductQty==1) {/
//                    builder = new AlertDialog.Builder(context);
////                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                    builder.setTitle("Konfirmasi Hapus Product "+product.getProductName()+" dari keranjang");
//                    builder.setMessage("Apakah anda yakin?");
//                    builder.setCancelable(false);
//
//                    builder.setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                        }
//                    });
//
//                    builder.setPositiveButton("YA", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
////                            Log.d("prdId",product.productId+"");
//                            delete(cartList.get(position).productID, cartList.get(position),memberID);
//                        }
//                    });
//                    dialog = builder.show();
                if (product.cartProductQty==0) {
                    Log.d("prdId",cartList.get(position).productID+"");
                    delete(cartList.get(position).productID, cartList.get(position),memberID);
                   if(ProductName != product.getProductName()){
                       Toast.makeText(context, "tidak ada", Toast.LENGTH_SHORT).show();
                   }
                    if(CartApotekActivity.temp == product.getProductName()){
                        Log.d("gak tauuu", "onBindViewHolder:"+CartApotekActivity.temp);
                    }

                }else {
                    //ubah(cartList.get(position).productID, --cartList.get(position).cartProductQty,Integer.parseInt(CustomerID));
                    holder.edtQty.setText(--product.cartProductQty+"");
                    if(isStoppedClicked){
                        isStoppedClicked=false;
                        new UpdateDataDecrease().execute(product);
                    }
                    notifyDataSetChanged();
                }
                notifyDataSetChanged();
            }
        });
    }

    private class UpdateDataIncrease extends AsyncTask<obat,obat,Void> {
        @Override
        protected Void doInBackground(obat... obats) {
            obat product =obats[0];
            ubah(product.productID, product.cartProductQty, memberID);
            //Log.d("TAGGG", "doInBackground: "+product.cartProductQty);
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            isStoppedClicked=true;
            //Log.d("TAGG","do in background kelar");
        }
    }

    private class DoneOnEditorActionListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return true;
            }
            return false;
        }
    }

    private class UpdateDataDecrease extends AsyncTask<obat,obat,Void> {

        @Override
        protected Void doInBackground(obat... obats) {
            obat product = obats[0];
            ubah(product.productID, product.cartProductQty, memberID);
            //Log.d("TAGGG", "doInBackground: "+product.cartProductQty);

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            isStoppedClicked=true;
            //Log.d("TAGG","do in background kelar");
        }
    }

    @Override
    public int getItemCount() {
        if (cartList.size() == 0) {
            return 0;
        } else {
            return cartList.size();
        }
    }

    public static class cartViewHolder extends RecyclerView.ViewHolder {
        ImageButton btnIncrease;
        ImageButton btnDecrease;
        TextView tvCartProductName;
        TextView tvCartProductPrice;
        TextView tvOutletStock;
        EditText edtQty;
        public cartViewHolder(View itemView) {
            super(itemView);
            btnIncrease =  itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            tvCartProductName = itemView.findViewById(R.id.tvCartProductName);
            tvCartProductPrice = itemView.findViewById(R.id.tvCartProductPrice);
//            tvOutletStock = itemView.findViewById(R.id.tvOutletStock);

            edtQty = itemView.findViewById(R.id.edtQty);
        }
    }

    public void ubah(String id, int qty , String memberID) {
        JSONObject objAdd = new JSONObject();
        try {
            JSONArray arrData = new JSONArray();
            JSONObject objDetail = new JSONObject();
            objDetail.put("Id_Product", id);
            objDetail.put("Qty", qty);
            objDetail.put("CustomerID",memberID);
            arrData.put(objDetail);
            objAdd.put("data", arrData);
            Log.d("asd", "ubah: "+objAdd);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, "http://pharmanet.apodoc.id/updateCartCustomer.php", objAdd,
                new Response.Listener<JSONObject>() {
                    @Override

                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("OK")) {
                                CartApotekActivity.refresh_total_cart(cartList);
//                                PromoSelengkapnyaActivity.refresh_total_cart(cartList);
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


    public void delete (final String product_id, final obat removedProduct, final String memberID){

        JSONObject objAdd = new JSONObject();
        Log.d("testapus2", memberID);
        try {
            JSONArray arrData = new JSONArray();
            JSONObject objDetail = new JSONObject();
            objDetail.put("ProductID", product_id);
            objDetail.put("CustomerID", memberID);
            arrData.put(objDetail);
            objAdd.put("data", arrData);
            Log.d("testapus", arrData.toString());

        } catch (JSONException e1) {
            e1.printStackTrace();
        }


        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, "http://pharmanet.apodoc.id/customer/deleteCartCustomer.php", objAdd,
                new Response.Listener<JSONObject>() {
                    @Override

                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("OK")) {

                                cartList.remove(removedProduct);
                                notifyDataSetChanged();

                                //Toast.makeText(context, cartList.size()+"", Toast.LENGTH_SHORT).show();
//                                CartApotekActivity.initiateTopAdapter();
//                                CartApotekActivity.refresh_cart(cartList,removedProduct);
                                CartApotekActivity.refresh_cart(cartList);
                                CartApotekActivity.showprodukterkait();

//                                PromoSelengkapnyaActivity.refresh_cart(cartList);

                                Toast.makeText(context, "terhapus", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(context,"ERROR FROM SERVER" + error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}

