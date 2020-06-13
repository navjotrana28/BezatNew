package com.bezatnew.bezat.fragments;

import android.content.Context;
import android.graphics.Color;
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
import com.bruce.pickerview.popwindow.DatePickerPopWin;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyScanHistory.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyScanHistory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyScanHistory extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View rootView;
    RecyclerView scanHistory;
    Loader loader;
    ImageView imgSearch;
    TextView txtDate;
    ImageView imgBack;
    String lang="";
    private OnFragmentInteractionListener mListener;

    public MyScanHistory() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyScanHistory.
     */
    // TODO: Rename and change types and number of parameters
    public static MyScanHistory newInstance(String param1, String param2) {
        MyScanHistory fragment = new MyScanHistory();
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
        container.setClickable(true);
        if (SharedPrefs.getKey(getActivity(),"selectedlanguage").contains("ar")) {
            getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            lang="_ar";
        } else {
            getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            lang="";

        }
        rootView=inflater.inflate(R.layout.fragment_my_scan_history, container, false);
        scanHistory=rootView.findViewById(R.id.scanHistory);
        txtDate = rootView.findViewById(R.id.txtDate);
        imgSearch = rootView.findViewById(R.id.imgSearch);
        imgBack = rootView.findViewById(R.id.imgBack);
        if(lang.equals("_ar")){
            imgBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back_rtl));
        }
        loader=new Loader(getContext());
        loader.show();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
        Date date = new Date();
       String currentDate=formatter.format(date);
        txtDate.setText(currentDate);
        getScanHistory(currentDate);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(getActivity(), new DatePickerPopWin.OnDatePickedListener() {
                    @Override
                    public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
//                        Toast.makeText(getActivity(), dateDesc, Toast.LENGTH_SHORT).show();
                        if ((month+1)<10)
                        {
                            txtDate.setText(year + "-0" + (month ));
                        }
                        else {
                            txtDate.setText(year + "-" + (month ));
                        }
                        loader.show();
                        getScanHistory(txtDate.getText().toString());
                    }
                }).textConfirm("Done") //text of confirm button
                        .textCancel("Cancel") //text of cancel button
                        .btnTextSize(16) // button text size
                        .viewTextSize(25) // pick view text size
                        .colorCancel(Color.parseColor("#ffffff")) //color of cancel button
                        .colorConfirm(Color.parseColor("#ffffff"))//color of confirm button
                        .minYear(1990) //min year in loop
                        .maxYear(2100) // max year in loop

                        .build();
                pickerPopWin.showPopWin(getActivity());

            }
        });

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(getActivity(), new DatePickerPopWin.OnDatePickedListener() {
                    @Override
                    public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
//                        Toast.makeText(getActivity(), dateDesc, Toast.LENGTH_SHORT).show();
                        if ((month+1)<10)
                        {
                            txtDate.setText(year + "-0" + (month ));
                        }
                        else {
                            txtDate.setText(year + "-" + (month ));
                        }
                        loader.show();
                        getScanHistory(txtDate.getText().toString());
                    }
                }).textConfirm("Done") //text of confirm button
                        .textCancel("Cancel") //text of cancel button
                        .btnTextSize(16) // button text size
                        .viewTextSize(25) // pick view text size
                        .colorCancel(Color.parseColor("#ffffff")) //color of cancel button
                        .colorConfirm(Color.parseColor("#ffffff"))//color of confirm button
                        .minYear(1990) //min year in loop
                        .maxYear(2100) // max year in loop

                        .build();
                pickerPopWin.showPopWin(getActivity());
            }
        });
        return rootView;
    }

    private void getScanHistory(String currentDate) {


        JSONObject object = new JSONObject();
        String Url= URLS.Companion.getSCAN_HISTORY()
                +"year_month="+currentDate+
                "&userId="+ SharedPrefs.getKey(getActivity(),"userId");

        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET,
                        Url ,
                        object,
                        response -> {
                            loader.dismiss();
                            try {
                                PostAdapter postAdapter = new PostAdapter(response.getJSONObject("result").getJSONArray("raffles"));
                                StaggeredGridLayoutManager layoutManager =
                                        new StaggeredGridLayoutManager(1, OrientationHelper.VERTICAL);
                                layoutManager.setGapStrategy(
                                        StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
                                scanHistory.setLayoutManager(layoutManager);
                                scanHistory.setItemAnimator(new DefaultItemAnimator());
                                if (postAdapter != null && postAdapter.getItemCount() > 0) {
                                    scanHistory.setAdapter(postAdapter);
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
            jsonObjectRequest.setShouldCache(false);
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
                    .inflate(R.layout.my_scan_history_item, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            try {

                Log.d("url",jsonArray.getJSONObject(position).getString("store_logo"));
                holder.storeName.setText(jsonArray.getJSONObject(position).getString("storeName"));
                holder.bill_date.setText("Date: "+jsonArray.getJSONObject(position).getString("bill_date"));
                holder.bill_no.setText("Bill No:"+jsonArray.getJSONObject(position).getString("bill_no"));
                holder.purchase_amount.setText("Amount : "+jsonArray.getJSONObject(position).getString("purchase_amount"));
                Picasso.get().load(jsonArray.getJSONObject(position).getString("store_logo")).into(holder.imgStore);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            return jsonArray.length();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView storeName,bill_date,bill_no,purchase_amount;
            ImageView imgStore;


            public MyViewHolder(View itemView) {
                super(itemView);
                imgStore=itemView.findViewById(R.id.imgStore);

                storeName=itemView.findViewById(R.id.storeName);
                bill_date=itemView.findViewById(R.id.bill_date);
                bill_no=itemView.findViewById(R.id.bill_no);
                purchase_amount=itemView.findViewById(R.id.purchase_amount);


                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                    }
                });

            }
        }
    }
}
