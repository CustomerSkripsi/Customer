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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import mobi.garden.bottomnavigationtest.Adapter.ObatFavoriteAdapter;
import mobi.garden.bottomnavigationtest.Model.obat;
import mobi.garden.bottomnavigationtest.R;

public class SearchResultApotek extends AppCompatActivity {

    TextView tvApotekName,tvApotekAddress,tvApotekhoneNumber,tvApotekOperationalHour;
    RatingBar rbApotek;
    RecyclerView rvObatPromo, rvObatFavorite;
    String produkk;
    ObatFavoriteAdapter favoriteAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result_apotek);

        tvApotekName = findViewById(R.id.tv_ApotekNameResult);
        tvApotekAddress = findViewById(R.id.tv_address_apotek_result);
        tvApotekhoneNumber = findViewById(R.id.tv_PhoneNumber);
        tvApotekOperationalHour = findViewById(R.id.tv_OperationalHourApotek);
        rbApotek = findViewById(R.id.rbApotek);


        rvObatPromo = findViewById(R.id.rvProdukPromo);
        rvObatPromo.setHasFixedSize(true);
        rvObatPromo.setLayoutManager(new LinearLayoutManager(this));

        rvObatFavorite = findViewById(R.id.rvProdukFavaorit);
        rvObatFavorite.setHasFixedSize(true);
        rvObatFavorite.setLayoutManager(new LinearLayoutManager(this));


        rbApotek.setRating(3);
        rbApotek.setEnabled(true);

        Intent intent = getIntent();
        produkk =  intent.getStringExtra("ProdukName");
        Log.d("test", "jass: "+produkk);
        if(produkk.contains(" ")){
            produkk = produkk.replace(" ","%20");
        }



//        showView();
    }

    public void showView(final RecyclerView cardlist, final List<obat> list, String url) {
        url ="http://pharmanet.apodoc.id/customer/select_apotek_result.php?"+produkk;
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

