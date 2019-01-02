package mobi.garden.bottomnavigationtest.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import mobi.garden.bottomnavigationtest.Activity.DetailObatHome;
import mobi.garden.bottomnavigationtest.Activity.WishList;
import mobi.garden.bottomnavigationtest.Model.ModelPromo;
import mobi.garden.bottomnavigationtest.R;
import mobi.garden.bottomnavigationtest.Session.SessionManagement;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.WishViewHolder>{
    public static List<ModelPromo> produkList;
    Context context;
    String tempurl;


    SessionManagement session;
    HashMap<String, String> login;
    public static String CustomerID,memberID, userName;
    String ProductName;

    public WishlistAdapter(List<ModelPromo> produkList, Context context) {
        this.produkList = produkList;
        this.context = context;
    }

    @NonNull
    @Override
    public WishlistAdapter.WishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cv_wishlist,parent,false);
        return new WishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistAdapter.WishViewHolder holder, int position) {
        final ModelPromo mp = produkList.get(position);
        session = new SessionManagement(context);
        login = session.getMemberDetails();
        userName= login.get(SessionManagement.USERNAME);
        memberID = login.get(SessionManagement.KEY_KODEMEMBER);


        holder.tvNamaProduk.setText(mp.getNameProduct());
        tempurl = mp.getProductNameUrl();
        if(tempurl.contains(" ")){
            tempurl = tempurl.replace(" ","%20");
        }
        Picasso.with(context).load(tempurl).into(holder.imgProduct, new Callback() {
            @Override
            public void onSuccess() { Picasso.with(context).load(tempurl).into(holder.imgProduct); }
            @Override
            public void onError() { holder.imgProduct.setImageResource(R.drawable.nopicture); }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete(mp.getProductID(),memberID);
            }
        });

        holder.btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context,DetailObatHome.class);
                i.putExtra("ProductName",mp.getPromoNameProduct());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return produkList.size();
    }

    public static class WishViewHolder extends RecyclerView.ViewHolder{
        ImageView imgProduct,imgDelete;
        Button btnBuy;
        TextView tvNamaProduk;
        public WishViewHolder(View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            tvNamaProduk = itemView.findViewById(R.id.tvNamaProduk);
            btnBuy = itemView.findViewById(R.id.btnBuy);
        }
    }

    public void delete(String product_id,String memberID){
        JSONObject objAdd = new JSONObject();
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
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, "http://pharmanet.apodoc.id/customer/DeleteProductWishList.php", objAdd,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.getString("status").equals("OK")) {
                            //produkList.remove(removedProduct);
                           notifyDataSetChanged();
                           WishList.viewWishList();
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(context,"ERROR FROM SERVER" + error.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

}
