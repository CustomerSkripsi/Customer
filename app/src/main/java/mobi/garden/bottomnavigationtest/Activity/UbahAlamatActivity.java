package mobi.garden.bottomnavigationtest.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import java.util.ArrayList;
import java.util.List;

import mobi.garden.bottomnavigationtest.LoginRegister.User;
import mobi.garden.bottomnavigationtest.LoginRegister.UserLocalStore;
import mobi.garden.bottomnavigationtest.R;

public class UbahAlamatActivity extends AppCompatActivity{

    private static UserLocalStore userLocalStore;
    static User currUser;
    public static List<User> userList = new ArrayList<>();
    Context context;

    String customerID, customerLocationID, namaPenerima, teleponPenerima, alamatPenerima, kotaPenerima, kodeposPenerima, provinsiPenerima;
    EditText EtRecipientName, EtRecipientNumber, EtCustomerCity, EtCustomerAddress, EtCustomerPostalCode, EtCustomerProvince;
    Button BtnUbah;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_alamat);

        EtRecipientName = findViewById(R.id.etNamaPenerima);
        EtRecipientNumber = findViewById(R.id.etTeleponPenerima);
        EtCustomerAddress = findViewById(R.id.etAlamatPenerima);
        EtCustomerCity = findViewById(R.id.etKotaPenerima);
        EtCustomerPostalCode = findViewById(R.id.etKodePosPenerima);
        EtCustomerProvince = findViewById(R.id.etProvinsiPenerima);
        BtnUbah = findViewById(R.id.btn_ubahAlamat);


        customerLocationID = getIntent().getStringExtra("LocationID");
        namaPenerima = getIntent().getStringExtra("NamaPenerima");
        teleponPenerima = getIntent().getStringExtra("TeleponPenerima");
        alamatPenerima = getIntent().getStringExtra("AlamatPenerima");
        kotaPenerima = getIntent().getStringExtra("KotaPenerima");
        kodeposPenerima = getIntent().getStringExtra("KodeposPenerima");
        provinsiPenerima = getIntent().getStringExtra("ProvinsiPenerima");

        EtRecipientName.setText(namaPenerima);
        EtRecipientNumber.setText(teleponPenerima);
        EtCustomerAddress.setText(alamatPenerima);
        EtCustomerCity.setText(kotaPenerima);
        EtCustomerPostalCode.setText(kodeposPenerima);
        EtCustomerProvince.setText(provinsiPenerima);

        userLocalStore = new UserLocalStore(getApplicationContext());
        currUser = userLocalStore.getLoggedInUser();
        customerID = String.valueOf(currUser.getUserID());
        context = getApplicationContext();

        Toolbar toolbar = findViewById(R.id.tbUbahAlamat);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        BtnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                namaPenerima = EtRecipientName.getText().toString();
                teleponPenerima = EtRecipientNumber.getText().toString();
                alamatPenerima = EtCustomerAddress.getText().toString();
                kotaPenerima= EtCustomerCity.getText().toString();
                kodeposPenerima = EtCustomerPostalCode.getText().toString();
                provinsiPenerima = EtCustomerProvince.getText().toString();

                User user = new User(customerLocationID, namaPenerima, teleponPenerima, alamatPenerima, kotaPenerima, kodeposPenerima, provinsiPenerima);
                ubah(user);

                startActivity(new Intent(UbahAlamatActivity.this, AlamatProfile.class));
            }
        });



    }
    public void ubah(User user) {
        JSONObject objAdd = new JSONObject();
        JSONArray arrData = new JSONArray();
        JSONObject objDetail = new JSONObject();
        try {

            objDetail.put("CustomerID", customerID);
            objDetail.put("CustomerLocationID", user.customerLocationID);
            objDetail.put("RecipientName", user.recipientName);
            objDetail.put("RecipientNumber", user.recipientNumber);
            objDetail.put("CustomerLocationAddress", user.customerAddress);
            objDetail.put("CustomerCity", user.customerCity);
            objDetail.put("CustomerPostalCode",user.customerPostalCode);
            objDetail.put("CustomerProvince", user.customerProvince);
            //objDetail.put("UpdatedBy", updatedBy);
            arrData.put(objDetail);
            objAdd.put("data", arrData);
            Log.d("taggg", String.valueOf(arrData));

            Toast.makeText(getApplicationContext(), String.valueOf(arrData), Toast.LENGTH_SHORT).show();
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        Log.d("objaddd", objAdd.toString());
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, "http://pharmanet.apodoc.id/update_alamat_customer.php", objAdd,
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
