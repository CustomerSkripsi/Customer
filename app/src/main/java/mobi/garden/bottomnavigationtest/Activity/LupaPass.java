package mobi.garden.bottomnavigationtest.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import mobi.garden.bottomnavigationtest.Session.SessionManagement;
import mobi.garden.bottomnavigationtest.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class LupaPass extends AppCompatActivity {

    SessionManagement session;

    //Firebase
    private FirebaseAuth mAuthLupaPass;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacksLupaPass;
    private PhoneAuthProvider.ForceResendingToken mResendTokenLupaPass;
    EditText etHPLupa_Pass, etLupaPassKodVerif;
    String nomorHPLupaAuth, mVerificationIdLupaPass;
    Button btnMasukLupaPass, btnLupaPassKirimVer, btnLupaPassVerifikasi, btnLupaPassKirimUlang;
    AlertDialog dialog;
    AlertDialog.Builder builder;
    long delay = 500;
    long last_text_edit = 0;
    Handler handler;

    //LinearLayout
    LinearLayout MenuLupaPass, linLupaPassValidasi;

    //RelativeLayout
    ProgressBar bar_LupaPassword;

    public static final String LUPAPASS_URL = "http://sayasehat.apodoc.id/lupapassword.php";


    //Tampungan hasil Query
    String Relasi_cardNumber, Relasi_nama, Relasi_Hp, Relasi_Address, Relasi_BirthDate, Relasi_City, Relasi_Email, Relasi_Gender, Relasi_Username;

    //tampungan validasi nomor hape
    int cekNomorHpLupaPass;
    Boolean verifikasiHpLupaPass;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_pass);

        //Session
        session = new SessionManagement(getApplicationContext());

        //LinearLayout
        MenuLupaPass = findViewById(R.id.MenuLupaPass);
        linLupaPassValidasi = findViewById(R.id.linLupaPassValidasi);

        //RelativeLayout
        bar_LupaPassword = findViewById(R.id.bar_LupaPassword);

        etHPLupa_Pass = findViewById(R.id.etHPLupa_Pass);
        btnMasukLupaPass = findViewById(R.id.btnMasukLupaPass);

        builder = new AlertDialog.Builder(this);

        //Firebase SMS
        btnLupaPassKirimVer = findViewById(R.id.btnLupaPassKirimVer);
        btnLupaPassVerifikasi = findViewById(R.id.btnLupaPassVerifikasi);
        btnLupaPassKirimUlang = findViewById(R.id.btnLupaPassKirimUlang);
        etLupaPassKodVerif = findViewById(R.id.etLupaPassKodVerif);
        mAuthLupaPass = FirebaseAuth.getInstance();
        setUpVerificationCallback();



        //validasi nomor hape
        queue = Volley.newRequestQueue(this);

//        etHPLupa_Pass.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//             //   jmlHps();
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                jmlHps();
//            }
//        });

        btnLupaPassKirimVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLupaPassKirimVer.setEnabled(false);
                if (etHPLupa_Pass.getText().toString().equals("")) {
                    builder.setMessage("Harap isi Nomor Anda");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            etHPLupa_Pass.setError("Harus diisi");
                        }
                    });
                    dialog = builder.show();
                    btnLupaPassKirimVer.setEnabled(true);

                } else if (!isValidphone(etHPLupa_Pass.getText().toString()) || etHPLupa_Pass.getText().length() < 7) {
                    builder.setMessage("Format Nomor Handphone Anda salah");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            etHPLupa_Pass.setError("Format salah");
                        }
                    });
                    builder.show();
                    btnLupaPassKirimVer.setEnabled(true);
                }
                else {
                    jmlHps();
                    bar_LupaPassword.setVisibility(View.VISIBLE);

                }
            }
        });


        btnLupaPassKirimUlang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resendVerificationCode(nomorHPLupaAuth,mResendTokenLupaPass);
                builder.setMessage("Kode telah dikirim, Harap tunggu sebentar");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        timers();
                    }
                });
                dialog = builder.show();

            }
        });

        btnLupaPassVerifikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = etLupaPassKodVerif.getText().toString().trim();
                if(code.isEmpty()){
                    Toast.makeText(LupaPass.this, "KODE tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }
                else{
                    verifyPhoneNumberWithCode(mVerificationIdLupaPass, code);
                }
            }
        });

//
//        btnMasukLupaPass.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                btnMasukLupaPass.setEnabled(false);
        //Validasi
//                String code = etHPLupa_Pass.getText().toString().trim();
//                if(code.isEmpty()){
//                    Toast.makeText(LupaPasswordActivity.this, "NOMOR tidak boleh kosong", Toast.LENGTH_SHORT).show();
//                    btnMasukLupaPass.setEnabled(true);
//                }
//                else if (!isValidphone(etHPLupa_Pass.getText().toString()) || etHPLupa_Pass.getText().length() < 7) {
//                    builder.setMessage("Format Nomor Handphone Anda salah");
//                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            etHPLupa_Pass.setError("Format salah");
//                        }
//                    });
//                    builder.show();
//                    bar_LupaPassword.setVisibility(View.GONE);
//                    btnMasukLupaPass.setEnabled(true);
//                }else {
//
//                    JSONObject objadd = new JSONObject();
////                    try {
////                        Thread.sleep(2000);
////                    } catch (InterruptedException e) {
////                        e.printStackTrace();
////                    }
//
//                    try {
//                        JSONObject objdetail = new JSONObject();
//                        objdetail.put("nomor_hp",etHPLupa_Pass.getText().toString().trim());
//                        objadd.put("data",objdetail);
//                        bar_LupaPassword.setVisibility(View.VISIBLE);
//
//                    }catch (JSONException e){
//                        e.printStackTrace();
//                    }
//
//
//                    JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, LUPAPASS_URL, objadd,
//                            new Response.Listener<JSONObject>() {
//                                @Override
//                                public void onResponse(JSONObject response) {
//                                    JSONArray users;
//                                    try {
//                                        if (response.getString("status").equals("OK")) {
//                                            users = response.getJSONArray("result");
//                                            for(int i = 0;i < users.length();i++){
//                                                JSONObject obj = users.getJSONObject(i);
//
//                                                Relasi_cardNumber = obj.getString("Relasi_CardNumber").trim();
//
//
//                                                if(Relasi_cardNumber.equals("KOSONG")){
//                                                    builder.setMessage("Maaf Member tidak ditemukan ");
//                                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                                        @Override
//                                                        public void onClick(DialogInterface dialogInterface, int i) {
//                                                            bar_LupaPassword.setVisibility(View.GONE);
//
//                                                        }
//                                                    });
//                                                    dialog = builder.show();
//                                                    btnMasukLupaPass.setEnabled(true);
//                                                    bar_LupaPassword.setVisibility(View.GONE);
//                                                }else{
//
//                                                    Relasi_cardNumber = obj.getString("Relasi_CardNumber").trim();
//                                                    Relasi_nama = obj.getString("Relasi_Name").trim();
//                                                    Relasi_Hp = obj.getString("Relasi_Hp").trim();
//                                                    Relasi_Address = obj.getString("Relasi_Address").trim();
//                                                    Relasi_BirthDate = obj.getString("Relasi_BirthDate").trim();
//                                                    Relasi_City = obj.getString("Relasi_City").trim();
//                                                    Relasi_Email = obj.getString("Relasi_Email").trim();
//                                                    Relasi_Gender = obj.getString("Relasi_Gender").trim();
//                                                    Relasi_Username = obj.getString("Relasi_Username").trim();
//
//
//
//
//                                                    builder.setMessage("Silahkan ubah password anda " + Relasi_nama.toUpperCase());
//                                                    bar_LupaPassword.setVisibility(View.GONE);
//                                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                                        @Override
//                                                        public void onClick(DialogInterface dialogInterface, int i) {
//                                                           // session.LoginSession(Relasi_nama,Relasi_cardNumber ,Relasi_Hp,Relasi_Username);
//                                                            Intent intent = new Intent(LupaPasswordActivity.this, EditLupaPasswordActivity.class);
//                                                            intent.putExtra("Relasi_nama",Relasi_nama);
//                                                            intent.putExtra("Relasi_cardNumber",Relasi_cardNumber);
//                                                            intent.putExtra("Relasi_Hp",Relasi_Hp);
//                                                            intent.putExtra("Relasi_Username",Relasi_Username);
//                                                            startActivity(intent);
////                                                            finish();
//                                                        }
//                                                    });
//                                                    dialog = builder.show();
//                                                    btnMasukLupaPass.setEnabled(true);
//                                                }
//
//                                            }
//
//                                        } else {
//                                            showErrorMessage();
//                                        }
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//
//                                }
//                            },
//                            new Response.ErrorListener() {
//                                @Override
//                                public void onErrorResponse(VolleyError error) {
//                                  //  Toast.makeText(LupaPasswordActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//                                    Toast.makeText(LupaPasswordActivity.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
//                                }
//
//                            }
//
//                    );
//                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//                    requestQueue.add(stringRequest);
//
//                }
//            }
//        });


    }

    private Runnable input_finish_checker = new Runnable() {
        @Override
        public void run() {
            if(System.currentTimeMillis()>(last_text_edit + delay - 5000)){
                jmlHps();
            }
        }
    };


    //    //validasi nomor hape
    private void jmlHps(){

        String url1 = "http://sayasehat.apodoc.id/cekNomorHp.php?phone="+etHPLupa_Pass.getText().toString();
        JsonObjectRequest reqHp = new JsonObjectRequest(url1, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray countHpMemberLama;
                        try {
                            countHpMemberLama = response.getJSONArray("result");

                            String y = countHpMemberLama.getJSONObject(0).getString("computed");
                            cekNomorHpLupaPass = Integer.parseInt(y);


                            if(cekNomorHpLupaPass == 0){
                                verifikasiHpLupaPass = true;
                                builder.setMessage("Member tidak ditemukan");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialog.dismiss();
                                    }
                                });
                                dialog = builder.show();
                                bar_LupaPassword.setVisibility(View.GONE);
//                                etHPMemberLama.setError("Member tidak ditemukan");
                                btnLupaPassKirimVer.setEnabled(true);
                            }
                            else{
                                verifikasiHpLupaPass = false;
                                builder.setMessage("Tekan OK untuk mengirimkan kode.");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        nomorHPLupaAuth = plusNomorHp(etHPLupa_Pass.getText().toString());
                                        kirimSMS(nomorHPLupaAuth);
                                        linLupaPassValidasi.setVisibility(View.VISIBLE);
                                        MenuLupaPass.setVisibility(View.GONE);
                                    }
                                });
                                dialog = builder.show();
                                bar_LupaPassword.setVisibility(View.GONE);
                                btnLupaPassKirimVer.setEnabled(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(ListPasien.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
                Toast.makeText(LupaPass.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
            }
        });
        queue.add(reqHp);

    }

    //ERROR MESSAGE
    private void showErrorMessage(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LupaPass.this);
        dialogBuilder.setMessage("Incorrect members");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }




    private void setUpVerificationCallback(){
        mCallbacksLupaPass = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                etLupaPassKodVerif.setText(phoneAuthCredential.getSmsCode());
                verifikasiPhoneCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                if(e instanceof FirebaseAuthInvalidCredentialsException){
                    Toast.makeText(LupaPass.this,"Kode salah", Toast.LENGTH_SHORT).show();
                }
                else if (e instanceof FirebaseTooManyRequestsException){
                    Toast.makeText(LupaPass.this,"Request berlebihan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                mVerificationIdLupaPass = s;
                mResendTokenLupaPass = forceResendingToken;

                Toast.makeText(LupaPass.this,"Kode sedang dikirim", Toast.LENGTH_SHORT).show();

            }
        };

    }

    //Kirim SMS
    public void kirimSMS(String nomorHps){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                nomorHps,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacksLupaPass
        );
    }

    //Resend SMS
    private void resendVerificationCode(String nomorHps, PhoneAuthProvider.ForceResendingToken token){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                nomorHps,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacksLupaPass,
                token
        );
    }

    //Verifikasi Kode SMS
    private void verifyPhoneNumberWithCode(String id, String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(id,code);
        verifikasiPhoneCredential(credential);
        Toast.makeText(LupaPass.this, "Tunggu Sebentar, Proses Verifikasi....", Toast.LENGTH_SHORT).show();
    }

    //Verifikasi Kode SMS setelah masukin kode
    private void verifikasiPhoneCredential(PhoneAuthCredential phoneAuthCredential) {
        mAuthLupaPass.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LupaPass.this, "Berhasil verifikasi nomor", Toast.LENGTH_SHORT).show();
                            linLupaPassValidasi.setVisibility(View.GONE);
                            MenuLupaPass.setVisibility(View.VISIBLE);
                            btnLupaPassKirimVer.setVisibility(View.GONE);
                            masuk();
                            //btnMasukLupaPass.setVisibility(View.VISIBLE);
                            etHPLupa_Pass.setEnabled(false);

                        } else {
                            // Sign in failed, display a message and update the UI
                            //                         Log.w("ee", "signInWithCredential:failure", task.getException());

                            Toast.makeText(LupaPass.this, "Kode Verifikasi Salah", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void masuk() {
        String code = etHPLupa_Pass.getText().toString().trim();
        if(code.isEmpty()){
            Toast.makeText(LupaPass.this, "NOMOR tidak boleh kosong", Toast.LENGTH_SHORT).show();
            btnMasukLupaPass.setEnabled(true);
        }
        else if (!isValidphone(etHPLupa_Pass.getText().toString()) || etHPLupa_Pass.getText().length() < 7) {
            builder.setMessage("Format Nomor Handphone Anda salah");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    etHPLupa_Pass.setError("Format salah");
                }
            });
            builder.show();
            bar_LupaPassword.setVisibility(View.GONE);
            btnMasukLupaPass.setEnabled(true);
        }else {

            JSONObject objadd = new JSONObject();
//                    try {
//                        Thread.sleep(2000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }

            try {
                JSONObject objdetail = new JSONObject();
                objdetail.put("nomor_hp",etHPLupa_Pass.getText().toString().trim());
                objadd.put("data",objdetail);
                bar_LupaPassword.setVisibility(View.VISIBLE);

            }catch (JSONException e){
                e.printStackTrace();
            }


            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, LUPAPASS_URL, objadd,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONArray users;
                            try {
                                if (response.getString("status").equals("OK")) {
                                    users = response.getJSONArray("result");
                                    for(int i = 0;i < users.length();i++){
                                        JSONObject obj = users.getJSONObject(i);

                                        Relasi_cardNumber = obj.getString("Relasi_CardNumber").trim();


                                        if(Relasi_cardNumber.equals("KOSONG")){
                                            builder.setMessage("Maaf Member tidak ditemukan ");
                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    bar_LupaPassword.setVisibility(View.GONE);

                                                }
                                            });
                                            dialog = builder.show();
                                            btnMasukLupaPass.setEnabled(true);
                                            bar_LupaPassword.setVisibility(View.GONE);
                                        }else{

                                            Relasi_cardNumber = obj.getString("Relasi_CardNumber").trim();
                                            Relasi_nama = obj.getString("Relasi_Name").trim();
                                            Relasi_Hp = obj.getString("Relasi_Hp").trim();
                                            Relasi_Address = obj.getString("Relasi_Address").trim();
                                            Relasi_BirthDate = obj.getString("Relasi_BirthDate").trim();
                                            Relasi_City = obj.getString("Relasi_City").trim();
                                            Relasi_Email = obj.getString("Relasi_Email").trim();
                                            Relasi_Gender = obj.getString("Relasi_Gender").trim();
                                            Relasi_Username = obj.getString("Relasi_Username").trim();




                                            builder.setMessage("Silahkan ubah password anda " + Relasi_nama.toUpperCase());
                                            bar_LupaPassword.setVisibility(View.GONE);
                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    // session.LoginSession(Relasi_nama,Relasi_cardNumber ,Relasi_Hp,Relasi_Username);
                                                    Intent intent = new Intent(LupaPass.this, EditLupaPasswordActivity.class);
                                                    intent.putExtra("Relasi_nama",Relasi_nama);
                                                    intent.putExtra("Relasi_cardNumber",Relasi_cardNumber);
                                                    intent.putExtra("Relasi_Hp",Relasi_Hp);
                                                    intent.putExtra("Relasi_Username",Relasi_Username);
                                                    startActivity(intent);
//                                                            finish();
                                                }
                                            });
                                            dialog = builder.show();
                                            btnMasukLupaPass.setEnabled(true);
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
                            //  Toast.makeText(LupaPasswordActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(LupaPass.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
                        }

                    }

            );
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);

        }
    }


    // Nomor HP dikasi Kode Negara +62
    private String plusNomorHp(String sNomorHp) {
        sNomorHp = sNomorHp.replaceAll("[^+0-9]", "");
        String country_code = "62";

        if(sNomorHp.substring(0,1).compareTo("0") == 0
                && sNomorHp.substring(1,2).compareTo("0") != 0){
            sNomorHp = "+" + country_code + sNomorHp.substring(1);
        }
        sNomorHp = sNomorHp.replaceAll("^[0]{1,4}","+");

        return sNomorHp;
    }

    private void timers(){
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {

                btnLupaPassKirimUlang.setEnabled(false);
            }

            public void onFinish() {
                btnLupaPassKirimUlang.setEnabled(true);
            }
        }.start();
    }


    public static boolean isValidphone(String phone){
        boolean validate;
        if (phone.substring(0,1).equals("0")) {
            validate = true;
        } else {
            validate = false;
        }

        return validate;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(LupaPass.this,HalamanAwalActivity.class);
        startActivity(i);

    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
