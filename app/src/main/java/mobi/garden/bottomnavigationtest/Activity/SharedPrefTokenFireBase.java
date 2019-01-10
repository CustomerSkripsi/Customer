package mobi.garden.bottomnavigationtest.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPrefTokenFireBase {

    private Context context;
    private String SHARED_PREF_NAME = "FCMSharedPref";
    private String TAG_TOKEN = "tagtoken";
    private String TAG_ADDED = "tagAdded";
    private SharedPreferences.Editor editor;

    public SharedPrefTokenFireBase(Context context ) {
        this.context = context;
    }

    public static SharedPrefTokenFireBase sharedPrefTokenFireBase = null;
    public static SharedPrefTokenFireBase getInstance(Context context){
        if (sharedPrefTokenFireBase == null) {
            sharedPrefTokenFireBase = new SharedPrefTokenFireBase(context);
        }
        return sharedPrefTokenFireBase;
    }

    public static void deleteInstance(){
        sharedPrefTokenFireBase = null;
    }

    public void saveAdd(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean(TAG_ADDED, true);
    }

    public Boolean saveDeviceToken(String token){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(TAG_TOKEN, token);
        editor.putBoolean(TAG_ADDED, true);
        editor.commit();
        Log.d("savedevice", getDeviceToken()+"");
        Log.d("sitoken", token);
        return true;
    }

    public String getDeviceToken(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(TAG_TOKEN,"aaa");
        return token;
    }

    public Boolean getAddedStatus(){
        return context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).getBoolean(TAG_ADDED,false);
    }

    public void clearToken(){
        editor.clear();
        editor.commit();
    }


}
