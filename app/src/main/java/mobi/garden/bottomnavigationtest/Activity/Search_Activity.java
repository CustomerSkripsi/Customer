package mobi.garden.bottomnavigationtest.Activity;


import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import mobi.garden.bottomnavigationtest.Fragment.fragment_apotek;
import mobi.garden.bottomnavigationtest.Fragment.fragment_obat;

import android.text.TextWatcher;
import android.text.Editable;

import mobi.garden.bottomnavigationtest.R;

import android.widget.EditText;

import java.util.List;
import java.util.ArrayList;

;


public class Search_Activity extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabLayout;
    ViewPagerAdapter myPagerAdapter;
    EditText searchText;
    List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);



        viewPager = (ViewPager) findViewById(R.id.pager);
        myPagerAdapter = new ViewPagerAdapter (getSupportFragmentManager(),fragmentList);
        myPagerAdapter.addFrag(new fragment_apotek(),"Apotek");
        myPagerAdapter.addFrag(new fragment_obat(),"Obat");
        viewPager.setAdapter(myPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        searchText = findViewById(R.id.inputSearch);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                myPagerAdapter.replaceFrag(new fragment_apotek(searchText.getText().toString()),"Apotek",0);
                myPagerAdapter.replaceFrag(new fragment_obat(searchText.getText().toString()),"Obat",1);
                viewPager.setAdapter(myPagerAdapter);
                tabLayout.setupWithViewPager(viewPager);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        setStatusBarGradiant(this);

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


    }


