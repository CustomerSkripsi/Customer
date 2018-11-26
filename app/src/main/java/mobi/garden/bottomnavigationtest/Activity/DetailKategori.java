package mobi.garden.bottomnavigationtest.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

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

import mobi.garden.bottomnavigationtest.Adapter.DetailKategoriAdapter;
import mobi.garden.bottomnavigationtest.Model.ModelKategori;
import mobi.garden.bottomnavigationtest.R;

public class DetailKategori extends AppCompatActivity {
    Context context;
    String url, kategoriname;
    TextView tvNamaObat;
    List<ModelKategori> KategorisList = new ArrayList<>();
    DetailKategoriAdapter dkAdapter;
    RecyclerView rvprodukdetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kategori);
        context = DetailKategori.this;
        rvprodukdetail = findViewById(R.id.rvprodukdetail);

//        rvprodukdetail.setHasFixedSize(true);
//        rvprodukdetail.setVisibility(View.VISIBLE);
//        LinearLayoutManager llm = new LinearLayoutManager(this);
//        llm.setOrientation(LinearLayoutManager.VERTICAL);
//        rvprodukdetail.setLayoutManager(llm);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),3);
        rvprodukdetail.setLayoutManager(gridLayoutManager);

       // tvNamaObat = findViewById(R.id.tvNamaObat);

        Intent intent = getIntent();
        kategoriname = intent.getStringExtra("CategoryName");
        showkategoris();

        android.support.v7.widget.Toolbar dToolbar = findViewById(R.id.toolbar4);
        dToolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        dToolbar.setTitle(kategoriname);
        dToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetailKategori.this, KategoriActivity.class);
                startActivity(i);
            }
        });



    }

    public void showkategoris(){
        url = "http://pharmanet.apodoc.id/customer/DetailKategori.php?kategoriname="+kategoriname;
        //Toast.makeText(context, "hmm"+kategoriname, Toast.LENGTH_SHORT).show();
        JsonObjectRequest req = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray result = null;
                try {
                    //Toast.makeText(DetailKategori.this, "aaar", Toast.LENGTH_SHORT).show();
                    result = response.getJSONArray("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for(int i=0; i<result.length();i++){
                    try {
                        JSONObject object = result.getJSONObject(i);
                        ModelKategori m = new ModelKategori();
                        m.setCategoryName(object.getString("ProductName"));
                        KategorisList.add(m);
                        Toast.makeText(context, "pjg:"+result.length(), Toast.LENGTH_SHORT).show();
                        Log.d("rwar", object.toString());
                        dkAdapter = new DetailKategoriAdapter(KategorisList,context);
                        rvprodukdetail.setAdapter(dkAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //tvNamaObat.setText(tempNamekategoriname);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Sedang Gangguan", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(req);
    }

}
