package mobi.garden.bottomnavigationtest.Activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import java.util.HashMap;
import java.util.List;

import mobi.garden.bottomnavigationtest.Adapter.WishlistAdapter;
import mobi.garden.bottomnavigationtest.Model.ModelPromo;
import mobi.garden.bottomnavigationtest.R;
import mobi.garden.bottomnavigationtest.Session.SessionManagement;

public class WishList extends AppCompatActivity {
    static Context context;
    SessionManagement session;
    HashMap<String, String> login;
    static String CustomerID,memberID, userName;
    static List<ModelPromo> productList = new ArrayList<>();
    static WishlistAdapter wishlistAdapter;
    static RecyclerView rvWish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);
        context = WishList.this;

        session = new SessionManagement(getApplicationContext());
        login = session.getMemberDetails();
        userName= login.get(SessionManagement.USERNAME);
        memberID = login.get(SessionManagement.KEY_KODEMEMBER);

        Toolbar dToolbar = findViewById(R.id.toolbarWish);
        dToolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        dToolbar.setTitle("Wish List");
        dToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        rvWish = findViewById(R.id.rvWish);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,2);
        rvWish.setLayoutManager(gridLayoutManager);

        viewWishList();

    }

    public static void viewWishList(){
        String url = "http://pharmanet.apodoc.id/customer/ShowWishList.php?CustomerID="+memberID;
        JsonObjectRequest req = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray result = null;
                try {
                    result = response.getJSONArray("result");
                    productList.clear();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for(int i=0; i< result.length();i++){
                    try {
                        JSONObject object = result.getJSONObject(i);
                        productList.add(new ModelPromo( object.getString("CustomerID"),
                                object.getString("ProductID"),
                                object.getString("ProductName"),object.getString("ProductImage")));

                        Log.d("rwar", object.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                wishlistAdapter = new WishlistAdapter(productList,context);
                rvWish.setAdapter(wishlistAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Jaringan Sedang Gangguan", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(req);
    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
