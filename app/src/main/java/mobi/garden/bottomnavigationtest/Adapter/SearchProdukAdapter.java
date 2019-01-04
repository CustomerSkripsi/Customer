package mobi.garden.bottomnavigationtest.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import mobi.garden.bottomnavigationtest.Activity.SearchProduk;
import mobi.garden.bottomnavigationtest.Model.ModelPromo;
import mobi.garden.bottomnavigationtest.R;
import mobi.garden.bottomnavigationtest.Session.SessionManagement;

public class SearchProdukAdapter extends RecyclerView.Adapter<SearchProdukAdapter.SearchProdukViewHolder> {
    List<ModelPromo> mp;
    Context context;
    private Boolean isClicked = false;
    SessionManagement session;
    HashMap<String, String> login;
    public static String CustomerID,memberID, userName;
    String ProductName;

    public SearchProdukAdapter(List<ModelPromo> mp, Context context) {
        this.mp = mp;
        this.context = context;
    }


    @NonNull
    @Override
    public SearchProdukAdapter.SearchProdukViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cv_search_product,parent,false);
        return new SearchProdukAdapter.SearchProdukViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchProdukAdapter.SearchProdukViewHolder holder, int position) {
        final ModelPromo m = mp.get(position);
        session = new SessionManagement(context);
        login = session.getMemberDetails();
        userName= login.get(SessionManagement.USERNAME);
        memberID = login.get(SessionManagement.KEY_KODEMEMBER);

        holder.tvNama.setText(m.getPromoNameProduct());
        Picasso.with(context).load(m.getProductNameUrl()).into(holder.imgProduct, new Callback() {
            @Override
            public void onSuccess() { }
            @Override
            public void onError() {
                holder.imgProduct.setImageResource(R.drawable.nopicture);
            }
        });
        holder.tvNama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,DetailObatHome.class);
                i.putExtra("ProductName",m.getPromoNameProduct());
                i.putExtra("ProductImage",m.getProductNameUrl());
                context.startActivity(i);
            }
        });

        if(!m.getCustomerID().equalsIgnoreCase("0")){ //jika tidak 0
            holder.ivStar.setColorFilter(Color.parseColor(  "#DC143C"));
            holder.ivStar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(m.getCustomerID().equalsIgnoreCase("0") || !isClicked){
                        holder.ivStar.setColorFilter(Color.parseColor("#0Dffffff"));
                        isClicked = !isClicked; //membuat dia jadi hitam klo ada merah
                        deletewishList(m.getProductID(),memberID);
                    }
//                    else {
//                        holder.ivStar.setColorFilter(Color.parseColor("#DC143C"));
//                        //isClicked = !isClicked; // membuat dia merah klo ada hitam
//                        Log.d("atas1", "eaea");
//                        //insertWishList(m.getProductID(),memberID);
//                    }
                }
            });
        }else{
            holder.ivStar.setColorFilter(Color.parseColor(  "#0Dffffff"));
            holder.ivStar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {// bwh bener
//                    if(!m.getCustomerID().equalsIgnoreCase("0") || !isClicked){
//                        holder.ivStar.setColorFilter(Color.parseColor("#DC143C"));
//                        isClicked = !isClicked;
//                        deletewishList(m.getProductID(),memberID);
//                    }
//                    else {
                        if(m.getCustomerID().equalsIgnoreCase("0") || !isClicked){
                        holder.ivStar.setColorFilter(Color.parseColor("#0Dffffff"));
                        isClicked = !isClicked;
                        insertWishList(m.getProductID(),memberID);
                    }
                }
            });
        }




//        holder.llproduk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(context, DetailObatHome.class);
//                i.putExtra("ProductName",m.getPromoNameProduct());
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return mp.size();
    }

    public class SearchProdukViewHolder extends RecyclerView.ViewHolder{
        TextView tvNama;
        ImageView imgProduct,ivStar;
        LinearLayout llproduk;
        public SearchProdukViewHolder(View itemView){
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvnama);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            llproduk = itemView.findViewById(R.id.llproduk);
            ivStar = itemView.findViewById(R.id.ivStar);
        }
    }

    public void deletewishList(String product_id,String memberID){
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
                                notifyDataSetChanged();
                                SearchProduk.showhasilproduk();
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


    public void insertWishList(String product_id,String memberID){
        JSONObject objAdd = new JSONObject();
        try {
            JSONArray arrData = new JSONArray();
            JSONObject objDetail = new JSONObject();
            objDetail.put("ProductID", product_id);
            objDetail.put("CustomerID", memberID);
            arrData.put(objDetail);
            objAdd.put("data", arrData);
            Log.d("testinsert", arrData.toString());
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, "http://pharmanet.apodoc.id/customer/insertProductWishList.php", objAdd,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("OK")) {
                                notifyDataSetChanged();
                                SearchProduk.showhasilproduk();
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,"ERROR FROM SERVER" + error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

}
