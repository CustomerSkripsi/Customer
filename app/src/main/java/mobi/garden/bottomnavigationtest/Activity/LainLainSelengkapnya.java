package mobi.garden.bottomnavigationtest.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import mobi.garden.bottomnavigationtest.Adapter.CartSearchResultApotekAdapter;
import mobi.garden.bottomnavigationtest.Adapter.SearchResultApotekAdapter;
import mobi.garden.bottomnavigationtest.Model.ModelPromo;
import mobi.garden.bottomnavigationtest.Model.apotek;
import mobi.garden.bottomnavigationtest.Model.obat;
import mobi.garden.bottomnavigationtest.R;
import mobi.garden.bottomnavigationtest.Session.SessionManagement;

public class LainLainSelengkapnya extends AppCompatActivity {
    public static String urlbawahs = "http://pharmanet.apodoc.id/customer/selectCurrentCartCustomer.php?CustomerID=";
    public static String add_url = "http://pharmanet.apodoc.id/customer/addCartCustomer.php";

    static TextView tvApotekName,tvApotekAddress,tvApotekhoneNumber,tvApotekOperationalHour;
    TextView btnSelengFav,btnSelengPromo,btnSelengkapnyaLain2;
    ImageView btnCancelSearch;
    ImageButton buyBtn;
    EditText search;
    static RatingBar rbApotek;
    public static RecyclerView  rvAllProduct;
    public static String apotekk;
    public static String urlAllProduct="http://pharmanet.apodoc.id/customer/select_all_product_outlet.php?OutletName=";

    private static JSONArray Obat = new JSONArray();

    public static SearchResultApotekAdapter searchresultApotekAdapterAllProduct;

    static int total_rating,outletProductPrice;
    static String outletID, OutletOprOpen, OutletOprClose, outletAddress ,outletPhone;
    public static int diskon;
    public static List<ModelPromo> allProduct = new ArrayList<>();
    public static List<ModelPromo> allProductList = new ArrayList<>();

    public static Context context;
    public static DecimalFormat df;

    public static List<obat> cartList = new ArrayList<>();
    private static RecyclerView recyclerViewCartList;
    public static String temp;
    private static CartSearchResultApotekAdapter adapterRvBelow;
    RecyclerView rvCart;

    private static TextView tvTotalPrice;
    private static NotificationBadge mBadge;
    private static int totalPrice = 0;
    private static int count = 0;
    private static int sizeproduct,currentItems,totalItems,scrollOutItems;
    private static int countAddList;
    private static ProgressBar pgBot;
    private boolean isScrolling = false;
//    private static SwipeRefreshLayout swipeRefreshLayout;


    //login
    SessionManagement session;
    HashMap<String, String> login;
    public static String CustomerID,memberID, userName;


    private BottomSheetBehavior mBottomSheetBehavior;

    ModelPromo mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lain_lain_selengkapnya);
        tvApotekName = findViewById(R.id.tv_ApotekNameResult);
        tvApotekAddress = findViewById(R.id.tv_address_apotek_result);
        buyBtn = (ImageButton) findViewById(R.id.buyBtn);
        btnCancelSearch = findViewById(R.id.btnCancelSearch);
        search = findViewById(R.id.search);
        pgBot = findViewById(R.id.pgBot);

        countAddList = 20;
//        swipeRefreshLayout = findViewById(R.id.swipe);
//        swipeRefreshLayout.setRefreshing(true);

//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                countAddList = 20;
//                initiateTopAdapter();
//                initiateBelowAdapter();
//            }
//        });
        Intent intent = getIntent();
        apotekk =  intent.getStringExtra("ApotekName");
        Log.d("test", "jass: "+apotekk);

        session = new SessionManagement(getApplicationContext());
        login = session.getMemberDetails();
        userName= login.get(SessionManagement.USERNAME);
        memberID = login.get(SessionManagement.KEY_KODEMEMBER);
        context = getApplicationContext();

        df = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setCurrencySymbol("Rp. ");
        dfs.setMonetaryDecimalSeparator('.');
        dfs.setGroupingSeparator('.');
        df.setDecimalFormatSymbols(dfs);
        df.setMaximumFractionDigits(0);

        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        mBadge = findViewById(R.id.badge);

        showViewAll();
        initBottomSheet();

        recyclerViewCartList = findViewById(R.id.rvCartList);
        recyclerViewCartList.setHasFixedSize(true);
        LinearLayoutManager setLayout = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerViewCartList.setLayoutManager(setLayout);
        initiateBelowAdapter();

        btnCancelSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!search.getText().toString().isEmpty()){
                    search.getText().clear();

                }
            }
        });

        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LainLainSelengkapnya.this, CartActivity.class));
            }
        });

        initiateTopAdapter();
    }

    private void initiateTopAdapter(){
        rvAllProduct = findViewById(R.id.rvLainLainSelengkapnya);
        rvAllProduct.setHasFixedSize(true); //biar tidak double scrollnya (false)
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),3);
        rvAllProduct.setLayoutManager(gridLayoutManager);
        showViewAll();

        rvAllProduct.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true;
//                    Toast.makeText(LainLainSelengkapnya.this, "asdqweasdqwe", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = gridLayoutManager.getChildCount();
                totalItems = gridLayoutManager.getItemCount();
                scrollOutItems = gridLayoutManager.findFirstVisibleItemPosition();
                Log.d("adeh", String.valueOf(currentItems));
                Log.d("adeh1", String.valueOf(totalItems));
                Log.d("adeh2", String.valueOf(scrollOutItems));
                if(isScrolling && (currentItems + scrollOutItems == totalItems) && countAddList!=Obat.length()){
                    isScrolling=false;
//                    Toast.makeText(LainLainSelengkapnya.this, "sdasdqoijoijoj", Toast.LENGTH_SHORT).show();
                    addProductLain();
                }
            }
        });

    }

    private void initiateBelowAdapter(){
        rvCart = findViewById(R.id.rvCartList);
        rvCart.setHasFixedSize(true);
        LinearLayoutManager setLayout = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
        rvCart.setLayoutManager(setLayout);

        show_cart(urlbawahs,memberID);
    }

    public static void showViewAll() {
        JsonObjectRequest rec= new JsonObjectRequest(urlAllProduct+"GUDANG PHARMANET", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                sizeproduct=0;
                try {
                    Obat = response.getJSONArray("result");
                    allProductList.clear();
                    Log.d("tyu", Obat.length()+"");

                    for (int i = sizeproduct; i < sizeproduct+20; i++) {
                        try {
                            rvAllProduct.setVisibility(View.VISIBLE);
                            JSONObject obj = Obat.getJSONObject(i);
                            allProductList.add(new ModelPromo(
                                    obj.getString("ProductID"),
                                    obj.getString("ProductName"),
                                    obj.getString("ProductImage"),
                                    obj.getInt("OutletID"),
                                    obj.getInt("OutletProductPrice"),
                                    diskon));
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                    }

                    for (int i = sizeproduct; i < Obat.length(); i++) {
                        try {
                            rvAllProduct.setVisibility(View.VISIBLE);

                            JSONObject obj = Obat.getJSONObject(i);
                            if(obj.getString("ProductPriceAfterDiscount").equals("null")){
                                diskon =  0; }else{ diskon =  obj.getInt("ProductPriceAfterDiscount"); }
                            allProduct.add(new ModelPromo(
                                    obj.getString("ProductID"),
                                    obj.getString("ProductName"),
                                    obj.getString("ProductImage"),
                                    obj.getInt("OutletID"),
                                    obj.getInt("OutletProductPrice"),
                                    diskon
                            ));
                            Log.d("asdtest", obj.toString());
                            Toast.makeText(context, ""+obj.getString("productName"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }

                    searchresultApotekAdapterAllProduct = new SearchResultApotekAdapter(allProductList,context);
                    searchresultApotekAdapterAllProduct.setProductList(allProductList);
                    searchresultApotekAdapterAllProduct.setProductListFull(allProduct);
                    //biar tidak double scrollnya // tapi tidak ketahuan kalau lagi discroll saat nambah barang per 20
//                    rvAllProduct.setNestedScrollingEnabled(false);
                    rvAllProduct.setAdapter(searchresultApotekAdapterAllProduct);
                    sizeproduct+=20;
                    Log.d("zxcad", String.valueOf(allProduct.size()));
                    Log.d("zxcad1", String.valueOf(Obat.length()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


//                searchresultApotekAdapterAllProduct = new SearchResultApotekAdapter(AllProduct,context);
//                rvAllProduct.setNestedScrollingEnabled(false); //biar tidak double scrollnya
//                rvAllProduct.setAdapter(searchresultApotekAdapterAllProduct);
//                sizeproduct+=3;
//                swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(context, "error loading obatttt", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue req = Volley.newRequestQueue(context);
        req.add(rec);
    }

    public static void addProductLain(){
        Toast.makeText(context, "ADDPRODUCT20", Toast.LENGTH_SHORT).show();
        pgBot.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = sizeproduct; i < sizeproduct+20; i++) {
                    try {
                        JSONObject obj = Obat.getJSONObject(i);
                        allProductList.add(new ModelPromo(
                                obj.getString("ProductID"),
                                obj.getString("ProductName"),
                                obj.getString("ProductImage"),
                                obj.getInt("OutletID"),
                                obj.getInt("OutletProductPrice"),
                                diskon));
                        countAddList+=1;
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }

                sizeproduct+=20;
                searchresultApotekAdapterAllProduct.notifyDataSetChanged();
                pgBot.setVisibility(View.GONE);
//                swipeRefreshLayout.setRefreshing(false);
            }
        },2000);
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

    public static void show_cart(String urlbawahs, String memberID) {
        Log.d("testurl", urlbawahs+ memberID);
        JsonObjectRequest rec = new JsonObjectRequest(urlbawahs + memberID, null, new Response.Listener<JSONObject>() {
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
                    try {
                        recyclerViewCartList.setVisibility(View.VISIBLE);
                        JSONObject obj = products.getJSONObject(i);
                        cartList.add(new obat(
                                obj.getString("ProductName"),
                                obj.getString("ProductID"),
                                obj.getInt("CartProductQty"),
                                obj.getInt("OutletProductStockQty"),
                                obj.getInt("CartProductPrice"),
                                obj.getInt("CartProductPriceAfterDiscount")));

                        temp = obj.getString("ProductName");
//                        Toast.makeText(context, ""+obj.getString("ProductName"), Toast.LENGTH_SHORT).show();
                        totalPrice += obj.getInt("CartProductQty")*obj.getInt("CartProductPrice");
                        count += obj.getInt("CartProductQty");
                        Log.d("yyyyyy", obj.toString());
                    } catch (JSONException e1) {
                        e1.printStackTrace();
//                        Toast.makeText(context, e1.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                tvTotalPrice.setText(df.format(totalPrice)+"");
                mBadge.setNumber(cartList.size());
                adapterRvBelow = new CartSearchResultApotekAdapter(context,cartList);
                adapterRvBelow.setCartList(cartList);
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


    public static void refresh_cart(List<obat>cartList){
        count=0;
        totalPrice=0;

        for (int i = 0; i < cartList.size(); i++) {
            totalPrice+= cartList.get(i).cartProductPrice*cartList.get(i).cartProductQty;
            count += cartList.get(i).cartProductQty;
        }
        tvTotalPrice.setText(df.format(totalPrice)+"");
        mBadge.setNumber(count);

        adapterRvBelow = new CartSearchResultApotekAdapter(context,cartList);
        adapterRvBelow.setCartList(cartList);
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
        show_cart(SearchResultApotek.urlbawahs,memberID);
        showViewAll();
        initBottomSheet();
        super.onResume();
        if(session.getUserLoggedIn()){

        }

    }

    @Override
    protected void onRestart() {
        showViewAll();
        show_cart(SearchResultApotek.urlbawahs,memberID);
        initBottomSheet();
        super.onRestart();

    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}