package mobi.garden.bottomnavigationtest.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import mobi.garden.bottomnavigationtest.Adapter.SearchApotekAdapter;
import mobi.garden.bottomnavigationtest.Model.apotek;
import mobi.garden.bottomnavigationtest.R;
import mobi.garden.bottomnavigationtest.Slider.ViewPagerAdapter;

public class SearchApotek extends AppCompatActivity {
    List<apotek> apoteklist = new ArrayList<>();
    List<String>imageUrls = new ArrayList<>();
    public static final String SEARCH_RESULT= "search_result";
    Context context;
    RecyclerView rvhasilSearchApotek;
    String url ,apotek,id_apotek,apoteknama,outletOprOpen,outletOprClose, tempApotekDay;
    int ratingbar;


    SearchApotekAdapter searchapotekAdapter;
    ViewPagerAdapter adapter;
    ViewPager viewPager;
    private ImageView[] dots;
    LinearLayout sliderDotspanel;

    double longitude,latitude;
    GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_apotek);
        context = SearchApotek.this;

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(SearchApotek.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SearchApotek.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SearchApotek.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                longitude = location.getLongitude();
                                latitude = location.getLatitude();
                                Log.d("test123", longitude + "");
                            } else {
                                Toast.makeText(SearchApotek.this, "Gagal menarik lokasi anda", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }


        rvhasilSearchApotek = findViewById(R.id.rvHasilSearchApotek);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvhasilSearchApotek.setLayoutManager(llm);


        getapoteksearched();



        //slider
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        showImageSlider(viewPager);
        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(), 2000, 4000);

    }

    public void getapoteksearched(){
        Intent intent = getIntent();
        apotek =  intent.getStringExtra(SearchApotek.SEARCH_RESULT);
        Log.d("test", "jassya: "+apotek);
        if(apotek.contains(" ")){
            apotek = apotek.replace(" ","%20");
        }
        showhasilapotek();
    }


    public void showhasilapotek(){
        getDay();
        url = "http://pharmanet.apodoc.id/customer/showSearchApotek.php?NamaApotek="+apotek+"&day="+tempApotekDay;
        //url = "http://pharmanet.apodoc.id/customer/showSearchApotek.php?NamaApotek="+apotek;
        JsonObjectRequest req = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray result = null;
                try {
                    result = response.getJSONArray("result");
                    apoteklist.clear();
                } catch (JSONException e) {
                    e.printStackTrace();
                } //(String id_apotek, String nama_apotek, String outletOprOpen, String outletOprClose)
                for(int i =0; i<result.length();i++){
                    try {
                        JSONObject object = result.getJSONObject(i);
                        id_apotek = object.getString("OutletID");
                        apoteknama = object.getString("OutletName");
                        ratingbar = object.getInt("TotalRating");
                        outletOprOpen = object.getString("OutletOprOpen");
                        outletOprClose = object.getString("OutletOprClose");
                        longitude = object.getDouble("OutletLatitude");
                        latitude = object.getDouble("OutletLongitude");
                        apoteklist.add(new apotek(id_apotek,apoteknama,outletOprOpen,ratingbar,outletOprClose,longitude,latitude));
                        //Toast.makeText(context, "pjg:"+result.length(), Toast.LENGTH_SHORT).show();
                        Log.d("ssqwes", object.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                searchapotekAdapter = new SearchApotekAdapter(apoteklist,context,longitude,latitude);
                rvhasilSearchApotek.setAdapter(searchapotekAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Gangguan", Toast.LENGTH_SHORT).show();
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

                                adapter = new ViewPagerAdapter(SearchApotek.this, imageUrls);
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
            SearchApotek.this.runOnUiThread(new Runnable() {
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

    private void getDay(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SUNDAY:
                // Current day is Sunday
                tempApotekDay = "Minggu";
                break;
            case Calendar.MONDAY:
                // Current day is Monday
                tempApotekDay = "Senin";
                break;
            case Calendar.TUESDAY:
                // Current day is Tuesday
                tempApotekDay = "Selasa";
                break;
            case Calendar.WEDNESDAY:
                // Current day is Wednesday
                tempApotekDay = "Rabu";
                break;
            case Calendar.THURSDAY:
                // Current day is Thursday
                tempApotekDay = "Kamis";
                break;
            case Calendar.FRIDAY:
                // Current day is Friday
                tempApotekDay = "Jumat";
                break;
            case Calendar.SATURDAY:
                // Current day is Saturday
                tempApotekDay = "Sabtu";
                break;
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
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

