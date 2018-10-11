package mobi.garden.bottomnavigationtest.Model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import mobi.garden.bottomnavigationtest.Activity.HomeActivity;

/**
 * Created by ASUS on 5/8/2018.
 */

public class session_obat {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    int private_mode = 0;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String ID = "idKey";
    public static final String NAMA = "namaKey";
    public static final String PHOTO = "photoKey";
    public static final String DESC = "descKey";
    public static final String INDIKASI = "indikasiKey";
    public static final String KANDUNGAN = "kandunganKey";
    public static final String DOSIS = "dosisKey";
    public static final String CARAPAKAI = "carapakaiKey";
    public static final String KEMASAN = "kemasanKey";
    public static final String GOLONGAN = "golonganKey";
    public static final String RESEPYN = "resepYNKey";
    public static final String KONTRAINDIKASI = "kontraindikasiKey";
    public static final String CARASIMPAN = "carasimpanKey";
    public static final String PRINCIPAL = "pricipalKey";
    public static final String CATEGORYID = "categoryIDkey";
    public static final String IS_LOGIN = "IsLoggedIn";

    public session_obat(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(MyPREFERENCES, private_mode);
        editor = sharedPreferences.edit();
    }

    public void createLoginSession(String id, String nama, String photo,String desc,String indikasi,String kandungan,String dosis,String carapakai,String kemasan,String golongan,String resepYN,String kontraindikasi,String carasimpan,String principal,String categoryID){

        editor.putBoolean(IS_LOGIN,true);
        editor.putString(ID, id);
        editor.putString(NAMA, nama);
        editor.putString(PHOTO,photo);
        editor.putString(DESC,desc);
        editor.putString(INDIKASI,indikasi);
        editor.putString(KANDUNGAN,kandungan);
        editor.putString(DOSIS,dosis);
        editor.putString(CARAPAKAI,carapakai);
        editor.putString(KEMASAN,kemasan);
        editor.putString(GOLONGAN,golongan);
        editor.putString(RESEPYN,resepYN);
        editor.putString(KONTRAINDIKASI,kontraindikasi);
        editor.putString(CARASIMPAN,carasimpan);
        editor.putString(PRINCIPAL,principal);
        editor.putString(CATEGORYID,categoryID);

        editor.commit();
    }

    public void checkLogin(){
        if(!this.isLoggedIn()){
            Intent i = new Intent(context, HomeActivity.class);

            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(i);

        }
    }
    public boolean isLoggedIn(){
        return sharedPreferences.getBoolean(IS_LOGIN, false);
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String,String> userDetail = new HashMap<String, String>();
        userDetail.put(ID, sharedPreferences.getString(ID,null));
        userDetail.put(NAMA, sharedPreferences.getString(NAMA,null));
        userDetail.put(PHOTO, sharedPreferences.getString(PHOTO,null));
        userDetail.put(DESC, sharedPreferences.getString(DESC,null));
        userDetail.put(INDIKASI, sharedPreferences.getString(INDIKASI,null));
        userDetail.put(KANDUNGAN, sharedPreferences.getString(KANDUNGAN,null));
        userDetail.put(DOSIS, sharedPreferences.getString(DOSIS,null));
        userDetail.put(CARAPAKAI, sharedPreferences.getString(CARAPAKAI,null));
        userDetail.put(KEMASAN, sharedPreferences.getString(KEMASAN,null));
        userDetail.put(GOLONGAN, sharedPreferences.getString(GOLONGAN,null));
        userDetail.put(RESEPYN, sharedPreferences.getString(RESEPYN,null));
        userDetail.put(KONTRAINDIKASI, sharedPreferences.getString(KONTRAINDIKASI,null));
        userDetail.put(CARASIMPAN, sharedPreferences.getString(CARASIMPAN,null));
        userDetail.put(PRINCIPAL, sharedPreferences.getString(PRINCIPAL,null));
        userDetail.put(CATEGORYID, sharedPreferences.getString(CATEGORYID,null));
        return userDetail;
    }

    public void logOutUser(){
        editor.clear();
        editor.commit();
    }
}
