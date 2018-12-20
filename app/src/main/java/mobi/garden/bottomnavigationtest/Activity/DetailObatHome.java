package mobi.garden.bottomnavigationtest.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import mobi.garden.bottomnavigationtest.Adapter.apotek_adapter;
import mobi.garden.bottomnavigationtest.Model.apotek;
import mobi.garden.bottomnavigationtest.R;

public class DetailObatHome extends AppCompatActivity {
    RequestQueue queue;
    apotek_adapter pa;

    //Button btnBack;
//    Button btnNext;
    Button btnInfoProduk;
    ImageView pictObat,iv_picture_obat2;
    TextView namaObat,tv_nama_obat2;
    RecyclerView RvApotek;
    HashMap<String, String> detail_obat;
    List<apotek> apoteks = new ArrayList<>();
    String obatName,gambarObat, ProductName,tempApotekDay;

    double longitude;
    double latitude;


    GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_obat_home);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(DetailObatHome.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(DetailObatHome.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DetailObatHome.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
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
                              //  Toast.makeText(DetailObatHome.this, "Gagal menarik lokasi anda", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

        btnInfoProduk = findViewById(R.id.btn_informasi_produk);
        btnInfoProduk.setVisibility(View.GONE);
        btnInfoProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailObatHome.this , InfromasiObat.class));
                Intent i = new Intent(getApplicationContext(),InfromasiObat.class);
                i.putExtra("produk",ProductName);
                startActivity(i);
            }
        });
//        btnBack=(Button)findViewById(R.id.btn_back_product);
//        btnNext=(Button)findViewById(R.id.btn_next_product);
        iv_picture_obat2 = (ImageView) findViewById(R.id.iv_picture_obat2);
        tv_nama_obat2 = (TextView) findViewById(R.id.tv_nama_obat2);

//        obatName = getIntent().getStringExtra("ProductName");
        gambarObat = getIntent().getStringExtra("ProductImage");

        Picasso.with(DetailObatHome.this).load(gambarObat).into(iv_picture_obat2);

        RvApotek = findViewById(R.id.rv_detail_obat);
        RvApotek.setHasFixedSize(true);

        Intent intent = getIntent();
        ProductName = intent.getStringExtra("ProductName");
        tv_nama_obat2.setText(ProductName);
        if(ProductName.contains(" ")){
            ProductName = ProductName.replace(" ","%20");
        }
        Log.d("ssss", "asdasd: "+ProductName);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        RvApotek.setLayoutManager(llm);
        queue = Volley.newRequestQueue(this);

        show_view(RvApotek, apoteks,"http://pharmanet.apodoc.id/customer/DetailObatB2C.php?ProductName="+ProductName+"&day=");

    }

    public void show_view(final RecyclerView cardlist, final List<apotek> list, String url){
        getDay();
        final JsonObjectRequest rec= new JsonObjectRequest(url+tempApotekDay, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray apoteks = null;

                try {
                    apoteks = response.getJSONArray("result");
                    for (int i = 0; i < apoteks.length(); i++) {
                        try {
                            //cardlist.setVisibility(View.VISIBLE);
                            JSONObject obj = apoteks.getJSONObject(i);
                            list.add(new apotek(obj.getString("OutletID")
                                    ,obj.getString("OutletName")
                                    ,obj.getString("ProductName")
                                    ,obj.getInt("OutletProductPrice")
                                    ,obj.getInt("OutletProductStockQty")
                                    ,obj.getDouble("OutletLatitude")
                                    ,obj.getDouble("OutletLongitude")
                                    ,obj.getInt("TotalRating")
                                    ,obj.getString("OutletOprOpen")
                                    ,obj.getString("OutletOprClose")));
                            Log.d("rwar", obj.toString());
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                           // Toast.makeText(DetailObatHome.this, e1.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    pa = new apotek_adapter(DetailObatHome.this,list,longitude,latitude);
                    cardlist.setAdapter(pa);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailObatHome.this, "error", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(rec);
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

}






//            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//////            Log.d("wasd" ,location.toString());
////            longitude = location.getLongitude();
////            latitude = location.getLatitude();
//            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
//              final LocationListener locationListener = new LocationListener() {
//                public void onLocationChanged(Location location) {
//                    longitude = location.getLongitude();
//                    latitude = location.getLatitude();
//
//                    Log.d("sssss", "onLocationChanged: ");
//                    Log.d("test1", longitude+"");
//                    Log.d("test2", latitude+"");
//                }
//
//                @Override
//                public void onStatusChanged(String provider, int status, Bundle extras) {
//
//                }
//
//                @Override
//                public void onProviderEnabled(String provider) {
//
//                }
//
//                @Override
//                public void onProviderDisabled(String provider) {
//
//                }
//            };
//
//            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
