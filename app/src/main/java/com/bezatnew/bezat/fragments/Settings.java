package com.bezatnew.bezat.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.*;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentTransaction;

import com.android.volley.*;
import com.bezatnew.bezat.MyApplication;
import com.bezatnew.bezat.R;
import com.bezatnew.bezat.activities.LoginActivity;
import com.bezatnew.bezat.utils.Loader;
import com.bezatnew.bezat.utils.SharedPrefs;
import com.bezatnew.bezat.utils.URLS;
import com.bezatnew.bezat.utils.VolleyMultipartRequest;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Settings.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Settings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Settings extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Switch switches;
    TextView txtMyScan, txtChangePassword, txtAbout, txtTerms,
            txtPrivacy, txtContactUs, txtFaq, txtChangeLanguage,
            txtLogout, txtMyFav;
    Button btnSave;
    boolean isGuestUser = false;
    View rootView;
    LinearLayout layoutLanguage;
    private OnFragmentInteractionListener mListener;

    public Settings() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Settings.
     */
    // TODO: Rename and change types and number of parameters
    public static Settings newInstance(String param1, String param2) {
        Settings fragment = new Settings();
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
        if (SharedPrefs.getKey(getActivity(), "selectedlanguage").contains("ar")) {
            getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            setLocale("ar");
        } else {
            getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        rootView = inflater.inflate(R.layout.fragment_settings2, container, false);

        txtMyScan = rootView.findViewById(R.id.txtMyScan);
        txtChangePassword = rootView.findViewById(R.id.txtChangePassword);
        txtAbout = rootView.findViewById(R.id.txtAbout);
        txtTerms = rootView.findViewById(R.id.txtTerms);
        txtPrivacy = rootView.findViewById(R.id.txtPrivacy);
        txtContactUs = rootView.findViewById(R.id.txtContactUs);
        txtFaq = rootView.findViewById(R.id.txtFaq);
        switches = rootView.findViewById(R.id.switches);
        txtChangeLanguage = rootView.findViewById(R.id.txtChangeLanguage);
        layoutLanguage = rootView.findViewById(R.id.layoutLanguage);
        txtLogout = rootView.findViewById(R.id.txtLogout);
        txtMyFav = rootView.findViewById(R.id.txtMyFav);
        txtChangePassword.setOnClickListener(this);
        txtMyScan.setOnClickListener(this);
        txtAbout.setOnClickListener(this);
        txtTerms.setOnClickListener(this);
        txtPrivacy.setOnClickListener(this);
        txtContactUs.setOnClickListener(this);
        txtFaq.setOnClickListener(this);
        txtLogout.setOnClickListener(this);
        txtChangeLanguage.setOnClickListener(this);
        txtMyFav.setOnClickListener(this);
        initSignOutLabel();


        if (SharedPrefs.getKey(getActivity(), "push_notification_status").equalsIgnoreCase("1")) {
            switches.setChecked(true);
        } else {
            switches.setChecked(false);
        }
        switches.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPrefs.setKey(getActivity(), "push_notification_status", "1");
                    notificationChange("1");
                } else {
                    SharedPrefs.setKey(getActivity(), "push_notification_status", "0");
                    notificationChange("0");
                }
            }
        });
        return rootView;
    }

    private void initSignOutLabel() {
        if (SharedPrefs.isGuestUser(getContext())) {
            txtLogout.setText(getString(R.string.sign_up));
        } else {
            txtLogout.setText(getString(R.string.sign_out));
        }
    }

    private void notificationChange(String status) {
        Loader loader = new Loader(getContext());
        loader.show();


        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URLS.Companion.getPushNotification(), new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                loader.dismiss();
                String res = new String(response.data);
                Log.v("pushresponse", res);


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
                params.put("userId", SharedPrefs.getKey(getActivity(), "userId"));
                params.put("status", status);

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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.txtMyScan) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, new MyScanHistory());
            ft.addToBackStack(null);
            ft.commit();
        }
        if (view.getId() == R.id.txtChangePassword) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, new ChangePassword());
            ft.addToBackStack(null);
            ft.commit();
        }
        if (view.getId() == R.id.txtAbout) {
            Bundle bundle = new Bundle();
            bundle.putString("pages", "about");
            Pages pages = new Pages();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, pages);
            pages.setArguments(bundle);
            ft.addToBackStack(null);

            ft.commit();
        }
        if (view.getId() == R.id.txtTerms) {
            Bundle bundle = new Bundle();
            bundle.putString("pages", "terms");
            Pages pages = new Pages();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, pages);
            pages.setArguments(bundle);
            ft.addToBackStack(null);

            ft.commit();
        }
        if (view.getId() == R.id.txtPrivacy) {
            Bundle bundle = new Bundle();
            bundle.putString("pages", "privacy");
            Pages pages = new Pages();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, pages);
            pages.setArguments(bundle);
            ft.addToBackStack(null);
            ft.commit();
        }
        if (view.getId() == R.id.txtContactUs) {
//            Bundle bundle=new Bundle();
//            bundle.putString("pages", "contactus");
//            Pages pages=new Pages();
            ContactUsFragment fragment = new ContactUsFragment();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }
        if (view.getId() == R.id.txtFaq) {
            Bundle bundle = new Bundle();
            bundle.putString("pages", "faq");
            Pages pages = new Pages();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, pages);
            pages.setArguments(bundle);
            ft.addToBackStack(null);
            ft.commit();
        }
        if (view.getId() == R.id.txtLogout) {
            if (txtLogout.getText().equals(getString(R.string.sign_up))) {
                new AlertDialog.Builder(getActivity(), R.style.DialogTheme)
                        .setMessage(getActivity().getString(R.string.you_will_take_to_login_screen))
                        .setPositiveButton(getString(R.string.yes_label), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPrefs.deleteSharedPrefs(getActivity());
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton(R.string.no_label, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            } else {
                new AlertDialog.Builder(getActivity(), R.style.DialogTheme)
                        .setMessage(getActivity().getString(R.string.logout_confirm))
                        .setPositiveButton(getString(R.string.yes_label), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPrefs.deleteSharedPrefs(getActivity());
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton(R.string.no_label, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }
        if (view.getId() == R.id.txtChangeLanguage) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, new ChangeLanguage());
            ft.addToBackStack(null);
            ft.commit();
        }
        if (view.getId() == R.id.txtMyFav) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, new MyFavourites());
            ft.addToBackStack(null);
            ft.commit();
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

    public void setLocale(String lang) {

        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

    }
}
