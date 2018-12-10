package mobi.garden.bottomnavigationtest.Activity;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mobi.garden.bottomnavigationtest.Adapter.SearchAdapter;
import mobi.garden.bottomnavigationtest.Model.CityItem;
import mobi.garden.bottomnavigationtest.R;
import mobi.garden.bottomnavigationtest.Session.SessionManagement;


public class RegisterMember extends AppCompatActivity{
    EditText etNama,etAlamat,etEmail,etPhone, etUsername, etPassword, etRegisterKonfirmasiPass;
    String Relasi_Username;

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

    RecyclerView recyclerView;
    List<CityItem> cityItemList = new ArrayList<>();
    List<CityItem> searchList = new ArrayList<>();
    SearchAdapter searchAdapter;


    public static EditText etKota, etTanggalLahir;
    int cekNomorHp, cekEmail, cekUserName;

    String m_city, namaLogin, strkota = "";
    Boolean verifikasiHp, verifikasiEmail, verifikasiUser = false;

    public static final String REGISTER_URL = "http://sayasehat.apodoc.id/registerMemberB2C.php";
    public static final String LISTKOTA_URL = "http://sayasehat.apodoc.id/listKota.php";
    //Kode bebas
    private static final String SALT_LOGIN = "Century";

    SessionManagement session;
    static String JK;

    // tanggal hari ini
    String today, awalbarcode = "";

    //
    String Relasi_CardNumber;



    //fireBase
    String nomorHPAuth, mVerificationId;
    Button btnRegis, btnVerifikasi, btnKirimKode, btnKirimUlang;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    EditText etVerifikasi;
    // LinearLayout
    LinearLayout formRegLayout, validasiForm;
    ProgressBar bar_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_member);

        queue = Volley.newRequestQueue(this);
        session = new SessionManagement(getApplicationContext());
        builder = new AlertDialog.Builder(this);
        builder2 = new AlertDialog.Builder(this);
        builder3 = new AlertDialog.Builder(this);

        context = this;


        etNama = findViewById(R.id.etNama);
        etTanggalLahir = (EditText) findViewById(R.id.etTanggalLahir);
        etAlamat = findViewById(R.id.etAlamat);
        etEmail = findViewById(R.id.etEmail);
        etKota = findViewById(R.id.etKota);
        rbPria = findViewById(R.id.rbPria);
        rbWanita = findViewById(R.id.rbWanita);
        etPhone = findViewById(R.id.etPhone);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etRegisterKonfirmasiPass = findViewById(R.id.etRegisterKonfirmasiPassword);

//Tanggal sekarang
        Date date = Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat("yyMM");
        today = formatter.format(date);
        awalbarcode = "8"+today;




// Recycler Search Kota
        recyclerView = findViewById(R.id.rvSearch);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        etTanggalLahir.setInputType(InputType.TYPE_NULL);
        bar_register = findViewById(R.id.bar_register);

//firebase
        etVerifikasi = findViewById(R.id.etKodeVeri);
        btnKirimUlang = findViewById(R.id.btnKirimUlang);
        btnKirimKode = findViewById(R.id.btnKirimVerifikasi);
        btnVerifikasi = findViewById(R.id.btnValid);
        formRegLayout = findViewById(R.id.FormRegLayout);
        validasiForm = findViewById(R.id.ValidasiForm);
        mAuth = FirebaseAuth.getInstance();
        setUpVerificationCallback();

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
                    Toast.makeText(RegisterMember.this, "KODE tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }
                else{
                    verifyPhoneNumberWithCode(mVerificationId, code);
                }
            }
        });


        btnKirimKode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etNama.getText().toString().equals("")) {
                    builder.setMessage("Harap isi Nama Anda");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            etNama.requestFocus();
                        }
                    });
                    dialog = builder.show();
                } else if (etAlamat.getText().toString().equals("")) {
                    builder.setMessage("Harap isi Alamat Anda");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            etAlamat.requestFocus();
                        }
                    });
                    dialog = builder.show();
                } else if (etKota.getText().toString().equals("")) {
                    builder.setMessage("Harap isi Kota Anda");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            etKota.requestFocus();
                        }
                    });
                    dialog = builder.show();
                } else if (etTanggalLahir.getText().toString().equals("")) {
                    builder.setMessage("Harap isi Tanggal Lahir Anda");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            etTanggalLahir.requestFocus();
                        }
                    });
                    dialog = builder.show();
                } else if (etEmail.getText().toString().equals("")) {
                    builder.setMessage("Harap isi Email Anda");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            etEmail.requestFocus();
                        }
                    });
                    dialog = builder.show();
                } else if (verifikasiEmail) {
                    builder.setMessage("Email Anda sudah terdaftar");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            etEmail.requestFocus();
                        }
                    });
                    dialog = builder.show();
                } else if (etPhone.getText().toString().equals("")) {
                    builder.setMessage("Harap isi Nomor Handphone Anda");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            etPhone.requestFocus();
                        }
                    });
                    dialog = builder.show();
                } else if (!isValidphone(etPhone.getText().toString()) || etPhone.getText().length() < 4) {
                    builder.setMessage("Format Nomor Handphone Anda salah");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            etPhone.requestFocus();
                        }
                    });
                    dialog = builder.show();
                } else if (verifikasiHp) {
                    builder.setMessage("Nomor Anda sudah terdaftar");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            etPhone.requestFocus();
                        }
                    });
                    dialog = builder.show();
                } else if (etUsername.getText().toString().equals("")) {
                    builder.setMessage("Harap isi Username Anda");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            etUsername.requestFocus();
                        }
                    });
                    dialog = builder.show();
                } else if (verifikasiUser) {
                    builder.setMessage("Username sudah digunakan");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            etUsername.requestFocus();
                        }
                    });
                    dialog = builder.show();
                }
                else if (etPassword.getText().toString().equals("")) {
                    builder.setMessage("Harap isi Password Anda");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            etPassword.requestFocus();
                        }
                    });
                    dialog = builder.show();
                } else if (!isValidpass(etPassword.getText().toString()) || etPassword.getText().length() < 7) {
                    builder.setMessage("Password minimal 7 karakter dan terdapat angka");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            etPassword.requestFocus();
                        }
                    });
                    dialog = builder.show();
                }else if (etRegisterKonfirmasiPass.getText().toString().equals("")) {
                    builder3.setMessage("Harap isi Password Anda");
                    builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            etRegisterKonfirmasiPass.requestFocus();
                        }
                    });
                    builder3.show();
                } else if (!etPassword.getText().toString().trim().equals(etRegisterKonfirmasiPass.getText().toString().trim())) {
                    builder3.setMessage("Password tidak sama dengan Konfirmasi");
                    builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            etRegisterKonfirmasiPass.setError("Harus sama");
                        }
                    });
                    builder3.show();
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()
                        || !isValidemail(etEmail.getText().toString())) {
                    builder.setMessage("Format Email tidak valid");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dialog = builder.show();
                } else if (!rbPria.isChecked() && !rbWanita.isChecked()) {
                    builder.setMessage("Harap pilih jenis kelamin Anda");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dialog = builder.show();
                } else {
                    builder.setMessage("Apakah anda yakin sudah benar? Tekan OK untuk mengirim kode");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            nomorHPAuth = plusNomorHp(etPhone.getText().toString());
                            kirimSMS(nomorHPAuth);
                            validasiForm.setVisibility(View.VISIBLE);
                            formRegLayout.setVisibility(View.GONE);
                        }
                    });
                    dialog = builder.show();

                }

            }
        });


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

        rbPria.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    JK="M";
                }
            }
        });

        rbWanita.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    JK="F";
                }
            }
        });


        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                jmlEmails();
            }
        });


        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                jmlHps();
            }
        });


        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                cekUsername();
            }
        });



        etKota.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!etKota.isFocused()){
                    recyclerView.setVisibility(View.GONE);

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

                    queue.add(req);
                }
                else{
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });

        btnRegis = findViewById(R.id.btnRegis);
        btnRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                builder.setMessage("Apakah Data Anda sudah benar?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (etNama.getText().toString().equals("")) {
                            builder3.setMessage("Harap isi Nama Anda");
                            builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    etNama.requestFocus();
                                }
                            });
                            builder3.show();
                        } else if (etAlamat.getText().toString().equals("")) {
                            builder3.setMessage("Harap isi Alamat Anda");
                            builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    etAlamat.requestFocus();
                                }
                            });
                            builder3.show();
                        } else if (etKota.getText().toString().equals("")) {
                            builder3.setMessage("Harap isi Kota Anda");
                            builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    etKota.requestFocus();
                                }
                            });
                            builder3.show();
                        } else if (etTanggalLahir.getText().toString().equals("")) {
                            builder3.setMessage("Harap isi Tanggal Lahir Anda");
                            builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    etTanggalLahir.requestFocus();
                                }
                            });
                            builder3.show();
                        } else if (etEmail.getText().toString().equals("")) {
                            builder3.setMessage("Harap isi Email Anda");
                            builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    etEmail.requestFocus();
                                }
                            });
                            builder3.show();
                        } else if (verifikasiEmail) {
                            builder3.setMessage("Email Anda sudah terdaftar");
                            builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    etEmail.requestFocus();
                                }
                            });
                            builder3.show();
                        } else if (etPhone.getText().toString().equals("")) {
                            builder3.setMessage("Harap isi Nomor Handphone Anda");
                            builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    etPhone.requestFocus();
                                }
                            });
                            builder3.show();
                        } else if (!isValidphone(etPhone.getText().toString()) || etPhone.getText().length() < 4) {
                            builder3.setMessage("Format Nomor Handphone Anda salah");
                            builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    etPhone.requestFocus();
                                }
                            });
                            builder3.show();
                        } else if (verifikasiHp) {
                            builder3.setMessage("Nomor Anda sudah terdaftar");
                            builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    etPhone.requestFocus();
                                }
                            });
                            builder3.show();
                        } else if (etUsername.getText().toString().equals("")) {
                            builder3.setMessage("Harap isi Username Anda");
                            builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    etUsername.requestFocus();
                                }
                            });
                            builder3.show();
                        } else if (verifikasiUser) {
                            builder3.setMessage("Nomor Anda sudah terdaftar");
                            builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    etUsername.requestFocus();
                                }
                            });
                            builder3.show();
                        }
                        else if (etPassword.getText().toString().equals("")) {
                            builder3.setMessage("Harap isi Password Anda");
                            builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    etPassword.requestFocus();
                                }
                            });
                            builder3.show();
                        } else if (!isValidpass(etPassword.getText().toString()) || etPassword.getText().length() < 7) {
                            builder3.setMessage("Format Password Anda salah");
                            builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    etPassword.requestFocus();
                                }
                            });
                            builder3.show();
                        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()
                                || !isValidemail(etEmail.getText().toString())) {
                            builder3.setMessage("Format Email tidak valid");
                            builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            builder3.show();
                        } else if (!rbPria.isChecked() && !rbWanita.isChecked()) {
                            builder3.setMessage("Harap pilih jenis kelamin Anda");
                            builder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            builder3.show();
                        } else {

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
        setCurrentDateOnView();
        addListenerOnButton();
    }

    //ERROR MESSAGE
    private void showErrorMessage(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(RegisterMember.this);
        dialogBuilder.setMessage("Gagal membuat Member");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }

    private void cekUsername() {
        String url1 = "http://sayasehat.apodoc.id/cekUsername.php?username="+etUsername.getText().toString();
        JsonObjectRequest reqUser = new JsonObjectRequest(url1, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray countUser;
                        try {
                            countUser = response.getJSONArray("result");
                            String y = countUser.getJSONObject(0).getString("COUNT_USER");
                            cekUserName = Integer.parseInt(y);

                            if(cekUserName >= 1){
                                verifikasiUser = true;
                                etUsername.setError("Username Sudah Digunakan");
                            }
                            else{
                                verifikasiUser = false;
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

        queue.add(reqUser);
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
                    Toast.makeText(RegisterMember.this,"Kode salah", Toast.LENGTH_SHORT).show();
                }
                else if (e instanceof FirebaseTooManyRequestsException){
                    Toast.makeText(RegisterMember.this,"Request berlebihan", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                mVerificationId = s;
                mResendToken = forceResendingToken;

                Toast.makeText(RegisterMember.this,"Kode sedang dikirim", Toast.LENGTH_SHORT).show();

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
        Toast.makeText(RegisterMember.this, "Tunggu Sebentar, Proses Verifikasi....", Toast.LENGTH_SHORT).show();
    }

    //Verifikasi Kode SMS
    private void verifikasiPhoneCredential(PhoneAuthCredential phoneAuthCredential) {
        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterMember.this, "Berhasil verifikasi nomor", Toast.LENGTH_SHORT).show();
                            JSONObject objRegister = new JSONObject();
//                            try {
//                                Thread.sleep(2000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }

                            try {

                                JSONObject objDetail = new JSONObject();

                                objDetail.put("nama", etNama.getText().toString().trim());
                                objDetail.put("alamat", etAlamat.getText().toString().trim());
                                objDetail.put("tanggal", etTanggalLahir.getText().toString().trim());
                                objDetail.put("jk", JK.trim());
                                objDetail.put("email", etEmail.getText().toString().trim());
                                objDetail.put("nomor_hp", etPhone.getText().toString().trim());
                                objDetail.put("kota", m_city.trim());
                                objDetail.put("username", etUsername.getText().toString().trim());
                                objDetail.put("password",etPassword.getText().toString().trim());
                                objDetail.put("awalmember", awalbarcode.trim());

                                objRegister.put("data", objDetail);

                                namaLogin = etNama.getText().toString();

                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                            Log.d("test123", objRegister.toString());
                            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, REGISTER_URL, objRegister,
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

                                                            builder2.setMessage("Selamat Datang " + namaLogin.toUpperCase());
                                                            builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                    session.LoginSession(namaLogin, Relasi_CardNumber, "","");
                                                                    Intent intent = new Intent(RegisterMember.this, HomeActivity.class);
                                                                    startActivity(intent);
                                                                }
                                                            });
                                                            bar_register.setVisibility(View.GONE);
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
                            RequestQueue requestQueue = Volley.newRequestQueue(RegisterMember.this);
                            requestQueue.add(stringRequest);

//                            Intent i = new Intent(RegisterActivity.this,MemberActivity.class);
//                            startActivity(i);
//                            validasiForm.setVisibility(View.GONE);
//                            formRegLayout.setVisibility(View.VISIBLE);
//                            btnRegis.setVisibility(View.VISIBLE);
//                            btnKirimKode.setVisibility(View.GONE);

                        } else {
                            // Sign in failed, display a message and update the UI
                            Toast.makeText(RegisterMember.this, "Kode Verifikasi Salah", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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

                btnKirimUlang.setEnabled(false);
            }

            public void onFinish() {
                btnKirimUlang.setEnabled(true);
            }
        }.start();
    }


    //Untuk Cek Email
    private void jmlEmails() {
        String url1 = "http://sayasehat.apodoc.id/cekEmail.php?email="+etEmail.getText().toString();
        JsonObjectRequest reqEmail = new JsonObjectRequest(url1, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray countEmail;
                        try {
                            countEmail = response.getJSONArray("result");

                            String yy = countEmail.getJSONObject(0).getString("computed");
                            cekEmail = Integer.parseInt(yy);

                            if(cekEmail >= 1){
                                verifikasiEmail = true;
                                etEmail.setError("Email sudah digunakan");
                            }
                            else{
                                verifikasiEmail = false;
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

        queue.add(reqEmail);

        if (isValidemail(etEmail.getText().toString())){
            //Toast.makeText(RegisterActivity.this, "Format email sudah benar", Toast.LENGTH_SHORT).show();
        } else {
            etEmail.setError("Format email salah");
        }

    }



    //Untuk Cek Nomor HP
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
                                etPhone.setError("Nomor Sudah Digunakan");
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



    //Validasi Format Email
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


    //Validasi Pass
    public boolean isValidpass( String s ) {
        Pattern p = Pattern.compile( "[0-9]" );
        Matcher m = p.matcher( s );

        return m.find();
    }


    //Generate Password
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


    //Validasi Phone
    public static boolean isValidphone(String phone){
        boolean validate;
        if (phone.substring(0,1).equals("0")) {
            validate = true;
        } else {
            validate = false;
        }

        return validate;
    }


    //Tampilin
    private void listKota(){
        JsonObjectRequest stringGet = new JsonObjectRequest(LISTKOTA_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray users = null;
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

    public void setCurrentDateOnView() {

        datePicker = findViewById(R.id.datePicker);
        etTanggalLahir = findViewById(R.id.etTanggalLahir);

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);


        datePicker.init(year, month, day, null);
    }

    public void addListenerOnButton() {
        etTanggalLahir.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                showDialog(DATE_DIALOG_ID);
                return false;
            }
        });
    }




    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener,
                        year, month,day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // set selected date into textview
            etTanggalLahir.setText(new StringBuilder().append(year).append("-").append(month + 1).append("-").append(day).append(" "), TextView.BufferType.EDITABLE);
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            // set selected date into datepicker also
            datePicker.init(year, month, day, null);

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(RegisterMember.this,HalamanAwalActivity.class);
        startActivity(i);
    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

