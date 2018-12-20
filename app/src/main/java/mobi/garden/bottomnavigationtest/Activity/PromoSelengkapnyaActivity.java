package mobi.garden.bottomnavigationtest.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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

import mobi.garden.bottomnavigationtest.Adapter.PromoAdapter;
import mobi.garden.bottomnavigationtest.Adapter.PromoSelengkapnyaAdapter;
import mobi.garden.bottomnavigationtest.Model.ModelPromo;
import mobi.garden.bottomnavigationtest.R;

public class PromoSelengkapnyaActivity extends AppCompatActivity {
    RecyclerView rvSelengkapnya,rvObatFavorite;
    String apotekk,geturl;
    String urlPromo="";
    String urlFavorite="";
    int diskon;


    PromoSelengkapnyaAdapter promoAdapter;
    PromoSelengkapnyaAdapter favAdapter;
    PromoAdapter FavAdapter;
    int total_rating,outletProductPrice;

    List<ModelPromo> PromoList = new ArrayList<>();
    List<ModelPromo> FavList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_selengkapnya);


        rvSelengkapnya = findViewById(R.id.rvActivitySelengkapnya);
        rvSelengkapnya.setHasFixedSize(true);
        LinearLayoutManager llFavorite = new LinearLayoutManager(this);
        llFavorite.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvSelengkapnya.setLayoutManager(llFavorite);

        Intent i = getIntent();
        geturl = i.getStringExtra("link");
        Log.d("URL", geturl);



        // showViewPromo(rvObatPromo,geturl);
        showViewFav(rvSelengkapnya, geturl);
    }



    public void showViewPromo(final RecyclerView cardlist, String url) {
        JsonObjectRequest rec1= new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("pertama", response.toString());
                JSONArray Obats = null;
                try {
                    Obats = response.getJSONArray("result");
                    PromoList.clear();
                    Toast.makeText(PromoSelengkapnyaActivity.this, "sss"+Obats.length(), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int j = 0; j < Obats.length(); j++) {
                    try {
                        cardlist.setVisibility(View.VISIBLE);
                        JSONObject obj = Obats.getJSONObject(j);
                        PromoList.add(new ModelPromo(obj.getString("ProductID")
                                ,obj.getString("ProductName")
                                ,obj.getString("ProductImage")
                                ,obj.getInt("OutletID"),
                                obj.getInt("OutletProductPrice"),
                                obj.getInt("ProductPriceAfterDiscount")));
                        Log.d("masuk", obj.toString());
                        Toast.makeText(PromoSelengkapnyaActivity.this, "woiiii", Toast.LENGTH_SHORT).show();
                        Toast.makeText(PromoSelengkapnyaActivity.this, ""+obj.getString("productName"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
                promoAdapter = new PromoSelengkapnyaAdapter(PromoList,PromoSelengkapnyaActivity.this);
                cardlist.setAdapter(promoAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PromoSelengkapnyaActivity.this, "error loading obatttt", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue req = Volley.newRequestQueue(this);
        req.add(rec1);
    }

    public void showViewFav(final RecyclerView cardlist, String url) {
        JsonObjectRequest rec1= new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("pertamaa", response.toString());
                JSONArray Obats = null;
                try {
                    Obats = response.getJSONArray("result");
                    FavList.clear();
                    Toast.makeText(PromoSelengkapnyaActivity.this, "sss"+Obats.length(), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int j = 0; j < Obats.length(); j++) {
                    try {
                        cardlist.setVisibility(View.VISIBLE);
                        JSONObject obj = Obats.getJSONObject(j);
                        if(obj.getString("ProductPriceAfterDiscount").equals("null")){
                            diskon =  0; }else{ diskon =  obj.getInt("ProductPriceAfterDiscount"); }
                        FavList.add(new ModelPromo(obj.getString("ProductID")
                                ,obj.getString("ProductName")
                                ,obj.getString("ProductImage")
                                ,obj.getInt("OutletID"),
                                obj.getInt("OutletProductPrice"),
                                diskon));
//                        FavList.add(new ModelPromo(obj.getString("ProductID")
//                                ,obj.getString("ProductName")
//                                ,obj.getString("ProductImage")
//                                ,obj.getInt("OutletID"),
//                                obj.getInt("OutletProductPrice"),
//                                obj.getInt("ProductPriceAfterDiscount")));
                        Log.d("masuk", obj.toString());
                        Toast.makeText(PromoSelengkapnyaActivity.this, "woiiii", Toast.LENGTH_SHORT).show();
                        Toast.makeText(PromoSelengkapnyaActivity.this, ""+obj.getString("productName"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
                favAdapter = new PromoSelengkapnyaAdapter(FavList,PromoSelengkapnyaActivity.this);
                cardlist.setAdapter(favAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PromoSelengkapnyaActivity.this, "error loading obatttt", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue req = Volley.newRequestQueue(this);
        req.add(rec1);
    }

}

