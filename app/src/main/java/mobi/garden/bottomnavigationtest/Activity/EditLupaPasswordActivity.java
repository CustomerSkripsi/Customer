package mobi.garden.bottomnavigationtest.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mobi.garden.bottomnavigationtest.R;
import mobi.garden.bottomnavigationtest.Session.SessionManagement;

public class EditLupaPasswordActivity extends AppCompatActivity {

    EditText etKonfirmasiPasswordLupaPass, etPasswordLupaPass;
    TextView tvUsernameLupaPass;
    Button btnEditLupaPass;


    //Tampungan hasil Query
    String Relasi_member;
    AlertDialog.Builder builder, builder2, builder3;

    AlertDialog dialog;

    //session
    SessionManagement session;
    public static final String EDITLUPAPASSWORD_URL = "http://sayasehat.apodoc.id/editPassLupaPassB2C.php";

    String Relasi_CardNumber;
    String nama,member,noHP,username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_lupa_password);

        Intent i = getIntent();
        nama = i.getStringExtra("Relasi_nama");
        member = i.getStringExtra("Relasi_cardNumber");
        noHP = i.getStringExtra("Relasi_Hp");
        username = i.getStringExtra("Relasi_Username");
//        password = i.getStringExtra("Relasi_Password");


        etKonfirmasiPasswordLupaPass = findViewById(R.id.etKonfirmasiPasswordLupaPass);
        etPasswordLupaPass = findViewById(R.id.etPasswordLupaPass);
        tvUsernameLupaPass = findViewById(R.id.tvUsernameLupaPass);
        btnEditLupaPass = findViewById(R.id.btnEditLupaPass);
        builder = new AlertDialog.Builder(this);
        builder2 = new AlertDialog.Builder(this);
        builder3 = new AlertDialog.Builder(this);

        session = new SessionManagement(getApplicationContext());

//        final HashMap<String, String> member = session.getMemberDetails();

        tvUsernameLupaPass.setText(username);


        btnEditLupaPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder.setMessage("Apakah Anda yakin sudah benar?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (etPasswordLupaPass.getText().toString().equals("")) {
                            builder3.setMessage("Harap isi Password Anda");
                            builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    etPasswordLupaPass.setError("Tidak boleh kosong");
                                }
                            });
                            builder3.show();
                        } else if (!isValidpass(etPasswordLupaPass.getText().toString()) || etPasswordLupaPass.getText().length() < 7) {
                            builder3.setMessage("Password harus lebih dari 7 karakter dan mengandung huruf dan angka");
                            builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    etPasswordLupaPass.setError("Format Salah");
                                }
                            });
                            builder3.show();
                        }
                        else if (etKonfirmasiPasswordLupaPass.getText().toString().equals("")) {
                            builder3.setMessage("Harap isi Password Anda");
                            builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    etKonfirmasiPasswordLupaPass.requestFocus();
                                }
                            });
                            builder3.show();
                        } else if (!etPasswordLupaPass.getText().toString().trim().equals(etKonfirmasiPasswordLupaPass.getText().toString().trim())) {
                            builder3.setMessage("Password tidak sama dengan Konfirmasi");
                            builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    etKonfirmasiPasswordLupaPass.setError("Harus sama");
                                }
                            });
                            builder3.show();
                        } else {

                            JSONObject objRegister = new JSONObject();
//                        try {
//                            Thread.sleep(2000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                            try {

                                JSONObject objDetail = new JSONObject();


                                objDetail.put("password",etKonfirmasiPasswordLupaPass.getText().toString().trim());
                                objDetail.put("member", member);
                                objRegister.put("data", objDetail);
                                Log.d("data1", objDetail.toString());
                            }catch (JSONException e){
                                e.printStackTrace();
                            }

                            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, EDITLUPAPASSWORD_URL, objRegister,
                                    new Response.Listener<JSONObject>() {
                                        @Override

                                        public void onResponse(JSONObject response) {
                                            JSONArray users;
                                            try {
                                                Log.d("qwer", "msk");
                                                if (response.getString("status").equals("OK")) {

                                                    users = response.getJSONArray("result");
                                                    Log.d("qwer1", "msk1");

                                                    for(int i = 0;i < users.length();i++){
                                                        JSONObject obj = users.getJSONObject(i);

                                                        Relasi_CardNumber = obj.getString("Relasi_CardNumber").trim();
                                                        Log.d("qwer2", Relasi_CardNumber);

                                                        if(Relasi_CardNumber.equals("KOSONG")){
                                                            showErrorMessage();
                                                        }else {

                                                            builder2.setMessage("Data sudah tersimpan");
                                                            builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                    session.LoginSession(nama,member ,noHP,username);
                                                                    Intent intent = new Intent(EditLupaPasswordActivity.this, MemberActivity.class);
                                                                    startActivity(intent);
                                                                }
                                                            });
                                                            builder2.show();
                                                        }
                                                    }


                                                }
                                            } catch (JSONException e1) {
                                                e1.printStackTrace();
                                            }

                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                             Toast.makeText(EditLupaPasswordActivity.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
                                        }
                                    });
                            RequestQueue requestQueue = Volley.newRequestQueue(EditLupaPasswordActivity.this);
                            requestQueue.add(stringRequest);


                        }
                    }
                });
                dialog = builder.show();
            }
        });

    }
    public boolean isValidpass( String s ) {
        Pattern p = Pattern.compile( "[0-9]" );
        Matcher m = p.matcher( s );

        return m.find();
    }
    //ERROR MESSAGE
    private void showErrorMessage(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EditLupaPasswordActivity.this);
        dialogBuilder.setMessage("Incorrect members");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
