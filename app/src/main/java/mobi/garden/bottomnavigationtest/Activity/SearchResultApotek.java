package mobi.garden.bottomnavigationtest.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
    TextView btnSelengFav,btnSelengPromo;
    RatingBar rbApotek;
    RecyclerView rvObatPromo, rvObatFavorite;
    String apotekk;
    String urlPromo="http://pharmanet.apodoc.id/customer/select_obat_promo_outlet.php?OutletName=";
    String urlFavorite="http://pharmanet.apodoc.id/customer/select_obat_favorite_outlet.php?OutletName=";


    PromoAdapter promoAdapter;
    PromoAdapter FavAdapter;
    int total_rating,outletProductPrice;
    String outletID, OutletOprOpen, OutletOprClose, outletAddress ,outletPhone;
    int diskon;
    List<apotek> ApotekList = new ArrayList<>();
    List<ModelPromo> PromoList = new ArrayList<>();
    List<ModelPromo> FavList = new ArrayList<>();

    ModelPromo mp;
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


        LinearLayoutManager llPromo = new LinearLayoutManager(this);
        llPromo.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvObatPromo.setLayoutManager(llPromo);



        rvObatFavorite = findViewById(R.id.rvProdukFavaorit);
        rvObatFavorite.setHasFixedSize(true);
        LinearLayoutManager llFavorite = new LinearLayoutManager(this);
        llFavorite.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvObatFavorite.setLayoutManager(llFavorite);


        Intent intent = getIntent();
        apotekk =  intent.getStringExtra("ApotekName");
        Log.d("test", "jass: "+apotekk);
        tvApotekName.setText(apotekk);
        if(apotekk.contains(" ")){
            apotekk = apotekk.replace(" ","%20");
        }

        btnSelengPromo = findViewById(R.id.btnSelengPromo);
        btnSelengPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),PromoSelengkapnyaActivity.class);
                i.putExtra("link","http://pharmanet.apodoc.id/customer/select_selengkapnya_promo.php?OutletName="+apotekk);
                startActivity(i);
            }
        });
        btnSelengFav = findViewById(R.id.btnSelengFavorite);
        btnSelengFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),PromoSelengkapnyaActivity.class);
                i.putExtra("link","http://pharmanet.apodoc.id/customer/select_selengkapnya_favorite.php?OutletName="+apotekk);
                startActivity(i);
            }
        });

        showApotek();
        showView(rvObatPromo,urlPromo+apotekk);
        showViewFav();
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
                tvApotekOperationalHour.setText(OutletOprOpen +" - "+ OutletOprClose);
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

    public void showView(final RecyclerView cardlist, String url) {

        JsonObjectRequest rec1= new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray Obats = null;
                try {
                    Obats = response.getJSONArray("result");
                    PromoList.clear();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int j = 0; j < Obats.length(); j++) {
                    try {
                        cardlist.setVisibility(View.VISIBLE);
                        JSONObject obj = Obats.getJSONObject(j);
                        if(obj.getString("ProductPriceAfterDiscount").equals("null")){
                            //mp.setProductPriceAfterDC(0);
                            diskon =  0;
                        }else{
                            diskon =  obj.getInt("ProductPriceAfterDiscount");
                        }
                        PromoList.add(new ModelPromo(obj.getString("ProductID")
                                ,obj.getString("ProductName")
                                ,obj.getString("ProductImage")
                                ,obj.getInt("OutletID"),
                                obj.getInt("OutletProductPrice"),
                                diskon));
                        Log.d("asd", obj.toString());

                        //Toast.makeText(SearchResultApotek.this, "sesuat", Toast.LENGTH_SHORT).show(); /
                        Toast.makeText(SearchResultApotek.this, ""+obj.getString("productName"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
                promoAdapter = new PromoAdapter(PromoList,SearchResultApotek.this);
                cardlist.setAdapter(promoAdapter);
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

    public void showViewFav() { // ,
        JsonObjectRequest rec= new JsonObjectRequest(urlFavorite+apotekk, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray Obat = null;
                try {
                    Obat = response.getJSONArray("result");
                    FavList.clear();
                    Toast.makeText(SearchResultApotek.this, "fs"+Obat.length(), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int j = 0; j < Obat.length(); j++) {
                    try {
                        //rvObatFavorite.setVisibility(View.VISIBLE);
                        JSONObject obj = Obat.getJSONObject(j);
                        if(obj.getString("ProductPriceAfterDiscount").equals("null")){
                            diskon =  0; }else{ diskon =  obj.getInt("ProductPriceAfterDiscount"); }
                        FavList.add(new ModelPromo(obj.getString("ProductID")
                                ,obj.getString("ProductName")
                                ,obj.getString("ProductImage")
                                ,obj.getInt("OutletID"),
                                obj.getInt("OutletProductPrice"),
                                diskon));
                        Log.d("asdtest", obj.toString());
                        Toast.makeText(SearchResultApotek.this, ""+obj.getString("productName"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
                FavAdapter = new PromoAdapter(FavList,SearchResultApotek.this);
                rvObatFavorite.setAdapter(FavAdapter);
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
    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(R.drawable.toolbar);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }
}


