package mobi.garden.bottomnavigationtest.Adapter;

import android.content.Context;
import android.os.AsyncTask;
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
import java.util.List;

import mobi.garden.bottomnavigationtest.Activity.CartActivity;
import mobi.garden.bottomnavigationtest.LoginRegister.User;
import mobi.garden.bottomnavigationtest.LoginRegister.UserLocalStore;
import mobi.garden.bottomnavigationtest.R;
import mobi.garden.bottomnavigationtest.Model.cart;

public class cart_adapter2 extends  RecyclerView.Adapter<cart_adapter2.cartViewHolder> {

    Context context;
    List<cart> cartList;
    int userID;
    String CustomerID;
    int hargaProduct;
    static DecimalFormat df;
    boolean isStoppedClicked = true;

    UserLocalStore userLocalStore;
    public cart_adapter2(Context context, List<cart> cartList, int userID) {
        this.context = context;
        this.cartList = cartList;
        this.userID = userID;
    }

    @Override
    public cart_adapter2.cartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list_row2, parent, false);
        return new cart_adapter2.cartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(cart_adapter2.cartViewHolder holder, int position) {
        final cart product = cartList.get(position);
        hargaProduct = product.getCartProductPrice() * product.getCartProductQty();


        userLocalStore  = new UserLocalStore(context);
        User currUser = userLocalStore.getLoggedInUser();
        CustomerID = currUser.getUserID();

        df = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setCurrencySymbol("Rp. ");
        dfs.setMonetaryDecimalSeparator(',');
        dfs.setGroupingSeparator('.');
        df.setDecimalFormatSymbols(dfs);
        df.setMaximumFractionDigits(0);


        holder.tvCartProductName.setText(product.getProductName());
        holder.tvCartProductPrice.setText(df.format(hargaProduct)+"");
        holder.edtQty.setText(product.getCartProductQty() + "");

        holder.edtQty.setOnEditorActionListener(new DoneOnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (holder.edtQty.getText().toString().equals("")){
                        holder.edtQty.setText("0");
                        holder.tvCartProductPrice.setText("0");
                    }
                    else if(Integer.parseInt(holder.edtQty.getText().toString())>product.outletProductStockQty){
                        product.cartProductQty = product.outletProductStockQty;
                        holder.edtQty.setText(product.cartProductQty+"");
                        hargaProduct= product.outletProductStockQty * product.cartProductPrice;
                        holder.tvCartProductPrice.setText(df.format(hargaProduct)+"");

                        Toast.makeText(context, "Stock barang hanya "+  product.outletProductStockQty, Toast.LENGTH_SHORT).show();
                        ubah(cartList.get(position).productID, product.cartProductQty, Integer.parseInt(CustomerID));
                    }
                    else {
                        product.cartProductQty = Integer.parseInt(holder.edtQty.getText().toString());
                        hargaProduct= product.cartProductQty * product.cartProductPrice;
                        holder.tvCartProductPrice.setText(df.format(hargaProduct)+"");

                        ubah(cartList.get(position).productID, product.cartProductQty, Integer.parseInt(CustomerID));
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
                    hargaProduct = product.cartProductQty * product.cartProductPrice;
                    holder.tvCartProductPrice.setText(df.format(hargaProduct)+"");

                    if(isStoppedClicked){
                        isStoppedClicked=false;
                        new cart_adapter2.UpdateDataIncrease().execute(product);
                    }
                    notifyDataSetChanged();

                }
                notifyDataSetChanged();
            }
        });

        holder.btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("qtynya",product.cartProductQty+"");

                if (product.cartProductQty==0) {
                    Log.d("prdId",cartList.get(position).productID+"");
                    delete(cartList.get(position).productID, cartList.get(position),Integer.parseInt(CustomerID));
                }else {
                    holder.edtQty.setText(--product.cartProductQty+"");
                    hargaProduct = product.cartProductQty * product.cartProductPrice;
                    holder.tvCartProductPrice.setText(df.format(hargaProduct)+"");

                    if(isStoppedClicked){
                        isStoppedClicked=false;
                        new cart_adapter2.UpdateDataDecrease().execute(product);
                    }
                    notifyDataSetChanged();
                }
                notifyDataSetChanged();
            }
        });
    }

    private class UpdateDataIncrease extends AsyncTask<cart,cart,Void> {

        @Override
        protected Void doInBackground(cart... carts) {
            cart product =carts[0];
            ubah(product.productID, product.cartProductQty, Integer.parseInt(CustomerID));
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


    class DoneOnEditorActionListener implements TextView.OnEditorActionListener {
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

    private class UpdateDataDecrease extends AsyncTask<cart,cart,Void> {

        @Override
        protected Void doInBackground(cart... carts) {
            cart product = carts[0];
            ubah(product.productID, product.cartProductQty, Integer.parseInt(CustomerID));
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
            edtQty = itemView.findViewById(R.id.edtQty);

        }

    }


    public void ubah(String id, int qty, int CustomerID) {
        JSONObject objAdd = new JSONObject();
        try {
            JSONArray arrData = new JSONArray();
            JSONObject objDetail = new JSONObject();
            objDetail.put("Id_Product", id);
            objDetail.put("Qty", qty);
            objDetail.put("CustomerID",CustomerID);
            arrData.put(objDetail);
            objAdd.put("data", arrData);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, "http://pharmanet.apodoc.id/updateCartCustomer.php", objAdd,
                new Response.Listener<JSONObject>() {
                    @Override

                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("OK")) {
                                CartActivity.refresh_total_cart(cartList);
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


    public void delete (final String product_id, final cart removedProduct, final int CustomerID){

        JSONObject objAdd = new JSONObject();
        try {
            JSONArray arrData = new JSONArray();
            JSONObject objDetail = new JSONObject();
            objDetail.put("ProductID", product_id);
            objDetail.put("CustomerID", CustomerID);
            arrData.put(objDetail);
            objAdd.put("data", arrData);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, "http://pharmanet.apodoc.id/deleteCartCustomer.php", objAdd,
                new Response.Listener<JSONObject>() {
                    @Override

                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("OK")) {

                                cartList.remove(removedProduct);
                                CartActivity.refresh_cart(cartList);
                                //Toast.makeText(context, cartList.size()+"", Toast.LENGTH_SHORT).show();
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
