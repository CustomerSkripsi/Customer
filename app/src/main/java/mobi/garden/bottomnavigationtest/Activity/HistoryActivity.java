package mobi.garden.bottomnavigationtest.Activity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import mobi.garden.bottomnavigationtest.BaseActivity;
import mobi.garden.bottomnavigationtest.Fragment.HistoryBerhasil;
import mobi.garden.bottomnavigationtest.Fragment.HistoryGagal;
import mobi.garden.bottomnavigationtest.Fragment.HistoryPending;
import mobi.garden.bottomnavigationtest.R;

public class HistoryActivity extends BaseActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    List<Fragment> fragmentList = new ArrayList<>();
    ImageView ivBack;


    @Override
    public  int getContentViewId() {
        return R.layout.activity_history;
    }

    @Override
    public int getNavigationMenuItemId() {

        return R.id.navigation_notifications;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        viewPager = findViewById(R.id.pagerHistory);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPagerAdapter.addFrag(new HistoryBerhasil(),"Berhasil");
        viewPagerAdapter.addFrag(new HistoryPending(), "Proses");
        viewPagerAdapter.addFrag(new HistoryGagal(), "Gagal");
        viewPager.setAdapter(viewPagerAdapter);




        tabLayout = findViewById(R.id.tablayoutHistory);
        tabLayout.setupWithViewPager(viewPager);
        //setIcon();

        setStatusBarGradiant(this);

    }

    public void BackBack(View view){
        super.onBackPressed();
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

    public class ViewPagerAdapter extends FragmentStatePagerAdapter
    {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private List<Fragment> fragments;

        public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return this.mFragmentList.get(position);
        }

        /* (non-Javadoc)
         * @see android.support.v4.view.PagerAdapter#getCount()
         */

        @Override
        public int getCount() {
            return this.mFragmentList.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        public void replaceFrag(Fragment fragment, String title,int position) {
            mFragmentList.set(position,fragment);
            mFragmentTitleList.set(position,title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

//    private void setIcon(){
//        tabLayout.getTabAt(0).setIcon(R.drawable.ic_check_circle_black_24dp);
//        tabLayout.getTabAt(1).setIcon(R.drawable.ic_access_time_yellow_24dp);
//        tabLayout.getTabAt(2).setIcon(R.drawable.ic_error_black_24dp);
//    }
}
