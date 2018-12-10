package mobi.garden.bottomnavigationtest.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mobi.garden.bottomnavigationtest.Adapter.SearchAdapter;
import mobi.garden.bottomnavigationtest.Model.CityItem;
import mobi.garden.bottomnavigationtest.R;
import mobi.garden.bottomnavigationtest.Session.SessionManagement;

public class EditEMember extends AppCompatActivity {
    EditText etNama,etAlamat,etEmail,etPhone, etPassword,etUsername;
    //, etKonfirmasiPassword;
//    TextInputLayout ;
    TextView tvUsername, tvTanggalLahir, tvJenisKelamin;
    RequestQueue queue;
    RadioButton rbPria,rbWanita;
    DatePicker datePicker;
    Context context;
    AlertDialog dialog;
    AlertDialog.Builder builder;
    AlertDialog.Builder builder2;
    AlertDialog.Builder builder3;
    ImageView ivBarcode;
    private int year;
    private int month;
    private int day;
    static final int DATE_DIALOG_ID = 999;
    Button btnEdit;

    RecyclerView recyclerView;
    List<CityItem> cityItemList = new ArrayList<>();
    List<CityItem> searchList = new ArrayList<>();
    SearchAdapter searchAdapter;


    public static EditText etKota;
    int cekNomorHp;

    String m_city, namaLogin, strkota = "";
    Boolean verifikasiHp;

    public static final String EDIT_URL = "http://sayasehat.apodoc.id/editB2C.php";
    public static final String LISTKOTA_URL = "http://sayasehat.apodoc.id/listKota.php";
    //Kode bebas
    private static final String SALT_LOGIN = "Century";

    SessionManagement session;
    static String JK;

    // tanggal hari ini
    String today, awalbarcode = "";

    //
    String Relasi_CardNumber;


    //Tampungan hasil Query
    String Relasi_member, Relasi_nama, Relasi_Hp, Relasi_Address, Relasi_BirthDate, Relasi_City, Relasi_Email, Relasi_Gender, Relasi_Username;

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

    public EditEMember() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_emember);

        queue = Volley.newRequestQueue(this);
        session = new SessionManagement(getApplicationContext());
        builder = new AlertDialog.Builder(this);
        builder2 = new AlertDialog.Builder(this);
        builder3 = new AlertDialog.Builder(this);

        context = this;

        etNama = findViewById(R.id.etNama);
        etAlamat = findViewById(R.id.etAlamat);
        etEmail = findViewById(R.id.etEmail);
        etKota = findViewById(R.id.etKota);
        rbPria = findViewById(R.id.rbPria);
        rbWanita = findViewById(R.id.rbWanita);
        etPhone = findViewById(R.id.etPhone);
//        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        // etKonfirmasiPassword = findViewById(R.id.etKonfirmasiPassword);
        tvUsername = findViewById(R.id.tvUsername);
        tvTanggalLahir = findViewById(R.id.tvTanggalLahir2);
        tvJenisKelamin = findViewById(R.id.tvJenisKelamin);

        btnEdit = findViewById(R.id.btnEdit);

        member = session.getMemberDetails();

//Tanggal sekarang
        Date date = Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat("yyMM");
        today = formatter.format(date);
        awalbarcode = "8"+today;

//firebase
        etVerifikasi = findViewById(R.id.etKodeVeri);
        btnKirimUlang = findViewById(R.id.btnKirimUlang);
        btnKirimKode = findViewById(R.id.btnKirimVerifikasi);
        btnVerifikasi = findViewById(R.id.btnValid);
        validasiForm = findViewById(R.id.ValidasiForm);
        FormRegLayout = findViewById(R.id.FormRegLayout);
        mAuth = FirebaseAuth.getInstance();
        setUpVerificationCallback();

// Recycler Search Kota
        recyclerView = findViewById(R.id.rvSearch);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

//        etTanggalLahir.setInputType(InputType.TYPE_NULL);

        btnKirimUlang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resendVerificationCode(nomorHPAuth,mResendToken);
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

        btnVerifikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = etVerifikasi.getText().toString().trim();
                if(code.isEmpty()){
                    Toast.makeText(EditEMember.this, "KODE tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }
                else{
                    verifyPhoneNumberWithCode(mVerificationId, code);
                }
            }
        });

//Panggil data
        String url1 = "http://sayasehat.apodoc.id/selectMember.php?member="+member.get(SessionManagement.KEY_KODEMEMBER);
        JsonObjectRequest req = new JsonObjectRequest(url1, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray users;
                        try {
                            if (response.getString("status").equals("OK")) {
                                users = response.getJSONArray("result");
                                for(int i = 0;i < users.length();i++){
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

                                        tvUsername.setText(Relasi_Username);
                                        tvTanggalLahir.setText(Relasi_BirthDate);
                                        tvJenisKelamin.setText(Relasi_Gender);

                                        if(Relasi_nama.equals("")){
                                            etNama.setError("Nama harus diisi");
                                        }else {
                                            etNama.setText(Relasi_nama);
                                        }
                                        if(Relasi_Address.equals("")){
                                            etAlamat.setError("Alamat harus diisi");
                                        }else {
                                            etAlamat.setText(Relasi_Address);
                                        }
                                        if(Relasi_Email.equals("")){
                                            etEmail.setError("Email harus diisi");
                                        }else {
                                            etEmail.setText(Relasi_Email);
                                        }
                                        if(Relasi_Hp.equals("")){
                                            etPhone.setError("Nomor Handphone harus diisi");
                                        }else {
                                            etPhone.setText(Relasi_Hp);
                                        }
                                        if (Relasi_City.equals("")){
                                            etKota.setError("Kota Harus diisi");
                                        }else{
                                            etKota.setText(Relasi_City);
                                        }
                                        if (Relasi_Gender.equals("M")){
                                            tvJenisKelamin.setText("Pria");
                                        }else{
                                            tvJenisKelamin.setText("Wanita");
                                        }
//                                        etPassword.setError("Password tidak boleh kosong");
//                                        etKonfirmasiPassword.setError("Password tidak boleh kosong");

                                        //  SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy");
                                        //SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");
                                        //try {
                                        //  Date date = dateFormat1.parse(Relasi_BirthDate);
//                                            etTanggalLahir.setText(dateFormat2.format(date));

                                        //} catch (ParseException e) {
                                        //  e.printStackTrace();
                                        //}

//                                        if(Relasi_Gender.equals("M")){
//                                            rbPria.setChecked(true);
//                                        }
//                                        else{
//                                            rbWanita.setChecked(true);
//                                        }


                                        //update kode kota jika edit text kota tidak kosong
                                        if(!etKota.getText().toString().isEmpty()){
                                            updateM_city();
                                        }

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


        etKota.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    return true;
                }
                return false;
            }
        });

        etKota.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int size = etKota.getText().length();
                searchList.clear();
                for (int i=0; i< cityItemList.size(); i++){
                    if(size<=cityItemList.get(i).getNamaKota().length()){
                        if(cityItemList.get(i).getNamaKota().toLowerCase().trim()
                                .contains(etKota.getText().toString().toLowerCase().trim())){
                            searchList.add(cityItemList.get(i));
                        }
                    }
                }
                searchAdapter = new SearchAdapter(searchList, getBaseContext());
                recyclerView.setAdapter(searchAdapter);

            }
        });

        listKota();

//        rbPria.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    JK="M";
//                }
//            }
//        });
//
//        rbWanita.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    JK="F";
//                }
//            }
//        });



        etKota.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!etKota.isFocused()){
                    recyclerView.setVisibility(View.GONE);

                    //update relasi kotanya
                    updateM_city();
                }
                else{
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });

//
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateM_city();

                builder.setMessage("Apakah Anda yakin sudah benar?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (etNama.getText().toString().equals("")) {
                            builder3.setMessage("Harap isi Nama Anda");
                            builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    etNama.setError("Tidak boleh kosong");
                                }
                            });
                            builder3.show();
                        } else if (etAlamat.getText().toString().equals("")) {
                            builder3.setMessage("Harap isi Alamat Anda");
                            builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    etAlamat.setError("Tidak boleh kosong");
                                }
                            });
                            builder3.show();
                        } else if (etKota.getText().toString().equals("")) {
                            builder3.setMessage("Harap isi Kota Anda");
                            builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    etKota.setError("Tidak boleh kosong");
                                }
                            });
                            builder3.show();
//                        } else if (etTanggalLahir.getText().toString().equals("")) {
//                            builder3.setMessage("Harap isi Tanggal Lahir Anda");
//                            builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    etTanggalLahir.setError("Tidak boleh kosong");
//                                }
//                            });
//                            builder3.show();
                        }
//                        else if (!rbPria.isChecked() && !rbWanita.isChecked()) {
//                            builder3.setMessage("Harap pilih jenis kelamin Anda");
//                            builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                }
//                            });
//                            builder3.show();
//                        }
                        else if (etEmail.getText().toString().equals("")) {
                            builder3.setMessage("Harap isi Email Anda");
                            builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    etEmail.setError("Tidak boleh kosong");
                                }
                            });
                            builder3.show();
                        } else if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()
                                || !isValidemail(etEmail.getText().toString())) {
                            builder3.setMessage("Format Email tidak valid");
                            builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    etEmail.setError("Format Email Anda salah");
                                }
                            });
                            builder3.show();
                        }  else if (etPhone.getText().toString().equals("")) {
                            builder3.setMessage("Harap isi Nomor Handphone Anda");
                            builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    etPhone.setError("Tidak boleh kosong");
                                }
                            });
                            builder3.show();
                        } else if (!isValidphone(etPhone.getText().toString()) || etPhone.getText().length() < 7) {
                            builder3.setMessage("Format Nomor Handphone Anda salah");
                            builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    etPhone.setError("Format Salah");
                                }
                            });
                            builder3.show();

//                        }  else if (etPassword.getText().toString().equals("")) {
//                            builder3.setMessage("Harap isi Password Anda");
//                            builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    etPassword.setError("Tidak boleh kosong");
//                                }
//                            });
//                            builder3.show();
//                        } else if (!isValidpass(etPassword.getText().toString()) || etPassword.getText().length() < 7) {
//                            builder3.setMessage("Format Password Anda salah");
//                            builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    etPassword.setError("Format Salah");
//                                }
//                            });
//                            builder3.show();
//                        }
//                        else if (etKonfirmasiPassword.getText().toString().equals("")) {
//                            builder3.setMessage("Harap isi Password Anda");
//                            builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    etKonfirmasiPassword.requestFocus();
//                                }
//                            });
//                            builder3.show();
//                        } else if (!etPassword.getText().toString().trim().equals(etKonfirmasiPassword.getText().toString().trim())) {
//                            builder3.setMessage("Password tidak sama dengan Konfirmasi");
//                            builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    etKonfirmasiPassword.setError("Harus sama");
//                                }
//                            });
//                            builder3.show();


                            // kalau edit nomor hape
                        } else if (!(etPhone.getText().toString().equals(Relasi_Hp))){

                            jmlHps();
                            builder3.setMessage("Anda Merubah Nomor Handphone Anda. Tekan OK Untuk Verifikasi Kode");
                            builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    nomorHPAuth = plusNomorHp(etPhone.getText().toString());
                                    kirimSMS(nomorHPAuth);
                                    validasiForm.setVisibility(View.VISIBLE);
                                    FormRegLayout.setVisibility(View.GONE);
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

                                objDetail.put("nama", etNama.getText().toString().trim());
                                objDetail.put("alamat", etAlamat.getText().toString().trim());
                                objDetail.put("kota", m_city);
                                objDetail.put("tanggal", tvTanggalLahir.getText().toString().trim());
                                // objDetail.put("jk", JK.trim());
                                objDetail.put("email", etEmail.getText().toString().trim());
                                objDetail.put("nomor_hp", etPhone.getText().toString().trim());
                                //objDetail.put("password",etKonfirmasiPassword.getText().toString().trim());
                                objDetail.put("member", member.get(SessionManagement.KEY_KODEMEMBER));

                                objRegister.put("data", objDetail);

                                namaLogin = etNama.getText().toString();

                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                            Log.d("testedit", objRegister.toString());
                            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, EDIT_URL, objRegister,
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
                                                        Toast.makeText(EditEMember.this, "qwe", Toast.LENGTH_SHORT).show();
                                                        if(Relasi_CardNumber.equals("KOSONG")){
                                                            showErrorMessage();
                                                        }else {
                                                            Toast.makeText(context, "123123", Toast.LENGTH_SHORT).show();
                                                            builder2.setMessage("Data sudah tersimpan");
                                                            builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                    Intent intent = new Intent(EditEMember.this, MemberActivity.class);
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
                            RequestQueue requestQueue = Volley.newRequestQueue(EditEMember.this);
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
        //    setCurrentDateOnView();
        //  addListenerOnButton();


    }
    private void timers(){
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {

                btnKirimUlang.setEnabled(false);
            }

            public void onFinish() {
                btnKirimUlang.setEnabled(true);
            }
        }.start();
    }

    private void jmlHps(){
        String url1 = "http://sayasehat.apodoc.id/cekNomorHp.php?phone="+etPhone.getText().toString();
        JsonObjectRequest reqHp = new JsonObjectRequest(url1, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray countHp;
                        try {
                            countHp = response.getJSONArray("result");

                            String y = countHp.getJSONObject(0).getString("computed");
                            cekNomorHp = Integer.parseInt(y);

                            if(cekNomorHp >= 1){
                                verifikasiHp = true;
                                //  etPhone.setError("Nomor Sudah Digunakan");
                            }
                            else{
                                verifikasiHp = false;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(ListPasien.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
            }
        });

        queue.add(reqHp);
    }


    //ERROR MESSAGE
    private void showErrorMessage(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EditEMember.this);
        dialogBuilder.setMessage("Incorrect members");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }

    public void updateM_city(){
        String url1 = "http://sayasehat.apodoc.id/selectCity.php?city="+etKota.getText().toString();
        JsonObjectRequest req = new JsonObjectRequest(url1, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray users = null;
                        try {
                            users = response.getJSONArray("result");

                            if(String.valueOf(users).equals("[]")){
                                m_city = "1";
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        for (int i = 0; i < users.length(); i++) {
                            try {
                                JSONObject obj = users.getJSONObject(i);
                                m_city = obj.getString("Cty_ID");
                            } catch (JSONException e) {
                                e.printStackTrace();

                            }
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(EditEMember.this);
        requestQueue.add(req);

    }
    // SMS
    private void setUpVerificationCallback(){
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                etVerifikasi.setText(phoneAuthCredential.getSmsCode());
                verifikasiPhoneCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                if(e instanceof FirebaseAuthInvalidCredentialsException){
                    Toast.makeText(EditEMember.this,"Kode salah", Toast.LENGTH_SHORT).show();
                }
                else if (e instanceof FirebaseTooManyRequestsException){
                    Toast.makeText(EditEMember.this,"Request berlebihan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                mVerificationId = s;
                mResendToken = forceResendingToken;

                Toast.makeText(EditEMember.this,"Kode sedang dikirim", Toast.LENGTH_SHORT).show();

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
                mCallbacks
        );
    }

    //Resend SMS
    private void resendVerificationCode(String nomorHps, PhoneAuthProvider.ForceResendingToken token){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                nomorHps,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks,
                token
        );
    }

    //Verifikasi Kode SMS
    private void verifyPhoneNumberWithCode(String id, String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(id,code);
        verifikasiPhoneCredential(credential);
        Toast.makeText(EditEMember.this, "Tunggu Sebentar, Proses Verifikasi....", Toast.LENGTH_SHORT).show();
    }


    //Verifikasi Kode SMS
    private void verifikasiPhoneCredential(PhoneAuthCredential phoneAuthCredential) {
        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditEMember.this, "Berhasil verifikasi nomor", Toast.LENGTH_SHORT).show();
                            session.LoginSession(namaLogin, Relasi_CardNumber, "","");

                            JSONObject objRegister = new JSONObject();
                            try {

                                JSONObject objDetail = new JSONObject();

                                objDetail.put("nama", etNama.getText().toString().trim());
                                objDetail.put("alamat", etAlamat.getText().toString().trim());
                                objDetail.put("kota", m_city);
                                objDetail.put("tanggal", tvTanggalLahir.getText().toString().trim());
                                // objDetail.put("jk", JK.trim());
                                objDetail.put("email", etEmail.getText().toString().trim());
                                objDetail.put("nomor_hp", etPhone.getText().toString().trim());
                                //objDetail.put("password",etKonfirmasiPassword.getText().toString().trim());
                                objDetail.put("member", member.get(SessionManagement.KEY_KODEMEMBER));

                                objRegister.put("data", objDetail);

                                namaLogin = etNama.getText().toString();

                            }catch (JSONException e){
                                e.printStackTrace();
                            }

                            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, EDIT_URL, objRegister,
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
                                                                    Intent intent = new Intent(EditEMember.this, MemberActivity.class);
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
                            RequestQueue requestQueue = Volley.newRequestQueue(EditEMember.this);
                            requestQueue.add(stringRequest);

                        } else {
                            // Sign in failed, display a message and update the UI
                            Toast.makeText(EditEMember.this, "Kode Verifikasi Salah", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

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





    public static boolean isValidemail(String email){
        boolean validate;
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String emailPattern2 = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+\\.+[a-z]+";

        if (email.matches(emailPattern)) {
            validate = true;
        } else if (email.matches(emailPattern2)) {
            validate = true;
        } else {
            validate = false;
        }

        return validate;
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

    public static boolean isValidphone(String phone){
        boolean validate;
        if (phone.substring(0,1).equals("0")) {
            validate = true;
        } else {
            validate = false;
        }

        return validate;
    }

    private void listKota(){
        JsonObjectRequest stringGet = new JsonObjectRequest(LISTKOTA_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray users;
                        try {
                            users = response.getJSONArray("result");
                            JSONObject obj;

                            for (int i = 0; i < users.length(); i++) {
                                obj = users.getJSONObject(i);
                                strkota = obj.getString("Cty_Name").trim();
                                cityItemList.add(new CityItem(strkota));
                                searchAdapter = new SearchAdapter(cityItemList,context);
                                recyclerView.setAdapter(searchAdapter);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringGet);
    }

//    public void setCurrentDateOnView() {
//
//        datePicker = findViewById(R.id.datePicker);
//        etTanggalLahir = findViewById(R.id.etTanggalLahir);
//
//        final Calendar c = Calendar.getInstance();
//        year = c.get(Calendar.YEAR);
//        month = c.get(Calendar.MONTH);
//        day = c.get(Calendar.DAY_OF_MONTH);
//
//
//        datePicker.init(year, month, day, null);
//    }

//    public void addListenerOnButton() {
//        etTanggalLahir.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//                showDialog(DATE_DIALOG_ID);
//                return false;
//            }
//        });
//    }

//    @Override
//    protected Dialog onCreateDialog(int id) {
//        switch (id) {
//            case DATE_DIALOG_ID:
//                // set date picker as current date
//                return new DatePickerDialog(this, datePickerListener,
//                        year, month,day);
//        }
//        return null;
//    }

//    private DatePickerDialog.OnDateSetListener datePickerListener
//            = new DatePickerDialog.OnDateSetListener() {
//
//        // when dialog box is closed, below method will be called.
//        public void onDateSet(DatePicker view, int selectedYear,
//                              int selectedMonth, int selectedDay) {
//            year = selectedYear;
//            month = selectedMonth;
//            day = selectedDay;
//
//            // set selected date into textview
//            etTanggalLahir.setText(new StringBuilder().append(day).append("-").append(month + 1).append("-").append(year).append(" "), TextView.BufferType.EDITABLE);
//            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//            // set selected date into datepicker also
//            datePicker.init(year, month, day, null);
//
//        }
//    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(EditEMember.this,MemberActivity.class);
        startActivity(i);
    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }
}
