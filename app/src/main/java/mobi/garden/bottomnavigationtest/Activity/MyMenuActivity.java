package mobi.garden.bottomnavigationtest.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import mobi.garden.bottomnavigationtest.BaseActivity;
import mobi.garden.bottomnavigationtest.LoginRegister.DialogProgress;
import mobi.garden.bottomnavigationtest.LoginRegister.Login;
import mobi.garden.bottomnavigationtest.LoginRegister.Register;
import mobi.garden.bottomnavigationtest.LoginRegister.UserLocalStore;
import mobi.garden.bottomnavigationtest.R;

public class MyMenuActivity extends BaseActivity implements View.OnClickListener{

    Button BLogin,BRegister,btnLogout;
    LinearLayout containerProfil,myProfil,agreement,setting,containerLoginRegister;

    DialogProgress dialogProgress;
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userLocalStore = new UserLocalStore(this);

        BLogin = (Button) findViewById(R.id.BLogin);
        BRegister = (Button) findViewById(R.id.BRegister);
        containerProfil = (LinearLayout) findViewById(R.id.containerProfil);
        myProfil = (LinearLayout) findViewById(R.id.myProfil);
        agreement = (LinearLayout) findViewById(R.id.agreement);
        setting = (LinearLayout) findViewById(R.id.setting);
        containerLoginRegister = (LinearLayout) findViewById(R.id.containerLoginRegister);
        btnLogout = (Button) findViewById(R.id.btnLogout);

        dialogProgress = new DialogProgress(MyMenuActivity.this);

        Toolbar toolbar = findViewById(R.id.toolbar_info);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        if(userLocalStore.getUserLoggedIn()){
            //set visibility container gone
            containerLoginRegister.setVisibility(containerLoginRegister.GONE);
        }
        else {
            containerProfil.setVisibility(containerProfil.GONE);
        }

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLocalStore.clearUserData();
                dialogProgress.hideDialog();
                startActivity(new Intent(MyMenuActivity.this, Login.class));
                finish();
            }
        });

        myProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyMenuActivity.this, ProfileCustomerActivity.class );
                startActivity(i);
            }
        });


        BLogin.setOnClickListener(this);
        BRegister.setOnClickListener(this);
    }

    @Override
    public  int getContentViewId() {
        return R.layout.activity_mymenu;
    }

    @Override
    public  int getNavigationMenuItemId() {
        return R.id.navigation_notifications2;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.BRegister:
                startActivity(new Intent(MyMenuActivity.this, Register.class));
                break;
            case R.id.BLogin:
                startActivity(new Intent(MyMenuActivity.this, Login.class));
                break;
        }
    }

    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(R.drawable.gradient);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setBackgroundDrawable(background);
        }
    }
}
