package mobi.garden.bottomnavigationtest.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mobi.garden.bottomnavigationtest.Adapter.apotek_adapter;
import mobi.garden.bottomnavigationtest.Model.apotek;
import mobi.garden.bottomnavigationtest.R;

public class DetailObatHome extends AppCompatActivity {

    RequestQueue queue;
    apotek_adapter pa;

//    Button btnBack;
//    Button btnNext;
//    Button btnInfoProduk;
    ImageView pictObat;
    TextView namaObat;
    RecyclerView RvApotek;
    ImageView iv_picture_obat2;
    TextView tv_nama_obat2;
    HashMap<String,String> detail_obat;
    List<apotek> pr = new ArrayList<>();

    String obatName;
    String gambarObat,ProductName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_obat_home);

//        btnInfoProduk=(Button) findViewById(R.id.btn_informasi_produk);
//        btnBack=(Button)findViewById(R.id.btn_back_product);
//        btnNext=(Button)findViewById(R.id.btn_next_product);

        iv_picture_obat2= (ImageView) findViewById(R.id.iv_picture_obat2);
        tv_nama_obat2=(TextView) findViewById(R.id.tv_nama_obat2);

//        obatName = getIntent().getStringExtra("ProductName");
        gambarObat = getIntent().getStringExtra("ProductImage");



        Picasso.with(DetailObatHome.this).load(gambarObat).into(iv_picture_obat2);


        RvApotek = findViewById(R.id.rv_detail_obat);
        RvApotek.setHasFixedSize(true);
        tv_nama_obat2.setText(ProductName);
        Intent intent = getIntent();
        ProductName = intent.getStringExtra("ProductName");
        if(ProductName.contains(" ")){
            ProductName = ProductName.replace(" ","%20");
        }
        Log.d("tttttttttttttt",ProductName);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        RvApotek.setLayoutManager(llm);
        queue = Volley.newRequestQueue(this);

//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//
//        });
//
//        btnNext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//        btnInfoProduk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        show_view(RvApotek, pr,"http://pharmanet.apodoc.id/customer/DetailObatB2C.php?ProductName="+ProductName);


    }

    public void show_view(final RecyclerView cardlist, final List<apotek> list, String url){
        final JsonObjectRequest rec= new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
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
                                    ,obj.getInt("OutletProductPrice")
                                    ,obj.getInt("OutletProductStockQty")
                                    ,obj.getDouble("OutletLatitude")
                                    ,obj.getDouble("OutletLongitude")
                                    ,obj.getInt("TotalRating")
                                    ,obj.getString("OutletOprOpen")
                                    ,obj.getString("OutletOprClose")));
                            Log.d("rwarss", list.toString());
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                            Toast.makeText(DetailObatHome.this, e1.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }

                    pa = new apotek_adapter(DetailObatHome.this,list,DetailObatHome.this);
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

}
