package com.bezatnew.bezat.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
 * {@link Prizes.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Prizes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Prizes extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView txtMeetaStart;
    View rootView;
    RecyclerView recPrizes;
    Loader loader;
    String currentDate;
    ImageView imgBack;
    String lang = "";
    RecyclerView bezatRec;
    RecyclerView otherRec;
    private OnFragmentInteractionListener mListener;

    public Prizes() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Prizes.
     */
    // TODO: Rename and change types and number of parameters
    public static Prizes newInstance(String param1, String param2) {
        Prizes fragment = new Prizes();
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
        rootView = inflater.inflate(R.layout.fragment_prizes, container, false);
        if (SharedPrefs.getKey(getActivity(), "selectedlanguage").contains("ar")) {
            getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            lang = "_ar";
        } else {
            getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            lang = "";
        }

        recPrizes = rootView.findViewById(R.id.recPrizes);
        bezatRec = rootView.findViewById(R.id.bezatRec);
        otherRec = rootView.findViewById(R.id.otheraff);
        imgBack = rootView.findViewById(R.id.imgBack);
        loader = new Loader(getContext());
        loader.show();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM", Locale.ENGLISH);
        Date date = new Date();
        currentDate = formatter.format(date);
//        currentDate="2019-06";
        getPrizes(rootView);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        return rootView;
    }

    private void getPrizes(View rootView) {
        JSONObject object = new JSONObject();
        String prizesUrl = URLS.Companion.getPRIZES_LIST() + currentDate;
        Log.v("prizesUrl", prizesUrl);
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET,
                        prizesUrl,
                        object,
                        response -> {
                            loader.dismiss();
                            LinearLayout linearLayout = rootView.findViewById(R.id.liner_prizees);
                            linearLayout.setVisibility(View.VISIBLE);
                            try {

                                JSONArray bezatJsonArray = new JSONArray();
                                JSONArray monthlyJsonArray = new JSONArray();
                                JSONArray otherJsonArray = new JSONArray();
                                JSONArray array = response.getJSONObject("result")
                                        .getJSONArray("raffles");
                                for (int i = 0; i < array.length(); i++) {
                                    if (array.getJSONObject(i).getString("raffle_category")
                                            .equalsIgnoreCase("1")) {
                                        JSONObject objectRaffles = new JSONObject();
                                        objectRaffles.put("raffle_id", array.getJSONObject(i).getString("raffle_id"));
                                        objectRaffles.put("raffle_name", array.getJSONObject(i).getString("raffle_name"));
                                        objectRaffles.put("raffle_name_ar", array.getJSONObject(i).getString("raffle_name_ar"));
                                        objectRaffles.put("prize", array.getJSONObject(i).getString("prize") + "$");
                                        objectRaffles.put("prize_ar", array.getJSONObject(i).getString("prize_ar"));
                                        objectRaffles.put("draw_date", array.getJSONObject(i).getString("draw_date"));
                                        objectRaffles.put("crcdt", array.getJSONObject(i).getString("crcdt"));
                                        objectRaffles.put("raffle_category", array.getJSONObject(i).getString("raffle_category"));
                                        objectRaffles.put("img", array.getJSONObject(i).getString("img"));
                                        bezatJsonArray.put(objectRaffles);
                                    } else if (array.getJSONObject(i).getString("raffle_category")
                                            .equalsIgnoreCase("2")) {
                                        JSONObject objectRaffles = new JSONObject();
                                        objectRaffles.put("raffle_id", array.getJSONObject(i).getString("raffle_id"));
                                        objectRaffles.put("raffle_name", array.getJSONObject(i).getString("raffle_name"));
                                        objectRaffles.put("raffle_name_ar", array.getJSONObject(i).getString("raffle_name_ar"));
                                        objectRaffles.put("prize", array.getJSONObject(i).getString("prize") + "$");
                                        objectRaffles.put("prize_ar", array.getJSONObject(i).getString("prize_ar"));
                                        objectRaffles.put("draw_date", array.getJSONObject(i).getString("draw_date"));
                                        objectRaffles.put("crcdt", array.getJSONObject(i).getString("crcdt"));
                                        objectRaffles.put("raffle_category", array.getJSONObject(i).getString("raffle_category"));
                                        objectRaffles.put("img", array.getJSONObject(i).getString("img"));
                                        monthlyJsonArray.put(objectRaffles);
                                    } else if (array.getJSONObject(i).getString("raffle_category")
                                            .equalsIgnoreCase("3")) {
                                        JSONObject objectRaffles = new JSONObject();
                                        objectRaffles.put("raffle_id", array.getJSONObject(i).getString("raffle_id"));
                                        objectRaffles.put("raffle_name", array.getJSONObject(i).getString("raffle_name"));
                                        objectRaffles.put("raffle_name_ar", array.getJSONObject(i).getString("raffle_name_ar"));
                                        objectRaffles.put("prize", array.getJSONObject(i).getString("prize") + "$");
                                        objectRaffles.put("prize_ar", array.getJSONObject(i).getString("prize_ar"));
                                        objectRaffles.put("draw_date", array.getJSONObject(i).getString("draw_date"));
                                        objectRaffles.put("crcdt", array.getJSONObject(i).getString("crcdt"));
                                        objectRaffles.put("raffle_category", array.getJSONObject(i).getString("raffle_category"));
                                        objectRaffles.put("img", array.getJSONObject(i).getString("img"));
                                        otherJsonArray.put(objectRaffles);
                                    }
                                }

                                PostAdapter1 postAdapter1 = new PostAdapter1(bezatJsonArray);
                                bezatRec.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

                                if (postAdapter1 != null && postAdapter1.getItemCount() > 0) {
                                    bezatRec.setAdapter(postAdapter1);
                                } else {
                                    CardView view = rootView.findViewById(R.id.bezat_raffles);
                                    view.setVisibility(View.GONE);
                                    bezatRec.setVisibility(View.GONE);
                                }
                                PostAdapter postAdapter = new PostAdapter(monthlyJsonArray);
                                recPrizes.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

                                recPrizes.setItemAnimator(new DefaultItemAnimator());
                                if (postAdapter != null && postAdapter.getItemCount() > 0) {
                                    recPrizes.setAdapter(postAdapter);
                                } else {
                                    CardView view = rootView.findViewById(R.id.monthlyraffles);
                                    view.setVisibility(View.GONE);
                                    recPrizes.setVisibility(View.GONE);
                                }

                                PostAdapterOther postAdapterOther = new PostAdapterOther(otherJsonArray);
                                otherRec.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                                otherRec.setItemAnimator(new DefaultItemAnimator());
                                if (postAdapterOther != null && postAdapterOther.getItemCount() > 0) {
                                    otherRec.setAdapter(postAdapterOther);
                                } else {
                                    CardView view = rootView.findViewById(R.id.otherRaffles);
                                    view.setVisibility(View.GONE);
                                    otherRec.setVisibility(View.GONE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        error ->

                        {
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

    //other raffles adapter--------------
    private class PostAdapterOther extends RecyclerView.Adapter<PostAdapterOther.MyViewHolder> {
        JSONArray jsonArray;

        public PostAdapterOther(JSONArray array) {
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
                    .inflate(R.layout.prizes_irtm, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            try {
                holder.txtPrizeName.setText(jsonArray.getJSONObject(position).getString("raffle_name" + lang));
                holder.txtPrizePrice.setText(jsonArray.getJSONObject(position).getString("prize" + lang));
                Picasso.get().load(jsonArray.getJSONObject(position).getString("img")).into(holder.imageView);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            return jsonArray.length();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txtPrizeName;
            TextView txtPrizePrice;
            ImageView imageView;

            public MyViewHolder(View itemView) {
                super(itemView);

                txtPrizeName = itemView.findViewById(R.id.txtPrizeName);
                txtPrizePrice = itemView.findViewById(R.id.txtPrizePrice);
                imageView = itemView.findViewById(R.id.imgMonth);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            PrizeDetails prizeDetails = new PrizeDetails();
                            Bundle args = new Bundle();
                            args.putString("raffle_id", jsonArray.getJSONObject(getAdapterPosition()).getString("raffle_id"));
                            args.putString("raffle_category", jsonArray.getJSONObject(getAdapterPosition()).getString("raffle_category"));
                            prizeDetails.setArguments(args);
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.container, prizeDetails);
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

    //monthly raffles adapter -----------------
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
                    .inflate(R.layout.prizes_irtm, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            try {
                holder.txtPrizeName.setText(jsonArray.getJSONObject(position).getString("raffle_name" + lang));
                holder.txtPrizePrice.setText(jsonArray.getJSONObject(position).getString("prize" + lang));
                Picasso.get().load(jsonArray.getJSONObject(position).getString("img")).into(holder.imageView);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            return jsonArray.length();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txtPrizeName;
            TextView txtPrizePrice;
            ImageView imageView;

            public MyViewHolder(View itemView) {
                super(itemView);

                txtPrizeName = itemView.findViewById(R.id.txtPrizeName);
                txtPrizePrice = itemView.findViewById(R.id.txtPrizePrice);
                imageView = itemView.findViewById(R.id.imgMonth);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            PrizeDetails prizeDetails = new PrizeDetails();
                            Bundle args = new Bundle();
                            args.putString("raffle_id", jsonArray.getJSONObject(getAdapterPosition()).getString("raffle_id"));
                            args.putString("raffle_category", jsonArray.getJSONObject(getAdapterPosition()).getString("raffle_category"));
                            prizeDetails.setArguments(args);
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.container, prizeDetails);
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


    private class PostAdapter1 extends RecyclerView.Adapter<PostAdapter1.MyViewHolder> {
        JSONArray jsonArray;

        public PostAdapter1(JSONArray array) {
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
                    .inflate(R.layout.prizes_irtm, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            try {
                holder.txtPrizeName.setText(jsonArray.getJSONObject(position).getString("raffle_name" + lang));
                holder.txtPrizePrice.setText(jsonArray.getJSONObject(position).getString("prize" + lang));
                Picasso.get().load(jsonArray.getJSONObject(position).getString("img")).into(holder.imageView);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            return jsonArray.length();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txtPrizeName;
            TextView txtPrizePrice;
            ImageView imageView;

            public MyViewHolder(View itemView) {
                super(itemView);

                txtPrizeName = itemView.findViewById(R.id.txtPrizeName);
                txtPrizePrice = itemView.findViewById(R.id.txtPrizePrice);
                imageView = itemView.findViewById(R.id.imgMonth);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            PrizeDetails prizeDetails = new PrizeDetails();
                            Bundle args = new Bundle();
                            args.putString("raffle_id", jsonArray.getJSONObject(getAdapterPosition()).getString("raffle_id"));
                            args.putString("raffle_category", jsonArray.getJSONObject(getAdapterPosition()).getString("raffle_category"));
                            prizeDetails.setArguments(args);
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.container, prizeDetails);
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
