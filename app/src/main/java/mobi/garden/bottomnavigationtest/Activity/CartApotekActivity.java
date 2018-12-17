package mobi.garden.bottomnavigationtest.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
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
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import mobi.garden.bottomnavigationtest.Adapter.cart_adapter;
import mobi.garden.bottomnavigationtest.Adapter.obat_adapter_as;
import mobi.garden.bottomnavigationtest.LoginRegister.UserLocalStore;
import mobi.garden.bottomnavigationtest.Model.obat;
import mobi.garden.bottomnavigationtest.R;
import mobi.garden.bottomnavigationtest.Session.SessionManagement;
import mobi.garden.bottomnavigationtest.Slider.SliderIndicator;
import mobi.garden.bottomnavigationtest.Slider.SliderPagerAdapter;
import mobi.garden.bottomnavigationtest.Slider.SliderView;
import mobi.garden.bottomnavigationtest.Slider.ViewPagerAdapter;

public class CartApotekActivity extends AppCompatActivity {

    private SliderPagerAdapter mAdapter;
    private SliderIndicator mIndicator;
    private SliderView sliderView;
    private LinearLayout mLinearLayout;
    public static String add_url = "http://pharmanet.apodoc.id/customer/addCartCustomer.php";

    ViewPagerAdapter adapter;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    List<String>imageUrls = new ArrayList<>();
    private int dotscount;
    private ImageView[] dots;

    public static Context context;
    public static DecimalFormat df;

    obat_adapter_as obatAdapter;
    List<obat> pr = new ArrayList<>();

    private static RecyclerView cardListBrand;
    private static RecyclerView recyclerViewCartList;
    private static List<obat> cartList = new ArrayList<>();
    private static cart_adapter adapterRvBelow;
    RecyclerView rvProdukAll;
    RecyclerView rvCart;

    private static TextView tvApotekName;
    private static TextView tvTotalPrice;
    private static NotificationBadge mBadge;
    private static int totalPrice = 0;
    private static int count = 0;

    private BottomSheetBehavior mBottomSheetBehavior;


    public static final String OUTLET_ID="outlet_id";
    public static final String CATEGORY_ID="category_id";
    public static final String PRODUCT_ID="product_id";
    public static final String OUTLET_NAME = "outlet_name";


    static String Outlet_ID , Product_ID, Outlet_Name , namaApotek;
    String namaproduk,tempfoto,idProduk;
    int qty, hargaproduk;

   // static String CustomerID;
    ImageButton buyBtn;
    UserLocalStore userLocalStore;

    //login
    SessionManagement session;
    HashMap<String, String> login;
    public static String CustomerID,memberID, userName;
    static LinearLayout empty, not_empty, not_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_apotek);

        session = new SessionManagement(getApplicationContext());
        login = session.getMemberDetails();
        userName= login.get(SessionManagement.USERNAME);
        memberID = login.get(SessionManagement.KEY_KODEMEMBER);
        context = getApplicationContext();

        Toast.makeText(this, memberID + " cartapotekact", Toast.LENGTH_SHORT).show();

        df = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setCurrencySymbol("Rp. ");
        dfs.setMonetaryDecimalSeparator(',');
        dfs.setGroupingSeparator('.');
        df.setDecimalFormatSymbols(dfs);
        df.setMaximumFractionDigits(0);

//        userLocalStore  = new UserLocalStore(context);
//        User currUser = userLocalStore.getLoggedInUser();
//        CustomerID = currUser.getUserID();

        Intent i = getIntent();
        //Category_ID = Integer.parseInt(i.getStringExtra(CATEGORY_ID));
        Outlet_ID = i.getStringExtra(OUTLET_ID);
        Product_ID = i.getStringExtra(PRODUCT_ID);
        Outlet_Name = i.getStringExtra(OUTLET_NAME);
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

        viewPager = (ViewPager) findViewById(R.id.sliderView2);
        showImageSlider(viewPager);
        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots2);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new CartApotekActivity.MyTimerTask(), 2000, 4000);

        Intent intent = getIntent();
        namaApotek =  intent.getStringExtra("ApotekName");
        Log.d("test", "jass: "+namaApotek);
        if(namaApotek.contains(" ")){
            namaApotek = namaApotek.replace(" ","%20");
        }

        tvApotekName = findViewById(R.id.tv_apotekname);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        mBadge = findViewById(R.id.badge);

        initBottomSheet();
        rvProdukAll = findViewById(R.id.rvProdukAll);
        GridLayoutManager lay = new GridLayoutManager(context,3);
        rvProdukAll.setLayoutManager(lay);

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

        cardListBrand = (RecyclerView) findViewById(R.id.rvCartList);
        cardListBrand.setHasFixedSize(true);
        cardListBrand.setVisibility(View.VISIBLE);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        cardListBrand.setLayoutManager(llm);



        initiateBelowAdapter();
        showprodukterkait();
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

    public static void initiateTopAdapter() {
        //  productTerkait(Outlet_ID,Category_ID,Product_ID);
    }
    static String urlbawah = "http://pharmanet.apodoc.id/customer/selectCurrentCartCustomer.php";

    private void initiateBelowAdapter(){
        rvCart = findViewById(R.id.rvCartList);
        rvCart.setHasFixedSize(true);
        LinearLayoutManager setLayout = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
        rvCart.setLayoutManager(setLayout);
                String urlbawahs = "http://pharmanet.apodoc.id/customer/selectCurrentCartCustomer.php?CustomerID=";
        show_cart(urlbawahs,"8181200006");
    }

    public static void show_cart(String urlbawah, String memberID) {
        JsonObjectRequest rec = new JsonObjectRequest(urlbawah+memberID, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray products = null;
                try {
                    products = response.getJSONArray("result");
                    cartList.clear();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                count=0;
                totalPrice=0;
                for (int i = 0; i < products.length(); i++) {
//                    Toast.makeText(CartApotekActivity.context, "masuk sini", Toast.LENGTH_SHORT).show();
                    try {
                        recyclerViewCartList.setVisibility(View.VISIBLE);
                        JSONObject obj = products.getJSONObject(i);
                        cartList.add(new obat(
                                obj.getString("ProductName"),
                                obj.getString("ProductID"),
                                obj.getInt("CartProductQty"),
                                obj.getInt("CartProductPrice"),
                                obj.getInt("CartProductPriceAfterDiscount")));
                        Toast.makeText(context, ""+obj.getString("ProductName"), Toast.LENGTH_SHORT).show();
                        totalPrice += obj.getInt("CartProductQty")*obj.getInt("CartProductPrice");
                        count += obj.getInt("CartProductQty");
                        Log.d("rwarqwe", obj.toString());
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                        Toast.makeText(context, e1.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                tvTotalPrice.setText(df.format(totalPrice)+"");
                mBadge.setNumber(count);
                Toast.makeText(context, cartList.size()+"sudah", Toast.LENGTH_SHORT).show();
                adapterRvBelow = new cart_adapter(context,cartList);
                recyclerViewCartList.setAdapter(adapterRvBelow);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
    //           Toast.makeText(Main2Activity.this, "error", Toast.LENGTH_SHORT).show();
                Log.d("ini errors", "onErrorResponse: ");
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

    public void showprodukterkait(){
        String url = "http://pharmanet.apodoc.id/customer/showProductTerkait.php?ApotekName="+namaApotek;
        JsonObjectRequest req = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray result = null;
                try {
                    result = response.getJSONArray("result");
                    pr.clear();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for(int i=0; i< result.length();i++){
                    try {
                        JSONObject object = result.getJSONObject(i);
                        idProduk = object.getString("ProductID");
                        namaproduk = object.getString("ProductName");
                        tempfoto = object.getString("ProductImage");
                        qty = object.getInt("OutletProductStockQty");
                        hargaproduk = object.getInt("OutletProductPrice");
                        Log.d("awasdaasd", namaproduk);
                        pr.add(new obat(idProduk,namaproduk,tempfoto,hargaproduk,qty));
                        //Toast.makeText(context, "pjg:"+result.length(), Toast.LENGTH_SHORT).show();
                        Log.d("rwarfgss", object.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                tvApotekName.setText(namaApotek);
                obatAdapter = new obat_adapter_as(pr,context);
                rvProdukAll.setAdapter(obatAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CartApotekActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(req);
    }

    public static void refresh_cart(List<obat>cartList){
        count=0;
        totalPrice=0;

        for (int i = 0; i < cartList.size(); i++) {
            totalPrice+= cartList.get(i).cartProductPrice*cartList.get(i).cartProductQty;
            count += cartList.get(i).cartProductQty;
        }
        tvTotalPrice.setText(df.format(totalPrice)+"");
        mBadge.setNumber(count);
        adapterRvBelow = new cart_adapter(context,cartList);
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



    protected void onResume() {
        super.onResume();
        if(session.getUserLoggedIn()){
//            not_empty.setVisibility(not_empty.VISIBLE);
            //show_cart(urlbawah, memberID);
//            show_cart(urlbawah,Integer.parseInt(CustomerID), Outlet_ID);
        }



    }
}






//    public static void productTerkait(String oID, int cID, String pID) {
//        JSONObject objAdd = new JSONObject();
//        try {
//            JSONArray arrData = new JSONArray();
//            JSONObject objDetail = new JSONObject();
//            objDetail.put("outletID", oID);
//            objDetail.put("categoryID", cID);
//            objDetail.put("productID", pID);
//
//            arrData.put(objDetail);
//            objAdd.put("data", arrData);
//            Log.d("ini_objadd", objAdd.toString());
//        } catch (JSONException e1) {
//            e1.printStackTrace();
//        }
//        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, "http://pharmanet.apodoc.id/customer/showProductTerkait.php?ApotekName="+namaApotek, objAdd,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        JSONArray obats = null;
//                        try {
//                            obats = response.getJSONArray("result");
//                            //pr.clear();
//
//                            for (int i = 0; i < obats.length(); i++) {
//                                try {
//                                    cardListBrand.setVisibility(View.VISIBLE);
//                                    JSONObject obj = obats.getJSONObject(i);
////
////                                    pr.add(new obat(
////                                            obj.getString("ProductID"),
////                                            obj.getString("ProductName"),
////                                            obj.getString("ProductImage"),
////                                            //obj.getString("ProductDescription"),
////                                            obj.getString("ProductIndicationUsage"),
////                                            obj.getString("ProductIngredients"),
////                                            obj.getString("ProductDosage"),
////                                            obj.getString("ProductHowToUse"),
////                                            obj.getString("ProductPackage"),
////                                            obj.getString("ProductClassification"),
////                                            obj.getString("ProductRecipe"),
////                                            obj.getString("ProductContraindication"),
////                                            obj.getString("ProductStorage"),
////                                            obj.getString("PrincipalName"),
////                                            obj.getString("CategoryID"),
////                                            obj.getInt("OutletProductPrice"),
////                                            obj.getInt("OutletProductStockQty")));
//                                } catch (JSONException e1) {
//                                    e1.printStackTrace();
//                                    Toast.makeText(context, e1.getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//
//                            }
////                            tvApotekName.setText(namaApotek);
////                            pa = new obat_adapter_as(context,pr, cartList);
////                            cardListBrand.setAdapter(pa);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(context, "No Connection", Toast.LENGTH_SHORT).show();
//                    }
//                });
//        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        requestQueue.add(stringRequest);
//    }