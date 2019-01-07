package mobi.garden.bottomnavigationtest.Activity;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mobi.garden.bottomnavigationtest.Adapter.CartHomeAdapter;
import mobi.garden.bottomnavigationtest.Adapter.cart_adapter2;
import mobi.garden.bottomnavigationtest.LoginRegister.UserLocalStore;
import mobi.garden.bottomnavigationtest.Model.ModelPromo;
import mobi.garden.bottomnavigationtest.Model.cart;
import mobi.garden.bottomnavigationtest.R;
import mobi.garden.bottomnavigationtest.Session.SessionManagement;

public class CartActivity extends AppCompatActivity {

    static TextView info, outlet_name, jumlah, jumlahPembayaran, alamat, biaya_pengiriman;
    static LinearLayout empty, not_empty, not_login;
    private static int total = 0;
    private static int totalPembayaran = 0;
    static RadioButton r_delivery, r_apotek;
    static RadioGroup r_group;
    static DecimalFormat df;
    static List<cart> cartlist = new ArrayList<>();
    static RecyclerView recyclerViewCartList;
    public static cart_adapter2 adapter;

    public static String urlbawah = "http://pharmanet.apodoc.id/selectCartCustomer2.php";
    public static Context context;

    public static final String insertTransaction = "http://pharmanet.apodoc.id/addHeaderTransaction.php";

    //static final String CUSTOMER_ID = "CustomerID";
    public static String CustomerID,memberID, userName;
    TextView dialog_Nama, dialog_No_Telepon, dialog_Alamat;
    Button btnLanjutPembelian,btnTambahBarang, button_lanjut, button_Batal, btn_login_cart,button_lanjut1, button_Batal1;
    Dialog myDialog,myDialog1;
    String urlUser = "Http://Pharmanet.Apodoc.id/select_customer_confirm.php?id=";
    static String OutletName;
    UserLocalStore userlocal;


    SessionManagement session;
    HashMap<String, String> login;


    static List<ModelPromo> CartList = new ArrayList<>();
    CartHomeAdapter cartHomeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        context = getApplicationContext();
        info = (TextView) findViewById(R.id.info);
        outlet_name = (TextView) findViewById(R.id.outlet_name);
        alamat = findViewById(R.id.tvOutletAddress);
        jumlah = findViewById(R.id.totalPembayaran);
        //jumlahPembayaran = findViewById(R.id.totalPembayaran);
        //biaya_pengiriman = findViewById(R.id.biayaPengiriman);
        r_apotek = findViewById(R.id.radio_apotek);
        r_group = findViewById(R.id.radio_group);
        r_apotek.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                Toast.makeText(CartActivity.this, "apotekkks", Toast.LENGTH_SHORT).show();

            }

        });

        empty = (LinearLayout) findViewById(R.id.kosong);
        not_empty = (LinearLayout) findViewById(R.id.tidak_kosong);
        not_login = (LinearLayout) findViewById(R.id.belum_login);
        btn_login_cart = (Button) findViewById(R.id.login_cart);

        df = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setCurrencySymbol("Rp. ");
        dfs.setMonetaryDecimalSeparator('.');
        dfs.setGroupingSeparator('.');
        df.setDecimalFormatSymbols(dfs);
        df.setMaximumFractionDigits(0);

        Toolbar toolbar = findViewById(R.id.toolbar_cart);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        Intent intent = getIntent();
        session = new SessionManagement(getApplicationContext());
        login = session.getMemberDetails();
        userName= login.get(SessionManagement.USERNAME);
        memberID = login.get(SessionManagement.KEY_KODEMEMBER);


        //myAlertDialog();
        setStatusBarGradiant(this);
        recyclerViewCartList = findViewById(R.id.rvCartList2);
        recyclerViewCartList.setHasFixedSize(true);
        LinearLayoutManager setLayout = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerViewCartList.setLayoutManager(setLayout);

///---ini radio
//        r_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                switch (checkedId) {
//                    case R.id.radio_apotek:
//                        Toast.makeText(context, "apotek", Toast.LENGTH_SHORT).show();
//                        break;
//                    default:
//                        break;
//                }
//            }
//        });

        btn_login_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this, MemberActivity.class));
            }
        });
        cartshow();
        btnTambahBarang = findViewById(R.id.btnTambahBarang);
        btnTambahBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // startActivity(new Intent(CartActivity.this , DetailObatHome.class));
                if(OutletName != null) {
                    Intent i = new Intent(context, SearchResultApotek.class);
                    i.putExtra("ApotekName", OutletName);
                    context.startActivity(i);
                }else{
                    Intent i = new Intent(getApplicationContext(),PromoActivity.class);
                    i.putExtra("allpromo","http://pharmanet.apodoc.id/customer/ProductPromoAll.php?input=");
                    startActivity(i);
                }
            }
        });
        
        btnLanjutPembelian = (Button) findViewById(R.id.btnLanjutPembelian);
        btnLanjutPembelian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CartList.size()==0){
                    btnLanjutPembelian.setEnabled(false);
                    Toast.makeText(CartActivity.this, "Anda belum memiliki keranjang belanja. Silahkan belanja terlebih dahulu", Toast.LENGTH_LONG).show();
                }
                else {
                    insertTransaction(insertTransaction, memberID);
                    startActivity(new Intent(CartActivity.this, PickUpActivity.class));
                    //myDialog.cancel();
                }}
        });

    }

    public void BackBack(View view){
        super.onBackPressed();
    }

    public void insertTransaction(String insertTransaction, String CustomerID) {
        JSONObject objAdd = new JSONObject();
        Log.d("ini transaksi", objAdd.toString());
        try {
            JSONArray arrData = new JSONArray();
            JSONObject objDetail = new JSONObject();
            objDetail.put("CustomerID", CustomerID);
            arrData.put(objDetail);
            objAdd.put("data", arrData);
            Log.d("nyas", ""+objAdd);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, insertTransaction, objAdd, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //  etxt1.setText(response.getString("status"));
                    Log.d("response", response.toString());
                    if (response.getString("status").equals("success")) {
                        Toast.makeText(getApplicationContext(), "Transaksi berhasil", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Transaksi gagal", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(CartActivity.this, e.getMessage()+"", Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CartActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("error_respon_transaksi", error.getMessage());
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void cartshow(){
        String url="http://pharmanet.apodoc.id/customer/CartActivity.php";
        JsonObjectRequest req = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray result = null;
                try {
                    result = response.getJSONArray("result");
                    CartList.clear();
                    total = 0;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for(int i=0; i< result.length();i++){
                    try {
                        JSONObject object = result.getJSONObject(i);
                        CartList.add(new ModelPromo( object.getString("ProductID"),object.getInt("CartProductPrice"),
                                object.getString("ProductName"),object.getInt("CartProductQty"),object.getInt("OutletProductStockQty")));
                        outlet_name.setText(object.getString("OutletName"));
                        OutletName = object.getString("OutletName");
                        alamat.setText(object.getString("OutletAddress"));
                        total += object.getInt("CartProductQty") * object.getInt("CartProductPrice");
                        totalPembayaran = total;
                        Log.d("rwar", object.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                cartHomeAdapter = new CartHomeAdapter(CartList,context);
                recyclerViewCartList.setAdapter(cartHomeAdapter);

                jumlah.setText(df.format(total) + "");
                //jumlahPembayaran.setText(df.format(totalPembayaran) + "");
                info.setText("ANDA MEMILIKI " + CartList.size() + " BARANG DI KERANJANG BELANJA");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Sedang Gangguan Jaringan", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(CartActivity.this);
        queue.add(req);
    }

    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btnLanjutPembelian:
                if(userlocal.getUserLoggedIn()){
                    myAlertDialog();
                    Toast.makeText(this, "anda sudah login", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(this, "anda belum login", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(CartActivity.this, MyMenuActivity.class));

                }
//                myAlertDialog();
        }
    }

    public void myAlertDialog() {

        myDialog = new Dialog(CartActivity.this);
        myDialog.setContentView(R.layout.dialog_layout);

        button_lanjut = (Button) myDialog.findViewById(R.id.button_lanjut);
        button_Batal = (Button) myDialog.findViewById(R.id.button_batal);
        dialog_Nama = (TextView) myDialog.findViewById(R.id.dialog_nama);
        dialog_No_Telepon = (TextView) myDialog.findViewById(R.id.dialog_no_telepon);
        dialog_Alamat = (TextView) myDialog.findViewById(R.id.dialog_alamat);
        Toast.makeText(this, CustomerID + "", Toast.LENGTH_SHORT).show();
        show_view(Integer.parseInt(CustomerID));
        button_Batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.cancel();
            }
        });

        btnLanjutPembelian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//intent hal lain
                Toast.makeText(CartActivity.this, "msk", Toast.LENGTH_SHORT).show();
                insertTransaction(insertTransaction, memberID);
                startActivity(new Intent(CartActivity.this, PickUpActivity.class));
                myDialog.cancel();
            }
        });
        myDialog.show();
    }

    public void myAlertDialog1() {

        myDialog1 = new Dialog(CartActivity.this);
        myDialog1.setContentView(R.layout.dialog_layout3);
        button_lanjut1 = (Button) myDialog1.findViewById(R.id.button_lanjut1);
        button_Batal1 = (Button) myDialog1.findViewById(R.id.button_batal1);

        button_Batal1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog1.cancel();
            }
        });
        button_lanjut1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//intent hal lain
                startActivity(new Intent(CartActivity.this, MyMenuActivity.class));

            }

        });
        myDialog1.show();
    }

    public void show_view(int CustomerID) {
        final JsonObjectRequest rec = new JsonObjectRequest(urlUser + CustomerID, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray Users = response.getJSONArray("result");
                    JSONObject User = Users.getJSONObject(0);
                    dialog_Nama.setText(User.getString("CustomerFullName"));
                    dialog_No_Telepon.setText(User.getString("CustomerPhone"));
                    dialog_Alamat.setText(User.getString("CustomerLocationAddress"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CartActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(rec);
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

    public static void refresh_cart(List<ModelPromo> cartList) {
        total = 0;
        totalPembayaran = 0;
        for (int i = 0; i < cartlist.size(); i++) {
            total += cartlist.get(i).cartProductQty * cartlist.get(i).cartProductPrice;
            totalPembayaran = total + cartlist.get(0).outletDeliveryFee;
        }
        jumlah.setText(df.format(total) + "");
        jumlahPembayaran.setText(df.format(totalPembayaran) + "");
        info.setText("ANDA MEMILIKI " + cartlist.size() + " BARANG DI KERANJANG BELANJA");
        adapter = new cart_adapter2(context, cartlist, 1);
        recyclerViewCartList.setAdapter(adapter);
    }

    public static void refresh_total_cart(List<ModelPromo> cartList) {
        total = 0;
        totalPembayaran = 0;
        for (int i = 0; i < cartList.size(); i++) {
            total += cartList.get(i).ProductQty*cartList.get(i).PriceProduct;
            totalPembayaran = total;
        }
        jumlah.setText(df.format(total) + "");
        //jumlahPembayaran.setText(df.format(totalPembayaran) + "");
        info.setText("ANDA MEMILIKI " + cartList.size() + " BARANG DI KERANJANG BELANJA");
    }

    public static void show_cart(String urlbawah, String CustomerID) {

        JSONObject objAdd = new JSONObject();
        try {
            JSONArray arrData = new JSONArray();
            JSONObject objDetail = new JSONObject();
            objDetail.put("CustomerID", CustomerID);
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
                    cartlist.clear();
                    total = 0;
                    for (int i = 0; i < products.length(); i++) {
                        try {
                            recyclerViewCartList.setVisibility(View.VISIBLE);
                            JSONObject obj = products.getJSONObject(i);
                            cartlist.add(new cart(
                                    obj.getString("OutletName"),
                                    obj.getString("OutletAddress"),
                                    obj.getString("ProductName"),
                                    obj.getString("ProductID"),
                                    obj.getInt("OutletDeliveryFee"),
                                    obj.getInt("OutletProductStockQty"),
                                    obj.getInt("CartProductQty"),
                                    obj.getInt("CartProductPrice")
                            ));

                            total += obj.getInt("CartProductQty") * obj.getInt("CartProductPrice");
                            totalPembayaran = total + obj.getInt("OutletDeliveryFee");

                            outlet_name.setText(products.getJSONObject(0).getString("OutletName"));
                            alamat.setText(products.getJSONObject(0).getString("OutletAddress"));
                            biaya_pengiriman.setText(df.format(products.getJSONObject(0).getInt("OutletDeliveryFee")) + "");

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                            Toast.makeText(context, e1.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    if(cartlist.size()==0){
                        //set visibility container gone
                        not_empty.setVisibility(not_empty.GONE);
                    } else {
                        empty.setVisibility(empty.GONE);
                    }

                    jumlah.setText(df.format(total) + "");
                    jumlahPembayaran.setText(df.format(totalPembayaran) + "");
                    info.setText("ANDA MEMILIKI " + cartlist.size() + " BARANG DI KERANJANG BELANJA");

                    //Toast.makeText(context, cartList.size()+"", Toast.LENGTH_SHORT).show();
                    //adapter = new cart_adapter2(context,cartlist,Integer.parseInt(CustomerID));
                    adapter = new cart_adapter2(context, cartlist, Integer.parseInt(CustomerID));
                    recyclerViewCartList.setAdapter(adapter);

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



    @Override
    protected void onResume() {
        super.onResume();

        if(session.getUserLoggedIn()){
            not_empty.setVisibility(not_empty.VISIBLE);
            show_cart(urlbawah, memberID);

        }
        else {
            not_empty.setVisibility(not_empty.GONE);
            empty.setVisibility(empty.GONE);
            not_login.setVisibility(not_login.VISIBLE);

        }
    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
