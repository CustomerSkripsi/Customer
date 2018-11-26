package mobi.garden.bottomnavigationtest.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

import mobi.garden.bottomnavigationtest.Adapter.KategoriAdapter;
import mobi.garden.bottomnavigationtest.BaseActivity;
import mobi.garden.bottomnavigationtest.Model.ModelKategori;
import mobi.garden.bottomnavigationtest.R;

public class KategoriActivity extends BaseActivity {
    Context context;
    RecyclerView rvKategori, rvDetailKategori;
    String url, kategoriname, tempNamekategoriname;
    List<ModelKategori> KategorisList = new ArrayList<>();
    KategoriAdapter kAdapter;

    @Override
    public  int getContentViewId() {
        return R.layout.activity_kategori;
    }
    @Override
    public int getNavigationMenuItemId() { return R.id.navigation_dashboard; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategori);
        context = KategoriActivity.this;
        rvKategori = findViewById(R.id.rvkategori);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvKategori.setLayoutManager(llm);
        kategoris();
       // setStatusBarGradiant(this);
        android.support.v7.widget.Toolbar dToolbar = findViewById(R.id.toolbar3);
        dToolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        dToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(KategoriActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });

    }

    public void kategoris(){
        url = "http://pharmanet.apodoc.id/customer/kategori.php";
        JsonObjectRequest req = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray result = null;
                try {
                    result = response.getJSONArray("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for(int i=0; i<result.length();i++){
                    try {
                        JSONObject obj = result.getJSONObject(i);
                        ModelKategori m = new ModelKategori();
                        m.setCategoryName(obj.getString("Kategori"));
                        kategoriname = obj.getString("Kategori");
                        //Toast.makeText(context, "panjangnya"+result.length(), Toast.LENGTH_SHORT).show();
                        KategorisList.add(m);
                        kAdapter = new KategoriAdapter(KategorisList,KategoriActivity.this);
                        rvKategori.setAdapter(kAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Response Faileds", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(KategoriActivity.this);
        queue.add(req);
    }

//    public static void setStatusBarGradiant(Activity activity) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = activity.getWindow();
//            Drawable background = activity.getResources().getDrawable(R.drawable.gradient);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
////            window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
//            window.setBackgroundDrawable(background);
//        }
//    }

}
