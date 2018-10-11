package mobi.garden.bottomnavigationtest.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mobi.garden.bottomnavigationtest.BaseActivity;
import mobi.garden.bottomnavigationtest.Model.apotek;
import mobi.garden.bottomnavigationtest.Adapter.apotek_adapter;
import mobi.garden.bottomnavigationtest.Model.session_obat;
import mobi.garden.bottomnavigationtest.R;

/**
 * Created by ASUS on 5/8/2018.
 */

public class
DetailObatHome extends BaseActivity {
    RequestQueue queue;
    apotek_adapter pa;
    RecyclerView cardListBrand;
    RecyclerView cardListBrand2;
    RecyclerView cardListBrand3;
    RecyclerView cardListBrand4;



    List<apotek> pr = new ArrayList<>();
    List<apotek> pr2 = new ArrayList<>();
    List<apotek> pr3 = new ArrayList<>();
    List<apotek> pr4 = new ArrayList<>();

    Button btn_informasi_product;
    Button btn_back_product;
    Button btn_next_product;
    Button btn_apotek_termurah;
    Button btn_apotek_terdekat;
    Button btn_apotek_24jam;
    Button btn_apotek_online;

    ImageView iv_picture_obat2;
    TextView tv_nama_obat2;
    TextView tv_deskripsi_obat;
    mobi.garden.bottomnavigationtest.Model.session_obat session_obat;
    HashMap<String,String> detail_obat;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cardListBrand=(RecyclerView) findViewById(R.id.rv_cv_apotek_termurah);
        cardListBrand2=(RecyclerView) findViewById(R.id.rv_cv_apotek_terdekat);
        cardListBrand3=(RecyclerView) findViewById(R.id.rv_cv_apotek_24jam);
        cardListBrand4=(RecyclerView) findViewById(R.id.rv_cv_apotek_online);

        cardListBrand.setHasFixedSize(true);
        cardListBrand.setVisibility(View.VISIBLE);
        cardListBrand2.setHasFixedSize(true);
        cardListBrand3.setHasFixedSize(true);
        cardListBrand4.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        LinearLayoutManager llm2 = new LinearLayoutManager(this);
        LinearLayoutManager llm3 = new LinearLayoutManager(this);
        LinearLayoutManager llm4 = new LinearLayoutManager(this);

        llm.setOrientation(LinearLayoutManager.VERTICAL);
        llm2.setOrientation(LinearLayoutManager.VERTICAL);
        llm3.setOrientation(LinearLayoutManager.VERTICAL);
        llm4.setOrientation(LinearLayoutManager.VERTICAL);

        cardListBrand.setLayoutManager(llm);
        cardListBrand2.setLayoutManager(llm2);
        cardListBrand3.setLayoutManager(llm3);
        cardListBrand4.setLayoutManager(llm4);

        queue = Volley.newRequestQueue(this);

        session_obat = new session_obat(getApplicationContext());
        detail_obat = session_obat.getUserDetails();

        iv_picture_obat2= (ImageView) findViewById(R.id.iv_picture_obat2);
        tv_nama_obat2=(TextView) findViewById(R.id.tv_nama_obat2);
        tv_deskripsi_obat= (TextView) findViewById(R.id.tv_deskripsi_obat);

        Picasso.with(DetailObatHome.this).load(detail_obat.get(session_obat.PHOTO)).into(iv_picture_obat2);
        tv_deskripsi_obat.setText(detail_obat.get(session_obat.DESC));
        tv_nama_obat2.setText(detail_obat.get(session_obat.NAMA));

        show_view(cardListBrand,pr,"http://pharmanet.apodoc.id/select_outlet_termurah.php?id="+detail_obat.get(session_obat.ID));
        show_view(cardListBrand3,pr3,"https://fransis.rawatwajah.com/belajar/tugas_mandiri/select_outlet_24jam.php?id="+detail_obat.get(session_obat.ID));
        show_view(cardListBrand4,pr4,"https://fransis.rawatwajah.com/belajar/tugas_mandiri/select_outlet_online.php?id="+detail_obat.get(session_obat.ID));

        setStatusBarGradiant(this);

        btn_informasi_product=(Button) findViewById(R.id.btn_informasi_product);
        btn_back_product=(Button)findViewById(R.id.btn_back_product);
        btn_next_product=(Button)findViewById(R.id.btn_next_product);
        btn_apotek_termurah=(Button)findViewById(R.id.btn_apotek_termurah);

        btn_apotek_terdekat=(Button)findViewById(R.id.btn_apotik_terdekat);
        btn_apotek_24jam=(Button)findViewById(R.id.btn_apotik_24jam);
        btn_apotek_online=(Button)findViewById(R.id.btn_apotik_online);
        btn_apotek_terdekat.setBackgroundDrawable(getResources().getDrawable(R.drawable.bordertrans));
        btn_apotek_terdekat.setTextColor(Color.GRAY);
        btn_apotek_24jam.setBackgroundDrawable(getResources().getDrawable(R.drawable.bordertrans));
        btn_apotek_24jam.setTextColor(Color.GRAY);
        btn_apotek_online.setBackgroundDrawable(getResources().getDrawable(R.drawable.bordertrans2));
        btn_apotek_online.setTextColor(Color.GRAY);
        btn_apotek_termurah.setBackgroundDrawable(getResources().getDrawable(R.drawable.borderhijau1));
        btn_apotek_termurah.setTextColor(Color.WHITE);


        btn_informasi_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(DetailObatHome.this,InfromasiObat.class);
                startActivity(i);
            }
        });

        btn_back_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_apotek_termurah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardListBrand.setVisibility(View.VISIBLE);
                cardListBrand2.setVisibility(View.INVISIBLE);
                cardListBrand3.setVisibility(View.INVISIBLE);
                cardListBrand4.setVisibility(View.INVISIBLE);
                btn_apotek_termurah.setBackgroundDrawable(getResources().getDrawable(R.drawable.borderhijau1));
                btn_apotek_termurah.setTextColor(Color.WHITE);
                btn_apotek_terdekat.setBackgroundDrawable(getResources().getDrawable(R.drawable.bordertrans));
                btn_apotek_terdekat.setTextColor(Color.GRAY);
                btn_apotek_24jam.setBackgroundDrawable(getResources().getDrawable(R.drawable.bordertrans));
                btn_apotek_24jam.setTextColor(Color.GRAY);
                btn_apotek_online.setBackgroundDrawable(getResources().getDrawable(R.drawable.bordertrans2));
                btn_apotek_online.setTextColor(Color.GRAY);

            }
        });
        btn_apotek_terdekat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardListBrand2.setVisibility(View.VISIBLE);
                cardListBrand.setVisibility(View.INVISIBLE);
                cardListBrand3.setVisibility(View.INVISIBLE);
                cardListBrand4.setVisibility(View.INVISIBLE);
                btn_apotek_terdekat.setBackgroundDrawable(getResources().getDrawable(R.drawable.borderhijau));
                btn_apotek_terdekat.setTextColor(Color.WHITE);
                btn_apotek_24jam.setBackgroundDrawable(getResources().getDrawable(R.drawable.bordertrans));
                btn_apotek_24jam.setTextColor(Color.GRAY);
                btn_apotek_online.setBackgroundDrawable(getResources().getDrawable(R.drawable.bordertrans2));
                btn_apotek_online.setTextColor(Color.GRAY);
                btn_apotek_termurah.setBackgroundDrawable(getResources().getDrawable(R.drawable.bordertrans1));
                btn_apotek_termurah.setTextColor(Color.GRAY);

            }
        });
        btn_apotek_24jam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardListBrand3.setVisibility(View.VISIBLE);
                cardListBrand.setVisibility(View.INVISIBLE);
                cardListBrand2.setVisibility(View.INVISIBLE);
                cardListBrand4.setVisibility(View.INVISIBLE);
                btn_apotek_24jam.setBackgroundDrawable(getResources().getDrawable(R.drawable.borderhijau));
                btn_apotek_24jam.setTextColor(Color.WHITE);
                btn_apotek_terdekat.setBackgroundDrawable(getResources().getDrawable(R.drawable.bordertrans));
                btn_apotek_terdekat.setTextColor(Color.GRAY);
                btn_apotek_online.setBackgroundDrawable(getResources().getDrawable(R.drawable.bordertrans2));
                btn_apotek_online.setTextColor(Color.GRAY);
                btn_apotek_termurah.setBackgroundDrawable(getResources().getDrawable(R.drawable.bordertrans1));
                btn_apotek_termurah.setTextColor(Color.GRAY);
            }
        });
        btn_apotek_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardListBrand4.setVisibility(View.VISIBLE);
                cardListBrand.setVisibility(View.INVISIBLE);
                cardListBrand2.setVisibility(View.INVISIBLE);
                cardListBrand3.setVisibility(View.INVISIBLE);
                btn_apotek_online.setBackgroundDrawable(getResources().getDrawable(R.drawable.borderhijau2));
                btn_apotek_online.setTextColor(Color.WHITE);
                btn_apotek_terdekat.setBackgroundDrawable(getResources().getDrawable(R.drawable.bordertrans));
                btn_apotek_terdekat.setTextColor(Color.GRAY);
                btn_apotek_24jam.setBackgroundDrawable(getResources().getDrawable(R.drawable.bordertrans));
                btn_apotek_24jam.setTextColor(Color.GRAY);
                btn_apotek_termurah.setBackgroundDrawable(getResources().getDrawable(R.drawable.bordertrans1));
                btn_apotek_termurah.setTextColor(Color.GRAY);
            }
        });
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
                            cardlist.setVisibility(View.VISIBLE);
                            JSONObject obj = apoteks.getJSONObject(i);

                            list.add(new apotek(obj.getString("OutletID")
                                    ,obj.getString("OutletName")
                                    ,obj.getString("OutletMinOrder")
                                    ,obj.getInt ("OutletDeliveryFee")
                                    ,obj.getInt("OutletProductPrice")
                                    ,obj.getInt("OutletProductStockQty")));
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                            Toast.makeText(DetailObatHome.this, e1.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }

                    pa = new apotek_adapter(DetailObatHome.this,list);
                    cardlist.setAdapter(pa);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(DetailObatHome.this, "error", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(rec);
    }

    public int getContentViewId() {

        return R.layout.detail_obat_home;
    }

    public int getNavigationMenuItemId() {

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
