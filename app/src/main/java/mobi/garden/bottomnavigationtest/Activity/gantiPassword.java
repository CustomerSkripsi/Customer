package mobi.garden.bottomnavigationtest.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mobi.garden.bottomnavigationtest.R;
import mobi.garden.bottomnavigationtest.Session.SessionManagement;

public class gantiPassword extends AppCompatActivity {
    EditText etPassLamaGantiPass, etPassBaruGantiPass,etKonfirmasiPassBaruGantiPass;
    TextView tvUsername, tvTanggalLahir, tvJenisKelamin,tvUsernameGantiPass;
    RequestQueue queue;

    Context context;
    AlertDialog dialog;
    AlertDialog.Builder builder;
    AlertDialog.Builder builder2;
    AlertDialog.Builder builder3;
    Button btnGantiPass;
    String  namaLogin = "";


    public static final String GANTIPASSWORD_URL = "http://sayasehat.apodoc.id/sp_gantiPassword2B2C.php";

    //Kode bebas
    private static final String SALT_LOGIN = "Century";

    SessionManagement session;
    String Relasi_CardNumber;

    //Tampungan hasil Query
    String Relasi_member, Relasi_nama, Relasi_Hp, Relasi_Address, Relasi_BirthDate, Relasi_City, Relasi_Email, Relasi_Gender, Relasi_Username, Relasi_Password;

    //fireBase
    String nomorHPAuth, mVerificationId;
    Button btnRegis, btnVerifikasi, btnKirimKode, btnKirimUlang;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    EditText etVerifikasi;
    HashMap<String, String> member = null;

    //Linear Layout
    LinearLayout FormRegLayout, validasiForm;

    public gantiPassword() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ganti_password);

        queue = Volley.newRequestQueue(this);
        session = new SessionManagement(getApplicationContext());
        builder = new AlertDialog.Builder(this);
        builder2 = new AlertDialog.Builder(this);
        builder3 = new AlertDialog.Builder(this);
        context = this;
        etPassLamaGantiPass = findViewById(R.id.etPassLamaGantiPass);
        etPassBaruGantiPass = findViewById(R.id.etPassBaruGantiPass);
        etKonfirmasiPassBaruGantiPass = findViewById(R.id.etKonfirmasiPassBaruGantiPass);
        builder = new AlertDialog.Builder(this);
        builder2 = new AlertDialog.Builder(this);
        builder3 = new AlertDialog.Builder(this);
        tvUsernameGantiPass = findViewById(R.id.tvUsernameGantiPass);
        btnGantiPass = findViewById(R.id.btnGantiPass);
        member = session.getMemberDetails();

//Panggil data
        String url1 = "http://sayasehat.apodoc.id/selectMemberPass.php?member="+member.get(SessionManagement.KEY_KODEMEMBER);
        JsonObjectRequest req = new JsonObjectRequest(url1, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray users;
                        try {
                            if (response.getString("status").equals("OK"))
                            {
                                users = response.getJSONArray("result");
                                for(int i = 0;i < users.length();i++)
                                {
                                    JSONObject obj = users.getJSONObject(i);

                                    Relasi_member = obj.getString("Relasi_CardNumber").trim();

                                    if(Relasi_member.equals("KOSONG")){
                                        builder.setMessage("Maaf Member tidak ditemukan ");
                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        });
                                        dialog = builder.show();

                                    }else{

                                        Relasi_member = obj.getString("Relasi_CardNumber").trim();
                                        Relasi_nama = obj.getString("Relasi_Name").trim();
                                        Relasi_Hp = obj.getString("Relasi_Phone1").trim();
                                        Relasi_Address = obj.getString("Relasi_Address1").trim();
                                        Relasi_BirthDate = obj.getString("Relasi_BirthDate").trim();
                                        Relasi_City = obj.getString("City_Name").trim();
                                        Relasi_Email = obj.getString("Relasi_Email").trim();
                                        Relasi_Gender = obj.getString("Relasi_Gender").trim();
                                        Relasi_Username = obj.getString("Relasi_Username").trim();
                                        Relasi_Password = obj.getString("Relasi_Password").trim();

                                        tvUsernameGantiPass.setText(Relasi_nama);
                                    }

                                }

                            } else {
                                showErrorMessage();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(EditActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }

        );
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(req);
        btnGantiPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                builder.setMessage("Apakah Anda yakin sudah benar?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (etPassLamaGantiPass.getText().toString().equals("")) {
                            builder3.setMessage("Harap isi Password Lama Anda");
                            builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    etPassLamaGantiPass.setError("Tidak boleh kosong");
                                    etPassLamaGantiPass.requestFocus();
                                }
                            });
                            builder3.show();
                        }else if (!etPassLamaGantiPass.getText().toString().trim().equals(Relasi_Password)) {
                            builder3.setMessage("Password lama Anda tidak cocok");
                            builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    etPassLamaGantiPass.setError("Tidak cocok");
                                    etPassLamaGantiPass.requestFocus();
                                }
                            });
                            builder3.show();
                        }else if (etPassBaruGantiPass.getText().toString().equals("")) {
                            builder3.setMessage("Harap isi Password Baru Anda");
                            builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    etPassBaruGantiPass.setError("harap diisi");
                                    etPassBaruGantiPass.requestFocus();
                                }
                            });
                            builder3.show();

                        }else if (!isValidpass(etPassBaruGantiPass.getText().toString()) || etPassBaruGantiPass.getText().length() < 7) {
                            builder3.setMessage("Password minimal 7 karakter dan mengandung huruf dan angka");
                            builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    etPassBaruGantiPass.setError("Format Salah");
                                    etPassBaruGantiPass.requestFocus();
                                }
                            });
                            builder3.show();
                        } else if (!etPassBaruGantiPass.getText().toString().trim().equals(etKonfirmasiPassBaruGantiPass.getText().toString().trim())) {
                            builder3.setMessage("Password tidak sama dengan Konfirmasi");
                            builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    etKonfirmasiPassBaruGantiPass.setError("Harus sama");
                                    etKonfirmasiPassBaruGantiPass.requestFocus();
                                }
                            });
                            builder3.show();
                        }

                        else {
                            JSONObject objRegister = new JSONObject();
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            try {

                                JSONObject objDetail = new JSONObject();

                                objDetail.put("password",etKonfirmasiPassBaruGantiPass.getText().toString().trim());
                                objDetail.put("member", member.get(SessionManagement.KEY_KODEMEMBER));
                                objRegister.put("data", objDetail);
                                Log.d("qwer", "qqqqqqqqqq");

                            }catch (JSONException e){
                                e.printStackTrace();
                            }

                            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, GANTIPASSWORD_URL, objRegister,
                                    new Response.Listener<JSONObject>() {
                                        @Override

                                        public void onResponse(JSONObject response) {
                                            JSONArray users;
                                            try {
                                                Log.d("qwer1", "qqqqqqqqqq");
                                                if (response.getString("status").equals("OK")) {
                                                    users = response.getJSONArray("result");
                                                    Log.d("qwer", "qqqqqqqqqq");
                                                    for(int i = 0;i < users.length();i++)
                                                    {
                                                        JSONObject obj = users.getJSONObject(i);
                                                        Relasi_CardNumber = obj.getString("Relasi_CardNumber").trim();
                                                        Log.d("qwer", Relasi_CardNumber);
                                                        if(Relasi_CardNumber.equals("KOSONG")){
                                                            showErrorMessage();
                                                        }else {

                                                            builder2.setMessage("Data sudah tersimpan");
                                                            builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                    Intent intent = new Intent(gantiPassword.this, MemberActivity.class);
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
                                            Toast.makeText(gantiPassword.this,"aaa",Toast.LENGTH_LONG ).show();
                                        }
                                    });
                                RequestQueue requestQueue = Volley.newRequestQueue(gantiPassword.this);
                            requestQueue.add(stringRequest);
                        }
                    }

                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                dialog = builder.show();

            }
        });
    }

    //ERROR MESSAGE
    private void showErrorMessage(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(gantiPassword.this);
        dialogBuilder.setMessage("Incorrect members");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }

    //Verifikasi Kode SMS
    private void verifikasiPhoneCredential(PhoneAuthCredential phoneAuthCredential) {
        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(gantiPassword.this, "Berhasil verifikasi nomor", Toast.LENGTH_SHORT).show();
                            session.LoginSession(namaLogin, Relasi_CardNumber, "","");

                            JSONObject objRegister = new JSONObject();
                            try {

                                JSONObject objDetail = new JSONObject();

                                objDetail.put("password",etKonfirmasiPassBaruGantiPass.getText().toString().trim());
                                objDetail.put("member", member.get(SessionManagement.KEY_KODEMEMBER));
                                objRegister.put("data", objDetail);

                                namaLogin = tvUsernameGantiPass.getText().toString();

                            }catch (JSONException e){
                                e.printStackTrace();
                            }

                            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, GANTIPASSWORD_URL, objRegister,
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
                                                        }else {

                                                            builder2.setMessage("Data Sudah tersimpan");
                                                            builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                    session.LoginSession(namaLogin, Relasi_CardNumber, "","");
                                                                    Intent intent = new Intent(gantiPassword.this, MemberActivity.class);
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
                                            // Toast.makeText(Register.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
                                        }
                                    });
                            RequestQueue requestQueue = Volley.newRequestQueue(gantiPassword.this);
                            requestQueue.add(stringRequest);

                        } else {
                            Toast.makeText(gantiPassword.this, "Kode Verifikasi Salah", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    public boolean isValidpass( String s ) {
        Pattern p = Pattern.compile( "[0-9]" );
        Matcher m = p.matcher( s );

        return m.find();
    }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }
}