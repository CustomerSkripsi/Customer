package mobi.garden.bottomnavigationtest.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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

import mobi.garden.bottomnavigationtest.Activity.CartApotekActivity;
import mobi.garden.bottomnavigationtest.LoginRegister.User;
import mobi.garden.bottomnavigationtest.LoginRegister.UserLocalStore;
import mobi.garden.bottomnavigationtest.Model.apotek;
import mobi.garden.bottomnavigationtest.Model.session_obat;
import mobi.garden.bottomnavigationtest.R;

/**
 * Created by ASUS on 5/8/2018.
 */

public class apotek_adapter extends RecyclerView.Adapter<apotek_adapter.apotekViewHolder> {

    List<apotek> apoteklist;
    Context context;
    session_obat session;

    String CustomerID,productName;
//    UserLocalStore userLocalStore;
    DecimalFormat df;

    Activity activity;

    double userLong;
    double userlat;

    public apotek_adapter(Context c, List<apotek> apoteklist,double longitude,double latitude) {
        this.apoteklist = apoteklist;
        this.context = c;
        session = new session_obat(c);
        userLong = longitude;
        userlat = latitude;
    }


    public apotek_adapter(Context c, List<apotek> apoteklist, Activity activity) {
        this.apoteklist = apoteklist;
        this.context = c;
        session = new session_obat(c);
        this.activity = activity;
    }

    @Override
    public apotek_adapter.apotekViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.cv_apotek, viewGroup, false);

        return new apotek_adapter.apotekViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(apotek_adapter.apotekViewHolder holder, int position) {
        final apotek pr = apoteklist.get(position);
//        userLocalStore = new UserLocalStore(context);
//        User currUser = userLocalStore.getLoggedInUser();
//        CustomerID = currUser.getUserID();


        df = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setCurrencySymbol("");
        dfs.setMonetaryDecimalSeparator(',');
        dfs.setGroupingSeparator('.');
        df.setDecimalFormatSymbols(dfs);
        df.setMaximumFractionDigits(0);

        Location loc1 = new Location("");
        loc1.setLatitude(userlat);
        loc1.setLongitude(userLong);
        Location loc2 = new Location("");
        loc2.setLatitude(pr.latitude);
        loc2.setLongitude(pr.longitude);
        double distanceInMeters = loc1.distanceTo(loc2)/1000;

        Log.d("Jarak", distanceInMeters+"");
        holder.tv_nama_apotek.setText(pr.getNama_apotek());
        holder.tv_harga_obat_apotek.setText(df.format(pr.getHarga())+"");
        holder.tv_stok_obat_apotek.setText(String.valueOf(pr.getStok()));
        if(userLong != 0 && userlat !=0) { holder.tv_jarak.setText(String.format("%.1f",distanceInMeters)+" KM"); }
        else { holder.tv_jarak.setText("0 KM"); }

        holder.RatObat.setRating(pr.getRating());
        holder.RatObat.setEnabled(false);
        holder.tv_jam_open.setText(pr.getOutletOprOpen());
        holder.tv_jam_close.setText(pr.getOutletOprClose());
        holder.containerApotek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productName = pr.getProductName();
                if(productName.contains(" ")){
                    productName = productName.replace(" ","%20");
                }
                addkeranjang(productName ,pr.getId_apotek());
                Intent i = new Intent(context,CartApotekActivity.class);
                i.putExtra("ApotekName", pr.getNama_apotek());
                Log.d( "ssssssss",pr.getNama_apotek());
                context.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return apoteklist.size();
    }

    public static class apotekViewHolder extends RecyclerView.ViewHolder {
        RatingBar RatObat;
        TextView tv_nama_apotek,tv_harga_obat_apotek,tv_stok_obat_apotek,tv_jarak, tv_jam_open, tv_jam_close;
        LinearLayout containerApotek;
        public apotekViewHolder(View v) {
            super(v);
            RatObat =v.findViewById(R.id.ratingBar);
            containerApotek=v.findViewById(R.id.containerApotek);
            tv_nama_apotek= (TextView) v.findViewById(R.id.tv_nama_apotek);
            tv_harga_obat_apotek=(TextView) v.findViewById(R.id.tv_harga_obat_apotek);
            tv_stok_obat_apotek=(TextView) v.findViewById(R.id.tv_stok_obat_apotek);
            tv_jarak= v.findViewById(R.id.tv_jarak);
            tv_jam_open = v.findViewById(R.id.tv_jam_open);
            tv_jam_close = v.findViewById(R.id.tv_jam_close);
            //containerApotek = v.findViewById(R.id.containerApotek);
        }
    }


    public void addkeranjang(String productname, String outletID){
       String url = "http://pharmanet.apodoc.id/customer/AddProductToCart.php?ProductName="+productname+"&OutletID="+outletID;
        Log.d("addkeranjangasd: ",url);

        JsonObjectRequest req = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray result = null;
                try {
                    result = response.getJSONArray("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Err", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(req);
    }


    public void add(String productID,String outletID, int productPrice,int productPriceAfterDiscount,int qty){
        JSONObject objAdd = new JSONObject();
        try {
            JSONArray arrData = new JSONArray();
            JSONObject objDetail = new JSONObject();
            objDetail.put("ProductID",productID);
            objDetail.put("OutletID",outletID);
            objDetail.put("Price",productPrice);
            objDetail.put("PriceAfterDiscount",productPriceAfterDiscount);
            objDetail.put("Qty",qty);
            arrData.put(objDetail);
            objAdd.put("data", arrData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("objaddnya",objAdd.toString());
        String url = "";
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, objAdd, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(context, "Berhasil", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(req);
    }



}
