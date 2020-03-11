package com.bezatnew.bezat.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

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
 * {@link FavouriteOffer.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FavouriteOffer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavouriteOffer extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    ImageView imgBarCode;
    ImageView imgBack;
    RecyclerView recOffer;
    Loader loader;
    View rootView;
    private String currentDate = "";

    public FavouriteOffer() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavouriteOffer.
     */
    // TODO: Rename and change types and number of parameters
    public static FavouriteOffer newInstance(String param1, String param2) {
        FavouriteOffer fragment = new FavouriteOffer();
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
        rootView = inflater.inflate(R.layout.activity_favourite_offer, container, false);
        imgBack = rootView.findViewById(R.id.imgBack);
        recOffer = rootView.findViewById(R.id.recOffer);
        loader = new Loader(getContext());
        loader.show();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = new Date();
        currentDate = formatter.format(date);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        getFavouriteItem();
        return rootView;
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


    private void getFavouriteItem() {
        JSONObject object = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET,
//                        URLS.Companion.getLATEST_OFFER() + "userId=16&currentDate=2019-06-25",
                        URLS.Companion.getLATEST_OFFER() + "userId=" + SharedPrefs.getKey(getActivity(),
                                "userId") + "&currentDate=" + currentDate,
                        object,
                        response -> {
                            loader.dismiss();
                            try {

                                PostAdapter postAdapter = new PostAdapter(response.getJSONArray("result"));

                                //  recyclerView.setHasFixedSize(true);
                                StaggeredGridLayoutManager layoutManager =
                                        new StaggeredGridLayoutManager(1, OrientationHelper.VERTICAL);
                                layoutManager.setGapStrategy(
                                        StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
                                recOffer.setLayoutManager(layoutManager);
                                recOffer.setItemAnimator(new DefaultItemAnimator());
                                if (postAdapter != null && postAdapter.getItemCount() > 0) {
                                    recOffer.setAdapter(postAdapter);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        error -> {
                            loader.dismiss();
                            Log.v("error", error.getMessage() + " ");


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
                    .inflate(R.layout.favourite_item, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            try {


                holder.reTailerName.setText(jsonArray.getJSONObject(position).getString("store_name"));
                Picasso.get().load(jsonArray
                        .optJSONObject(position).optString("store_logo"))
                        .into(holder.imgBanner);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            return jsonArray.length();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView reTailerName;
            ImageView imgBanner;

            public MyViewHolder(View itemView) {
                super(itemView);

                reTailerName = itemView.findViewById(R.id.reTailerName);
                imgBanner = itemView.findViewById(R.id.imgBanner);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        try {
                            OfferDetails offerDetails = new OfferDetails();
                            Bundle args = new Bundle();
                            args.putString("offerId", jsonArray.getJSONObject(getAdapterPosition()).getString("offer_id"));
                            offerDetails.setArguments(args);
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.container, offerDetails);
                            ft.addToBackStack(null);
                            ft.commit();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        }
    }
}
