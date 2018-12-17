package mobi.garden.bottomnavigationtest.Adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.List;

import mobi.garden.bottomnavigationtest.Model.ModelPromo;
import mobi.garden.bottomnavigationtest.R;

public class CartHomeAdapter extends  RecyclerView.Adapter<CartHomeAdapter.cartViewHolder> {
    Context context;
    List<ModelPromo> CartModel;
    int hargaProduk;
    boolean isStoppedClicked = true;

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
        holder.tvCartProductName.setText(carts.getNameProduct());
        hargaProduk = carts.getPriceProduct() * carts.getProductQty();
        holder.tvCartProductPrice.setText(String.valueOf(hargaProduk));
        holder.edtQty.setText(String.valueOf(carts.getProductQty()));
        
        holder.btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("qtynya",carts.getProductQty()+"");
                holder.edtQty.setText(--carts.ProductQty+"");
                hargaProduk = carts.getPriceProduct() * carts.getProductQty();
                holder.tvCartProductPrice.setText(String.valueOf(hargaProduk));
                if(isStoppedClicked){
                    isStoppedClicked=false;
                    new CartHomeAdapter.UpdateDataDecrease().execute(carts);
                }
                notifyDataSetChanged();
            }
        });
        holder.btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.edtQty.setText(++carts.ProductQty+"");
                hargaProduk = carts.getPriceProduct() * carts.getProductQty();
                holder.tvCartProductPrice.setText(String.valueOf(hargaProduk));
                if(isStoppedClicked){
                    isStoppedClicked=false;
                    new CartHomeAdapter.UpdateDataIncrease().execute(carts);
                    Toast.makeText(context, "haii", Toast.LENGTH_SHORT).show();
                }
            }
        });


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
            ubah(product.ProductID, product.ProductQty, "8181200006");//Integer.parseInt(CustomerID)
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
            ubah(product.ProductID, product.ProductQty, "8181200006");//Integer.parseInt(CustomerID)
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
                            Log.d("onResponse: ", "masuk1");
                            if (response.getString("status").equals("OK")) {
                               // CartActivity.refresh_total_cart(cartList);
                                Log.d("onResponse: ", "masuk2");
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


}
