package com.bezatnew.bezat.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bezatnew.bezat.MyApplication;
import com.bezatnew.bezat.R;
import com.bezatnew.bezat.utils.Loader;
import com.bezatnew.bezat.utils.SharedPrefs;
import com.bezatnew.bezat.utils.URLS;
import com.bezatnew.bezat.utils.VolleyMultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePassword extends AppCompatActivity {
    String code, phone;
    TextView etCode;
    EditText etPhone, etPassword, etConfirmPassword;
    Button btnSave;
    ImageView imgBack;
    Loader loader;
    String lang = "";
    Context context = ChangePassword.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedPrefs.getKey(ChangePassword.this, "selectedlanguage").contains("ar")) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            lang = "_ar";

        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            lang = "";
        }
        setContentView(R.layout.activity_change_password);
        phone = getIntent().getStringExtra("phone");
        code = getIntent().getStringExtra("code");

        etCode = findViewById(R.id.etCode);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etPhone.setText(phone);
        etCode.setText(code);

        btnSave = findViewById(R.id.btnSave);
        imgBack = findViewById(R.id.imgBack);

        loader = new Loader(context);
        loader.dismiss();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etCode.getText().toString().isEmpty()) {
                    etCode.setError(getString(R.string.please_enter_code));
                } else if (etPhone.getText().toString().isEmpty()) {
                    etPhone.setError(getString(R.string.please_enter_phone_number));
                } else if (etPassword.getText().toString().isEmpty()) {
                    etPhone.setError(getString(R.string.please_enter_password));
                } else if (etConfirmPassword.getText().toString().isEmpty()) {
                    etConfirmPassword.setError(getString(R.string.please_enter_confirm_password));
                } else if (!etConfirmPassword.getText().toString().equals(etPassword.getText().toString())) {
                    etConfirmPassword.setError(getString(R.string.passwords_dont_match));
                } else {
                    String phone = etCode.getText().toString() + etPhone.getText().toString();
                    String password = etPassword.getText().toString();
                    changePassword(phone, password);
                }
            }
        });
    }

    private void changePassword(String phone, String password) {

        loader.show();
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URLS.Companion.getCHANGE_PASSWORD(), new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                loader.dismiss();
                String res = new String(response.data);
                Log.v("changepassword", res);
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    Toast.makeText(context, jsonObject.getString("success_msg"), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ChangePassword.this, LoginActivity.class));
                    finishAffinity();
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
                params.put("password", password);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
