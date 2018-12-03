package mobi.garden.bottomnavigationtest;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Window;
import android.view.WindowManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;

import mobi.garden.bottomnavigationtest.R;

public class CONFIG {

  //  private static String uniqueID = null;
  //  private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
    public static final String PREV_PAGE = "PREV_PAGE";
    public static final int requestCode = 11000;

    public static String ConvertNominal(int input){
        DecimalFormat df = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setCurrencySymbol("Rp. ");
        dfs.setMonetaryDecimalSeparator(',');
        dfs.setGroupingSeparator('.');
        df.setDecimalFormatSymbols(dfs);
        df.setMaximumFractionDigits(0);
        String hsl = df.format(input);
        return hsl;
    }



    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
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


//    public synchronized static String id(Context context) {
//        if (uniqueID == null) {
//            SharedPreferences sharedPrefs = context.getSharedPreferences(
//                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
//            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
//            if (uniqueID == null) {
//                uniqueID = UUID.randomUUID().toString();
//                SharedPreferences.Editor editor = sharedPrefs.edit();
//                editor.putString(PREF_UNIQUE_ID, uniqueID);
//                editor.commit();
//            }
//        }
//        return uniqueID;
//    }

}
