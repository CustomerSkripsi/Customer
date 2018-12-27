package mobi.garden.bottomnavigationtest.Adapter;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
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
import java.util.HashMap;
import java.util.List;

import mobi.garden.bottomnavigationtest.Activity.CartApotekActivity;
import mobi.garden.bottomnavigationtest.Activity.SearchResultApotek;
import mobi.garden.bottomnavigationtest.Model.apotek;
import mobi.garden.bottomnavigationtest.R;
import mobi.garden.bottomnavigationtest.Session.SessionManagement;

public class SearchApotekAdapter extends RecyclerView.Adapter<SearchApotekAdapter.SearchApotekViewHolder> {
    List<apotek> apoteks;
    Context context;
    static DecimalFormat df;
    public static String add_url = "http://pharmanet.apodoc.id/customer/addCartCustomer.php";

    //login
    SessionManagement session;
    HashMap<String, String> login;
    public static String CustomerID,memberID, userName;

    double userLong;
    double userlat;

    public SearchApotekAdapter(List<apotek> apoteks, Context context, double userLong, double userlat) {
        this.apoteks = apoteks;
        this.context = context;
        this.userLong = userLong;
        this.userlat = userlat;
    }


    public SearchApotekAdapter(List<apotek> apoteks, Context context) {
        this.apoteks = apoteks;
        this.context = context;
    }

    @NonNull
    @Override
    public SearchApotekAdapter.SearchApotekViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cv_search_apotek,parent,false);
        return new SearchApotekAdapter.SearchApotekViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchApotekAdapter.SearchApotekViewHolder holder, int position) {
        final apotek ap = apoteks.get(position);
        holder.tvnamaapotek.setText(ap.getNama_apotek());
        holder.tvJamOperasional.setText(ap.getOutletOprOpen()+"-"+ap.getOutletOprClose());
        holder.rbApotek.setRating((float)ap.getRatingbar());
        //holder.rbApotek.setRating((float)3.7888);
//        holder.llapotek.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //Intent i = new Intent(context, )
//                //i.putExtra("ProductName",);
//            }
//        });

        session = new SessionManagement(context);
        login = session.getMemberDetails();
        userName= login.get(SessionManagement.USERNAME);
        memberID = login.get(SessionManagement.KEY_KODEMEMBER);


        df = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setCurrencySymbol("Rp. ");
        dfs.setMonetaryDecimalSeparator('.');
        dfs.setGroupingSeparator('.');
        df.setDecimalFormatSymbols(dfs);
        df.setMaximumFractionDigits(0);

        Location loc1 = new Location("");
        loc1.setLatitude(userlat);
        loc1.setLongitude(userLong);

        Location loc2 = new Location("");
        loc2.setLatitude(ap.latitude);
        loc2.setLongitude(ap.longitude);
        double distanceInMeters = loc1.distanceTo(loc2)/1000;
        Log.d("Jarak", distanceInMeters+"");
        if(userLong != 0 && userlat !=0)
        {
            holder.tvJarak.setText(String.format("%.1f",distanceInMeters)+" KM");
        }
        else
        {
            holder.tvJarak.setText("0 KM");
        }

        holder.llapotek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context,SearchResultApotek.class);
                i.putExtra("ApotekName", ap.getNama_apotek());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return apoteks.size();
    }

    public class SearchApotekViewHolder extends RecyclerView.ViewHolder{
        TextView tvnamaapotek, tvJamOperasional,tvJarak;
        LinearLayout llapotek;
        RatingBar rbApotek;
        public SearchApotekViewHolder(View itemView){
            super(itemView);
            tvnamaapotek = itemView.findViewById(R.id.tvnamaapotek);
            tvJamOperasional = itemView.findViewById(R.id.tvJamOperasional);
            tvJarak = itemView.findViewById(R.id.tvJarak);
            llapotek = itemView.findViewById(R.id.llApotek);
            rbApotek = itemView.findViewById(R.id.rbApotek);
        }
    }
    public void add(String product_id, int product_price, int qty, String memberID) {
        Log.d("dsa", memberID);
        JSONObject objAdd = new JSONObject();
        try {
            JSONArray arrData = new JSONArray();
            JSONObject objDetail = new JSONObject();
            objDetail.put("ProductID", product_id);
//            objDetail.put("ProductName", product_name);
            objDetail.put("outletProductPrice", product_price);
            objDetail.put("Qty", qty);
            objDetail.put("CustomerID", memberID);
            objDetail.put("UpdatedBy",memberID);
            arrData.put(objDetail);
            objAdd.put("data", arrData);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
//        Log.d("testtest1", objAdd.toString());
        Toast.makeText(context, "poipoi", Toast.LENGTH_SHORT).show();
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, add_url, objAdd,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        Toast.makeText(context, "asdqwe", Toast.LENGTH_SHORT).show();
                        try {
                            if (response.getString("status").equals("OK")) {
                                //CartApotekActivity.initiateBelowAdapter();
                                SearchResultApotek.show_cart(SearchResultApotek.urlbawahs,memberID);

                                //  String temp = ss.getProductName();
                                //Log.d("hahahhas", "onResponse: "+temp);
//                                Toast.makeText(context, "obatadapterberhasil", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

}
