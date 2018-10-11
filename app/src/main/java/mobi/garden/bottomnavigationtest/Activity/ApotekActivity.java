package mobi.garden.bottomnavigationtest.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mobi.garden.bottomnavigationtest.Model.obat;
import mobi.garden.bottomnavigationtest.Adapter.obat_adapter_as;
import mobi.garden.bottomnavigationtest.R;

public class ApotekActivity extends AppCompatActivity {

    private boolean isPressed;
    public static final String OUTLET_ID = "OutletID";
    String OutletID;
    TextView tv_name_apotek,tv_address_apotek,tv_no_telepon,tv_jam_operasional,tv_no_sia,tv_no_sipa,tv_metode_pengiriman;
    ImageView iv_picture_apotek;
    Button hideButton;
    LinearLayout containerHide;

    //obat adapter
    RequestQueue queue;
    obat_adapter_as pa;
    RecyclerView cardListBrandd;
    RecyclerView cardListBrandd2;
    RecyclerView cardListBrandd3;
    List<obat> prr = new ArrayList<>();
    List<obat> prr2 = new ArrayList<>();
    List<obat> prr3 = new ArrayList<>();
    SwipeRefreshLayout swiperefresh;

    String urlApotek = "Http://Pharmanet.Apodoc.id/select_apotek_online.php?id=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apotek);
        Intent intent = getIntent();
        OutletID = intent.getStringExtra(OUTLET_ID);


        cardListBrandd = (RecyclerView) findViewById(R.id.rv_cv_obat_promo);
        cardListBrandd2= (RecyclerView) findViewById(R.id.rv_cv_obat_rekomendasi);
        cardListBrandd3= (RecyclerView) findViewById(R.id.rv_cv_obat_terlaris);

        cardListBrandd.setHasFixedSize(true);
        cardListBrandd.setVisibility(View.VISIBLE);
        cardListBrandd2.setHasFixedSize(true);
        cardListBrandd2.setVisibility(View.VISIBLE);
        cardListBrandd3.setHasFixedSize(true);
        cardListBrandd3.setVisibility(View.VISIBLE);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        LinearLayoutManager llm2 = new LinearLayoutManager(this);
        LinearLayoutManager llm3 = new LinearLayoutManager(this);

        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        llm2.setOrientation(LinearLayoutManager.HORIZONTAL);
        llm3.setOrientation(LinearLayoutManager.HORIZONTAL);

        cardListBrandd.setLayoutManager(llm);
        cardListBrandd2.setLayoutManager(llm2);
        cardListBrandd3.setLayoutManager(llm3);

        queue = Volley.newRequestQueue(this);
        Intent i=getIntent();

        setStatusBarGradiant(this);

        isPressed=false;
        tv_name_apotek= (TextView) findViewById(R.id.tv_name_apotek);
        tv_address_apotek=(TextView) findViewById(R.id.tv_address_apotek);
        tv_no_telepon=(TextView) findViewById(R.id.tv_no_telepon);
        tv_jam_operasional= (TextView) findViewById(R.id.tv_jam_operasional);
        tv_no_sia=(TextView) findViewById(R.id.tv_no_sia);
        tv_no_sipa= (TextView) findViewById(R.id.tv_no_sipa);
        tv_metode_pengiriman = (TextView) findViewById(R.id.tv_metode_pengiriman);
        //iv_picture_apotek = (ImageView) findViewById(R.id.iv_picture_apotek);


        hideButton = (Button) findViewById(R.id.hideButton);
        containerHide = (LinearLayout) findViewById(R.id.containerHide);
        Toast.makeText(this, OutletID+"", Toast.LENGTH_SHORT).show();

        hideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isPressed) {
                    containerHide.setVisibility(View.GONE);
                    isPressed=true;
                    hideButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_right_black_24dp));
                }else{
                    isPressed = false;
                    containerHide.setVisibility(View.VISIBLE);
                    hideButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_black_24dp));
                }
            }
        });

        show_view(OutletID);
        show_view1(cardListBrandd,prr,"http://pharmanet.apodoc.id/select_product_promo_apotik.php?id=");
        show_view1(cardListBrandd2,prr2,"http://pharmanet.apodoc.id/select_product_rekomendasi_apotik.php?id=");
        show_view1(cardListBrandd3,prr3,"http://pharmanet.apodoc.id/select_product_terlaris_apotik.php?id=");


    }


    public void show_view1(final RecyclerView cardlist, final List<obat> list, String url){
        final JsonObjectRequest rec= new JsonObjectRequest(url+OutletID, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray obats = null;

                try {
                    obats = response.getJSONArray("result");

                    for (int i = 0; i < obats.length(); i++) {
                        try {
                            cardlist.setVisibility(View.VISIBLE);
                            JSONObject obj = obats.getJSONObject(i);

                            list.add(new obat(
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
                                    obj.getString("CategoryID")));
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                            Toast.makeText(ApotekActivity.this, e1.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }

                    pa = new obat_adapter_as(ApotekActivity.this,list);
                    cardlist.setAdapter(pa);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ApotekActivity.this, "No Connection", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(rec);
    }

    public void show_view(String OutletID){
        final JsonObjectRequest rec= new JsonObjectRequest(urlApotek+OutletID, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray apoteks = response.getJSONArray("result");
                    JSONObject apotek = apoteks.getJSONObject(0);
                    tv_name_apotek.setText(apotek.getString("OutletName"));
                    tv_address_apotek.setText(apotek.getString("OutletAddress"));
                    tv_no_telepon.setText(apotek.getString("OutletPhone"));
                    tv_jam_operasional.setText(apotek.getString("OutletPhone"));
                    tv_no_sia.setText(apotek.getString("OutletSIA"));
                    tv_no_sipa.setText(apotek.getString("OutletSIPA"));
                    tv_metode_pengiriman.setText(apotek.getString("deliveryYN"));

                    //Picasso.with(ApotekActivity.this).load("http://apotekkeluarga.com/wp-content/uploads/2015/06/apotik-terlengkap-di-pekanbaru.jpg").into(iv_picture_apotek);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ApotekActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

}
