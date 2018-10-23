package mobi.garden.bottomnavigationtest.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import mobi.garden.bottomnavigationtest.LoginRegister.Register;
import mobi.garden.bottomnavigationtest.LoginRegister.User;
import mobi.garden.bottomnavigationtest.LoginRegister.UserLocalStore;
import mobi.garden.bottomnavigationtest.R;

public class ProfileCustomerActivity extends AppCompatActivity {
    ImageView IvChevronLeft;
    TextView TvCustomerName,TvMemberID, TvCustomerDOB, TvCustomerPhone, TvCustomerEmail;
    LinearLayout ll_CustomerDOB, ll_CustomerPhone, ll_CustomerEmail, ll_CustomerAlamat, ll_ChangePassword;
    CircleImageView ProfileCIV;
    public Calendar myCalendar;

    Uri file_uri;

    //untuk foto profile
    private static final int CAMERA_REQUEST = 1100;
    private static final int GALLERY_REQUEST = 1002;

    String memberID, customerID, customerFullName, customerDOB, customerEmail, customerPhone, customerPhoto, customerGender;

    private static UserLocalStore userLocalStore;
    static User currUser;

    Context context;


    private static final String TAG = "Profile";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        IvChevronLeft = findViewById(R.id.ivChevronLeftProfile);
        TvCustomerName = findViewById(R.id.tvCustomerName);
        TvMemberID = findViewById(R.id.tvMemberID);
        TvCustomerDOB = findViewById(R.id.tvCustomerDOB);
        TvCustomerPhone = findViewById(R.id.tvCustomerPhone);
        TvCustomerEmail = findViewById(R.id.tvCustomerEmail);
        ll_CustomerDOB = findViewById(R.id.llCustomerDOB);
        ll_CustomerPhone = findViewById(R.id.llCustomerPhone);
        ll_CustomerEmail = findViewById(R.id.llCustomerEmail);
        ll_CustomerAlamat = findViewById(R.id.llDaftarAlamatCustomer);
        ll_ChangePassword = findViewById(R.id.llChangePassword);
        ProfileCIV = findViewById(R.id.imageProfile);

        userLocalStore = new UserLocalStore(getApplicationContext());
        currUser = userLocalStore.getLoggedInUser();
        customerID = String.valueOf(currUser.getUserID());

        myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date;

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        IvChevronLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileCustomerActivity.this, MyMenuActivity.class);
                updateDOB();
                startActivity(i);
            }
        });

        ProfileCIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPhoto();
            }
        });

        ll_CustomerDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ProfileCustomerActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        ll_CustomerEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileCustomerActivity.this, EmailProfile.class );
                startActivity(i);
            }
        });

        ll_CustomerPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ll_CustomerAlamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileCustomerActivity.this, AlamatProfile.class );
                startActivity(i);
            }
        });

        ll_ChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePassword changePassword = new ChangePassword(ProfileCustomerActivity.this);
                changePassword.changePass();
            }
        });

        DatePickerDialog.OnDateSetListener nDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month +1;
                Log.d(TAG,"onDateSet: yyyy/mm/dd: " + year + "/" + month + "/" + day);

                String date = year + "/" + month + "/" + day;
                TvCustomerDOB.setText(date);
            }
        };

        getCustomerProfile();

    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        TvCustomerDOB.setText(sdf.format(myCalendar.getTime()));
    }

    private void getCustomerProfile (){
        String url = "http://pharmanet.apodoc.id/getCustomerProfile.php";
        JSONObject objCustomer = new JSONObject();
        try{
            JSONArray arrData = new JSONArray();
            JSONObject objDetail = new JSONObject();
            objDetail.put("CustomerID", customerID);
            arrData.put(objDetail);
            objCustomer.put("data", arrData);
        }catch (JSONException e){
            e.printStackTrace();
        }
        Log.d("objCustomerrrrrrr",objCustomer.toString());
        Toast.makeText(this, objCustomer.toString(), Toast.LENGTH_SHORT).show();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, objCustomer, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray ownerArray = null;
                try {
                    ownerArray = response.getJSONArray("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i<ownerArray.length();i++){
                    try {
                        JSONObject obj = ownerArray.getJSONObject(i);
                        memberID = obj.getString("CustomerID");
                        customerFullName = obj.getString("CustomerFullName");
                        //customerGender = obj.getString("CustomerGender");
                        customerPhone = obj.getString("CustomerPhone");
                        customerDOB = obj.getString("CustomerDOB");
                        customerEmail = obj.getString("CustomerEmail");
                        //customerAddress = obj.getString("CustomerAddress");
                        customerPhoto = obj.getString("CustomerPhoto");

                        //Toast.makeText(ProfileCustomerActivity.this, obj.getString("CustomerName"), Toast.LENGTH_SHORT).show();
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    TvMemberID.setText(memberID);
                    TvCustomerName.setText(customerFullName);
                    TvCustomerPhone.setText(customerPhone);
                    String[] parts = customerDOB.split(" ");
                    String part1 = parts[0];
                    TvCustomerDOB.setText(part1);
                    TvCustomerEmail.setText(customerEmail);
                    Picasso.with(ProfileCustomerActivity.this).load(customerPhoto).into(ProfileCIV);

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProfileCustomerActivity.this, "Maaf sedang terjadi kendala..", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(ProfileCustomerActivity.this);
        requestQueue.add(request);
    }

    private void dialogPhoto(){
        final Dialog dialog = new Dialog(ProfileCustomerActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_photo_profile);

        TextView TvCamera, TvGallery, TvCancel;
        TvCamera = dialog.findViewById(R.id.tvCamera);
        TvGallery = dialog.findViewById(R.id.tvGallery);
        TvCancel = dialog.findViewById(R.id.tvCancel);

        TvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TvGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    public void updateDOB(){
        JSONObject objectCustomer = new JSONObject();
        JSONArray array = new JSONArray();
        JSONObject objectDetail = new JSONObject();
        try {
            objectDetail.put("CustomerID", customerID);
            objectDetail.put("CustomerDOB", TvCustomerDOB.getText().toString());
            array.put(objectDetail);
            objectCustomer.put("data", array);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("error",e+"");
        }
        String url = "http://pharmanet.apodoc.id/updateCustomerDOB.php";
        Log.d("tesssss", objectCustomer.toString());

        Log.d("objOwner_update_profile", objectCustomer.toString());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, objectCustomer,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(ProfileCustomerActivity.this,
                                "Update data berhasil!", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(ProfileOwnerActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(ProfileCustomerActivity.this);
        requestQueue.add(request);
    }

    @Override
    public void onBackPressed() {
        updateDOB();
        super.onBackPressed();
    }


    //    private void openCamera() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
//            file_uri = Uri.fromFile(createImageFile());
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, file_uri);
//
//        } else if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
//            if(intent.resolveActivity(getPackageManager()) != null) {
//                File photoFile = null;
//                try {
//                    photoFile = createImageFile();
//                }catch (Exception e){
//                    //Toast.makeText(this, "Error photo file Nougat", Toast.LENGTH_SHORT).show();
//                }
//                if(photoFile!=null) {
//                    Uri photoURI = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", photoFile);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                    startActivityForResult(intent, CAMERA_REQUEST);
//                }
//            }
//        }else{
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, getFileUriCamera());
//            //Toast.makeText(this, file_uri.getPath().toString(), Toast.LENGTH_SHORT).show();
//        }
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
//            startActivityForResult(intent, CAMERA_REQUEST);
//        }
//    }
}
