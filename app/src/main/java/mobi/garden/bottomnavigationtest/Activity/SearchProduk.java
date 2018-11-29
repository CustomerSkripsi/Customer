package mobi.garden.bottomnavigationtest.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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

import java.util.ArrayList;
import java.util.List;

import mobi.garden.bottomnavigationtest.Adapter.SearchProdukAdapter;
import mobi.garden.bottomnavigationtest.Model.ModelPromo;
import mobi.garden.bottomnavigationtest.R;

public class SearchProduk extends AppCompatActivity {
    private RequestQueue queue;
    RecyclerView rvhasilSearchProduk;
    public static final String SEARCH_RESULT= "search_result";
    String produkNama, url,produkid,promoName,productUrl;
    List<ModelPromo> prodk = new ArrayList<>();

    SearchProdukAdapter searchprodukAdapter;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_produk);
        context = SearchProduk.this;

        rvhasilSearchProduk = findViewById(R.id.rvHasilSearchProduk);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvhasilSearchProduk.setLayoutManager(llm);

        Intent intent = getIntent();
        produkNama =  intent.getStringExtra("ProdukName");
        Log.d("test", "jasidjas: "+produkNama);
        if(produkNama.contains(" ")){
            produkNama = produkNama.replace(" ","%20");
        }
        showhasilproduk();

    }

    public void showhasilproduk(){
        url="http://pharmanet.apodoc.id/customer/showSearchProduk.php?ProdukNama="+produkNama;
        JsonObjectRequest req = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray result = null;
                try {
                    result = response.getJSONArray("result");
                    prodk.clear();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for(int i =0; i<result.length();i++){
                    try {
                        JSONObject object = result.getJSONObject(i);
                        promoName = object.getString("ProductName");
                        productUrl = object.getString("ProductImage");
                        prodk.add(new ModelPromo(promoName,productUrl));
                        //Toast.makeText(context, "pjg:"+result.length(), Toast.LENGTH_SHORT).show();
                        Log.d("ssqwe", object.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                searchprodukAdapter = new SearchProdukAdapter(prodk,context);
                rvhasilSearchProduk.setAdapter(searchprodukAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SearchProduk.this, "Gangguan", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(req);
    }



}

