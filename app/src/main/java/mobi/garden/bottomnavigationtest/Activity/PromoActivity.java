package mobi.garden.bottomnavigationtest.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import mobi.garden.bottomnavigationtest.Adapter.DetailKategoriAdapter;
import mobi.garden.bottomnavigationtest.Adapter.PromoAdapter;
import mobi.garden.bottomnavigationtest.Model.ModelKategori;
import mobi.garden.bottomnavigationtest.Model.ModelPromo;
import mobi.garden.bottomnavigationtest.R;

public class PromoActivity extends AppCompatActivity {
    private boolean isScrolling = false;
    private static int sizeproduct,currentItems,totalItems,scrollOutItems;
    private static ProgressBar pgBot;
    private static JSONArray products = new JSONArray();
    private static int countAddList;
    String url,input;
    RecyclerView rvPromo;
    Context context;
    public static  List<ModelPromo> PromoList = new ArrayList<>();
    public static PromoAdapter promoAdapter;
    TextView etSearchPromo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo);
        context = PromoActivity.this;

        pgBot = findViewById(R.id.pgBot);
        rvPromo = findViewById(R.id.rvActivityPromo);
//        LinearLayoutManager llm = new LinearLayoutManager(this);
//        llm.setOrientation(LinearLayoutManager.VERTICAL);
//        rvPromo.setLayoutManager(llm);

        initiateTopAdapter();

//
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),3);
//        rvPromo.setLayoutManager(gridLayoutManager);
        etSearchPromo = findViewById(R.id.search);
        etSearchPromo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //String content= etSearchPromo.getText().toString();
                //showallpromo();
            }
           // String content= etSearchPromo.getText().toString();
        });
        PromoList.clear();
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

    private void initiateTopAdapter(){
        rvPromo.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),3);
        rvPromo.setLayoutManager(gridLayoutManager);


        rvPromo.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = gridLayoutManager.getChildCount();
                //Toast.makeText(context, "ini cur"+currentItems, Toast.LENGTH_SHORT).show();
                totalItems = gridLayoutManager.getItemCount();
                //Toast.makeText(context, "ini total"+totalItems, Toast.LENGTH_SHORT).show();
                scrollOutItems = gridLayoutManager.findFirstVisibleItemPosition();
                if(isScrolling && (currentItems + scrollOutItems == totalItems)){
                    isScrolling=false;
                    addshowpromo();
                }
            }
        });
    }

    public static void addshowpromo(){
        pgBot.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for(int i=sizeproduct; i<sizeproduct+20;i++){
                    try {
                        JSONObject object = products.getJSONObject(i);
                        ModelPromo mp = new ModelPromo();
                        mp.setPromoNameProduct(object.getString("ProductName"));
                        mp.setProductNameUrl(object.getString("ProductImage"));
                        mp.setPriceProduct(object.getInt("ProductPrice"));
                        PromoList.add(mp);
                        //Toast.makeText(context, "pjg:"+result.length(), Toast.LENGTH_SHORT).show();
                        Log.d("rwar", object.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //tvNamaObat.setText(tempNamekategoriname);

                }
                sizeproduct+=20;
                promoAdapter.notifyDataSetChanged();
                pgBot.setVisibility(View.GONE);
            }
        },2000);
    }

    public void showallpromo(){
        url="http://pharmanet.apodoc.id/customer/ProductPromoAll.php";
        JsonObjectRequest req = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray result = null;
                sizeproduct = 0;
                try {
                    //Toast.makeText(DetailKategori.this, "aaar", Toast.LENGTH_SHORT).show();
                    result = response.getJSONArray("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for(int i=sizeproduct; i<sizeproduct+20;i++){
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
                sizeproduct+=20;
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
