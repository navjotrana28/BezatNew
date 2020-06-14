package com.bezatnew.bezat.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bezatnew.bezat.MyApplication;
import com.bezatnew.bezat.R;
import com.bezatnew.bezat.utils.Loader;
import com.bezatnew.bezat.utils.SharedPrefs;
import com.bezatnew.bezat.utils.URLS;
import com.bezatnew.bezat.utils.VolleyMultipartRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {

    TextView etCode;
    Loader loader;
    String lang = "";
    PostAdapter postAdapter;
    Context context = ForgotPassword.this;
    SmsHashCodeHelper smsHashCodeHelper = new SmsHashCodeHelper(this);
    Button btnSend;
    ImageView imgBack;
    EditText etPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedPrefs.getKey(this, "selectedlanguage").contains("ar")) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            lang = "_ar";
            setLocale("ar");
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        setContentView(R.layout.activity_forgot_password);
        etCode = findViewById(R.id.txtCode);
        btnSend = findViewById(R.id.btnSend);
        etPhone = findViewById(R.id.etPhone);
        imgBack = findViewById(R.id.imgBackForgot);
        etCode.setOnClickListener(this);
        loader = new Loader(context);
        if (lang.equals("_ar")) {
            imgBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back_rtl));
        }
        loader.show();
        // sms hash code helper :

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etPhone.getText().toString().isEmpty()) {
                    dialog();
                } else {
                    getOTP(etPhone.getText().toString());
                }
            }
        });
        getCountryList();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void dialog() {
        ForgotPasswordDialog forgotPasswordDialog = new ForgotPasswordDialog();
        forgotPasswordDialog.show(getSupportFragmentManager(), "Pass");
    }

    private void getOTP(String phone) {
        loader.show();


        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URLS.Companion.getFORGOT_PASSWORD(), new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                loader.dismiss();
                String res = new String(response.data);
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                        Intent intent = new Intent(ForgotPassword.this, OTP.class);
                        intent.putExtra("Forgot", "Forgot");
                        intent.putExtra("phone", phone);
                        intent.putExtra("mobileCode", etCode.getText().toString());
                        startActivity(intent);
                        Toast.makeText(ForgotPassword.this,
                                jsonObject.getString("success_msg"), Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(ForgotPassword.this,
                                jsonObject.getString("error_msg"), Toast.LENGTH_LONG).show();
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
                Log.v("response", response.data + "");
            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("phone", phone);
                String hashCode = String.valueOf(smsHashCodeHelper.getAppHashCode());
                params.put("smsHashCode", hashCode.substring(1, hashCode.length() - 1));
                System.out.println("object" + params + " ");
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

    private void getCountryList() {
        JSONObject object = new JSONObject();
        String Url = URLS.Companion.getGET_COUNTRY();
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET,
                        Url,
                        object,
                        response -> {
                            loader.dismiss();
                            try {
                                postAdapter = new PostAdapter(response.getJSONArray("result"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        error -> {
                            loader.dismiss();
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

    Dialog dialog;

    private void showDialog() {
        dialog = new Dialog(ForgotPassword.this);
        dialog.setContentView(R.layout.country_dialog);
        dialog.show();
        RecyclerView recCountry = dialog.findViewById(R.id.recCountry);
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(1, OrientationHelper.VERTICAL);
        layoutManager.setGapStrategy(
                StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recCountry.setLayoutManager(layoutManager);
        recCountry.setItemAnimator(new DefaultItemAnimator());
        if (postAdapter != null && postAdapter.getItemCount() > 0) {
            recCountry.setAdapter(postAdapter);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.txtCode) {
            showDialog();
        }
    }


    private class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {
        JSONArray jsonArray;

        public PostAdapter(JSONArray array) {
            this.jsonArray = array;
        }

        public void append(JSONArray array) {
            try {
                for (int i = 0; i < array.length(); i++) {
                    this.jsonArray.put(array.get(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public PostAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.country_item, parent, false);
            return new PostAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(PostAdapter.MyViewHolder holder, int position) {
            try {


                holder.txtCountry.setText(jsonArray.getJSONObject(position).getString("name"));
                holder.txtCode.setText(jsonArray.getJSONObject(position).getString("phone_code"));
                holder.txtCountryCode.setText("(" + jsonArray.getJSONObject(position).getString("country_code") + ")");
                Picasso.get().load(jsonArray.getJSONObject(position).getString("img")).into(holder.imgFlag);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            return jsonArray.length();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txtCountry, txtCode, txtCountryCode;
            ImageView imgFlag;


            public MyViewHolder(View itemView) {
                super(itemView);
                imgFlag = itemView.findViewById(R.id.imgFlag);

                txtCountry = itemView.findViewById(R.id.txtCountry);
                txtCode = itemView.findViewById(R.id.txtCode);
                txtCountryCode = itemView.findViewById(R.id.txtCountryCode);


                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        try {
                            etCode.setText(jsonArray.getJSONObject(getAdapterPosition()).getString("phone_code"));
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        }

    }

    public void setLocale(String lang) {
        SharedPrefs.setKey(ForgotPassword.this, "selectedlanguage", lang);
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

    }
}
