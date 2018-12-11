package mobi.garden.bottomnavigationtest.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

import mobi.garden.bottomnavigationtest.CONFIG;
import mobi.garden.bottomnavigationtest.LoginRegister.User;
import mobi.garden.bottomnavigationtest.LoginRegister.UserLocalStore;
import mobi.garden.bottomnavigationtest.Model.obat;
import mobi.garden.bottomnavigationtest.R;
//import mobi.garden.bottomnavigationtest.R;

public class ProdukApotekAdapter extends RecyclerView.Adapter<ProdukApotekAdapter.viewObatHolder> {
    List<obat> obatlist;
    List<obat> cartlist;
    Context context;
    User curruser;
    String CustomerID;
    static DecimalFormat df;

    UserLocalStore userLocalStore;
    public ProdukApotekAdapter(Context c, List<obat> obat, List<obat> cartlist) {
        this.obatlist = obat;
        this.context = c;
        this.cartlist = cartlist;
    }

    public ProdukApotekAdapter(Context c, List<obat> obatlist) {
        this.obatlist = obatlist;
        this.context = c;
    }


    @Override
    public ProdukApotekAdapter.viewObatHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.cv_product_apotik, viewGroup, false);

        return new ProdukApotekAdapter.viewObatHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProdukApotekAdapter.viewObatHolder holder, final int position) {
        final obat pr = obatlist.get(position);

        if(pr.productPhoto.equalsIgnoreCase("null")){
            Picasso.with(context).load("http://www.pharmanet.co.id/images/logo.png").into(holder.iv_foto_obat);
        }else {
            Picasso.with(context).load(pr.productPhoto).into(holder.iv_foto_obat);
        }

        holder.tv_nama_obat.setText(pr.productName);
        holder.tv_harga_obat.setText(CONFIG.ConvertNominal(pr.outletProductPrice));
        holder.btn_addObat.setEnabled(true);
        holder.btn_addObat.setBackgroundResource(R.drawable.btn_unclicked_home);


        if(pr.outletProductStockQty != 0 ){
            holder.btn_addObat.setEnabled(true);
            holder.btn_addObat.setBackgroundResource(R.drawable.btn_unclicked_home);
        }else{
            holder.tv_harga_obat.setVisibility(View.GONE);
            holder.tv_harga_obat.setText("Produk ini telah habis");
            holder.tv_harga_obat.setTextColor(ContextCompat.getColor(context, R.color.red));
            holder.tv_harga_obat.setTypeface(null, Typeface.BOLD_ITALIC);
            holder.tv_harga_obat.setTextSize(10);
            holder.btn_addObat.setEnabled(false);
            holder.btn_addObat.setBackgroundResource(R.drawable.btn_beli_clicked);
        }


        for(int j=0;j<obatlist.size();j++){
            if(pr.productID.equals(obatlist.get(j).productID)){
                holder.btn_addObat.setEnabled(false);
                holder.btn_addObat.setBackgroundResource(R.drawable.btn_beli_clicked);
                break;
            }
        }


        holder.btn_addObat.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                add(pr.productID,pr.productName, pr.outletProductStockQty,pr.outletProductPrice,"1",curruser.userID, curruser.outletID);
                holder.btn_addObat.setEnabled(false);
                holder.btn_addObat.setBackgroundResource(R.drawable.btn_beli_clicked);
                
            }
        });

    }



    public void add(String productID, String productName,int cartProductQty ,int outletProductPrice, String userID, String userID1, String outletID) {
        JSONObject objAdd = new JSONObject();
        try {
            JSONArray arrData = new JSONArray();
            JSONObject objDetail = new JSONObject();
            objDetail.put("ProductName", productName);
            objDetail.put("Price", outletProductPrice);
            objDetail.put("cartProductQty", cartProductQty);
            objDetail.put("userID", userID);
            objDetail.put("UpdatedBy",userID);
            objDetail.put("ProductID", productID);
            arrData.put(objDetail);
            objAdd.put("data", arrData);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, "http://pharmanet.apodoc.id/customer/addCartApotik.php", objAdd,
                new Response.Listener<JSONObject>() {
                    @Override

                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("OK")) {
                              //  CartApotekActivity.initiateBelowAdapter();
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
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }


    @Override
    public int getItemCount() {
        return obatlist.size();
    }

    public static class viewObatHolder extends RecyclerView.ViewHolder {

        int i=0;
        ImageView iv_foto_obat;
        LinearLayout ll_cv_product_apotik;
        TextView tv_nama_obat, tv_qty_obat, tv_harga_obat;
        Button btn_addObat;

        public viewObatHolder(View v) {
            super(v);

            iv_foto_obat=(ImageView) v.findViewById(R.id.iv_foto_obat);
            ll_cv_product_apotik = (LinearLayout) v.findViewById(R.id.ll_cv_product_apotik);
            tv_nama_obat= (TextView) v.findViewById(R.id.tv_nama_obat);
            tv_qty_obat = (TextView) v.findViewById(R.id.tv_qty_obat);
            tv_harga_obat =(TextView) v.findViewById(R.id.tv_harga_obat);
            btn_addObat =(Button)v.findViewById(R.id.btnDecrease);
        }
    }




}
