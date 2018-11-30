package mobi.garden.bottomnavigationtest.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

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

import java.util.ArrayList;
import java.util.List;

import mobi.garden.bottomnavigationtest.Adapter.DetailKategoriAdapter;
import mobi.garden.bottomnavigationtest.Adapter.FavoritAdapter;
import mobi.garden.bottomnavigationtest.Adapter.PromoAdapter;
import mobi.garden.bottomnavigationtest.Model.ModelKategori;
import mobi.garden.bottomnavigationtest.Model.ModelPromo;
import mobi.garden.bottomnavigationtest.R;

public class DetailKategori extends AppCompatActivity {
    Context context;
    String url, kategoriname, CategoryName,namaproduk,tempimage;
    int harga1, hargacoret1;
    PromoAdapter promoAdapter;
    FavoritAdapter favoritAdapter;

    List<ModelKategori> KategorisList = new ArrayList<>();
    List<ModelPromo> PromoList = new ArrayList<>();
    List<ModelPromo> FavoritList = new ArrayList<>();

    DetailKategoriAdapter dkAdapter;
    RecyclerView rvprodukdetail, rv3promo, rv3favorit;
    ImageView imgProduct,imgProduct2,imgProduct3;
    Button btnSelengkapPromo, btnFavoritSeleng;
    LinearLayout ll_favorite, ll_promo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kategori);
        context = DetailKategori.this;
        rvprodukdetail = findViewById(R.id.rvprodukdetail);
        rv3promo = findViewById(R.id.rv3promo);
        rv3favorit = findViewById(R.id.rv3favorite);
        ll_promo = findViewById(R.id.ll_promo);
        ll_favorite = findViewById(R.id.ll_favorite);



//        rvprodukdetail.setHasFixedSize(true);
//        rvprodukdetail.setVisibility(View.VISIBLE);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv3promo.setLayoutManager(llm);

        LinearLayoutManager llms = new LinearLayoutManager(this);
        llms.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv3favorit.setLayoutManager(llms);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),3);
        rvprodukdetail.setLayoutManager(gridLayoutManager);

       // tvNamaObat = findViewById(R.id.tvNamaObat);

        Intent intent = getIntent();
        kategoriname = intent.getStringExtra("CategoryName");

        btnSelengkapPromo = findViewById(R.id.btnPromoSeleng);
        btnSelengkapPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),PromoActivity.class);
                i.putExtra("allpromo","http://pharmanet.apodoc.id/customer/PromoCategory.php?CategoryName="+kategoriname+"&ProductName=");
                startActivity(i);
            }
        });

        btnFavoritSeleng = findViewById(R.id.btnFavoritSeleng);
        btnFavoritSeleng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),FavoritActivity.class);
                i.putExtra("allfavorit","http://pharmanet.apodoc.id/customer/FavoritCategory.php?CategoryName="+kategoriname+"&ProductName=");
                startActivity(i);
            }
        });


        android.support.v7.widget.Toolbar dToolbar = findViewById(R.id.toolbar4);
        dToolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        dToolbar.setTitle(kategoriname);
        dToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetailKategori.this, KategoriActivity.class);
                startActivity(i);
            }
        });


        if(kategoriname.contains(" ")){
            kategoriname = kategoriname.replace(" ","%20");
        }



        show3promo();
        show3favorit();
        showkategoris();





    }

    public void showkategoris(){
        url = "http://pharmanet.apodoc.id/customer/DetailKategori.php?kategoriname="+kategoriname;
        //Toast.makeText(context, "hmm"+kategoriname, Toast.LENGTH_SHORT).show();
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
                        ModelKategori m = new ModelKategori();
                        m.setCategoryName(object.getString("ProductName"));
                        KategorisList.add(m);
                        //Toast.makeText(context, "pjg:"+result.length(), Toast.LENGTH_SHORT).show();
                        Log.d("rwar", object.toString());
                        dkAdapter = new DetailKategoriAdapter(KategorisList,context);
                        rvprodukdetail.setAdapter(dkAdapter);
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
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(req);
    }

    public void show3promo(){
        String url2 = "http://pharmanet.apodoc.id/customer/Category3promo.php?CategoryName="+kategoriname;
        JsonObjectRequest quest = new JsonObjectRequest(url2, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray result = null;
                try {
                    result = response.getJSONArray("result");
                    PromoList.clear();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for(int i=0;i<result.length();i++){
                    try {
                        JSONObject object = result.getJSONObject(i);
                        namaproduk = object.getString("ProductName");
                        tempimage = object.getString("ProductImage");
                        hargacoret1 = object.getInt("ProductPrice");
                        harga1 = object.getInt("ProductPriceAfterDiscount");
                        PromoList.add(new ModelPromo(namaproduk,tempimage,hargacoret1,harga1));
                        Log.d("rwar", object.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(PromoList.size()==0) {
                    ll_promo.setVisibility(View.GONE);
                }
                promoAdapter = new PromoAdapter(PromoList,context);
                rv3promo.setAdapter(promoAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error Response", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue=Volley.newRequestQueue(context);
        queue.add(quest);
    }


    public void show3favorit(){
        String url = "http://pharmanet.apodoc.id/customer/Category3favorit.php?CategoryName="+kategoriname;
        JsonObjectRequest req = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray result = null;
                try {
                    result = response.getJSONArray("result");
                    FavoritList.clear();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for(int i=0;i<result.length();i++){
                    try {
                        JSONObject object = result.getJSONObject(i);
                        namaproduk = object.getString("ProductName");
                        tempimage = object.getString("ProductImage");
                        hargacoret1 = object.getInt("ProductPrice");
                        FavoritList.add(new ModelPromo(namaproduk,tempimage,hargacoret1));
                        Log.d("rwarss", object.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if(FavoritList.size()==0) {
                    ll_favorite.setVisibility(View.GONE);
                }
                favoritAdapter = new FavoritAdapter(FavoritList,context);
                rv3favorit.setAdapter(favoritAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error Response", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue=Volley.newRequestQueue(context);
        queue.add(req);
    }


}
