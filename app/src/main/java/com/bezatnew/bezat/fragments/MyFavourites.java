package com.bezatnew.bezat.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyFavourites.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyFavourites#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyFavourites extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    View rootView;
    RecyclerView recFav;
    Loader loader;
    public MyFavourites() {
        // Required empty public constructor
    }


    public static MyFavourites newInstance(String param1, String param2) {
        MyFavourites fragment = new MyFavourites();
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
        rootView= inflater.inflate(R.layout.activity_my_favourite, container, false);
        recFav=rootView.findViewById(R.id.recFav);
        loader=new Loader(getContext());
        loader.show();
        getFavList();
        return rootView;
    }

    private void getFavList() {

        JSONObject object = new JSONObject();
        String Url= URLS.Companion.getFAV_STORE_LIST()+ SharedPrefs.getKey(getActivity(),"userId");

        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET,
                        Url ,
                        object,
                        response -> {
                            loader.dismiss();
                            Log.v("favouriteresponse",response+" ");
                            try {
                               PostAdapter postAdapter = new PostAdapter(response.getJSONArray("result"));
                                StaggeredGridLayoutManager layoutManager =
                                        new StaggeredGridLayoutManager(1, OrientationHelper.VERTICAL);
                                layoutManager.setGapStrategy(
                                        StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
                                recFav.setLayoutManager(layoutManager);
                                recFav.setItemAnimator(new DefaultItemAnimator());
                                if (postAdapter != null && postAdapter.getItemCount() > 0) {
                                    recFav.setAdapter(postAdapter);
                                }
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
        jsonObjectRequest.setShouldCache(true);
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
        public PostAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.favourite_item, parent, false);
            return new PostAdapter.MyViewHolder(itemView);
        }
        @Override
        public void onBindViewHolder(PostAdapter.MyViewHolder holder, int position) {
            try {
                holder.reTailerName.setText(jsonArray.getJSONObject(position).getString("store_name"));
                Picasso.get()
                        .load(jsonArray.getJSONObject(position).getString("store_logo")).
                        placeholder(R.drawable.ic_image_black_24dp).
                        into(holder.imgBanner);
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
                    }
                });

            }
        }
    }
}
