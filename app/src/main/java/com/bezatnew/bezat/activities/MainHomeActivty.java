package com.bezatnew.bezat.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.*;
import androidx.viewpager.widget.ViewPager;
import com.bezatnew.bezat.MyApplication;
import com.bezatnew.bezat.R;
import com.bezatnew.bezat.adapter.SliderAdapter;
import com.bezatnew.bezat.models.DashBoardItem;
import com.bezatnew.bezat.utils.SharedPrefs;
import com.bezatnew.bezat.utils.URLS;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.tabs.TabLayout;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class MainHomeActivty extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    JSONArray jsonArray;
    List<String> iconName;
    RecyclerView recycle;
    ViewPager viewPager;
    TabLayout indicator;
    List<DashBoardItem> dashBoardItem=new ArrayList<>();
    TextView txtMyProfile,tvNotification,tvFavourite,tvContact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedPrefs.getKey(this,"selectedlanguage").contains("ar")) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        setContentView(R.layout.activity_main_home_activty);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        initNavPage();
        initViewPager();
        setDashboardData();
    }
    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }
    private void initNavPage() {
        txtMyProfile=findViewById(R.id.txtMyProfile);
        tvNotification=findViewById(R.id.tvNotification);
        tvFavourite=findViewById(R.id.tvFavourite);
        tvFavourite.setOnClickListener(this);
        tvNotification.setOnClickListener(this);
        txtMyProfile.setOnClickListener(this);
    }

    private void setDashboardData() {

        recycle=findViewById(R.id.recycle);
        dashBoardItem.add(new DashBoardItem(
                R.drawable.get_coupon,
                getString(R.string.get_coupon)+""
        ));
        dashBoardItem.add(new DashBoardItem(
                R.drawable.fav_offers,
                getString(R.string.fav_offers)+""
        ));


        dashBoardItem.add(new DashBoardItem(
                R.drawable.prizes,
                getString(R.string.prizes)+""
        ));
        dashBoardItem.add(new DashBoardItem(
                R.drawable.total_coupon,
                getString(R.string.total_coupon)+""
        ));

        dashBoardItem.add(new DashBoardItem(
                R.drawable.partners,
                getString(R.string.partners)+""
        ));
        dashBoardItem.add(new  DashBoardItem(
                R.drawable.winners,
                getString(R.string.winners)+""
        ));

        dashBoardItem.add(new DashBoardItem(
                R.drawable.my_profile,
                getString(R.string.my_profile)+""
        ));

        dashBoardItem.add(new DashBoardItem(
                R.drawable.vip_offers,
                getString(R.string.vip_offers)+""
        ));

        dashBoardItem.add(new DashBoardItem(
                R.drawable.logout,
                getString(R.string.sign_out)+""
        ));

        PostAdapter postAdapter=new PostAdapter(dashBoardItem);

        recycle.setLayoutManager(new GridLayoutManager(MainHomeActivty.this, 3));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(MainHomeActivty.this, R.dimen._1sdp);
        recycle.addItemDecoration(itemDecoration);
        recycle.setItemAnimator(new DefaultItemAnimator());
        if (postAdapter != null && postAdapter.getItemCount() > 0) {

            recycle.setAdapter(postAdapter);
        }


    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.txtMyProfile)
        {
            startActivity(new Intent(MainHomeActivty.this,MyProfile.class));
        }
        if (view.getId()==R.id.tvNotification)
        {
            startActivity(new Intent(MainHomeActivty.this,Notifications.class));
        }
        if (view.getId()==R.id.tvFavourite)
        {
            startActivity(new Intent(MainHomeActivty.this,MyFavourite.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {

        private int mItemOffset;
        public ItemOffsetDecoration(int itemOffset) {
            mItemOffset = itemOffset;
        }

        public ItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
            this(context.getResources().getDimensionPixelSize(itemOffsetId));
        }
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
        }
    }
    private void initViewPager() {

        viewPager = findViewById(R.id.viewPager);
        indicator = findViewById(R.id.indicator);

        getUserBanner();
    }

    private void getUserBanner() {

        JSONObject object = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET,
                        URLS.Companion.getUSER_BANNER(),
                        object,
                        response -> {
                            try {

                                jsonArray=response.getJSONArray("result");
                                viewPager.setAdapter(new SliderAdapter(this, jsonArray));
                                indicator.setupWithViewPager(viewPager, true);

                                Timer timer = new Timer();
                                timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        error -> {
                            Log.v("error",error.getMessage()+" ");


                        }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("apikey", "12345678");
                        return headers;
                    }
                };

        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            MainHomeActivty.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() < jsonArray.length() - 1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {
        List<DashBoardItem> dashBoardItems;

        public PostAdapter( List<DashBoardItem> dashBoardItems) {
            this.dashBoardItems = dashBoardItems;
        }



        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_dashboard_item, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            try {

               holder.text.setText(dashBoardItems.get(position).getName()+" ");
                final int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    holder.image.setBackgroundDrawable(ContextCompat.getDrawable(MainHomeActivty.this,
                            dashBoardItems.get(position).getDrawable()) );
                } else {
                    holder.image.setBackground(ContextCompat.getDrawable(MainHomeActivty.this,
                            dashBoardItems.get(position).getDrawable()));
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            return dashBoardItems.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView text;
            ImageView image;

            public MyViewHolder(View itemView) {
                super(itemView);

                text=itemView.findViewById(R.id.text);
                image=itemView.findViewById(R.id.image);


                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    if (dashBoardItems.get(getAdapterPosition())
                    .getName().equalsIgnoreCase(getString(R.string.sign_out)))
                    {
                        SharedPrefs.deleteSharedPrefs(MainHomeActivty.this);
                        startActivity(new Intent(MainHomeActivty.this,LoginActivity.class));
                        finish();
                    }
                    else if (dashBoardItems.get(getAdapterPosition())
                            .getName().equalsIgnoreCase(getString(R.string.total_coupon)))
                    {
                        startActivity(new Intent(MainHomeActivty.this,TotalCoupons.class));

                    }
                    else if (dashBoardItems.get(getAdapterPosition())
                            .getName().equalsIgnoreCase(getString(R.string.prizes)))
                    {
                        startActivity(new Intent(MainHomeActivty.this,Prizes.class));

                    }
                    else if (dashBoardItems.get(getAdapterPosition())
                            .getName().equalsIgnoreCase(getString(R.string.winners)))
                    {
                        startActivity(new Intent(MainHomeActivty.this,BezatWinner.class));

                    }
                    else if (dashBoardItems.get(getAdapterPosition())
                            .getName().equalsIgnoreCase(getString(R.string.fav_offers)))
                    {
                        startActivity(new Intent(MainHomeActivty.this,FavouriteOffer.class));

                    }
                    }
                });

            }
        }
    }


}
