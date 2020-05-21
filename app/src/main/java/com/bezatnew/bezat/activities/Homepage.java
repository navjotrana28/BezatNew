package com.bezatnew.bezat.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.bezatnew.bezat.R;
import com.bezatnew.bezat.fragments.BlankFragment;
import com.bezatnew.bezat.fragments.Dashboard;
import com.bezatnew.bezat.fragments.MyProfile;
import com.bezatnew.bezat.fragments.Notification;
import com.bezatnew.bezat.fragments.Settings;
import com.bezatnew.bezat.utils.SharedPrefs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Locale;

public class Homepage extends AppCompatActivity {

    Fragment currentFragment = null;
    FragmentTransaction ft;
    ViewPagerAdapter adapter;
    ViewPager viewPager;
    FrameLayout frameLayout;
    MenuItem prevMenuItem;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            /*switch (item.getItemId()) {
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
            }*/
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    if(lang.equals("a")){
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.container, new BlankFragment());
                        ft.commit();
                        viewPager.setCurrentItem(3);
                    }else{
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.container, new BlankFragment());
                        ft.commit();
                        viewPager.setCurrentItem(0);
                    }
                    break;
                case R.id.navigation_bell:
                    if(lang.equals("a")){
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.container, new BlankFragment());
                        ft.commit();
                        viewPager.setCurrentItem(2);
                    }else{
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.container, new BlankFragment());
                        ft.commit();
                        viewPager.setCurrentItem(1);
                    }
                    break;
                case R.id.navigation_profile:
                    if(lang.equals("a")){
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.container, new BlankFragment());
                        ft.commit();
                        viewPager.setCurrentItem(1);
                    }else{
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.container, new BlankFragment());
                        ft.commit();
                        viewPager.setCurrentItem(2);
                    }
                    break;
                case R.id.navigation_settings:
                    if(lang.equals("a")){
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.container, new BlankFragment());
                        ft.commit();
                        viewPager.setCurrentItem(0);
                    }else{
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.container, new BlankFragment());
                        ft.commit();
                        viewPager.setCurrentItem(3);
                    }
                    break;
            }
            return false;
        }
    };

    String lang;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        frameLayout.setClickable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (SharedPrefs.getKey(this, "selectedlanguage").contains("ar")) {
            lang = "a";
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            setLocale("ar");
        } else {
            lang = "e";
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel
                    ("MyNotifications", "MyNotifications", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = ("Successful");
                        if (!task.isSuccessful()) {
                            msg = ("Failed");
                        }
                        Log.d("FirebaseMessaging", msg);
                    }
                });


// ----------------------------------------------------------
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, new BlankFragment());
        ft.commit();

        BottomNavigationView navView = findViewById(R.id.navigation);
        frameLayout = findViewById(R.id.container);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        viewPager = findViewById(R.id.viewPagerhome);
        if(lang.equals("a")){
            addTabsArabic(viewPager);
        }else{
            addTabsEnglish(viewPager);
        }

        frameLayout.setClickable(false);

        setPageChangeListener(navView);

    }

    private void setPageChangeListener(BottomNavigationView navView) {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null)
                    prevMenuItem.setChecked(false);
                else
                    navView.getMenu().getItem(0).setChecked(false);

                if(lang.equals("a")){
                    navView.getMenu().getItem(3-position).setChecked(true);
                }else{
                    navView.getMenu().getItem(position).setChecked(true);
                }
                prevMenuItem = navView.getMenu().getItem(position);
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container, new BlankFragment());
                ft.commit();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void addTabsEnglish(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new Dashboard(), " ");
        adapter.addFrag(new Notification(), " ");
        adapter.addFrag(new MyProfile(), " ");
        adapter.addFrag(new Settings(), " ");
        viewPager.setAdapter(adapter);
    }

    private void addTabsArabic(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new Settings(), " ");
        adapter.addFrag(new MyProfile(), " ");
        adapter.addFrag(new Notification(), " ");
        adapter.addFrag(new Dashboard(), " ");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(3);
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
