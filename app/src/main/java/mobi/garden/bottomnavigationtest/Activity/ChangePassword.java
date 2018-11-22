package mobi.garden.bottomnavigationtest.Activity;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mobi.garden.bottomnavigationtest.LoginRegister.User;
import mobi.garden.bottomnavigationtest.LoginRegister.UserLocalStore;
import mobi.garden.bottomnavigationtest.R;

public class ChangePassword {

    String tempID, tempUsername ,tempPassword, customerID;
    TextInputLayout til_PassBaru, til_PassLama, til_PassBaruKonfirm;
    TextInputEditText etPassBaru, etPassLama, etKonfirmPassBaru;
    TextView  TvOKE, TvBatal; 

    private static final String SALT_LOGIN = "Pharmanet";

    private static UserLocalStore userLocal;
    static User currUser;
    Context context;

    public ChangePassword( Context context){
        this.context = context;
    }


    public void changePass(){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_change_password);

        userLocal = new UserLocalStore(context);
        currUser = userLocal.getLoggedInUser();
        customerID = String.valueOf(currUser.getUserID());


        etPassLama = dialog.findViewById(R.id.et_pass_lama);
        etPassBaru = dialog.findViewById(R.id.et_pass_baru);
        etKonfirmPassBaru = dialog.findViewById(R.id.et_pass_baru_confirm);
        TvOKE = dialog.findViewById(R.id.tvOk);
        TvBatal = dialog.findViewById(R.id.tvBatal);

        getPassword(currUser.getUserID());

        TvOKE.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                //belom password SHA
                if(etPassLama.getText().toString().equals(tempPassword) && etPassBaru.getText().toString().equals(etKonfirmPassBaru.getText().toString()) && stringContainsNumber(etPassBaru.getText().toString()) && !etPassBaru.getText().toString().equals(etPassLama.getText().toString())) {
                    updatePassword(tempUsername, etPassBaru.getText().toString(), dialog);
                }else if(!etPassLama.getText().toString().equals(tempPassword)){
                    etPassLama.setError("Password lama tidak cocok");
                }else if( !etPassBaru.getText().toString().equals(etKonfirmPassBaru.getText().toString())){
                    etPassBaru.setError("Password baru tidak cocok");
                    etKonfirmPassBaru.setError("Password baru tidak cocok");
                }else if(!stringContainsNumber(etPassBaru.getText().toString())){
                    etPassBaru.setError("harus mengandung minimal 1 angka");
                }
            }
        });

        TvBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    public void getPassword(String customerID)
    {
        JSONObject objAdd = new JSONObject();
        try {
            JSONArray arrData = new JSONArray();
            JSONObject objDetail = new JSONObject();
            objDetail.put("CustomerID", customerID);
            arrData.put(objDetail);
            objAdd.put("data", arrData);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        Log.d("objaddd", objAdd.toString());
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, "http://pharmanet.apodoc.id/getCustomerPassword.php", objAdd,
                new Response.Listener<JSONObject>() {
                    @Override

                    public void onResponse(JSONObject response) {
                        try {
                            tempPassword = response.getString("Password");
                            tempUsername = response.getString("Username");
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void updatePassword(String username,String password,final Dialog dialog){

        Log.d("username-----", username);
        JSONObject objadd= new JSONObject();

        try {
            JSONArray arrdata=new JSONArray();
            JSONObject objdetail=new JSONObject();
            objdetail.put("UserName",username);
            objdetail.put("Password",password);
            arrdata.put(objdetail);
            objadd.put("data",arrdata);
        }catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest stringRequest= new JsonObjectRequest(Request.Method.POST, "http://pharmanet.apodoc.id/updateCustomerPassword.php",
                objadd, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("OK")) {
                        Toast.makeText(context, "Password berhasil diganti", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                    else{
                        Toast.makeText(context, "update password gagal", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "update password gagal", Toast.LENGTH_LONG).show();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
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

    public boolean stringContainsNumber( String s )
    {
        Pattern p = Pattern.compile( "[0-9]" );
        Matcher m = p.matcher( s );

        return m.find();
    }
}
