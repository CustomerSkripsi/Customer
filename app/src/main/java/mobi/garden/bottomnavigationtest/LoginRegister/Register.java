package mobi.garden.bottomnavigationtest.LoginRegister;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mobi.garden.bottomnavigationtest.R;

public class Register extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    Spinner etGender;

    private String[] state = { "Male", "Female"};

    Button bRegister, etDOB;

    EditText etFullName, etContact, etEmail,etUserName, etPassword, etConfirmPassword, etAddress, etCodeReferral;
    ImageView iv_logo;
    private static final String TAG = "Register";

    public static final String SERVER_ADDRESS = "http://pharmanet.apodoc.id/RegisterCustomer.php";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        System.out.println(state.length);
        etGender = (Spinner) findViewById(R.id.etGender);
        ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, state);
        adapter_state
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etGender.setAdapter(adapter_state);
        etGender.setOnItemSelectedListener(this);


        etFullName = (EditText) findViewById(R.id.etFullName);
        etDOB = (Button) findViewById(R.id.etDOB);
        //etGender = (EditText) findViewById(R.id.etGender);
        etContact = (EditText) findViewById(R.id.etContact);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        etAddress = (EditText) findViewById(R.id.etAddress);
        etCodeReferral = (EditText) findViewById(R.id.etCodeReferral);
        bRegister = (Button) findViewById(R.id.bRegister);
        iv_logo = (ImageView) findViewById(R.id.iv_logo);

        setStatusBarGradiant(this);


        bRegister.setOnClickListener(this);

        etDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        Register.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,nDateSetListener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
    }

    DatePickerDialog.OnDateSetListener nDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            month = month +1;
            Log.d(TAG,"onDateSet: yyyy/mm/dd: " + year + "/" + month + "/" + day);

            String date = year + "/" + month + "/" + day;
            etDOB.setText(date);
        }
    };

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bRegister:

                if (etFullName.getText().toString().length() == 0) {
                    etFullName.setError("Nama Lengkap Harus Diisi!");
                } else if (etDOB.getText().toString().length() == 0) {
                    etDOB.setError("Tanggal Lahir Harus Diisi!");
                } else if (etContact.getText().toString().length() == 0) {
                    etContact.setError("Nomor Telepon/ HP Harus Diisi!");
                } else if (etEmail.getText().toString().length() == 0) {
                    etEmail.setError("Email Harus Diisi!");
                } else if (etPassword.getText().toString().length() == 0) {
                    etPassword.setError("Password Harus Diisi!");
                } else if (etConfirmPassword.getText().toString().length() == 0) {
                    etConfirmPassword.setError("Confirm Password Harus Diisi!");
                } else if (!etConfirmPassword.getText().toString().equals(etPassword.getText().toString())){
                    etConfirmPassword.setError("Password Harus Sama!");
                } else if (etUserName.getText().toString().length() == 0) {
                    etUserName.setError("UserName harus Diisi!");
                } else if (etAddress.getText().toString().length() == 0) {
                    etAddress.setError("Alamat Harus Diisi!");
                } else if (etContact.getText().toString().length() > 12 ||etContact.getText().toString().length() <= 10) {
                    etContact.setError("Nomor Telepon/ HP Harus di antara 10-12 Digit!");
                } else if (etEmail.getText().toString().length() == 0) {
                    etEmail.setError("Email Harus Diisi!");
                } else if (isEmailValid(etEmail.getText().toString())==false) {
                    etEmail.setError("Email Harus Valid. Ex. abc@abc.abc");
                } else if (etCodeReferral.getText().toString().length() == 0) {
                    etCodeReferral.setError("Kode Refferal Harus Diisi!");
                } else{
                    Toast.makeText(getApplicationContext(), "Registrasi Berhasil!", Toast.LENGTH_SHORT).show();
                    String fullName = etFullName.getText().toString();
                    String DOB = etDOB.getText().toString();
                    String email = etEmail.getText().toString();
                    String gender = (String) etGender.getSelectedItem();
                    String contact = etContact.getText().toString();
                    String userName = etUserName.getText().toString();
                    String password = etPassword.getText().toString();
                    String address = etAddress.getText().toString();
                    String codeReferral = etCodeReferral.getText().toString();

                    User user = new User(fullName,DOB,gender,contact,email,userName,password,address,codeReferral);

                    register(user);
                    startActivity(new Intent(Register.this, Login.class));
                }

                break;
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        etGender.setSelection(position);
        String selState = (String) etGender.getSelectedItem();
    }



    public void register(User user){
        //Toast.makeText(this,"masuk",Toast.LENGTH_SHORT).show();

        JSONObject objadd= new JSONObject();

        try {
            JSONArray arrdata=new JSONArray();
            JSONObject objdetail=new JSONObject();
            objdetail.put("fullName",user.fullName);
            objdetail.put("DOB",user.DOB);
            objdetail.put("gender", user.gender);
            objdetail.put("contact",user.contact);
            objdetail.put("email",user.email);
            objdetail.put("userName",user.userName);
            objdetail.put("password",user.password);
            objdetail.put("address",user.address);
            objdetail.put("codeReferral",user.codeReferral);
            arrdata.put(objdetail);
            objadd.put("data",arrdata);
        }catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest stringRequest= new JsonObjectRequest(Request.Method.POST, SERVER_ADDRESS, objadd, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //  etxt1.setText(response.getString("status"));

                    Log.d("response",response.toString());
                    if (response.getString("status").equals("success")) {
                        Toast.makeText(getApplicationContext(), "register berhasil", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "register gagal", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(Register.this, e.getMessage()+"", Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Register.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("error_response_register",error.getMessage());
                    }

                }

        );
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


    //fungsi untuk validasi email
    public boolean isEmailValid(String etEmail){
        final boolean temp;
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = etEmail;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    //fungsi yg di pakai untuk merubah tampilan header jadi gradient
    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(R.drawable.gradient);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
//            window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }
}
