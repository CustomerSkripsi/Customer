package mobi.garden.bottomnavigationtest.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import mobi.garden.bottomnavigationtest.Model.Model_Obat;
import mobi.garden.bottomnavigationtest.Model.session_obat;
import mobi.garden.bottomnavigationtest.R;

/**
 * Created by ASUS on 5/9/2018.
 */

public class InfromasiObat extends BaseActivity {
    List<Model_Obat> obatList = new ArrayList<>();
    ImageView iv_picture_obat3;
    Context context;
    Model_Obat mo;
    String Product,obatID,tempurl,kegunaan,kandungan,Dosis, Carapemakaian, Kemasan,golongan,perluresep,kontraindikasi,carapenyimpanan,principal;
    TextView tv_nama_obat3, tv_indikasi_obat,tv_kandungan_obat,tv_dosis_obat,tv_carapakai_obat,
            tv_kemasan_obat,tv_golongan_obat,tv_resepYN_obat,tv_kontraindikasi_obat,tv_carasimpan_obat,tv_principal_obat;

    mobi.garden.bottomnavigationtest.Model.session_obat session_obat;
    HashMap<String,String> detail_obat;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.informasi_obat);

        session_obat = new session_obat(getApplicationContext());
        detail_obat = session_obat.getUserDetails();

        context = InfromasiObat.this;

        Intent intent = getIntent();
        Product = intent.getStringExtra("produk");
        if(Product.contains(" ")){
            Product = Product.replace(" ","%20");
        }
        Log.d("onClick: ",Product);


        iv_picture_obat3=(ImageView) findViewById(R.id.iv_picture_obat3);
        tv_nama_obat3=(TextView) findViewById(R.id.tv_nama_obat3);
        tv_indikasi_obat= (TextView) findViewById(R.id.tv_indikasi_obat);
        tv_kandungan_obat= (TextView) findViewById(R.id.tv_kandungan_obat);
        tv_dosis_obat= (TextView) findViewById(R.id.tv_dosis_obat);
        tv_carapakai_obat= (TextView) findViewById(R.id.tv_carapakai_obat);
        tv_kemasan_obat= (TextView) findViewById(R.id.tv_kamasan_obat);
        tv_golongan_obat= (TextView) findViewById(R.id.tv_golongan_obat);
        tv_resepYN_obat= (TextView) findViewById(R.id.tv_resepYN_obat);
        tv_kontraindikasi_obat= (TextView) findViewById(R.id.tv_kontraindikasi_obat);
        tv_carasimpan_obat= (TextView) findViewById(R.id.tv_carasimpan_obat);
        tv_principal_obat= (TextView) findViewById(R.id.tv_principal_obat);

        setStatusBarGradiant(this);


        Picasso.with(InfromasiObat.this).load(detail_obat.get(session_obat.PHOTO)).into(iv_picture_obat3);


        android.support.v7.widget.Toolbar dToolbar = findViewById(R.id.toolbar_info);
        dToolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        dToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(InfromasiObat.this, DetailObatHome.class);
                startActivity(i);
            }
        });
        showinformasiproduk();
//        tv_indikasi_obat.setText(mo.getIndikasi());
//        tv_kandungan_obat.setText(mo.getKandungan());
//        tv_dosis_obat.setText(mo.getDosis());
//        tv_carapakai_obat.setText(mo.getCarapakai());
//        tv_kemasan_obat.setText(mo.getKemasan());
//        tv_golongan_obat.setText(mo.getGolongan());
//        tv_resepYN_obat.setText(mo.getResepYN());
//        tv_kontraindikasi_obat.setText(mo.getKontraindikasi());
//        tv_carasimpan_obat.setText(mo.getPenyimpanan());
//        tv_principal_obat.setText(mo.getPrincipal());
    }

    public int getContentViewId() {

        return R.layout.informasi_obat;
    }

    public  int getNavigationMenuItemId() {

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


    public void showinformasiproduk(){
        String url = "http://pharmanet.apodoc.id/customer/showInformasiProduk.php?ProdukName="+Product;
        JsonObjectRequest req = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray result = null;
                try {
                    result = response.getJSONArray("result");
                    obatList.clear();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for(int i=0; i< result.length();i++){
                    try {
                        JSONObject object = result.getJSONObject(i);
                        obatID = object.getString("ProductID");
                        tempurl = object.getString("ProductImage");
                        kegunaan = object.getString("ProductDescription");
                        kandungan = object.getString("ProductIngredients");
                        Dosis = object.getString("ProductDosage");
                        Carapemakaian = object.getString("ProductHowToUse");
                        Kemasan = object.getString("ProductPackage");
                        golongan = object.getString("ProductClassification");
                        perluresep = object.getString("ProductRecipe");
                        kontraindikasi = object.getString("ProductContraindication");
                        carapenyimpanan = object.getString("ProductStorage");
                        principal = object.getString("PrincipalName");
                        obatList.add(new Model_Obat(obatID,tempurl,kegunaan,kandungan,Dosis,Carapemakaian,Kemasan,golongan,
                                perluresep,kontraindikasi,carapenyimpanan,principal));
                        //Toast.makeText(context, "pjg:"+result.length(), Toast.LENGTH_SHORT).show();
                        Log.d("rwaras", object.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                tv_indikasi_obat.setText(kegunaan);
                tv_kandungan_obat.setText(kandungan);
                tv_dosis_obat.setText(Dosis);
                tv_carapakai_obat.setText(Carapemakaian);
                tv_kemasan_obat.setText(Kemasan);
                tv_golongan_obat.setText(golongan);
                tv_resepYN_obat.setText(perluresep);
                tv_kontraindikasi_obat.setText(kontraindikasi);
                tv_carasimpan_obat.setText(carapenyimpanan);
                tv_principal_obat.setText(principal);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Sedang Gangguan", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(InfromasiObat.this);
        queue.add(req);
//
    }


}

//tv_nama_obat3.setText(detail_obat.get(session_obat.NAMA));
//        tv_indikasi_obat.setText(detail_obat.get(session_obat.INDIKASI));
//        tv_kandungan_obat.setText(detail_obat.get(session_obat.KANDUNGAN));
//        tv_dosis_obat.setText(detail_obat.get(session_obat.DOSIS));
//        tv_carapakai_obat.setText(detail_obat.get(session_obat.CARAPAKAI));
//        tv_kemasan_obat.setText(detail_obat.get(session_obat.KEMASAN));
//        tv_golongan_obat.setText(detail_obat.get(session_obat.GOLONGAN));
//        tv_resepYN_obat.setText(detail_obat.get(session_obat.RESEPYN));
//        tv_kontraindikasi_obat.setText(detail_obat.get(session_obat.KONTRAINDIKASI));
//        tv_carasimpan_obat.setText(detail_obat.get(session_obat.CARASIMPAN));
//        tv_principal_obat.setText(detail_obat.get(session_obat.PRINCIPAL));