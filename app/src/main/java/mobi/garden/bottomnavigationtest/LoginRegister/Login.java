package mobi.garden.bottomnavigationtest.LoginRegister;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

import mobi.garden.bottomnavigationtest.Activity.HomeActivity;
import mobi.garden.bottomnavigationtest.R;

public class Login extends AppCompatActivity implements View.OnClickListener {

    Button bLogin;
    TextView bRegisterLink;
    EditText etPassword, etUserName;

    public static final String SERVER_ADDRESS = "http://pharmanet.apodoc.id/FetchUserDataCustomer.php";

    UserLocalStore userLocalStore;
    DialogProgress dialogProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUserName = findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bLogin = (Button) findViewById(R.id.bLogin);
        bRegisterLink = (TextView) findViewById(R.id.bRegisterLink);

        bLogin.setOnClickListener(this);
        bRegisterLink.setOnClickListener(this);
        userLocalStore = new UserLocalStore(this);
        dialogProgress = new DialogProgress(Login.this);

        setStatusBarGradiant(this);

    }


    public void login(User user){
        dialogProgress.showDialog();
        JSONObject objadd = new JSONObject();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            JSONArray arrdata=new JSONArray();
            JSONObject objdetail=new JSONObject();
            objdetail.put("userName",etUserName.getText()+"");
            objdetail.put("password",etPassword.getText()+"");
            //arrdata.put(objdetail);
            objadd.put("data",objdetail);

        }catch (JSONException e){
            e.printStackTrace();
        }
        Log.d("objaddLogin",objadd+"");
        JsonObjectRequest stringRequest= new JsonObjectRequest(Request.Method.POST, SERVER_ADDRESS, objadd,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("responyn", response.toString());
                            if (response.getString("status").equals("success")) {

                                Intent i = new Intent(getApplicationContext(), HomeActivity.class);

                                userLocalStore.setUserLoggedIn(true);
                                userLocalStore.storeUserData(new User(
                                        response.getString("CustomerUsername"),
                                        response.getString("CustomerID")
                                ));

                                dialogProgress.hideDialog();
                                Toast.makeText(getApplicationContext(), "Login Berhasil", Toast.LENGTH_SHORT).show();
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                            } else {

                                dialogProgress.hideDialog();
                                Toast.makeText(getApplicationContext(), "username/password salah", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialogProgress.hideDialog();
                            Log.d("erronya",e.getMessage());
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Login.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }

        );
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bLogin:

                String userName = etUserName.getText().toString();
                String password = etPassword.getText().toString();

                User user = new User(userName,password);
                login(user);
                //authenticate(user);

                break;

            case R.id.bRegisterLink:

                startActivity(new Intent(this, Register.class));
                break;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

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
