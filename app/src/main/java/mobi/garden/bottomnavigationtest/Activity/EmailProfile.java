package mobi.garden.bottomnavigationtest.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import mobi.garden.bottomnavigationtest.R;

public class EmailProfile extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_email_profile);

        Toolbar mToolbar = findViewById(R.id.tbUpdateEmail);
        setSupportActionBar(mToolbar);
        setTitle("Edit Email");
        mToolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
