package mobi.garden.bottomnavigationtest.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mobi.garden.bottomnavigationtest.Adapter.ObatFavoriteAdapter;
import mobi.garden.bottomnavigationtest.Model.apotek;
import mobi.garden.bottomnavigationtest.Model.obat;
import mobi.garden.bottomnavigationtest.R;

public class SearchResultApotek extends AppCompatActivity {

    TextView tvApotekName,tvApotekAddress,tvApotekhoneNumber,tvApotekOperationalHour;
    RatingBar rbApotek;
    RecyclerView rvObatPromo, rvObatFavorite;
    String apotekk;
    apotek ap;
    ObatFavoriteAdapter favoriteAdapter;
    int total_rating;
    String outletID, OutletOprOpen, OutletOprClose, outletAddress ,outletPhone;
    List<apotek> ApotekList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result_apotek);

        tvApotekName = findViewById(R.id.tv_ApotekNameResult);
        tvApotekAddress = findViewById(R.id.tv_address_apotek_result);
        //tvApotekAddress.setText(ap.getAddress());
        tvApotekhoneNumber = findViewById(R.id.tv_PhoneNumber);
        tvApotekOperationalHour = findViewById(R.id.tv_OperationalHourApotek);
        rbApotek = findViewById(R.id.rbApotek);
        //rbApotek.setRating(rbApotek.getRating());
        rbApotek.setEnabled(false);
        rvObatPromo = findViewById(R.id.rvProdukPromo);
        rvObatPromo.setHasFixedSize(true);
        rvObatPromo.setLayoutManager(new LinearLayoutManager(this));
        rvObatFavorite = findViewById(R.id.rvProdukFavaorit);
        rvObatFavorite.setHasFixedSize(true);
        rvObatFavorite.setLayoutManager(new LinearLayoutManager(this));



        Intent intent = getIntent();
        apotekk =  intent.getStringExtra("ApotekName");
        Log.d("test", "jass: "+apotekk);
        if(apotekk.contains(" ")){
            apotekk = apotekk.replace(" ","%20");
        }
        showApotek();
//        showView();
    }

    public void showApotek() {
        String url ="http://pharmanet.apodoc.id/customer/select_apotek_detail.php?OutletName="+apotekk;
        JsonObjectRequest rec1= new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray apoteks = null;
                try {
                    //Toast.makeText(DetailKategori.this, "aaar", Toast.LENGTH_SHORT).show();
                    apoteks = response.getJSONArray("result");
                    ApotekList.clear();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int j = 0; j < apoteks.length(); j++) {
                    try {
                        JSONObject obj = apoteks.getJSONObject(j);
                        outletID = obj.getString("OutletID");
                        outletAddress = obj.getString("OutletAddress");
                        OutletOprOpen = obj.getString("OutletOprOpen");
                        OutletOprClose = obj.getString("OutletOprClose");
                        total_rating = obj.getInt("TotalRating");
                        outletPhone = obj.getString("OutletPhone");
                        ApotekList.add(new apotek(outletID,outletAddress,OutletOprOpen,OutletOprClose,total_rating,outletPhone));

                        Log.d("rwars", obj.toString());
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    } //
                }

                tvApotekName.setText(apotekk);
                rbApotek.setRating(total_rating);
                tvApotekAddress.setText(outletAddress);
                tvApotekhoneNumber.setText(outletPhone);
//                tvApotekOperationalHour.setText(ope);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SearchResultApotek.this, "error loading obatttt", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue req = Volley.newRequestQueue(this);
        req.add(rec1);
    }

    public void showView(final RecyclerView cardlist, final List<obat> list, String url) {
        url ="http://pharmanet.apodoc.id/customer/select_apotek_result.php?"+apotekk;
        JsonObjectRequest rec1= new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray Obats = null;
                try {
                    Obats = response.getJSONArray("result");
                    for (int j = 0; j < Obats.length(); j++) {
                        try {
                            cardlist.setVisibility(View.VISIBLE);
                            JSONObject obj = Obats.getJSONObject(j);
                            list.add(new obat(obj.getString("productID")
                                    ,obj.getString("productName")
                                    ,obj.getString("productPhoto"),
                                    obj.getInt("outletProductPrice")));
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                    favoriteAdapter = new ObatFavoriteAdapter(SearchResultApotek.this,list);
                    cardlist.setAdapter(favoriteAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(context, "error loading obatttt", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue req = Volley.newRequestQueue(this);
        req.add(rec1);
    }
}

