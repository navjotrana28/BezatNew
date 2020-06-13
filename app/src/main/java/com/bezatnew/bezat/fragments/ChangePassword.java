package com.bezatnew.bezat.fragments;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.android.volley.*;
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
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChangePassword.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChangePassword#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangePassword extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ViewGroup container_view;

    private OnFragmentInteractionListener mListener;
    View rootView;
    EditText etConfirmPassword,etPassword,etPhone;
    TextView etCode;
    Button btnSave;
    ImageView imgBack;
    PostAdapter postAdapter;
    Loader loader;
    String lang="";
    public ChangePassword() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChangePassword.
     */
    // TODO: Rename and change types and number of parameters
    public static ChangePassword newInstance(String param1, String param2) {
        ChangePassword fragment = new ChangePassword();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (SharedPrefs.getKey(getActivity(),"selectedlanguage").contains("ar")) {
            getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            lang="_ar";
        } else {
            getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            lang="";
        }
        container_view = container;
        container.setClickable(true);
        rootView=inflater.inflate(R.layout.fragment_change_password, container, false);
        etCode=rootView.findViewById(R.id.etCode);
        etPhone=rootView.findViewById(R.id.etPhone);
        etPassword=rootView.findViewById(R.id.etPassword);
        etConfirmPassword=rootView.findViewById(R.id.etConfirmPassword);
        btnSave=rootView.findViewById(R.id.btnSave);
        imgBack = rootView.findViewById(R.id.imgBack);
        if(lang.equals("_ar")){
            imgBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back_rtl));
        }
        etCode.setOnClickListener(this);
        etCode.setText(SharedPrefs.getKey(getActivity(),"phone_code"));
        etPhone.setText(SharedPrefs.getKey(getActivity(),"phone"));
        loader=new Loader(getContext());
        loader.show();
        getCountryList();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
                container.setClickable(false);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etCode.getText().toString().isEmpty())
                {
                    etCode.setError("please fill code");
                }
                else if (etPhone.getText().toString().isEmpty())
                {
                    etPhone.setError("Please fill the phone");
                }
                else if (etPassword.getText().toString().isEmpty())
                {
                    etPhone.setError("Please fill the password");
                }
                else if(etConfirmPassword.getText().toString().isEmpty())
                {
                    etConfirmPassword.setError("please fill confirm password");
                }
                else if (!etConfirmPassword.getText().toString().equals(etPassword.getText().toString()))
                {
                    etConfirmPassword.setError("password and confirm password doesn't match");
                }
                else {
                    String phone=etCode.getText().toString()+etPhone.getText().toString();
                    String password=etPassword.getText().toString();
                    changePassword(phone,password);
                }
            }
        });
        return rootView;
    }

    private void changePassword(String phone, String password) {
        Loader loader=new Loader(getContext());
        loader.show();
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URLS.Companion.getCHANGE_PASSWORD(), new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                loader.dismiss();
                String res = new String(response.data);
                Log.v("changepassword",res);
                try {
                    JSONObject jsonObject=new JSONObject(res);
                    Toast.makeText(getActivity(),jsonObject.getString("success_msg"),Toast.LENGTH_LONG).show();
                    getActivity().onBackPressed();
//                    {"status":"success","success_msg":"Password changed successfully"}
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
                params.put("phone", phone);
                params.put("password",password);

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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.etCode)
        {

            showDialog();
        }
    }

    private void getCountryList() {

        JSONObject object = new JSONObject();
        String Url= URLS.Companion.getGET_COUNTRY();

        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET,
                        Url ,
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
         dialog=new Dialog(getActivity());
        dialog.setContentView(R.layout.country_dialog);
        dialog.show();
        RecyclerView recCountry=dialog.findViewById(R.id.recCountry);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.country_item, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            try {


                holder.txtCountry.setText(jsonArray.getJSONObject(position).getString("name"));
                holder.txtCode.setText(jsonArray.getJSONObject(position).getString("phone_code"));
                holder.txtCountryCode.setText("("+jsonArray.getJSONObject(position).getString("country_code")+")");
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

            TextView txtCountry,txtCode,txtCountryCode;
            ImageView imgFlag;


            public MyViewHolder(View itemView) {
                super(itemView);
                imgFlag=itemView.findViewById(R.id.imgFlag);

                txtCountry=itemView.findViewById(R.id.txtCountry);
                txtCode=itemView.findViewById(R.id.txtCode);
                txtCountryCode=itemView.findViewById(R.id.txtCountryCode);



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
}
