package mobi.garden.bottomnavigationtest.Session;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;


import java.util.HashMap;

import mobi.garden.bottomnavigationtest.Activity.HalamanAwalActivity;

public class SessionManagement {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "AndroidHivePref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    // Email address (make variable public to access from outside)
    public static final String KEY_PASSWORD = "password";

    public static final String KEY_KODEMEMBER = "member";

    public static final String KEY_NOHP = "nomor_hp";

    public static final String KEY_NAMA = "nama";

    public static final String USERNAME = "username";

    // Constructor
    public SessionManagement(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    // Constructor Cart
    public SessionManagement(String username, String member) {
        editor.putString(USERNAME, username);
        editor.putString(KEY_KODEMEMBER, member);
        editor.commit();
    }


    public void createLoginSession(String email,String password){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD,password);
        // commit changes
        editor.commit();
    }


    public void LoginSession(String nama, String member, String nomor_hp, String username){
        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_NAMA, nama);
        editor.putString(KEY_KODEMEMBER, member);
        editor.putString(KEY_NOHP, nomor_hp);
        editor.putString(USERNAME, username);

        editor.commit();
    }

    public void GantiPasswordSession(String nama, String member, String username, String password) {
        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_NAMA, nama);
        editor.putString(KEY_KODEMEMBER, member);
        editor.putString(USERNAME, username);
        editor.putString(KEY_PASSWORD, password);

        editor.commit();
    }

    public void OTPSession(String nomor_hp){
        editor.putString(KEY_NOHP, nomor_hp);
        editor.commit();
    }

    public String getUserID() {
        return KEY_KODEMEMBER;
    }

    //
    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, HalamanAwalActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        // user email id
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));

        // return user
        return user;
    }

    public HashMap<String, String> getUserPhones(){
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_NOHP, pref.getString(KEY_NOHP, null));

        // return user
        return user;
    }

    public HashMap<String, String> getMemberDetails(){
        HashMap<String, String> user = new HashMap<>();

        user.put(KEY_NAMA, pref.getString(KEY_NAMA,null));

        user.put(KEY_KODEMEMBER, pref.getString(KEY_KODEMEMBER, null));

        user.put(KEY_NOHP, pref.getString(KEY_NOHP, null));

        user.put(USERNAME, pref.getString(USERNAME, null));

        return user;
    }

    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, HalamanAwalActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    public void cleanUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
    }

    public void setUserLoggedIn(Boolean isLoggedIn){
        editor.putBoolean("loggedIn", isLoggedIn);
        editor.commit();
    }

    //cart
    public SessionManagement getLoggedInIdSession() {
        String member = pref.getString("member", "");
        String username = pref.getString("nama", "");
        Log.d("asd",member);
        SessionManagement storedUser = new SessionManagement(member,username);
        return storedUser;
    }
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public boolean getUserLoggedIn(){
        if(pref.getBoolean("IsLoggedIn",false)){
            return true;
        }
        else {
//            Intent i = new Intent(context, Login.class);
//            //reset activities
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//            //masuk login
//            context.startActivity(i);
            return false;
        }
    }

}
