package com.bezat.bezat.activities;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.bezat.bezat.R;
import com.bezat.bezat.fragments.Dashboard;
import com.bezat.bezat.fragments.MyProfile;
import com.bezat.bezat.fragments.Notification;
import com.bezat.bezat.fragments.Settings;
import com.bezat.bezat.utils.SharedPrefs;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

public class Homepage extends AppCompatActivity {

    Fragment currentFragment = null;
    FragmentTransaction ft;
    ViewPagerAdapter adapter;
    ViewPager viewPager;
    FrameLayout frameLayout;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    getSupportFragmentManager().popBackStack();
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.container, new Dashboard());
                    ft.commit();

//                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_bell:
                    getSupportFragmentManager().popBackStack();
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.container, new Notification());
                    ft.commit();
//                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_profile:
                    getSupportFragmentManager().popBackStack();
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.container, new MyProfile());
                    ft.commit();
//                    viewPager.setCurrentItem(2);
                    return true;

                case R.id.navigation_settings:
                    getSupportFragmentManager().popBackStack();
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.container, new Settings());
                    ft.commit();
//                    viewPager.setCurrentItem(3);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedPrefs.getKey(this, "selectedlanguage").contains("ar")) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            setLocale("ar");
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        setContentView(R.layout.activity_home);

        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, new Dashboard());
        ft.commit();

        BottomNavigationView navView = findViewById(R.id.navigation);
        frameLayout = findViewById(R.id.container);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//        viewPager = findViewById(R.id.viewPagerhome);
//        addTabs(viewPager);
        setPageChangeListener(navView);

    }

    private void setPageChangeListener(BottomNavigationView navView) {
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                navView.getMenu().getItem(position).setChecked(true);
//                if (position == 3) {
//                    findViewById(R.id.container).setVisibility(View.VISIBLE);
//                }
//                navView.getMenu().getItem(position).setChecked(true);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
    }

    private void addTabs(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new Dashboard(), " ");
        adapter.addFrag(new Notification(), " ");
        adapter.addFrag(new MyProfile(), " ");
        adapter.addFrag(new Settings(), " ");
        viewPager.setAdapter(adapter);
    }

    public void setLocale(String lang) {

        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

    }

}
