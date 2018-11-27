package mobi.garden.bottomnavigationtest.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import mobi.garden.bottomnavigationtest.Model.Code128;
import mobi.garden.bottomnavigationtest.R;
import mobi.garden.bottomnavigationtest.Session.SessionManagement;

public class MemberActivity extends AppCompatActivity {

        RequestQueue queue;
        SessionManagement session;
        AlertDialog dialog;
        AlertDialog.Builder builder;
        ImageView ivBarcode,ivLogout, ivFilter1, ivFilter2, ivFilter3,ivChevronLeftProfile;
        Context context;
        TextView tvNama,tvLogout;
        String today, awalbarcode = "";
        Button btnRefresh, btnLogout, btnEditPass;
        ConstraintLayout mainMember;
        LinearLayout koneksiLayout;

        EditText etMemberID, etTanggalLahir,etNoTelp,etEmail;

    //Tampungan hasil Query
    String Relasi_member, Relasi_nama, Relasi_Hp, Relasi_Address, Relasi_BirthDate, Relasi_City, Relasi_Email, Relasi_Gender, Relasi_Username;

    // ADD TOOLBAR
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.itemmenu, menu);
            return super.onCreateOptionsMenu(menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()){
//                case R.id.edit:
//                    Intent intent = new Intent(MemberActivity.this, EditActivity.class);
//                    startActivity(intent);
//                    break;
//
                case R.id.GantiPassword:
                    Intent intent1 = new Intent(MemberActivity.this, gantiPassword.class);
                    startActivity(intent1);
                    break;
                case R.id.logout:
                    builder.setMessage("Apakah Anda ingin Logout member ?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            session.logoutUser();
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
                    break;
                default:
            }

            return super.onOptionsItemSelected(item);

        }
//END TOOLBAR


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_member);
            queue = Volley.newRequestQueue(this);
            builder = new AlertDialog.Builder(this);
            btnLogout = findViewById(R.id.btnLogout);
            btnEditPass = findViewById(R.id.btnEditPass);
            etEmail = findViewById(R.id.etEmail);
            etMemberID = findViewById(R.id.etMemberID);
            etNoTelp = findViewById(R.id.etNoTelp);
            etTanggalLahir = findViewById(R.id.etTanggalLahir);

            session = new SessionManagement(getApplicationContext());

            final HashMap<String, String> member = session.getMemberDetails();

            Toolbar mToolbar =findViewById(R.id.toolbar);
            mToolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);

            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MemberActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            });


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

                                            //etUsername.setText(Relasi_Username);
                                            etTanggalLahir.setText(Relasi_BirthDate);
//                                            tvJenisKelamin.setText(Relasi_Gender);
                                            etMemberID.setText(Relasi_member);

                                            if(Relasi_Email.equals("")){
                                                etEmail.setError("Email harus diisi");
                                            }else {
                                                etEmail.setText(Relasi_Email);
                                            }
                                            if(Relasi_Hp.equals("")){
                                                etNoTelp.setError("Nomor Handphone harus diisi");
                                            }else {
                                                etNoTelp.setText(Relasi_Hp);
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
//                                            if(!etKota.getText().toString().isEmpty()){
//                                                updateM_city();
//                                            }

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


//            btnRefresh = findViewById(R.id.btnRefresh);
//            koneksiLayout = findViewById(R.id.koneksiLayout);
//            mainMember = findViewById(R.id.mainMember);
            ivBarcode = findViewById(R.id.ivBarcode);

//            ivLogout = findViewById(R.id.ivLogout);
            tvNama = findViewById(R.id.tvNama);

//            ivFilter1 = findViewById(R.id.ivFilter1);
//            ivFilter2 = findViewById(R.id.ivFilter2);
//            ivFilter3 = findViewById(R.id.ivFilter3);

            context = this;

            Date date = Calendar.getInstance().getTime();
            DateFormat formatter = new SimpleDateFormat("yyMM");
            today = formatter.format(date);
            awalbarcode = "8"+today;


//            ivFilter1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Code128 code = new Code128(context);
//                    code.setData(member.get(SessionManagement.KEY_KODEMEMBER));
//                    Bitmap bitmap = code.getBitmap(800, 200);
//                    ivBarcode = findViewById(R.id.ivBarcode);
//                    ivBarcode.setImageBitmap(bitmap);
//
//                }
//            });
//            ivFilter2.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Code128 code = new Code128(context);
//                    code.setData(member.get(SessionManagement.KEY_KODEMEMBER));
//                    Bitmap bitmap = code.getBitmap(1000, 300);
//                    ivBarcode = findViewById(R.id.ivBarcode);
//                    ivBarcode.setImageBitmap(bitmap);
//                }
//            });
//            ivFilter3.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Code128 code = new Code128(context);
//                    code.setData(member.get(SessionManagement.KEY_KODEMEMBER));
//                    Bitmap bitmap = code.getBitmap(1200, 400);
//                    ivBarcode = findViewById(R.id.ivBarcode);
//                    ivBarcode.setImageBitmap(bitmap);
//                }
//            });


//            btnRefresh.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    if(isDataConnectionAvailable(MemberActivity.this) == false){
//                        Toast.makeText(context, "Tidak Ada Koneksi Internet", Toast.LENGTH_SHORT).show();
//                        tvNama.setVisibility(View.GONE);
//                        ivBarcode.setVisibility(View.GONE);
//                        ivLogout.setVisibility(View.GONE);
//                        ivFilter1.setVisibility(View.GONE);
//                        ivFilter2.setVisibility(View.GONE);
//                        ivFilter3.setVisibility(View.GONE);
//
//                        koneksiLayout.setVisibility(View.VISIBLE);
//                    }else{
//                        koneksiLayout.setVisibility(View.GONE);
//
//                        overridePendingTransition( 0, 0);
//                        startActivity(getIntent());
//                        finish();
//                        overridePendingTransition( 0, 0);
//                    }
//
//                }
//            });

            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    builder.setMessage("Apakah Anda ingin Logout member ?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            session.logoutUser();
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            startActivity(intent);
                            return;
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

            btnEditPass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    builder.setMessage("Apakah Anda ingin merubah password ?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent1 = new Intent(MemberActivity.this, gantiPassword.class);
                            startActivity(intent1);

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


//            tvNama.setVisibility(View.VISIBLE);
            ivBarcode.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.VISIBLE);
//            tvLogout.setVisibility(View.VISIBLE);
//            ivFilter1.setVisibility(View.VISIBLE);
//            ivFilter2.setVisibility(View.VISIBLE);
//            ivFilter3.setVisibility(View.VISIBLE);

// DRAW BARCODE
            drawBarcode(member.get(SessionManagement.KEY_KODEMEMBER));
            tvNama.setText(member.get(SessionManagement.KEY_NAMA.trim()));
        }

        //DRAW BARCODE
        private void drawBarcode(String drawMember) {

            Code128 code = new Code128(context);
            code.setData(drawMember);
            Bitmap bitmap = code.getBitmap(800, 200);
            ivBarcode = findViewById(R.id.ivBarcode);
            ivBarcode.setImageBitmap(bitmap);


        }

        @Override
        public void onBackPressed() {
            builder.setMessage("Apakah Anda ingin keluar dari aplikasi ?");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    startActivity(intent);
                    return;
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            dialog = builder.show();

        }

        @Override
        protected void onStart() {
            super.onStart();
            if(isDataConnectionAvailable(MemberActivity.this) == false){
                tvNama.setVisibility(View.GONE);
                ivBarcode.setVisibility(View.GONE);
//                ivLogout.setVisibility(View.GONE);
                ivFilter1.setVisibility(View.GONE);
                ivFilter2.setVisibility(View.GONE);
                ivFilter3.setVisibility(View.GONE);

                koneksiLayout.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected void onRestart() {
            super.onRestart();

        }

        public static boolean isDataConnectionAvailable(Context context) {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
        }
    //ERROR MESSAGE
    private void showErrorMessage(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MemberActivity.this);
        dialogBuilder.setMessage("Incorrect members");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }

    }
