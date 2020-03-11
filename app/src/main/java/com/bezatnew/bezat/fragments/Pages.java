package com.bezatnew.bezat.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bezatnew.bezat.MyApplication;
import com.bezatnew.bezat.R;
import com.bezatnew.bezat.utils.Loader;
import com.bezatnew.bezat.utils.URLS;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Pages.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Pages#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Pages extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String msg;

    private OnFragmentInteractionListener mListener;
    WebView web;
    View rootView;
    Loader loader;
    TextView txtPages;
    ImageView imgBack;
    public Pages() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Pages.
     */
    // TODO: Rename and change types and number of parameters
    public static Pages newInstance(String param1, String param2) {
        Pages fragment = new Pages();
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
        rootView=inflater.inflate(R.layout.fragment_pages, container, false);
        web=rootView.findViewById(R.id.web);
        loader=new Loader(getContext());
        imgBack = rootView.findViewById(R.id.imgBack);
        txtPages=rootView.findViewById(R.id.txtPages);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        msg=getArguments().getString("pages");

        if (msg.equalsIgnoreCase("about"))
        {
            txtPages.setText("About us");
            getPages(URLS.Companion.getPAGES_ABOUT());
        }
        else if (msg.equalsIgnoreCase("terms"))
        {
            txtPages.setText("Terms & Conditions");
            getPages(URLS.Companion.getPAGES_TERMS());
        }
        else if (msg.equalsIgnoreCase("privacy"))
        {
            txtPages.setText("Privacy Policy");
            getPages(URLS.Companion.getPAGES_PRIVACY());
        }
        else if (msg.equalsIgnoreCase("contactus"))
        {
            txtPages.setText("Contact Us");
            getPages(URLS.Companion.getPAGES_CONTACTUS());
        }
        else if (msg.equalsIgnoreCase("faq"))
        {
            txtPages.setText("FAQ");
            getPages(URLS.Companion.getPAGES_FAQ());
        }

        loader.show();

        return rootView;
    }

    private void getPages(String url) {
        JSONObject object = new JSONObject();
        Log.v("profile",url+" ");
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET,
                        url ,
                        object,
                        response -> {
                                    loader.dismiss();
                            Log.v("NotificationResponse",response+" ");
                            try {
                                String title=response.getJSONObject("result").getJSONObject("page").getString("content");
                                web.loadData(title, "text/html", "UTF-8");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        error -> {
                        loader.dismiss();
                            Log.v("NotificationError",error.toString()+" ");
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
}
