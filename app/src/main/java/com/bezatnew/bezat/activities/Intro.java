package com.bezatnew.bezat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.bezatnew.bezat.MyApplication;
import com.bezatnew.bezat.R;
import com.bezatnew.bezat.adapter.SliderAdapter;
import com.bezatnew.bezat.utils.URLS;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.tabs.TabLayout;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Intro extends AppCompatActivity implements View.OnClickListener {
    ImageView imgNext;
    ViewPager viewPager;
    TabLayout indicator;
    JSONArray jsonArray;
    Button btnDone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        imgNext=findViewById(R.id.imgNext);
        btnDone=findViewById(R.id.btnDone);

        btnDone.setOnClickListener(this);
        imgNext.setOnClickListener(this);
        initViewPager();
    }
    private void initViewPager() {

        viewPager = findViewById(R.id.viewPager);
        indicator = findViewById(R.id.indicator);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                if (position == 3) {
                    btnDone.setText(R.string.done);
                } else {
                    btnDone.setText(R.string.skip_login);
                }
                // Check if this is the page you want.
            }
        });

        getUserBanner();
    }
    private void getUserBanner() {

        JSONObject object = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET,
                        URLS.Companion.getOUTER_URL(),
                        object,
                        response -> {
                            try {
                                jsonArray=response.getJSONArray("result");
                                viewPager.setAdapter(new SliderAdapter(this, jsonArray));
                                indicator.setupWithViewPager(viewPager, false);

//                                Timer timer = new Timer();
//                                timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);
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
           runOnUiThread(new Runnable() {
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
    public void onClick(View view) {
        if (view.getId()==R.id.btnDone)
        {
            startActivity(new Intent(Intro.this,ChooseLanguage.class));

        }
    }
}
