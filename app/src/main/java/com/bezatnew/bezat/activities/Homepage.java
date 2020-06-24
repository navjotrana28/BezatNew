package com.bezatnew.bezat.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
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
    Context context;
    ViewPagerAdapter adapter;
    ViewPager viewPager;
    FrameLayout frameLayout;
    MenuItem prevMenuItem;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    if (lang.equals("a")) {
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.container, new BlankFragment());
                        ft.commit();
                        viewPager.setCurrentItem(3);
                    } else {
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.container, new BlankFragment());
                        ft.commit();
                        viewPager.setCurrentItem(0);
                    }
                    frameLayout.setClickable(false);
                    break;
                case R.id.navigation_bell:
                    if (!SharedPrefs.isGuestUser(getApplicationContext())) {

                        if (lang.equals("a")) {
                            ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.container, new BlankFragment());
                            ft.commit();
                            viewPager.setCurrentItem(2);
                        } else {
                            ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.container, new BlankFragment());
                            ft.commit();
                            viewPager.setCurrentItem(1);
                        }
                        frameLayout.setClickable(false);
                    } else {
                        //toast msg
                        toastMsgHere();
                    }
                    break;
                case R.id.navigation_profile:
                    if (!SharedPrefs.isGuestUser(getApplicationContext())) {

                        if (lang.equals("a")) {
                            ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.container, new BlankFragment());
                            ft.commit();
                            viewPager.setCurrentItem(1);
                        } else {
                            ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.container, new BlankFragment());
                            ft.commit();
                            viewPager.setCurrentItem(2);
                        }
                        frameLayout.setClickable(false);
                    } else {
                        //toast msg here
                        toastMsgHere();
                    }
                    break;
                case R.id.navigation_settings:
                    if (lang.equals("a")) {
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.container, new BlankFragment());
                        ft.commit();
                        viewPager.setCurrentItem(0);
                    } else {
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.container, new BlankFragment());
                        ft.commit();
                        viewPager.setCurrentItem(3);
                    }
                    frameLayout.setClickable(false);
                    break;
            }
            return false;
        }
    };

    private void toastMsgHere() {
        Toast.makeText(getApplicationContext(), getString(R.string.sign_in_to_access_this_action),
                Toast.LENGTH_LONG).show();
    }

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
            setLocale("en");
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel
                    ("MyNotifications", "MyNotifications", NotificationManager.IMPORTANCE_HIGH);

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
        viewPager.setOffscreenPageLimit(0);

        if (SharedPrefs.isGuestUser(getApplicationContext())) {
            if (lang.equals("a")) {
                addTabsArabicGuest(viewPager);
            } else {
                addTabsEnglishGuest(viewPager);
            }
        } else {
            if (lang.equals("a")) {
                addTabsArabic(viewPager);
            } else {
                addTabsEnglish(viewPager);
            }
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

                if (SharedPrefs.isGuestUser(getApplicationContext())) {
                    if (prevMenuItem != null)
                        prevMenuItem.setChecked(false);
                    else
                        navView.getMenu().getItem(0).setChecked(false);

                    if (lang.equals("a")) {
                        if (position == 0) {
                            navView.getMenu().getItem(3).setChecked(true);
                        } else {
                            navView.getMenu().getItem(0).setChecked(true);
                        }
                    } else {
                        if (position == 0) {
                            navView.getMenu().getItem(0).setChecked(true);
                        } else {
                            navView.getMenu().getItem(3).setChecked(true);
                        }

                    }
                    if (position == 0) {
                        prevMenuItem = navView.getMenu().getItem(0);
                    } else {
                        prevMenuItem = navView.getMenu().getItem(3);
                    }

                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.container, new BlankFragment());
                    ft.commit();
                    frameLayout.setClickable(false);
                } else {
                    if (prevMenuItem != null)
                        prevMenuItem.setChecked(false);
                    else
                        navView.getMenu().getItem(0).setChecked(false);

                    if (lang.equals("a")) {
                        navView.getMenu().getItem(3 - position).setChecked(true);
                    } else {
                        navView.getMenu().getItem(position).setChecked(true);
                    }
                    prevMenuItem = navView.getMenu().getItem(position);
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.container, new BlankFragment());
                    ft.commit();
                    frameLayout.setClickable(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void addTabsEnglishGuest(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new Dashboard(), " ");
        adapter.addFrag(new Settings(), " ");
        viewPager.setAdapter(adapter);
    }

    private void addTabsEnglish(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new Dashboard(), " ");
        adapter.addFrag(new Notification(), " ");
        adapter.addFrag(new MyProfile(), " ");
        adapter.addFrag(new Settings(), " ");
        viewPager.setAdapter(adapter);
    }

    private void addTabsArabicGuest(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new Settings(), " ");
        adapter.addFrag(new Dashboard(), " ");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(3);
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
