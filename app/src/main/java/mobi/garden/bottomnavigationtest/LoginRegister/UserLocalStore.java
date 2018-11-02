package mobi.garden.bottomnavigationtest.LoginRegister;

import android.content.Context;
import android.content.SharedPreferences;

public class UserLocalStore {public static final String SP_Name = "userDetails";
    SharedPreferences userLocalDatabase;
    Context context;
    SharedPreferences.Editor editor;

    public UserLocalStore(Context context){
        userLocalDatabase = context.getSharedPreferences(SP_Name,0);
        this.context = context;
        editor = userLocalDatabase.edit();
    }

    public void storeUserData(User user){
        editor.putString("userName", user.userName);
        editor.putString("userID", user.userID);
        //editor.putString("fullName", user.fullName);
 //       editor.putString("DOB", user.DOB);
//        editor.putString("gender", user.gender);
//        editor.putString("contact", user.contact);
//        editor.putString("email", user.email);
//        editor.putString("address", user.address);
//        editor.putString("codeReferral", user.codeReferral);
//        editor.putBoolean("loggedIn",true);


        editor.commit();
    }

    public User getLoggedInUser(){
        String id = userLocalDatabase.getString("userID", "");
        String userName = userLocalDatabase.getString("userName", "");

        User storedUser = new User(userName,id);
        return storedUser;
    }

    public void setUserLoggedIn(Boolean loggedIn){
        editor.putBoolean("loggedIn", loggedIn);
        editor.commit();
    }



    public boolean getUserLoggedIn(){
        if(userLocalDatabase.getBoolean("loggedIn",false)){
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
    public void clearUserData(){
        editor.clear();
        editor.commit();
    }
}
