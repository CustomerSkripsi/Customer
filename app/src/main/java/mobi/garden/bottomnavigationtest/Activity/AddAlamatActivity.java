package mobi.garden.bottomnavigationtest.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import mobi.garden.bottomnavigationtest.LoginRegister.User;
import mobi.garden.bottomnavigationtest.LoginRegister.UserLocalStore;
import mobi.garden.bottomnavigationtest.R;

public class AddAlamatActivity extends AppCompatActivity{

    private static UserLocalStore userLocalStore;
    static User currUser;
    public static List<User> userList = new ArrayList<>();
    Context context;
    String customerID, recipientName, recipientNumber, customerAddress, customerCity, customerPostalCode, customerProvince;

    Button btnAdd;
    EditText EtNamaPenerima, EtAlamatPenerima, EtKotaPenerima, EtKodePosPenerima, EtProvinsiPenerima, EtTeleponPenerima;
    TextInputLayout tilNamaPenerima, tilTeleponPenerima, tilAlamatPenerima, tilKotaPenerima, tilKodePosPenerima, tilProvinsiPenerima;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alamat);

        userLocalStore = new UserLocalStore(getApplicationContext());
        currUser = userLocalStore.getLoggedInUser();
        customerID = String.valueOf(currUser.getUserID());
        context = getApplicationContext();

        EtNamaPenerima = (EditText)findViewById(R.id.etNamaPenerima);
        EtTeleponPenerima = (EditText) findViewById(R.id.etTeleponPenerima);
        EtAlamatPenerima = (EditText) findViewById(R.id.etAlamatPenerima);
        EtKotaPenerima = (EditText) findViewById(R.id.etKotaPenerima);
        EtKodePosPenerima = (EditText) findViewById(R.id.etKodePosPenerima);
        EtProvinsiPenerima = (EditText) findViewById(R.id.etProvinsiPenerima);
        btnAdd = findViewById(R.id.btn_addAlamat);

        tilNamaPenerima = findViewById(R.id.til_namaPenerima);
        tilTeleponPenerima = findViewById(R.id.til_teleponPerimna);
        tilAlamatPenerima = findViewById(R.id.til_alamat);
        tilKotaPenerima = findViewById(R.id.til_kotaPenerima);
        tilKodePosPenerima = findViewById(R.id.til_kodeposPerimna);
        tilProvinsiPenerima = findViewById(R.id.til_provinsiPenerima);


        Toolbar toolbar = findViewById(R.id.tbTambahAlamat);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recipientName = EtNamaPenerima.getText().toString();
                recipientNumber = EtTeleponPenerima.getText().toString();
                customerAddress = EtAlamatPenerima.getText().toString();
                customerCity = EtKotaPenerima.getText().toString();
                customerPostalCode = EtKodePosPenerima.getText().toString();
                customerProvince = EtProvinsiPenerima.getText().toString();

                add();
                startActivity(new Intent(AddAlamatActivity.this, AlamatProfile.class));
            }
        });
    }

    public void add() {
        JSONObject objAdd = new JSONObject();
        JSONArray arrData = new JSONArray();
        JSONObject objDetail = new JSONObject();
        try {

            objDetail.put("CustomerID", customerID);
            objDetail.put("RecipientName", recipientName);
            objDetail.put("RecipientNumber", recipientNumber);
            objDetail.put("CustomerLocationAddress", customerAddress);
            objDetail.put("CustomerCity", customerCity);
            objDetail.put("CustomerPostalCode",customerPostalCode);
            objDetail.put("CustomerProvince", customerProvince);
            //objDetail.put("UpdatedBy", updatedBy);
            arrData.put(objDetail);
            objAdd.put("data", arrData);
            Log.d("taggg", String.valueOf(arrData));

            Toast.makeText(getApplicationContext(), String.valueOf(arrData), Toast.LENGTH_SHORT).show();
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        Log.d("objaddd", objAdd.toString());
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, "http://pharmanet.apodoc.id/add_alamat_customer.php", objAdd,
                new Response.Listener<JSONObject>() {
                    @Override

                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("OK")) {
                                Toast.makeText(getApplicationContext(), "alamat berhasil ditambahkan", Toast.LENGTH_SHORT).show();                       }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
