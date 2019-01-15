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

public class SearchResultApotek extends AppCompatActivity {
    public static String urlbawahs = "http://pharmanet.apodoc.id/customer/selectCurrentCartCustomer.php?CustomerID=";
    public static String add_url = "http://pharmanet.apodoc.id/customer/addCartCustomer.php";

    static TextView tvApotekName,tvApotekAddress,tvApotekhoneNumber,tvApotekOperationalHour;
    TextView btnSelengFav,btnSelengPromo;
    ImageView btnCancelSearch;
    ImageButton buyBtn;
    EditText search;
    static RatingBar rbApotek;
    public static RecyclerView rvObatPromo, rvObatFavorite, rvAllProduct;
    public static String apotekk;
    public static String urlPromo="http://pharmanet.apodoc.id/customer/select_obat_promo_outlet.php?OutletName=";
    public static String urlFavorite="http://pharmanet.apodoc.id/customer/select_obat_favorite_outlet.php?OutletName=";
    public static String urlAllProduct="http://pharmanet.apodoc.id/customer/select_all_product_outlet.php?OutletName=";

    private static JSONArray Obat = new JSONArray();

    public static SearchResultApotekAdapter searchresultApotekAdapter;
    public static SearchResultApotekAdapter searchresultApotekAdapterfavorit;
    public static SearchResultApotekAdapter searchresultApotekAdapterAllProduct;

    //    public static PromoSelengkapnyaAdapter FavAdapter;
    static int total_rating,outletProductPrice;
    static String outletID, OutletOprOpen, OutletOprClose, outletAddress ,outletPhone;
    public static int diskon;
    static List<apotek> ApotekList = new ArrayList<>();
    public static List<ModelPromo> PromoList = new ArrayList<>();
    public static List<ModelPromo> FavList = new ArrayList<>();
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
        setContentView(R.layout.activity_search_result_apotek);
        tvApotekName = findViewById(R.id.tv_ApotekNameResult);
        tvApotekAddress = findViewById(R.id.tv_address_apotek_result);
        buyBtn = (ImageButton) findViewById(R.id.buyBtn);
        btnCancelSearch = findViewById(R.id.btnCancelSearch);
        search = findViewById(R.id.search);
        //tvApotekAddress.setText(ap.getAddress());
        tvApotekhoneNumber = findViewById(R.id.tv_PhoneNumber);
        tvApotekOperationalHour = findViewById(R.id.tv_OperationalHourApotek);
        rbApotek = findViewById(R.id.rbApotek);
        //rbApotek.setRating(rbApotek.getRating());
        rbApotek.setEnabled(false);
        rvObatPromo = findViewById(R.id.rvProdukPromo);
        rvObatPromo.setHasFixedSize(true);
        rvObatPromo.setLayoutManager(new LinearLayoutManager(this));

        rvObatFavorite = findViewById(R.id.rvProdukFavaorit);
        rvObatFavorite.setHasFixedSize(true);
        rvObatFavorite.setLayoutManager(new LinearLayoutManager(this));



//        rvAllProduct.setLayoutManager(new GridLayoutManager(this));
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,2);
//        rvAllProduct.setLayoutManager(gridLayoutManager);

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


        LinearLayoutManager llPromo = new LinearLayoutManager(this);
        llPromo.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvObatPromo.setLayoutManager(llPromo);

//        LinearLayoutManager llAllProduk = new LinearLayoutManager(this);
//        llAllProduk.setOrientation(LinearLayoutManager.HORIZONTAL);
//        rvAllProduct.setLayoutManager(llAllProduk);

        Intent intent = getIntent();
        apotekk =  intent.getStringExtra("ApotekName");
        Log.d("test", "jass: "+apotekk);
        tvApotekName.setText(apotekk);
        Toolbar dToolbar = findViewById(R.id.toolbarResultApotek);
        dToolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        dToolbar.setTitle(apotekk);
        dToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if(apotekk.contains(" ")){
            apotekk = apotekk.replace(" ","%20");
        }

        rvObatFavorite = findViewById(R.id.rvProdukFavaorit);
        rvObatFavorite.setHasFixedSize(true);
        LinearLayoutManager llFavorite = new LinearLayoutManager(this);
        llFavorite.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvObatFavorite.setLayoutManager(llFavorite);

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
                Intent i = new Intent(getApplicationContext(),FavoritSelengkapnyaActivity.class);
                i.putExtra("link","http://pharmanet.apodoc.id/customer/select_selengkapnya_favorite.php?OutletName="+apotekk);
                startActivity(i);
            }
        });

        showApotek();
        showView(rvObatPromo,urlPromo+apotekk);
        showViewFav();
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
                startActivity(new Intent(SearchResultApotek.this, CartActivity.class));
            }
        });

        initiateTopAdapter();
    }

    private void initiateTopAdapter(){
        rvAllProduct = findViewById(R.id.rvAllProduk);
        rvAllProduct.setHasFixedSize(false); //biar tidak double scrollnya (false)
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),3);
        rvAllProduct.setLayoutManager(gridLayoutManager);
        showViewAll();

        rvAllProduct.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true;

//                    addProductLain();
//                    finish();
//                    Toast.makeText(SearchResultApotek.this, "asdsasadasdasd", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = gridLayoutManager.getChildCount();
                totalItems = gridLayoutManager.getSpanCount();
                scrollOutItems = gridLayoutManager.findFirstVisibleItemPosition();
//                Toast.makeText(SearchResultApotek.this, "currentitem = "+currentItems, Toast.LENGTH_SHORT).show();
//                Toast.makeText(SearchResultApotek.this, "totalItem = "+totalItems, Toast.LENGTH_SHORT).show();
//                Toast.makeText(SearchResultApotek.this, "scrollOut = "+scrollOutItems, Toast.LENGTH_SHORT).show();

                //benerin ifnya biar ketahuan kalau sudah mentok
//                if(isScrolling && ((currentItems + scrollOutItems) < totalItems)&& countAddList!= Obat.length()){
//                    isScrolling=false;
//                    addProductLain();
//                    Toast.makeText(SearchResultApotek.this, "2222222222222222222", Toast.LENGTH_SHORT).show();

                if(isScrolling && ((currentItems + scrollOutItems) < totalItems)&& countAddList!= Obat.length()){
                    isScrolling=false;
                    addProductLain();
//                    Toast.makeText(SearchResultApotek.this, "2222222222222222222", Toast.LENGTH_SHORT).show();

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

    public static void showApotek() {
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
                Toast.makeText(context, "error loading obat", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue req = Volley.newRequestQueue(context);
        req.add(rec1);
    }

    public static void showView(final RecyclerView cardlist, String url) {
//        url ="http://pharmanet.apodoc.id/customer/select_apotek_result.php?"+apotekk;
        JsonObjectRequest rec1= new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray Obats = null;
                try {
                    Obats = response.getJSONArray("result");
                    PromoList.clear();
//                    Toast.makeText(context, "promo"+Obats.length(), Toast.LENGTH_SHORT).show();
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
//                        Toast.makeText(context, ""+obj.getString("productName"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
                searchresultApotekAdapter = new SearchResultApotekAdapter(PromoList,context);
                cardlist.setAdapter(searchresultApotekAdapter);//
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "error loading obat", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue req = Volley.newRequestQueue(context);
        req.add(rec1);
    }

    public static void showViewFav() {
        JsonObjectRequest rec= new JsonObjectRequest(urlFavorite+apotekk, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray Obat = null;
                try {
                    Obat = response.getJSONArray("result");
                    FavList.clear();
//                    Toast.makeText(context, "fs"+Obat.length(), Toast.LENGTH_SHORT).show();
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
//                        Toast.makeText(context, ""+obj.getString("productName"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
                searchresultApotekAdapterfavorit = new SearchResultApotekAdapter(FavList,context);
                rvObatFavorite.setAdapter(searchresultApotekAdapterfavorit);
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

    public static void showViewAll() {
        JsonObjectRequest rec= new JsonObjectRequest(urlAllProduct+apotekk, null, new Response.Listener<JSONObject>() {
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
                pgBot.setVisibility(View.VISIBLE);
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
    static String urlbawah = "http://pharmanet.apodoc.id/customer/selectCurrentCartCustomer.php";

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
//       showApotek();
        showView(rvObatPromo,urlPromo+apotekk);
        showViewFav();
        show_cart(SearchResultApotek.urlbawahs,memberID);
        showViewAll();
        initBottomSheet();
        super.onResume();
        if(session.getUserLoggedIn()){
//            not_empty.setVisibility(not_empty.VISIBLE);
            //show_cart(urlbawah, memberID);
//            show_cart(urlbawah,Integer.parseInt(CustomerID), Outlet_ID);
        }

    }

    @Override
    protected void onRestart() {
        showApotek();
        showView(rvObatPromo,urlPromo+apotekk);
        showViewFav();
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