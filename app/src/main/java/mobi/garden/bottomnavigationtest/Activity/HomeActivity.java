package mobi.garden.bottomnavigationtest.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import mobi.garden.bottomnavigationtest.BaseActivity;
import mobi.garden.bottomnavigationtest.LoginRegister.UserLocalStore;
import mobi.garden.bottomnavigationtest.Model.obat;
import mobi.garden.bottomnavigationtest.Adapter.obat_adapter;
import mobi.garden.bottomnavigationtest.R;
import mobi.garden.bottomnavigationtest.Slider.SliderIndicator;
import mobi.garden.bottomnavigationtest.Slider.SliderPagerAdapter;
import mobi.garden.bottomnavigationtest.Slider.SliderView;
import mobi.garden.bottomnavigationtest.Slider.ViewPagerAdapter;


public class HomeActivity extends BaseActivity {
    //Slider Image
    private SliderPagerAdapter mAdapter;
    private SliderIndicator mIndicator;
    private SliderView sliderView;
    private LinearLayout mLinearLayout;

    //obat adapter
    RequestQueue queue;
    obat_adapter pa;
    RecyclerView cardListBrand;
    RecyclerView cardListBrand2;
    RecyclerView cardListBrand3;
    List<obat> pr = new ArrayList<>();
    List<obat> pr2 = new ArrayList<>();
    List<obat> pr3 = new ArrayList<>();
    SwipeRefreshLayout swiperefresh;
    UserLocalStore userLocalStore;

    TextView editText;
    ViewPagerAdapter adapter;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    List<String>imageUrls = new ArrayList<>();
    private int dotscount;
    private ImageView[] dots;

    ImageView ivHistory, ivkategory ,ivPromo;
    //String iv

//    private String[] imageUrls = new String[]{
//            "http://analisadaily.com/assets/image/news/big/2015/10/buah-buahan-terbaik-kaya-alkali-182507-1.jpg",
//            "https://asset.kompas.com/crop/0x0:900x600/750x500/data/photo/2017/12/11/14051175721.jpg",
//            "https://www.rimma.co/wp-content/uploads/-000//1/Banana3.jpg",
//            "https://cdn.idntimes.com/content-images/post/20180226/che-lg-grande-2f06368abd39d78579ba163793d36535-ee6d92b3cb0b8679d93c8cce22a95ca0_600x400.jpg",
//            "https://www.cantikbijak.com/wp-content/uploads/2017/07/000038-00_buah-yang-baik-untuk-ibu-hamil_macam-macam-buah_800x450_cc0-min.jpg"
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        sliderView = (SliderView) findViewById(R.id.sliderView);
//        mLinearLayout = (LinearLayout) findViewById(R.id.pagesContainer);
//        setupSlider();

        editText = (TextView) findViewById(R.id.editText);
        cardListBrand = (RecyclerView) findViewById(R.id.rv_cv_obat_promo);
        cardListBrand2= (RecyclerView) findViewById(R.id.rv_cv_obat_rekomendasi);
        cardListBrand3= (RecyclerView) findViewById(R.id.rv_cv_obat_terlaris);

        ivHistory = findViewById(R.id.ivHistory);
        ivkategory = findViewById(R.id.ivKategori);
        ivPromo = findViewById(R.id.ivPromo);
        ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this , HistoryActivity.class));
            }
        });
        ivkategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this , KategoriActivity.class));
            }
        });
        ivPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, PromoActivity.class));
            }
        });


        cardListBrand.setHasFixedSize(true);
        cardListBrand.setVisibility(View.VISIBLE);
        cardListBrand2.setHasFixedSize(true);
        cardListBrand2.setVisibility(View.VISIBLE);
        cardListBrand3.setHasFixedSize(true);
        cardListBrand3.setVisibility(View.VISIBLE);

//        userLocalStore = new UserLocalStore(this);
//        userLocalStore.getUserLoggedIn();

        LinearLayoutManager llm = new LinearLayoutManager(this);
        LinearLayoutManager llm2 = new LinearLayoutManager(this);
        LinearLayoutManager llm3 = new LinearLayoutManager(this);

        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        llm2.setOrientation(LinearLayoutManager.HORIZONTAL);
        llm3.setOrientation(LinearLayoutManager.HORIZONTAL);

        cardListBrand.setLayoutManager(llm);
        cardListBrand2.setLayoutManager(llm2);
        cardListBrand3.setLayoutManager(llm3);

        queue = Volley.newRequestQueue(this);
        Intent i=getIntent();

        setStatusBarGradiant(this);

        swiperefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {

                        // Berhenti berputar/refreshing
                        swiperefresh.setRefreshing(false);

                        // fungsi-fungsi lain yang dijalankan saat refresh berhenti
                         pa.notifyDataSetChanged();
                    }
                }, 3500);
            }
        });
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, Search_Activity.class));
            }
        });
        show_view(cardListBrand,pr,"http://pharmanet.apodoc.id/select_product_promo.php");
        show_view(cardListBrand2,pr2,"http://pharmanet.apodoc.id/select_product_rekomendasi.php");
        show_view(cardListBrand3,pr3,"http://pharmanet.apodoc.id/select_product_terlaris.php");




        //slider
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        showImageSlider(viewPager);

        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(), 2000, 4000);

    }


    //Slider
//    private void setupSlider() {
//        sliderView.setDurationScroll(800);
//        List<Fragment> fragments = new ArrayList<>();
//        fragments.add(FragmentSlider.newInstance("http://app.apotikcentury.id/public/data/images/slide/illuminare%20Brightening%20Solution.jpg"));
//        fragments.add(FragmentSlider.newInstance("http://app.apotikcentury.id/public/data/images/slide/Hyco%20Care.jpg"));
//        fragments.add(FragmentSlider.newInstance("http://app.apotikcentury.id/public/data/images/slide/Omepros%20april%20revisi-min.jpg"));
//        fragments.add(FragmentSlider.newInstance("http://app.apotikcentury.id/public/data/images/slide/Nourish%20April%202018%20revisi%202.jpg"));
//
//        mAdapter = new SliderPagerAdapter(getSupportFragmentManager(), fragments);
//        sliderView.setAdapter(mAdapter);
//        mIndicator = new SliderIndicator(this, mLinearLayout, sliderView, R.drawable.indicator_circle);
//        mIndicator.setPageCount(fragments.size());
//        mIndicator.show();
//    }

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

                                adapter = new ViewPagerAdapter(HomeActivity.this, imageUrls);
                                viewPager.setAdapter(adapter);

//                                mAdapter = new SliderPagerAdapter(getFragmentManager(), fragmentList);
//                                sliderView.setAdapter(mAdapter);
//                                mIndicator = new SliderIndicator(getActivity(), mLinearLayout, sliderView, R.drawable.indicator_circle);
//                                mIndicator.setPageCount(fragmentList.size());
//                                mIndicator.show();

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
        queue.add(req);
    }

    public void Dots(int dotscount){
//        dotscount = adapter.getCount();
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
            HomeActivity.this.runOnUiThread(new Runnable() {
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

    public void show_view(final RecyclerView cardlist, final List<obat> list, String url){
        final JsonObjectRequest rec= new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray obats = null;

                try {
                    obats = response.getJSONArray("result");

                    for (int i = 0; i < obats.length(); i++) {
                        try {
                            cardlist.setVisibility(View.VISIBLE);
                            JSONObject obj = obats.getJSONObject(i);

                            list.add(new obat(
                                    obj.getString("ProductID"),
                                    obj.getString("ProductName"),
                                    obj.getString("ProductImage"),
                                    obj.getString("ProductDescription"),
                                    obj.getString("ProductIndicationUsage"),
                                    obj.getString("ProductIngredients"),
                                    obj.getString("ProductDosage"),
                                    obj.getString("ProductHowToUse"),
                                    obj.getString("ProductPackage"),
                                    obj.getString("ProductClassification"),
                                    obj.getString("ProductRecipe"),
                                    obj.getString("ProductContraindication"),
                                    obj.getString("ProductStorage"),
                                    obj.getString("PrincipalName"),
                                    obj.getString("CategoryID")));
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                            Toast.makeText(HomeActivity.this, e1.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }

                    pa = new obat_adapter(HomeActivity.this,list);
                    cardlist.setAdapter(pa);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeActivity.this, "No Connection", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(rec);
    }

    public  int getContentViewId () {

        return R.layout.activity_home;
        }

    public  int getNavigationMenuItemId () {
            return R.id.navigation_home;
        }

    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(R.drawable.gradient);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
//            window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }

    }
