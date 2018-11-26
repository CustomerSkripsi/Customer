package mobi.garden.bottomnavigationtest.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

import mobi.garden.bottomnavigationtest.Adapter.DetailKategoriAdapter;
import mobi.garden.bottomnavigationtest.Adapter.PromoAdapter;
import mobi.garden.bottomnavigationtest.Model.ModelKategori;
import mobi.garden.bottomnavigationtest.Model.ModelPromo;
import mobi.garden.bottomnavigationtest.R;

public class PromoActivity extends AppCompatActivity {
    String url;
    RecyclerView rvPromo;
    Context context;
    List<ModelPromo> PromoList = new ArrayList<>();
    PromoAdapter promoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo);
        context = PromoActivity.this;

        rvPromo = findViewById(R.id.rvActivityPromo);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvPromo.setLayoutManager(llm);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),3);
        rvPromo.setLayoutManager(gridLayoutManager);
        showallpromo();

        Toolbar dToolbar = findViewById(R.id.toolbar2);
        dToolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        dToolbar.setTitle("PROMO");
        dToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PromoActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });



    }

    public void showallpromo(){
        url="http://pharmanet.apodoc.id/customer/ProductPromoAll.php";
        JsonObjectRequest req = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray result = null;
                try {
                    //Toast.makeText(DetailKategori.this, "aaar", Toast.LENGTH_SHORT).show();
                    result = response.getJSONArray("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for(int i=0; i<result.length();i++){
                    try {
                        JSONObject object = result.getJSONObject(i);
                        ModelPromo mp = new ModelPromo();
                        mp.setPromoNameProduct(object.getString("ProductName"));
                        mp.setProductNameUrl(object.getString("ProductImage"));
                        mp.setPriceProduct(object.getInt("ProductPrice"));
                        PromoList.add(mp);
                        //Toast.makeText(context, "pjg:"+result.length(), Toast.LENGTH_SHORT).show();
                        Log.d("rwar", object.toString());
                        promoAdapter = new PromoAdapter(PromoList,context);
                        rvPromo.setAdapter(promoAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //tvNamaObat.setText(tempNamekategoriname);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Sedang Gangguan", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(PromoActivity.this);
        queue.add(req);
    }



}
