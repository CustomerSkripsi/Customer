package mobi.garden.bottomnavigationtest.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import mobi.garden.bottomnavigationtest.LoginRegister.User;
import mobi.garden.bottomnavigationtest.LoginRegister.UserLocalStore;
import mobi.garden.bottomnavigationtest.R;

public class EmailProfile extends AppCompatActivity {
    private static UserLocalStore userLocalStore;
    static User currUser;
    String customerID, customerEmail;

    TextView TvEmailAnda;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_email_profile);

        TvEmailAnda = findViewById(R.id.tvEmailAnda);

        Toolbar mToolbar = findViewById(R.id.tbUpdateEmail);
        setSupportActionBar(mToolbar);
        setTitle("Edit Email");
        mToolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        userLocalStore = new UserLocalStore(getApplicationContext());
        currUser = userLocalStore.getLoggedInUser();
        customerID = String.valueOf(currUser.getUserID());

        getEmailAnda();
    }

    private void getEmailAnda (){
        String url = "http://pharmanet.apodoc.id/getCustomerProfile.php";
        JSONObject objCustomer = new JSONObject();
        try{
            JSONArray arrData = new JSONArray();
            JSONObject objDetail = new JSONObject();
            objDetail.put("CustomerID", customerID);
            arrData.put(objDetail);
            objCustomer.put("data", arrData);
        }catch (JSONException e){
            e.printStackTrace();
        }
        Log.d("objCustomer",objCustomer.toString());
        Toast.makeText(this, objCustomer.toString(), Toast.LENGTH_SHORT).show();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, objCustomer, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray ownerArray = null;
                try {
                    ownerArray = response.getJSONArray("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i<ownerArray.length();i++){
                    try {
                        JSONObject obj = ownerArray.getJSONObject(i);
                        customerEmail = obj.getString("CustomerEmail");
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    TvEmailAnda.setText(customerEmail);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EmailProfile.this, "Maaf sedang terjadi kendala..", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(EmailProfile.this);
        requestQueue.add(request);
    }
}
