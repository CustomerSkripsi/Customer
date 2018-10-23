package mobi.garden.bottomnavigationtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import mobi.garden.bottomnavigationtest.Activity.CartActivity;
import mobi.garden.bottomnavigationtest.Activity.HistoryActivity;
import mobi.garden.bottomnavigationtest.Activity.HomeActivity;
import mobi.garden.bottomnavigationtest.Activity.KategoriActivity;
import mobi.garden.bottomnavigationtest.Activity.MyMenuActivity;


public abstract class BaseActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    //Navbar-btn
    protected BottomNavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

    }


    @Override
    protected void onStart() {
        super.onStart();
        updateNavigationBarState();
    }
    // Remove inter-activity transition to avoid screen tossing on tapping bottom navigation items
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        navigationView.postDelayed(() -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(this, HomeActivity.class));
            } else if (itemId == R.id.navigation_dashboard) {
                    startActivity(new Intent(this, KategoriActivity.class));
            } else if (itemId == R.id.navigation_notifications) {
                    startActivity(new Intent(this, HistoryActivity.class));
            } else if(itemId == R.id.navigation_notifications1){
                startActivity(new Intent(this, CartActivity.class));
            } else if(itemId == R.id.navigation_notifications2){
                startActivity(new Intent(this, MyMenuActivity.class));
            }
            finish();
        }, 50);
        return true;
    }
    private void updateNavigationBarState(){
        int actionId = getNavigationMenuItemId();
        selectBottomNavigationBarItem(actionId);
    }
    void selectBottomNavigationBarItem(int itemId) {
        MenuItem item = navigationView.getMenu().findItem(itemId);
        item.setChecked(true);
    }
    public abstract int getContentViewId();
    public abstract int getNavigationMenuItemId();

}
