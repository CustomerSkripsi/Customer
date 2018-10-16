package mobi.garden.bottomnavigationtest.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.support.v7.widget.Toolbar;
import android.view.View;

import mobi.garden.bottomnavigationtest.R;

public class AlamatProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_alamat);

        Toolbar toolbar = findViewById(R.id.tbDaftarAlamat);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Daftar Alamat");
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addtoolbar, menu);
        return true;
    }
}
