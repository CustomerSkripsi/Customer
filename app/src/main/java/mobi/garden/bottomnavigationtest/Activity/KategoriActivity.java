package mobi.garden.bottomnavigationtest.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
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

import mobi.garden.bottomnavigationtest.Adapter.KategoriAdapter;
import mobi.garden.bottomnavigationtest.Model.ModelKategori;
import mobi.garden.bottomnavigationtest.R;

public class KategoriActivity extends AppCompatActivity {
    Context context;
    RecyclerView rvKategori, rvDetailKategori;
    String url, kategoriname, tempNamekategoriname;
    List<ModelKategori> KategorisList = new ArrayList<>();
    KategoriAdapter kAdapter;
    TextView tvSearch;
    String content;
    ImageView ivCart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategori);
        context = KategoriActivity.this;
        rvKategori = findViewById(R.id.rvkategori);
        tvSearch = findViewById(R.id.tvSearch);
        ivCart = findViewById(R.id.ImageView);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvKategori.setLayoutManager(llm);

       // setStatusBarGradiant(this);
        //kategoris(content);
        tvSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                 content = tvSearch.getText().toString();
                kategoris(content);
            }

        });
        String u = "";
        kategoris(u);
        tvSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    return false;
                }
                View view   = getCurrentFocus();
                if(view!=null){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return false;
            }
        });
        ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(KategoriActivity.this , CartActivity.class));
            }
        });


    }


    public void kategoris(String input){
        url = "http://pharmanet.apodoc.id/customer/kategori.php?input="+input;
        JsonObjectRequest req = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray result = null;
                try {
                    result = response.getJSONArray("result");
                    KategorisList.clear();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for(int i=0; i<result.length();i++){
                    try {
                        JSONObject obj = result.getJSONObject(i);
                        //ModelKategori m = new ModelKategori();
                       // m.setCategoryName(obj.getString("Kategori"));
                        kategoriname = obj.getString("Kategori");
                        //Toast.makeText(context, "panjangnya"+result.length(), Toast.LENGTH_SHORT).show();
                        KategorisList.add(new ModelKategori(kategoriname));
                        Log.d("rwar", obj.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    kAdapter = new KategoriAdapter(KategorisList,KategoriActivity.this);
                    rvKategori.setAdapter(kAdapter);
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
