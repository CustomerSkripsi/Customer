package mobi.garden.bottomnavigationtest.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
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
import java.util.List;

import mobi.garden.bottomnavigationtest.Adapter.AlamatProfileAdapter;
import mobi.garden.bottomnavigationtest.LoginRegister.User;
import mobi.garden.bottomnavigationtest.LoginRegister.UserLocalStore;
import mobi.garden.bottomnavigationtest.R;

public class AlamatProfile extends AppCompatActivity {

    private static RequestQueue queue;
    public static String customerID;
    private static UserLocalStore userLocalStore;
    static User currUser;

    static SwipeRefreshLayout swipeRefreshLayout;

    static RecyclerView rvAlamat;
    TextView TvUbah, TvHapus;

    public static List<User> userList = new ArrayList<>();
   static AlamatProfileAdapter alamatProfileAdapter;
    static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_alamat);

        Toolbar toolbar = findViewById(R.id.tbDaftarAlamat);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Daftar Alamat");
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        userLocalStore = new UserLocalStore(getApplicationContext());
        currUser = userLocalStore.getLoggedInUser();
        customerID = String.valueOf(currUser.getUserID());
        context = AlamatProfile.this;
        queue = Volley.newRequestQueue(AlamatProfile.this);

        TvUbah = findViewById(R.id.tvUbahAlamat);
        TvHapus = findViewById(R.id.tvHapusAlamat);

        swipeRefreshLayout = findViewById(R.id.swipeAlamat);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initAlamat();
            }
        });

        initAlamat();

//        TvUbah.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//        TvHapus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            finish();
//            }
//        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addtoolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.add){
            Intent i = new Intent(AlamatProfile.this, AddAlamatActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    public void initAlamat(){
        rvAlamat = findViewById(R.id.rvAlamatList);
        rvAlamat.setHasFixedSize(true);
        rvAlamat.setVisibility(View.VISIBLE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvAlamat.setLayoutManager(linearLayoutManager);

        getAlamat();
    }

    public static void getAlamat(){
//        queue = Volley.newRequestQueue(view.getContext());
        final JsonObjectRequest rec= new JsonObjectRequest(Request.Method.GET,"http://pharmanet.apodoc.id/select_alamat_customer.php?CustomerID="+currUser.getUserID(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray users = null;
                try {
                    users = response.getJSONArray("result");
                   userList.clear();

                    for (int i = 0; i < users.length(); i++) {
                        try {
                            rvAlamat.setVisibility(View.VISIBLE);
                            JSONObject obj = users.getJSONObject(i);
                            userList.add(new User(
                                    obj.getString("CustomerLocationID"),
                                    obj.getString("RecipientName"),
                                    obj.getString("RecipientNumber"),
                                    obj.getString("CustomerLocationAddress"),
                                    obj.getString("CustomerCity"),
                                    obj.getString("CustomerPostalCode"),
                                    obj.getString("CustomerProvince"))
                            );

                            Toast.makeText(context, obj.getString("CustomerLocationID"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e1) {
                            //Toast.makeText(BlurPopupWindow.this, e1.getMessage(), Toast.LENGTH_SHORT).show();
                            e1.printStackTrace();
                        }
                    }
                    alamatProfileAdapter = new AlamatProfileAdapter(userList,context,customerID);
                    rvAlamat.setAdapter(alamatProfileAdapter);
                    alamatProfileAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Toast.makeText(BlurPopupWindow.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(rec);
    }
}
