package mobi.garden.bottomnavigationtest.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
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

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import mobi.garden.bottomnavigationtest.R;
import mobi.garden.bottomnavigationtest.Session.SessionManagement;

public class HalamanAwalActivity extends AppCompatActivity {

    EditText etPhone;
    SessionManagement session;
    Context context;
    AlertDialog dialog;
    AlertDialog.Builder builder;
    AlertDialog.Builder builder2;
    RequestQueue queue;
    public static final String LOGIN_URL = "http://sayasehat.apodoc.id/login.php";
    String Relasi_nama, Relasi_CardNumber;


    EditText etUsername, etPassword;
    Button btnLogin, btnDaftar, btnSudah, btnBelum, btnLupaPass;
    TextView tvDaftar,tvLupaPass, tvGoogle, tvFacebook;


    //Kode bebas
    private static final String SALT_LOGIN = "Century";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halaman_awal);

        context = this;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        queue = Volley.newRequestQueue(this);
        session = new SessionManagement(getApplicationContext());
        btnLogin = findViewById(R.id.btnLogin);
        tvDaftar = findViewById(R.id.tvdaftar);
        tvLupaPass = findViewById(R.id.tvLupaPass);
//        tvGoogle = findViewById(R.id.tvGoogle);
//        tvFacebook = findViewById(R.id.tvfacebook);
        etUsername = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);

        if(session.isLoggedIn()){
            Intent i = new Intent(HalamanAwalActivity.this,HomeActivity.class);
            startActivity(i);
            finish();
        }

        builder = new AlertDialog.Builder(this);
        builder2 = new AlertDialog.Builder(this);



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogIn(etUsername.getText().toString(), etPassword.getText().toString());
            }
        });

        tvDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iii = new Intent(HalamanAwalActivity.this,RegisterMember.class);
                startActivity(iii);
            }
        });


       tvLupaPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iii = new Intent(HalamanAwalActivity.this,LupaPass.class);
                startActivity(iii);

            }
        });

//       tvGoogle.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View v) {
//               Toast.makeText(HalamanAwalActivity.this, "Coming soon", Toast.LENGTH_SHORT).show();
//           }
//       });
//
//       tvFacebook.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View v) {
//               Toast.makeText(HalamanAwalActivity.this, "Coming soon", Toast.LENGTH_SHORT).show();
//           }
//       });
    }

    //GENERATE PASSWORD
    public String get_SHA_512_SecurePassword(String passwordToHash, String salt){
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return generatedPassword;
    }


    private void LogIn(String s, String s1) {
        if (s.equals(""))
        {
            etUsername.setError("Harap diisi");
            builder.setMessage("Username tidak boleh kosong");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            dialog = builder.show();
        }
        else if(s1.equals("")){
            etPassword.setError("Harap diisi");
            builder.setMessage("Password tidak boleh kosong");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            dialog = builder.show();
        }
        else
        {
            JSONObject objadd = new JSONObject();
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            try {
                JSONObject objdetail = new JSONObject();
                objdetail.put("username",s);
//              objdetail.put("password",get_SHA_512_SecurePassword(s1,SALT_LOGIN));
                objdetail.put("password",s1);
                objadd.put("data",objdetail);
            }catch (JSONException e){
                e.printStackTrace();
            }


            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, LOGIN_URL, objadd,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONArray users;
                            try {
                                if (response.getString("status").equals("OK")) {

                                    users = response.getJSONArray("result");
                                    for(int i = 0;i < users.length();i++){
                                        JSONObject obj = users.getJSONObject(i);

                                        Relasi_CardNumber = obj.getString("Relasi_CardNumber").trim();

                                        if(Relasi_CardNumber.equals("KOSONG")){
                                            showErrorMessage();
                                        }
                                        else {
                                            Relasi_nama = obj.getString("Relasi_Name").trim();

                                            builder2.setMessage("Selamat Datang " + Relasi_nama.toUpperCase());
                                            builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    session.LoginSession(Relasi_nama, Relasi_CardNumber, "","");
                                                    Intent intent = new Intent(HalamanAwalActivity.this, HomeActivity.class);
                                                    finish();
                                                    startActivity(intent);
                                                }
                                            });
                                            builder2.show();
                                        }
                                    }

                                } else {
                                    builder.setMessage("Terjadi Kesalahan Koneksi");
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                        }
                                    });
                                    dialog = builder.show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(HalamanAwalActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }

            );
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);

        }

    }

    //ERROR MESSAGE
    private void showErrorMessage(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(HalamanAwalActivity.this);
        dialogBuilder.setMessage("Username dan Password tidak sesuai");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }

    @Override
    public void onBackPressed() {
        builder.setMessage("Ingin keluar dari aplikasi ?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog = builder.show();
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

}