package com.bezatnew.bezat.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
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
import com.bezatnew.bezat.utils.SharedPrefs;
import com.bezatnew.bezat.utils.URLS;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PrizeDetails.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PrizeDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrizeDetails extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String raffle_category, raffle_id;
    Loader loader;
    View rootView;
    TextView txtPrize;
    ImageView imgPrize;
    Button btnDraw;
    String lang = "";
    ImageView imgBack;
    private OnFragmentInteractionListener mListener;

    public PrizeDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PrizeDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static PrizeDetails newInstance(String param1, String param2) {
        PrizeDetails fragment = new PrizeDetails();
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
        rootView = inflater.inflate(R.layout.fragment_prize_details, container, false);
        if (SharedPrefs.getKey(getActivity(), "selectedlanguage").contains("ar")) {
            getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            lang = "_ar";
//            Typeface typeface=Typeface.createFromAsset(getActivity().getAssets(),"font/tajawal_regular");
//            txtPrize.setTypeface(typeface);
        } else {
            getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            lang = "";
//            Typeface typeface=Typeface.createFromAsset(getActivity().getAssets(),"font/muli_regular");
//            txtPrize.setTypeface(typeface);
        }
        txtPrize = rootView.findViewById(R.id.txtPrize);
        imgPrize = rootView.findViewById(R.id.imgPrize);
        imgBack = rootView.findViewById(R.id.imgBack);
        btnDraw = rootView.findViewById(R.id.btnDraw);
        raffle_category = getArguments().getString("raffle_category");
        raffle_id = getArguments().getString("raffle_id");
        loader = new Loader(getContext());
        loader.show();
        getRaffledetails(raffle_category, raffle_id);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        return rootView;
    }

    private void getRaffledetails(String raffle_category, String raffle_id) {
        JSONObject object = new JSONObject();
        String vipUrl = URLS.Companion.getPRIZES_DETAILS() + raffle_id + "&raffle_category=" + raffle_category;
        Log.v("raffle_details", vipUrl + " ");
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET,
                        vipUrl,
                        object,
                        response -> {
                            loader.dismiss();
                            try {
                                String draw = "";
                                JSONObject jsonObject = response.getJSONObject("result").getJSONObject("raffles");
                                if (lang.equals("_ar")) {
                                    txtPrize.setText(getString(R.string.prize_prizes_section) + " " + jsonObject.getString("prize_ar"));
                                } else {
                                    txtPrize.setText(getString(R.string.prize_prizes_section) + " " + jsonObject.getString("prize"));
                                }
                                String difDate = getDifDays(jsonObject.getString("draw_date"));
                                if (Integer.parseInt(difDate) > 1) {
                                    int temp = Integer.parseInt(difDate) - 1;
                                    draw = getString(R.string.raffle_draw_in) + " " + temp + " " + getString(R.string.days_prizes);
                                } else if (Integer.parseInt(difDate) < 0) {
                                    draw = getString(R.string.raffle_held) + " " + Math.abs(Integer.parseInt(difDate))
                                            + " " + getString(R.string.days_before);
                                } else if (Integer.parseInt(difDate) == 0) {
                                    draw = getString(R.string.raffle_draw_announce_today);
                                } else if (Integer.parseInt(difDate) == 1) {
                                    draw = getString(R.string.raffle_draw_announce_tomorrow);
                                }
                                btnDraw.setText(draw);
                                Picasso.get().load(jsonObject.getString("img")).into(imgPrize);
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

    public String getDifDays(String inputString2) {
        String diffDays = "";
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = new Date();
        String inputString1 = formatter.format(date);


        try {
            Date date1 = myFormat.parse(inputString1);
            Date date2 = myFormat.parse(inputString2);
            long diff = date2.getTime() - date1.getTime();
            diffDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + " ";
            System.out.println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return diffDays.replace(" ", "");
    }
}
