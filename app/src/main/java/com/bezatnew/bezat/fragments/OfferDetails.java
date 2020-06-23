package com.bezatnew.bezat.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentTransaction;

import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bezatnew.bezat.MyApplication;
import com.bezatnew.bezat.R;
import com.bezatnew.bezat.utils.Loader;
import com.bezatnew.bezat.utils.SharedPrefs;
import com.bezatnew.bezat.utils.URLS;
import com.bezatnew.bezat.utils.VolleyMultipartRequest;
import com.squareup.picasso.Picasso;
import net.glxn.qrgen.android.QRCode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OfferDetails.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OfferDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OfferDetails extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View rootView;
    ImageView imgBarCode,offer_img;
    TextView offer_coupon_code,store_name,offer_descp,discount_price,actual_price,customer_phone,customer_code;
    Loader loader;
    ImageView imgSaved;
    private OnFragmentInteractionListener mListener;
    boolean is_saved;
    String offerId;
    ImageView imgBack;
    String lang="";
    LinearLayout linearLayout;
    public OfferDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OfferDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static OfferDetails newInstance(String param1, String param2) {
        OfferDetails fragment = new OfferDetails();
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
//            Typeface typeface=Typeface.createFromAsset(getActivity().getAssets(),"font/tajawal_regular");
//            offer_coupon_code.setTypeface(typeface);
//            store_name.setTypeface(typeface);
//            offer_descp.setTypeface(typeface);
//            discount_price.setTypeface(typeface);
//            actual_price.setTypeface(typeface);
        } else {
            getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            lang="";
//            Typeface typeface=Typeface.createFromAsset(getActivity().getAssets(),"font/muli_regular");
//            offer_coupon_code.setTypeface(typeface);
//            store_name.setTypeface(typeface);
//            offer_descp.setTypeface(typeface);
//            discount_price.setTypeface(typeface);
//            actual_price.setTypeface(typeface);
        }
        rootView=inflater.inflate(R.layout.fragment_offer_details, container, false);
        imgBarCode=rootView.findViewById(R.id.imgBarCode);
        offer_img=rootView.findViewById(R.id.offer_img);
        offer_coupon_code=rootView.findViewById(R.id.offer_coupon_code);
        store_name=rootView.findViewById(R.id.store_name);
        offer_descp=rootView.findViewById(R.id.offer_descp);
        discount_price=rootView.findViewById(R.id.discount_price);
        actual_price=rootView.findViewById(R.id.actual_price);
        imgSaved = rootView.findViewById(R.id.imgSaved);
        customer_code = rootView.findViewById(R.id.offer_customer_code);
        customer_phone = rootView.findViewById(R.id.offer_mobile_number);
        imgBack = rootView.findViewById(R.id.imgBack);
        if(lang.equals("_ar")){
            imgBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back_rtl));
        }
        if(!SharedPrefs.getKey(getActivity(),"user_code").equals("")) {
            customer_code.setText(SharedPrefs.getKey(getActivity(), "user_code"));
            customer_phone.setText(SharedPrefs.getKey(getActivity(), "phone"));
        }
        linearLayout = rootView.findViewById(R.id.offer_detail_layout);
        loader=new Loader(getContext());
         offerId=getArguments().getString("offerId");
         loader.show();
        getOfferDetails(SharedPrefs.getKey(getActivity(),"userId"),
                offerId);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        imgSaved.setOnClickListener(this);
        return rootView;
    }

    private void getOfferDetails(String userId,String	offerId) {
        JSONObject object = new JSONObject();
        String Url= URLS.Companion.getOFFER_DETAILS()+"userId="+ userId
                +"&offerId="+offerId;
        Log.v("offerdetails",Url+" ");
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET,
                        Url ,
                        object,
                        response -> {
                            try {
                                JSONObject jsonResult=response.getJSONObject("result");
                                Picasso.get().load(jsonResult.getString("offer_img")).into(offer_img);
                                offer_coupon_code.setText(jsonResult.getString("offer_coupon_code"));
                                store_name.setText(jsonResult.getString("store_name"+lang));
                                offer_descp.setText(jsonResult.getString("offer_descp"+lang));
                                discount_price.setText(jsonResult.getString("discount_price"));
                                actual_price.setText(jsonResult.getString("actual_price"));
                                Bitmap myBitmap = QRCode.from(jsonResult.getString("offer_coupon_code")).bitmap();
                                imgBarCode.setScaleType(ImageView.ScaleType.FIT_XY);
                                imgBarCode.setImageBitmap(myBitmap);
                                imgBarCode.setScaleType(ImageView.ScaleType.FIT_XY);
                                if (jsonResult.getString("is_saved").equalsIgnoreCase("1")) {
                                    imgSaved.setImageResource(R.drawable.ic_favorite_black_24dp);
                                    is_saved=true;
                                } else {
                                    imgSaved.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                                    is_saved=false;
                                }
                                linearLayout.setVisibility(View.VISIBLE);
                                loader.dismiss();
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
        if (view.getId()==R.id.imgSaved)
        {
            loader.show();
            if (is_saved)
            {
                removeOffers(SharedPrefs.getKey(getActivity(),"userId"),
                        offerId );
            }
            else {
                saveOffers(SharedPrefs.getKey(getActivity(),"userId"),
                        offerId );
            }
        }
    }

    private void saveOffers(String userId, String offerId) {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URLS.Companion.getSAVE_OFFER(), new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                loader.dismiss();
                String res = new String(response.data);
                try {

                    JSONObject jsonObject=new JSONObject(res).getJSONObject("result");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("success"))
                    {
                        imgSaved.setImageResource(R.drawable.ic_favorite_black_24dp);
                        is_saved=true;
                    } else
                    {
                        imgSaved.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                        is_saved=false;
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
                params.put("userId",userId);
                params.put("offerId",offerId);
                params.put("apiKey","12345678");
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
    private void removeOffers(String userId, String offerId) {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URLS.Companion.getREMOVE_SAVE_OFFER(), new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                loader.dismiss();
                String res = new String(response.data);
                Log.v("response",res+" ");
                try {

                    JSONObject jsonObject=new JSONObject(res).getJSONObject("result");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("success"))
                    {
                        imgSaved.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                        is_saved=false;
                    }
                    else
                    {
                        imgSaved.setImageResource(R.drawable.ic_favorite_black_24dp);
                        is_saved=true;
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
                params.put("userId",userId);
                params.put("offerId",offerId);
                params.put("apikey","12345678");
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
