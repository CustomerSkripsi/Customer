package mobi.garden.bottomnavigationtest.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import mobi.garden.bottomnavigationtest.BuildConfig;
import mobi.garden.bottomnavigationtest.Model.Code128;
import mobi.garden.bottomnavigationtest.R;
import mobi.garden.bottomnavigationtest.Session.SessionManagement;

public class MemberActivity extends AppCompatActivity {

        RequestQueue queue;
        SessionManagement session;
        AlertDialog dialog;
        AlertDialog.Builder builder;
        ImageView ivBarcode,ivLogout, ivFilter1, ivFilter2, ivFilter3,ivChevronLeftProfile,ivEdit;
        Context context;
        TextView tvNama,tvLogout;
        String today, awalbarcode = "";
        Button btnRefresh, btnLogout, btnEditPass;
        ConstraintLayout mainMember;
        LinearLayout koneksiLayout;
        CircleImageView ProfileCIV;

        EditText etMemberID, etTanggalLahir,etNoTelp,etEmail;

        Uri file_uri;

        //untuk foto profile
        private static final int CAMERA_REQUEST = 1100;
        private static final int GALLERY_REQUEST = 1002;
//        String ipSaya =  BuildConfig.BASE_URL+"belajarUpload.php";


        private String encoded_string, image_name,imagePath;
        private Bitmap imgBitmap;


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
            ivEdit = findViewById(R.id.ivEdit);
            etEmail = findViewById(R.id.etEmail);
            etMemberID = findViewById(R.id.etMemberID);
            etNoTelp = findViewById(R.id.etNoTelp);
            etTanggalLahir = findViewById(R.id.etTanggalLahir);
            ProfileCIV = findViewById(R.id.imageProfile);
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
            ProfileCIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogPhoto();
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
                                                    session.logoutUser();
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
            ivEdit.setOnClickListener((new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent11 = new Intent(MemberActivity.this, EditEMember.class);
                    startActivity(intent11);
                }
            }));
            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    builder.setMessage("Apakah Anda ingin Logout member ?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            session.logoutUser();
                            Intent intent = new Intent(MemberActivity.this, HalamanAwalActivity.class);
                            startActivity(intent);
                            return;
//                            Intent intent = new Intent(Intent.ACTION_MAIN);
//                            intent.addCategory(Intent.CATEGORY_HOME);
//                            startActivity(intent);
//                            return;
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

    private void dialogPhoto(){
        final Dialog dialog = new Dialog(MemberActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_photo_profile);

        TextView TvCamera, TvGallery, TvCancel;
        TvCamera = dialog.findViewById(R.id.tvCamera);
        TvGallery = dialog.findViewById(R.id.tvGallery);
        TvCancel = dialog.findViewById(R.id.tvCancel);

        TvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
                dialog.dismiss();
            }
        });

        TvGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
                dialog.dismiss();
            }
        });

        TvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if(checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED
                    &&
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED
                    &&
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            }else{
                //Toast.makeText(this, "Please open Say YESSSS", Toast.LENGTH_SHORT).show();

                if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(this,new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                    },CAMERA_REQUEST);
                    return;
                }

            }
        }
    }

    private void openGallery() {

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.N){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST  );
        }else{
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(intent, GALLERY_REQUEST);
        }
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            file_uri = Uri.fromFile(createImageFile());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, file_uri);

        } else if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            if(intent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                }catch (Exception e){
                    //Toast.makeText(this, "Error photo file Nougat", Toast.LENGTH_SHORT).show();
                }
                if(photoFile!=null) {
//                    Uri photoURI = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", photoFile);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(intent, CAMERA_REQUEST);
                }
            }
        }else{
            intent.putExtra(MediaStore.EXTRA_OUTPUT, getFileUriCamera());
            //Toast.makeText(this, file_uri.getPath().toString(), Toast.LENGTH_SHORT).show();
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA_REQUEST);
        }
    }

    public Uri getFileUriCamera() {
        //String authorize = getApplicationContext().getPackageName()+".fileprovider";
        String authorize = BuildConfig.APPLICATION_ID + ".provider";
        file_uri = FileProvider.getUriForFile(MemberActivity.this,
                authorize,
                createImageFile());
        return file_uri;
    }

    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + "_";
//        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_DCIM), "Camera");
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(imageFileName,".jpg",storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Toast.makeText(this, image.getName(), Toast.LENGTH_SHORT).show();
        imagePath = image.getAbsolutePath();
        return image;
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
        super.onBackPressed();
        Intent i = new Intent(MemberActivity.this,HalamanAwalActivity.class);
        startActivity(i);
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
    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(R.drawable.toolbar);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }

    }
