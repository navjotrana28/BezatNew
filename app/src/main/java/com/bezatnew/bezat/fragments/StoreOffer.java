package com.bezatnew.bezat.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bezatnew.bezat.MyApplication;
import com.bezatnew.bezat.R;
import com.bezatnew.bezat.utils.Loader;
import com.bezatnew.bezat.utils.SharedPrefs;
import com.bezatnew.bezat.utils.URLS;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StoreOffer.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StoreOffer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoreOffer extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View rootView;
    Loader loader;
    ImageView imgBanner;
    Button btnAllOffer;
    ImageView imgBack;
    String lang="";
    LinearLayout wholeStorelayout;
    TextView txtStoreName;
    ImageButton txtTwitter, txtInsta, txtFb, txtWeb, txtPhone, txtSnap, txtWhatsapp, txtLocation;
    private OnFragmentInteractionListener mListener;
    String phone = "", google = "", fb = "", insta = "", twit = "", snap = "", whatsapp = "",location = "";

    public StoreOffer() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StoreOffer.
     */
    // TODO: Rename and change types and number of parameters
    public static StoreOffer newInstance(String param1, String param2) {
        StoreOffer fragment = new StoreOffer();
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
        container.setClickable(true);
        rootView = inflater.inflate(R.layout.fragment_store_offer, container, false);
        String storeId=getArguments().getString("storeId");
        Log.d("storeIdnam", storeId);
        imgBanner=rootView.findViewById(R.id.imgBanner);
        txtTwitter=rootView.findViewById(R.id.txtTwitter);
        txtInsta=rootView.findViewById(R.id.txtInsta);
        txtFb=rootView.findViewById(R.id.txtFb);
        txtWeb=rootView.findViewById(R.id.txtWeb);
        txtPhone=rootView.findViewById(R.id.txtPhone);
        txtWhatsapp = rootView.findViewById(R.id.txtWhatsapp);
        txtSnap = rootView.findViewById(R.id.txtSnap);
        txtStoreName=rootView.findViewById(R.id.txtStoreName);
        txtLocation=rootView.findViewById(R.id.txtLocation);
        btnAllOffer=rootView.findViewById(R.id.btnAllOffer);
        imgBack=rootView.findViewById(R.id.imgBack);
        if(lang.equals("_ar")){
            imgBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back_rtl));
        }
        wholeStorelayout = rootView.findViewById(R.id.wholeStorelayout);

        imgBack.setOnClickListener(this);
        txtTwitter.setOnClickListener(this);
        txtInsta.setOnClickListener(this);
        txtFb.setOnClickListener(this);
        txtWeb.setOnClickListener(this);
        txtPhone.setOnClickListener(this);
        txtWhatsapp.setOnClickListener(this);
        txtLocation.setOnClickListener(this);

        loader=new Loader(getContext());
        loader.show();
        System.out.println("storeId"+storeId);
        getStoreOffer(storeId);
        return rootView;
    }

    private void getStoreOffer(String storeId) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = new Date();
        String currentDate=formatter.format(date);
        JSONObject object = new JSONObject();
        String vipUrl= URLS.Companion.getSTORE_BY_OFFER()+"userId="+ SharedPrefs.getKey(getActivity(),"userId")
                +"&storeId="+storeId
                +"&currentDate="+currentDate;
        Log.v("STORE_BY_OFFER",vipUrl+" ");
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET,
                        vipUrl ,
                        object,
                        response -> {
                            loader.dismiss();
                            try {
                                JSONObject jsonObject=response.getJSONObject("result");
                                Log.d("---response---",jsonObject.toString());
                                Picasso.get().load(jsonObject.getString("store_image")).into(imgBanner);
                                txtStoreName.setText(jsonObject.getString("storeName" + lang));
                                if (jsonObject.isNull("twitter")
                                        || jsonObject.getString("twitter").equals("")) {
                                    txtTwitter.setBackgroundTintList(getResources().getColorStateList(R.color.btn_cancel_color));
                                } else {
                                    twit = jsonObject.getString("twitter");
                                }
                                if (jsonObject.isNull("instagram")
                                        || jsonObject.getString("instagram").equals("")) {
                                    txtInsta.setBackgroundTintList(getResources().getColorStateList(R.color.btn_cancel_color));
                                } else {
                                    insta = jsonObject.getString("instagram");
                                }
                                if (jsonObject.isNull("facebook")
                                        || jsonObject.getString("facebook").equals("")) {
                                    txtFb.setBackgroundTintList(getResources().getColorStateList(R.color.btn_cancel_color));
                                } else {
                                    fb = jsonObject.getString("facebook");
                                }
                                if (jsonObject.isNull("website")
                                        || jsonObject.getString("website").equals("")) {
                                    txtWeb.setBackgroundTintList(getResources().getColorStateList(R.color.btn_cancel_color));
                                } else {
                                    google = jsonObject.getString("website");
                                    Log.d("website",google);
                                }
                                if (jsonObject.isNull("snapchat")
                                        || jsonObject.getString("snapchat").equals("")) {
                                    txtSnap.setBackgroundTintList(getResources().getColorStateList(R.color.btn_cancel_color));
                                } else {
                                    snap = jsonObject.getString("snapchat");
                                    Log.d("snap",snap);
                                }
                                if (jsonObject.isNull("whatsapp")
                                        || jsonObject.getString("whatsapp").equals("")) {
                                    txtWhatsapp.setBackgroundTintList(getResources().getColorStateList(R.color.btn_cancel_color));
                                } else {
                                    whatsapp = jsonObject.getString("whatsapp");
                                }
//                                txtPhone.setText(jsonObject.getString("phone_no"));
                                if (jsonObject.isNull("phone_no")
                                        || jsonObject.getString("phone_no").equals("")) {
                                    txtPhone.setBackgroundTintList(getResources().getColorStateList(R.color.btn_cancel_color));
                                } else {
                                    phone = jsonObject.getString("phone_no");
                                }
                                if (jsonObject.isNull("google_location")
                                        || jsonObject.getString("google_location").equals("")) {
                                    //txtLocation.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.location_inactive));
                                    txtLocation.setBackgroundTintList(getResources().getColorStateList(R.color.btn_cancel_color));
                                } else {
                                    location = jsonObject.getString("google_location");
                                }
                                JSONArray storeArray = jsonObject.getJSONArray("store_offers");
                                wholeStorelayout.setVisibility(View.VISIBLE);
                                loader.dismiss();
                                btnAllOffer.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (storeArray!=null)
                                        {
                                            Offers offers=new Offers();
                                            Bundle args = new Bundle();
                                            args.putString("storeArray",storeArray.toString() );
                                            offers.setArguments(args);
                                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                            ft.replace(R.id.container,offers);
                                            ft.addToBackStack(null);
                                            ft.commit();
                                        }
                                        else {
                                            Toast.makeText(getActivity(), getString(R.string.no_offer_found), Toast.LENGTH_LONG).show();
                                        }
                                        }
                                });


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
        if (view.getId()==R.id.txtInsta)
        {
            if (insta!=null && !insta.isEmpty()) {
               startActivity( new Intent(Intent.ACTION_VIEW,Uri.parse(insta)));
            }
            else {
                //startActivity( new Intent(Intent.ACTION_VIEW,Uri.parse("https://instagram.com/")));
            }
        }
       else if (view.getId()==R.id.txtFb)
        {
            if (fb!=null &&  !fb.isEmpty()) {
                startActivity( new Intent(Intent.ACTION_VIEW,Uri.parse(fb)));
            }
            else {
                //startActivity( new Intent(Intent.ACTION_VIEW,Uri.parse("https://facebook.com/")));
            }
        } else if (view.getId() == R.id.txtWhatsapp) {
            //Toast.makeText(getActivity().getBaseContext(), "Yay", Toast.LENGTH_SHORT).show();
            if (whatsapp != null && !whatsapp.isEmpty()) {
                Intent i = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(
                                whatsapp
                        )
                );
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            } else {
                txtWhatsapp.setEnabled(false);
            }
        } else if (view.getId() == R.id.txtLocation) {
            //Toast.makeText(getActivity().getBaseContext(), "Yay", Toast.LENGTH_SHORT).show();
            if (location != null && !location.isEmpty()) {
                Intent i = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(
                                "http://maps.google.com/maps?q="+location
                        )
                );
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            } else {
                txtLocation.setEnabled(false);
            }
        }
        else if (view.getId() == R.id.txtSnap) {
            if (snap != null && !snap.isEmpty()) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(snap)));
            } else {
                txtSnap.setEnabled(false);
            }
        }
        else if (view.getId()==R.id.txtWeb)
        {
            if (google!=null && !google.isEmpty()) {
                startActivity( new Intent(Intent.ACTION_VIEW,Uri.parse(google)));
                Log.d("---web---",google);
            }
            else {
                //startActivity( new Intent(Intent.ACTION_VIEW,Uri.parse("https://google.com/")));
            }
        }
        else if (view.getId()==R.id.txtPhone)
        {
            if (phone!=null && phone!="") {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }
        }
        else if (view.getId()==R.id.txtTwitter)
        {
            if (twit!=null &&  !twit.isEmpty()) {
                startActivity( new Intent(Intent.ACTION_VIEW,Uri.parse(twit)));
            }
            else {
                //startActivity( new Intent(Intent.ACTION_VIEW,Uri.parse("https://twitter.com/")));
            }
        }
        else if (view.getId() == R.id.imgBack)
        {
            getActivity().onBackPressed();
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



}
