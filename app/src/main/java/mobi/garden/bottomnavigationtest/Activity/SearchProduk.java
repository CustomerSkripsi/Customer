package mobi.garden.bottomnavigationtest.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.Timer;
import java.util.TimerTask;

import mobi.garden.bottomnavigationtest.Adapter.SearchProdukAdapter;
import mobi.garden.bottomnavigationtest.Model.ModelPromo;
import mobi.garden.bottomnavigationtest.R;
import mobi.garden.bottomnavigationtest.Slider.ViewPagerAdapter;

public class SearchProduk extends AppCompatActivity {
    private RequestQueue queue;
    RecyclerView rvhasilSearchProduk;
    public static final String SEARCH_RESULT= "search_result";
    String produkNama, url,produkid,promoName,productUrl;
    List<ModelPromo> prodk = new ArrayList<>();
    List<String>imageUrls = new ArrayList<>();

    SearchProdukAdapter searchprodukAdapter;
    Context context;
    ViewPagerAdapter adapter;
    ViewPager viewPager;
    private ImageView[] dots;
    LinearLayout sliderDotspanel;

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

        //slider
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        showImageSlider(viewPager);
        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(), 2000, 4000);

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

    private void showImageSlider(final View view) {
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET,
                "http://pharmanet.apodoc.id/select_banner_owner.php",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray banners = null;
                        try {
                            banners = response.getJSONArray("result");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            for (int i = 0; i < banners.length(); i++) {
                                JSONObject banner = banners.getJSONObject(i);
                                imageUrls.add(banner.getString("SliderImage"));

                                adapter = new ViewPagerAdapter(SearchProduk.this, imageUrls);
                                viewPager.setAdapter(adapter);
                            }

                            Dots(imageUrls.size());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR VOLLEY SLIDER"+error, error.getMessage());
                    }
                }
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(req);
    }


    public void Dots(int dotscount){
        dots = new ImageView[dotscount];
        for(int z = 0; z < dotscount; z++){
            dots[z] = new ImageView(this);
            dots[z].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            sliderDotspanel.addView(dots[z], params);
        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                for(int i = 0; i< dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }


    public class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            SearchProduk.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(viewPager.getCurrentItem()==0){
                        viewPager.setCurrentItem(1);
                    }
                    else if(viewPager.getCurrentItem()==1){
                        viewPager.setCurrentItem(2);
                    }
                    else if(viewPager.getCurrentItem()==2){
                        viewPager.setCurrentItem(3);
                    }
                    else{
                        viewPager.setCurrentItem(0);
                    }
                }
            });

        }
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

