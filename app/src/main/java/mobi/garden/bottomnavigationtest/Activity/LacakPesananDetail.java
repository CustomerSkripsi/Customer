package mobi.garden.bottomnavigationtest.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import mobi.garden.bottomnavigationtest.CONFIG;
import mobi.garden.bottomnavigationtest.R;

public class LacakPesananDetail extends AppCompatActivity {
    Context context;
    static String ID;
    static int total = 0;
    static int totalPembayaran = 0;
    String Date, OutletName, StatusOrder;
    String url, lacakdetail;
    String namaproduk;
    static int harga;
    String jumlah;
    String berat;
    RecyclerView rvLacakDetail;


    TextView lacakPesananDetail_OrderID, lacakPesananDetail_Date, lacakPesananDetail_apoName, lacakPesananDetail_Nama_Item;
    TextView lacakPesananDetail_Qty, lacakPesananDetail_Price, lacakPesananDetail_Berat ;// lacakPesananDetail_Status;
    TextView totaltotal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = LacakPesananDetail.this;
        setContentView(R.layout.activity_lacak_pesanan_detail);


        lacakPesananDetail_OrderID = findViewById(R.id.lacakPesananDetail_OrderID);
        lacakPesananDetail_Date = findViewById(R.id.lacakPesananDetail_Date);
        lacakPesananDetail_apoName = findViewById(R.id.lacakPesananDetail_apoName);
        lacakPesananDetail_Nama_Item = findViewById(R.id.lacakPesananDetail_Nama_Item);
        lacakPesananDetail_Price = findViewById(R.id.lacakPesananDetail_Price);
        lacakPesananDetail_Qty = findViewById(R.id.lacakPesananDetail_Qty);
        lacakPesananDetail_Berat = findViewById(R.id.lacakPesananDetail_Berat);
        totaltotal = findViewById(R.id.sub_total_detail);
//
//        lacakPesananDetail_Nama_Item = findViewById(R.id.product_name);
//        lacakPesananDetail_Price = findViewById(R.id.product_price);
//        lacakPesananDetail_Qty = findViewById(R.id.product_quantity);
//        lacakPesananDetail_Berat = findViewById(R.id.product_weight);

        rvLacakDetail = findViewById(R.id.rvLacakDetail);

        //parse data intent
        ID = getIntent().getStringExtra("OrderID");
        Date = getIntent().getStringExtra("Tanggal");
        OutletName = getIntent().getStringExtra("OutletName");
        StatusOrder = getIntent().getStringExtra("StatusOrder");

        //Masukan data Parse
        lacakPesananDetail_OrderID.setText(ID);
        lacakPesananDetail_Date.setText(Date);
        lacakPesananDetail_apoName.setText(OutletName);
        // lacakPesananDetail_Status.setText(StatusOrder);

        Intent intent = getIntent();
        lacakdetail = intent.getStringExtra("LacakDetail");
        Log.d("te", "asddd"+lacakdetail);
        showviewDetail();

    }

    public  void showviewDetail(){
        url = "http://pharmanet.apodoc.id/customer/lacakdetail.php?CustomerID="+lacakdetail;
        Log.d("url", url);
        JsonObjectRequest quest = new JsonObjectRequest (url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray result = null;
                try {
                    result = response.getJSONArray("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                    total = 0;
                }
                for(int i=0;i<result.length();i++){
                    try {
                        JSONObject object = result.getJSONObject(i);
                        namaproduk = object.getString("ProductName");
                        harga = object.getInt("OrderProductPrice");
                        jumlah = object.getString("OrderProductQty");
                        berat = object.getString("ProductWeight");
                        total += object.getInt("OrderProductQty") * object.getInt("OrderProductPrice");
                        totalPembayaran = total;
                        Log.d("asd678", jumlah);
                        Log.d("qeqq", String.valueOf(harga));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                    totaltotal.setText(CONFIG.ConvertNominal(total)+" ");
                    lacakPesananDetail_Nama_Item.setText(namaproduk);
                    lacakPesananDetail_Price.setText(CONFIG.ConvertNominal(harga));
                    lacakPesananDetail_Qty.setText("Jumlah : "+jumlah);

                    if(berat=="null"){
                        lacakPesananDetail_Berat.setText("Berat "+"-");
                    }else{
                        lacakPesananDetail_Berat.setText("Berat "+berat);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error Response", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue= Volley.newRequestQueue(context);
        queue.add(quest);
    }
    public void BackBack4(View view){
        super.onBackPressed();
    }


    public static String ConvertNominal(double input){
        DecimalFormat df = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setCurrencySymbol("Rp. ");
        dfs.setMonetaryDecimalSeparator(',');
        dfs.setGroupingSeparator('.');
        df.setDecimalFormatSymbols(dfs);
        df.setMaximumFractionDigits(0);
        String hsl = df.format(input);
        return hsl;
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



