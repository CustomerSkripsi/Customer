package mobi.garden.bottomnavigationtest.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
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
import com.nex3z.notificationbadge.NotificationBadge;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import mobi.garden.bottomnavigationtest.Adapter.cart_adapter;
import mobi.garden.bottomnavigationtest.LoginRegister.User;
import mobi.garden.bottomnavigationtest.LoginRegister.UserLocalStore;
import mobi.garden.bottomnavigationtest.Model.obat;
import mobi.garden.bottomnavigationtest.Adapter.obat_adapter_as;
import mobi.garden.bottomnavigationtest.R;
import mobi.garden.bottomnavigationtest.Slider.SliderIndicator;
import mobi.garden.bottomnavigationtest.Slider.SliderPagerAdapter;
import mobi.garden.bottomnavigationtest.Slider.SliderView;
import mobi.garden.bottomnavigationtest.Slider.ViewPagerAdapter;

public class CartApotekActivity extends AppCompatActivity {

    private SliderPagerAdapter mAdapter;
    private SliderIndicator mIndicator;
    private SliderView sliderView;
    private LinearLayout mLinearLayout;

    ViewPagerAdapter adapter;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    List<String>imageUrls = new ArrayList<>();
    private int dotscount;
    private ImageView[] dots;

    public static Context context;
    public static DecimalFormat df;
    private static obat_adapter_as pa;
    private static RecyclerView cardListBrand;
    private static List<obat> pr = new ArrayList<>();
    private static RecyclerView recyclerViewCartList;
    private static List<obat> cartList = new ArrayList<>();
    private static cart_adapter adapterRvBelow;

    private static TextView tvTotalPrice;
    private static NotificationBadge mBadge;
    private static int totalPrice = 0;
    private static int count = 0;

    private BottomSheetBehavior mBottomSheetBehavior;

    public static final String OUTLET_ID="outlet_id";
    public static final String CATEGORY_ID="category_id";
    public static final String PRODUCT_ID="product_id";
    public static final String OUTLET_NAME = "outlet_name";

    static int Category_ID;
    static String Outlet_ID , Product_ID, Outlet_Name;

    static String CustomerID;

    UserLocalStore userLocalStore;

    ImageButton buyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_apotek);

        context = getApplicationContext();

        df = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setCurrencySymbol("Rp. ");
        dfs.setMonetaryDecimalSeparator(',');
        dfs.setGroupingSeparator('.');
        df.setDecimalFormatSymbols(dfs);
        df.setMaximumFractionDigits(0);

        userLocalStore  = new UserLocalStore(context);
        User currUser = userLocalStore.getLoggedInUser();
        CustomerID = currUser.getUserID();

        Intent i = getIntent();
//        Category_ID = Integer.parseInt(i.getStringExtra(CATEGORY_ID));
//        Outlet_ID = i.getStringExtra(OUTLET_ID);
//        Product_ID = i.getStringExtra(PRODUCT_ID);
//        Outlet_Name = i.getStringExtra(OUTLET_NAME);

        setStatusBarGradiant(this);

        buyBtn = (ImageButton) findViewById(R.id.buyBtn);

        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_as);

        setSupportActionBar(toolbar);
        setTitle(Outlet_Name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_chevron_left_black_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        //slider
//        sliderView = (SliderView) findViewById(R.id.sliderView2);
//        mLinearLayout = (LinearLayout) findViewById(R.id.pagesContainer2);
//        setupSlider();

        viewPager = (ViewPager) findViewById(R.id.sliderView2);
        showImageSlider(viewPager);
        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots2);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new CartApotekActivity.MyTimerTask(), 2000, 4000);



        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        mBadge = findViewById(R.id.badge);

        initBottomSheet();

        recyclerViewCartList = findViewById(R.id.rvCartList);
        recyclerViewCartList.setHasFixedSize(true);
        LinearLayoutManager setLayout = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerViewCartList.setLayoutManager(setLayout);
        //initiateBelowAdapter();

        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CartApotekActivity.this, CartActivity.class));
            }
        });

        cardListBrand = (RecyclerView) findViewById(R.id.rv_cv_obat_terkait);
        cardListBrand.setHasFixedSize(true);
        cardListBrand.setVisibility(View.VISIBLE);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        cardListBrand.setLayoutManager(llm);
        //initiateTopAdapter();

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

                                adapter = new ViewPagerAdapter(CartApotekActivity.this, imageUrls);
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
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(req);
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
            CartApotekActivity.this.runOnUiThread(new Runnable() {
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


    public static void initiateTopAdapter()
    {
        productTerkait(Outlet_ID,Category_ID,Product_ID);
    }
    static String urlbawah = "http://pharmanet.apodoc.id/selectCartCustomer.php";
    public static void initiateBelowAdapter(){ show_cart(urlbawah,Integer.parseInt(CustomerID), Outlet_ID); }


    public static void refresh_cart(List<obat>cartList){
        count=0;
        totalPrice=0;

        for (int i = 0; i < cartList.size(); i++) {
            totalPrice+= cartList.get(i).cartProductPrice*cartList.get(i).cartProductQty;
            count += cartList.get(i).cartProductQty;
        }
        tvTotalPrice.setText(df.format(totalPrice)+"");
        mBadge.setNumber(count);
        adapterRvBelow = new cart_adapter(context,cartList,Integer.parseInt(CustomerID));
        recyclerViewCartList.setAdapter(adapterRvBelow);
    }

    public static void refresh_total_cart(List<obat> cartList){
        count=0;
        totalPrice=0;

        for (int i = 0; i < cartList.size(); i++) {
            totalPrice+= cartList.get(i).cartProductPrice*cartList.get(i).cartProductQty;
            count += cartList.get(i).cartProductQty;
        }
        tvTotalPrice.setText(df.format(totalPrice)+"");
        mBadge.setNumber(count);
    }


    public static void productTerkait(String oID, int cID, String pID) {
        JSONObject objAdd = new JSONObject();
        try {
            JSONArray arrData = new JSONArray();
            JSONObject objDetail = new JSONObject();
            objDetail.put("outletID", oID);
            objDetail.put("categoryID", cID);
            objDetail.put("productID", pID);

            arrData.put(objDetail);
            objAdd.put("data", arrData);
            Log.d("ini_objadd", objAdd.toString());
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, "http://pharmanet.apodoc.id/select_product_terkait.php", objAdd,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray obats = null;
                        try {
                            obats = response.getJSONArray("result");
                            pr.clear();

                            for (int i = 0; i < obats.length(); i++) {
                                try {
                                    cardListBrand.setVisibility(View.VISIBLE);
                                    JSONObject obj = obats.getJSONObject(i);

                                    pr.add(new obat(
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
                                            obj.getString("CategoryID"),
                                            obj.getInt("OutletProductPrice"),
                                            obj.getInt("OutletProductStockQty")));
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                    Toast.makeText(context, e1.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }

                            pa = new obat_adapter_as(context,pr, cartList);
                            cardListBrand.setAdapter(pa);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "No Connection", Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


    public static void show_cart(String urlbawah, int CustomerID, String outletID) {

        JSONObject objAdd = new JSONObject();
        try {
            JSONArray arrData = new JSONArray();
            JSONObject objDetail = new JSONObject();
            objDetail.put("CustomerID", CustomerID);
            objDetail.put("OutletID", outletID );
            arrData.put(objDetail);
            objAdd.put("data", arrData);
            Log.d("ini showcart", objAdd.toString());
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        JsonObjectRequest rec = new JsonObjectRequest(urlbawah, objAdd, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray products = null;
                try {
                    products = response.getJSONArray("result");
                    cartList.clear();
                    count=0;
                    totalPrice=0;
                    for (int i = 0; i < products.length(); i++) {
                        try {
                            recyclerViewCartList.setVisibility(View.VISIBLE);
                            JSONObject obj = products.getJSONObject(i);
                            cartList.add(new obat(
                                    obj.getString("ProductName"),
                                    obj.getString("ProductID"),
                                    obj.getInt("OutletProductStockQty"),
                                    obj.getInt("CartProductPrice"),
                                    obj.getInt("CartProductQty")
                            ));

                            totalPrice += obj.getInt("CartProductQty")*obj.getInt("CartProductPrice");
                            count += obj.getInt("CartProductQty");

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                            Toast.makeText(context, e1.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    tvTotalPrice.setText(df.format(totalPrice)+"");
                    mBadge.setNumber(count);

                    //Toast.makeText(context, cartList.size()+"", Toast.LENGTH_SHORT).show();
                    adapterRvBelow = new cart_adapter(context,cartList,1);
                    recyclerViewCartList.setAdapter(adapterRvBelow);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(Main2Activity.this, "error", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(rec);
    }

    private void initBottomSheet() {
        //BOTTOM SHEET STATE
        View bottomSheet = findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    protected void onResume() {
        super.onResume();
        show_cart(urlbawah,Integer.parseInt(CustomerID), Outlet_ID);
        productTerkait(Outlet_ID,Category_ID,Product_ID);
    }
}
