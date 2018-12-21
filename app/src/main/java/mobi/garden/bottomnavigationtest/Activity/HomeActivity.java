package mobi.garden.bottomnavigationtest.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.nex3z.notificationbadge.NotificationBadge;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import mobi.garden.bottomnavigationtest.Adapter.GlobalSearchAdapter;
import mobi.garden.bottomnavigationtest.Adapter.RatingAdapter;
import mobi.garden.bottomnavigationtest.BaseActivity;
import mobi.garden.bottomnavigationtest.LoginRegister.UserLocalStore;
import mobi.garden.bottomnavigationtest.Model.Rating;
import mobi.garden.bottomnavigationtest.Model.obat;
import mobi.garden.bottomnavigationtest.R;
import mobi.garden.bottomnavigationtest.Slider.SliderIndicator;
import mobi.garden.bottomnavigationtest.Slider.SliderPagerAdapter;
import mobi.garden.bottomnavigationtest.Slider.SliderView;
import mobi.garden.bottomnavigationtest.Slider.ViewPagerAdapter;


public class HomeActivity extends BaseActivity {
    //Slider Image
    private SliderPagerAdapter mAdapter;
    private SliderIndicator mIndicator;private SliderView sliderView;private LinearLayout mLinearLayout;
    private int context_pilihan;

    RecyclerView rvSearchGlobal;
    List<String> listRekomen = new ArrayList<>();
    List<String> listRekomenApotek = new ArrayList<>();
    List<String> listRekomenProduct = new ArrayList<>();
    List<String>imageUrls = new ArrayList<>();

    //obat adapter
    RequestQueue queue;
    Context context;
    //obat_adapter pa;
    RecyclerView cardListBrand,cardListBrand2,cardListBrand3;
    List<obat> pr = new ArrayList<>();
    List<obat> pr2 = new ArrayList<>();
    List<obat> pr3 = new ArrayList<>();
    SwipeRefreshLayout swiperefresh;
    UserLocalStore userLocalStore;

    TextView editText;
    ViewPagerAdapter adapter;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    private static NotificationBadge mBadge;
    GlobalSearchAdapter globalSearchAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    ImageView ivHistory, ivkategory ,ivPromo, ivFavorit,ivCart,ivMember,ivLainlain;
    EditText etSearch;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    double longitude;
    double latitude;
    private FusedLocationProviderClient mFusedLocationClient;


    //rating
    Button btn, mSendFeedback, btnKategori;
    RatingBar rtBar;
    TextView mRatingScale;
    EditText mFeedback;
    String url;
    RecyclerView rvButton;
    public static int number;
    public int ratingNum;
    RatingAdapter buttonRatingAdapteradapter;
    List<Rating> ratingList = new ArrayList<>();
    public String review;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        sliderView = (SliderView) findViewById(R.id.sliderView);
//        mLinearLayout = (LinearLayout) findViewById(R.id.pagesContainer);
//        setupSlider();
        rvSearchGlobal = findViewById(R.id.rvSearchglobal);
        builder = new AlertDialog.Builder(this);
        editText = (TextView) findViewById(R.id.editText);
        cardListBrand = (RecyclerView) findViewById(R.id.rv_cv_obat_promo);
        cardListBrand2= (RecyclerView) findViewById(R.id.rv_cv_obat_rekomendasi);
        cardListBrand3= (RecyclerView) findViewById(R.id.rv_cv_obat_terlaris);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                                //Toast.makeText(HomeActivity.this, "Gagal menarik lokasi anda", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }


        InitiateSearchAdapter();

        ivHistory = findViewById(R.id.ivHistory);
        ivkategory = findViewById(R.id.ivKategori);
        ivPromo = findViewById(R.id.ivPromo);
        ivFavorit = findViewById(R.id.ivFavorit);
        ivMember = findViewById(R.id.ivMember);
        ivLainlain = findViewById(R.id.ivLainlain);
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
//              startActivity(new Intent(HomeActivity.this, PromoActivity.class));
                Intent i = new Intent(getApplicationContext(),PromoActivity.class);
                i.putExtra("allpromo","http://pharmanet.apodoc.id/customer/ProductPromoAll.php?input=");
                startActivity(i);
            }
        });
        ivFavorit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),FavoritActivity.class);
                i.putExtra("allfavorit","http://pharmanet.apodoc.id/customer/ProductFavoritAll.php?ProductName=");
                startActivity(i);
            }
        });
        ivLainlain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });

        ivCart = findViewById(R.id.ivCart);
        ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this , CartActivity.class));
            }
        });

        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startActivity(new Intent(HomeActivity.this, HomeActivity.class));
                etSearch.setText("");
                mBadge.setVisibility(View.VISIBLE);
                rvSearchGlobal.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        etSearch = findViewById(R.id.tvSearch);
        etSearch.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId== EditorInfo.IME_ACTION_SEARCH||actionId == EditorInfo.IME_ACTION_DONE){
                    String input = etSearch.getText().toString();
                    if(input.contains(" ")){input = input.replace(" ","%20"); }
                    if(input.contains("+")){input = input.replace("+","%2B"); }
                    if(input.contains("/")){input = input.replace("/","\\/"); }
                    if(input.contains(",")){input = input.replace(",","%2C"); }
                    Log.d("inputnya",input+"");
                    if(context_pilihan==1){
                        Intent i = new Intent(HomeActivity.this,SearchProduk.class);
                        i.putExtra(SearchProduk.SEARCH_RESULT, input);
                        startActivity(i);
                    }else if(context_pilihan==2){
                        //Intent i = new Intent(HomeActivity.this,);
                    }
                }
                return false;
            }
        });
        ivMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,MemberActivity.class));
            }
        });


        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(etSearch.getText().toString().equals("")||etSearch.getText().toString().length()==0||etSearch.getText().toString().isEmpty()) {
                    rvSearchGlobal.setVisibility(View.GONE);
//                    Toast.makeText(HomeActivity.this, "blblalblabla", Toast.LENGTH_SHORT).show();
                }else if(listRekomenApotek.size()!=0 && listRekomenProduct.size()!=0)
                    //set ulang rekomendasinya
                    Log.d("jumlah",etSearch.getText().toString());
                    search(etSearch.getText().toString());
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(etSearch.getText().toString().equals("")||etSearch.getText().toString().length()==0||etSearch.getText().toString().isEmpty()) {
                    rvSearchGlobal.setVisibility(View.GONE); } }
        });

        LinearLayoutManager llm = new LinearLayoutManager(this);
        LinearLayoutManager llm2 = new LinearLayoutManager(this);
        LinearLayoutManager llm3 = new LinearLayoutManager(this);

        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        llm2.setOrientation(LinearLayoutManager.HORIZONTAL);
        llm3.setOrientation(LinearLayoutManager.HORIZONTAL);

//        cardListBrand.setLayoutManager(llm);
//        cardListBrand2.setLayoutManager(llm2);
//        cardListBrand3.setLayoutManager(llm3);
        queue = Volley.newRequestQueue(this);
        Intent i=getIntent();
        setStatusBarGradiant(this);

        swiperefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Berhenti berputar/refreshing
                        swiperefresh.setRefreshing(false);
                        // fungsi-fungsi lain yang dijalankan saat refresh berhenti
                        //pa.notifyDataSetChanged();
                    }
                },3500);
            }
        });


        //slider
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        showImageSlider(viewPager);
        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(), 2000, 4000);


        dialograting("RATING APOTEK");


    }

    public void dialograting(String title){
        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_rating);
        TextView tvNamaApotek = dialog.findViewById(R.id.tvNamaApotek);
        TextView tvalamat = dialog.findViewById(R.id.tvAlamat);
        TextView tvmohon = dialog.findViewById(R.id.tvMohon);
        mSendFeedback = dialog.findViewById(R.id.btnKirim);
        mFeedback = dialog.findViewById(R.id.etFeed);
        rtBar = dialog.findViewById(R.id.ratingBar);
        mRatingScale = dialog.findViewById(R.id.tvRatingScale);

        rvButton = dialog.findViewById(R.id.rvdialograting);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvButton.setLayoutManager(llm);

        rtBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean b) {
                mRatingScale.setText(String.valueOf(rating));
                switch ((int) ratingBar.getRating()){
                    case 1:
                        mRatingScale.setText("Sangat Buruk");
                        try{
                            if(buttonRatingAdapteradapter.getItemCount()>=0){
                                buttonRatingAdapteradapter.clear();
                            }
                        }catch (Exception e){}
                        break;
                    case 2:
                        mRatingScale.setText("Buruk");
                        try{
                            if(buttonRatingAdapteradapter.getItemCount()>=0){
                                buttonRatingAdapteradapter.clear();
                            }
                        }catch (Exception e){}
                        break;
                    case 3:
                        mRatingScale.setText("Cukup");
                        try{
                            if(buttonRatingAdapteradapter.getItemCount()>=0){
                                buttonRatingAdapteradapter.clear();
                            }
                        }catch (Exception e){}
                        break;
                    case 4:
                        mRatingScale.setText("Baik");
                        try{
                            if(buttonRatingAdapteradapter.getItemCount()>=0){
                                buttonRatingAdapteradapter.clear();
                            }
                        }catch (Exception e){}
                        break;
                    case 5:
                        mRatingScale.setText("Sangat Baik");
                        try{
                            if(buttonRatingAdapteradapter.getItemCount()>=0){
                                buttonRatingAdapteradapter.clear();
                            }
                        }catch (Exception e){}
                        break;
                    default:
                        mRatingScale.setText("");
                }
                ratingNum = Integer.parseInt(Math.round(rating)+"");
                ratinginput(ratingNum);
            }
        });

        mSendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ratingNum == 0){
                    Toast.makeText(HomeActivity.this, "Rating harus diisi", Toast.LENGTH_SHORT).show();
                }else if(number == 0){
                    Toast.makeText(HomeActivity.this, "Ulasan harus diisi", Toast.LENGTH_SHORT).show();
                }else {
                    review = mFeedback.getText().toString();
                    saveFeedback(ratingNum,number,review);
                    dialog.dismiss();
                    Toast.makeText(HomeActivity.this, "Terimakasih atas feedback dan ulasan yang anda berikan", Toast.LENGTH_LONG).show();
                }
            }
        });
        dialog.show();
    }

    public void ratinginput (int ratingNum){
        final JSONObject objadd = new JSONObject();
        try {
            JSONArray arrdata = new JSONArray();
            JSONObject objdetail = new JSONObject();
            objdetail.put("RatingBar",ratingNum);
            arrdata.put(objdetail);
            objadd.put("data",arrdata);
            Log.d("testssss",objadd.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        url = "http://pharmanet.apodoc.id/customer/feedbackrating.php";
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, objadd, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //Toast.makeText(MainActivity.this, "aaa", Toast.LENGTH_SHORT).show();
                    //Log.d("s",objadd.toString());
                    JSONArray objs = response.getJSONArray("result");
                    for (int i=0; i<objs.length(); i++){
                        JSONObject obj = objs.getJSONObject(i);
                        //Toast.makeText(context, "panjangnya"+objs.length(), Toast.LENGTH_SHORT).show();
                        ratingList.add(new Rating(obj.getString("FeedbackOption"),obj.getInt("FeedbackID")));
                    }
                    buttonRatingAdapteradapter = new RatingAdapter(ratingList,HomeActivity.this,mFeedback);
                    rvButton.setAdapter(buttonRatingAdapteradapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "hmm", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue queue = Volley.newRequestQueue(HomeActivity.this);
        queue.add(req);
    }


    public void saveFeedback (int ratingNum, int number, String review){
        JSONObject objadd = new JSONObject();
        try {
            JSONArray arrdata = new JSONArray();
            JSONObject objDetail = new JSONObject();
            objDetail.put("RatingBar",ratingNum);
            objDetail.put("number",number);
            objDetail.put("review",review);
            arrdata.put(objDetail);
            objadd.put("data",arrdata);
            Log.d("testsave",objadd.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, "aa", Toast.LENGTH_SHORT).show();
        }
        url = "http://pharmanet.apodoc.id/customer/saveFeedback.php";
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, objadd
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(HomeActivity.this, "Terimakasih atas feedback yang anda berikan", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(context, "Maaf Sedang Terjadi Gangguan", Toast.LENGTH_SHORT).show();
                //Log.e("test",error+"");
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(req);
    }

    private void search(String s) {
        listRekomen = new ArrayList();
        int i = 0;
        for(int j = 0;j<listRekomenApotek.size();j++){
            if(listRekomenApotek.get(j).toLowerCase().contains(s.toLowerCase())){
                listRekomen.add(listRekomenApotek.get(j));
                Log.d("category",listRekomenApotek.get(j));
                i++;
                globalSearchAdapter.setContext_pilihan(1);
                context_pilihan = 1;
                if(i==10){
                    break;
                }
            }
        }
        Log.d("jumlah_i",i+"");
        if(i==0){
            for(int j = 0;j<listRekomenProduct.size();j++){
                if(listRekomenProduct.get(j).toLowerCase().contains(s.toLowerCase())){
                    listRekomen.add(listRekomenProduct.get(j));
                    Log.d("product",listRekomenProduct.get(j));
                    i++;
                    context_pilihan = 2;
                    globalSearchAdapter.setContext_pilihan(2);
                    if(i==10){
                        break;
                    }
                }
            }
        }
        //update recyclerview
        globalSearchAdapter.setProductList(listRekomen);
        rvSearchGlobal.setAdapter(globalSearchAdapter);
        globalSearchAdapter.notifyDataSetChanged();
        rvSearchGlobal.setVisibility(View.VISIBLE);
    }

    private void InitiateSearchAdapter() {
        globalSearchAdapter = new GlobalSearchAdapter(HomeActivity.this);
        rvSearchGlobal = findViewById(R.id.rvSearchglobal);
        rvSearchGlobal.setVisibility(View.GONE);
        rvSearchGlobal.setLayoutManager(new LinearLayoutManager(context));
        setRekomen();
    }

    private void setRekomen(){
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, "http://pharmanet.apodoc.id/customer/Searchglobal.php", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray resultApotek = response.getJSONArray("resultApotek");
                    JSONArray resultProduct = response.getJSONArray("resultProduct");
                    listRekomenProduct.clear();
                    listRekomenApotek.clear();

                    for (int i = 0; i<resultApotek.length(); i++)
                    {
                        JSONObject result = resultApotek.getJSONObject(i);
                        listRekomenApotek.add(result.getString("SearchResult"));
                    }
                    for (int i = 0; i<resultProduct.length(); i++)
                    {
                        JSONObject result = resultProduct.getJSONObject(i);
                        listRekomenProduct.add(result.getString("SearchResult"));
                    }
                    Log.d("ListCategory",listRekomenApotek.size()+"");
                    Log.d("ListProduct",listRekomenProduct.size()+"");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity.this);
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
            dots[z] = new ImageView(HomeActivity.this);
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
    @Override
    public void onBackPressed() {
        builder.setMessage("Apakah Anda ingin keluar dari aplikasi ?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                return;
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        dialog = builder.show();

    }


    public  int getContentViewId () {
        return R.layout.activity_home;
     }

    public  int getNavigationMenuItemId () {
            return R.id.navigation_home;
    }

    public  void setStatusBarGradiant(Activity activity) {
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

////        cardListBrand.setHasFixedSize(true);
////        cardListBrand.setVisibility(View.VISIBLE);
////        cardListBrand2.setHasFixedSize(true);
////        cardListBrand2.setVisibility(View.VISIBLE);
////        cardListBrand3.setHasFixedSize(true);
////        cardListBrand3.setVisibility(View.VISIBLE);
//
////        userLocalStore = new UserLocalStore(this);
////        userLocalStore.getUserLoggedIn();

//        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        // Berhenti berputar/refreshing
//                        swiperefresh.setRefreshing(false);
//                        // fungsi-fungsi lain yang dijalankan saat refresh berhenti
//                        pa.notifyDataSetChanged();
//                    }
//                }, 3500));
//                editText.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        startActivity(new Intent(HomeActivity.this, Search_Activity.class));
//                    }
//                });
//            }
//

//        show_view(cardListBrand, pr, "http://pharmanet.apodoc.id/select_product_promo.php");
//        show_view(cardListBrand2, pr2, "http://pharmanet.apodoc.id/select_product_rekomendasi.php");
//        show_view(cardListBrand3, pr3, "http://pharmanet.apodoc.id/select_product_terlaris.php");
//
