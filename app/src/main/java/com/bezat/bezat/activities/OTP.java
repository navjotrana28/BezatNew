package com.bezat.bezat.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bezat.bezat.MyApplication;
import com.bezat.bezat.R;
import com.bezat.bezat.utils.Loader;
import com.bezat.bezat.utils.SharedPrefs;
import com.bezat.bezat.utils.URLS;
import com.bezat.bezat.utils.VolleyMultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class OTP extends AppCompatActivity implements View.OnClickListener {
        EditText etOTP;
        Button btnSave;
        String otp,deviceId,dob,email,gender,mobileCode,
                password,phone;
        Context context=OTP.this;
        TextView txtResend;
        String forgot="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedPrefs.getKey(this,"selectedlanguage").contains("ar")) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            setLocale("ar");
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        setContentView(R.layout.activity_otp);
        etOTP=findViewById(R.id.etOTP);
        btnSave=findViewById(R.id.btnSave);
        txtResend=findViewById(R.id.txtResend);
        btnSave.setOnClickListener(this);
        txtResend.setOnClickListener(this);

        if (getIntent().getStringExtra("Forgot")!=null)
        {
            forgot=getIntent().getStringExtra("Forgot");
        }

        otp = getIntent().getExtras().get("otp").toString();
        deviceId=getIntent().getStringExtra("deviceId");
        dob=getIntent().getStringExtra("dob");
        email=getIntent().getStringExtra("email");
        gender=getIntent().getStringExtra("gender");
        mobileCode=getIntent().getStringExtra("mobileCode");
        password=getIntent().getStringExtra("password");
        phone=getIntent().getStringExtra("phone");

        Log.v("otp details",otp+" "+deviceId+" "+
                dob+" "+email+" "+gender+" "+mobileCode+" "+
                password+" "+phone+" ");
    }

    @Override
    public void onClick(View view) {
        if (otp != null && otp.equals(etOTP.getText().toString()) && forgot.equals("")) {
            finish();
            return;
        }
        if (view.getId()==R.id.btnSave)
        {
         if (etOTP.getText().toString().equals(""))
         {
             etOTP.setError("Enter OTP");
         }
         else {
                 otpVerification();
         }
        }
        else if (view.getId()==R.id.txtResend)
        {
            resendOTP();
        }
    }

    private void resendOTP() {


        Loader loader=new Loader(context);
        loader.show();

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URLS.Companion.getRESEND_OTP(), new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                loader.dismiss();
                String res = new String(response.data);
                try {
                    JSONObject jsonObject=new JSONObject(res);
                    if (jsonObject.getString("status").equalsIgnoreCase("success"))
                    {
                        Toast.makeText(OTP.this,
                                jsonObject.getString("success_msg"),Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(OTP.this,
                                jsonObject.getString("error_msg"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loader.dismiss();
                String json = null;
                String Message;
                NetworkResponse response = error.networkResponse;
                Log.v("response",response.data+"");
            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("phone",phone);
                System.out.println("object"+params+" ");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("apikey", "12345678");
                return headers;
            }

            @Override
            protected Map<String, VolleyMultipartRequest.DataPart> getByteData() throws AuthFailureError {
                Map<String, VolleyMultipartRequest.DataPart> params = new HashMap<>();
                return params;
            }
        };
        MyApplication.getInstance().addToRequestQueue(volleyMultipartRequest);
    }

    private void otpVerification() {
        Loader loader=new Loader(context);
        loader.show();

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URLS.Companion.getOTP_VALIDATION(), new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                loader.dismiss();
                String res = new String(response.data);
                try {
                    JSONObject jsonObject=new JSONObject(res);
                    if (jsonObject.getString("status").equalsIgnoreCase("success"))
                    {
                        if (forgot.equals("Forgot"))
                        {
                            Intent intent =new Intent(OTP.this,ChangePassword.class);
                            intent.putExtra("code",mobileCode);
                            intent.putExtra("phone",phone);
                            startActivity(intent);

                        }
                        else {
                            finish();
                        }
                        Toast.makeText(OTP.this,
                                jsonObject.getString("success_msg"),Toast.LENGTH_LONG).show();

                    }
                    else {
                        Toast.makeText(OTP.this,
                                jsonObject.getString("error_msg"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loader.dismiss();
                String json = null;
                String Message;
                NetworkResponse response = error.networkResponse;
                Log.v("response",response.data+"");
            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("phone",phone);
                params.put("otp_code",etOTP.getText().toString());
                System.out.println("object"+params+" ");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("apikey", "12345678");
                return headers;
            }

            @Override
            protected Map<String, VolleyMultipartRequest.DataPart> getByteData() throws AuthFailureError {
                Map<String, VolleyMultipartRequest.DataPart> params = new HashMap<>();
                return params;
            }
        };
        MyApplication.getInstance().addToRequestQueue(volleyMultipartRequest);
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
