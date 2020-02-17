package com.bezat.bezat.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bezat.bezat.MyApplication;
import com.bezat.bezat.R;
import com.bezat.bezat.utils.Loader;
import com.bezat.bezat.utils.MySpinner;
import com.bezat.bezat.utils.SharedPrefs;
import com.bezat.bezat.utils.URLS;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Partner_store.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Partner_store#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Partner_store extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView recStore;
    JSONArray jsonArray;
    View rootView;
    Loader loader;
    MySpinner spinner,spinnerLoc;
    String category_id="",
            location_id="";
    ImageView imgStore,imgLocation;
    ArrayList<String> stores ;
    ArrayList<String> catcategory_ids;

    ArrayList<String> location ;
    ArrayList<String> location_ids ;
    private OnFragmentInteractionListener mListener;
    TextView spinnerText;
    TextView txtLocation;
    ImageView imgSearch;
    ImageView imgBack;
    String lang="";
    public Partner_store() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Partner_store.
     */
    // TODO: Rename and change types and number of parameters
    public static Partner_store newInstance(String param1, String param2) {
        Partner_store fragment = new Partner_store();
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
        rootView=inflater.inflate(R.layout.fragment_partner_store, container, false);
        recStore=rootView.findViewById(R.id.recStore);
        imgStore=rootView.findViewById(R.id.imgStore);
        imgLocation=rootView.findViewById(R.id.imgLocation);
        spinner=rootView.findViewById(R.id.spinner);
        spinnerLoc=rootView.findViewById(R.id.spinnerLoc);
        spinnerText=rootView.findViewById(R.id.spinnerText);
        txtLocation=rootView.findViewById(R.id.txtLocation);
        imgSearch=rootView.findViewById(R.id.imgSearch);
        imgBack = rootView.findViewById(R.id.imgBack);
        imgLocation.setOnClickListener(this);
        imgSearch.setOnClickListener(this);
        spinnerText.setOnClickListener(this);
        txtLocation.setOnClickListener(this);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        loader=new Loader(getContext());
        loader.show();
         category_id=getArguments().getString("category_id");
         String category_name=getArguments().getString("category_name"+lang);
         spinnerText.setText(category_name);

        stores =new ArrayList<>();
        catcategory_ids =new ArrayList<>();
        location =new ArrayList<>();
        location_ids =new ArrayList<>();
        try {
            jsonArray=new JSONArray(getArguments().getString("jsonArray"));
            for (int i=0;i<jsonArray.length();i++)
            {
                stores.add(jsonArray.getJSONObject(i).getString("category"+lang));
                catcategory_ids.add(jsonArray.getJSONObject(i).getString("category_id"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter<ArrayList<String>> aa = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,stores);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        //Setting the ArrayAdapter data on the Spinner
        spinner.setAdapter(aa);
        spinner.setEnabled(true);
        spinner.setOnItemSelectedEvenIfUnchangedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerText.setText(stores.get(i));
                category_id=catcategory_ids.get(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinnerLoc.setEnabled(true);
        spinnerLoc.setOnItemSelectedEvenIfUnchangedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                txtLocation.setText(location.get(i));
                location_id=location_ids.get(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        getStoreList(category_id,location_id);
        getLocation();
        imgStore.setOnClickListener(this);

        return rootView;
    }

    private void getLocation() {

        JSONObject object = new JSONObject();
        String Url= URLS.Companion.getGET_LOCATION();
        Log.v("uselocation",Url+" ");
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET,
                        Url ,
                        object,
                        response -> {
                            loader.dismiss();
                            try {
                               JSONArray jsonArray=response.getJSONArray("result");
                               for (int i=0;i<jsonArray.length();i++)
                               {
                                   location_ids.add(jsonArray.getJSONObject(i).getString("location_id"));
                                   location.add(jsonArray.getJSONObject(i).getString("location"+lang));
                               }
                                ArrayAdapter<ArrayList<String>> aaa = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,location);
                                aaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinnerLoc.setAdapter(aaa);
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

    public void getStoreList(String category_id, String location_id) {
        JSONObject object = new JSONObject();
        String Url= URLS.Companion.getSTORE_BY_LOCATION()+"userId="+ SharedPrefs.getKey(getActivity(),"userId")
                +"&category_id="+category_id+"&location_id="+location_id;
        Log.v("partnerurl",Url+" ");
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET,
                        Url ,
                        object,
                        response -> {
                            loader.dismiss();
                            try {
                                PostAdapter postAdapter = new PostAdapter(response.getJSONArray("result")
                                .getJSONObject(0).getJSONArray("stores"));
                                StaggeredGridLayoutManager layoutManager =
                                        new StaggeredGridLayoutManager(1, OrientationHelper.VERTICAL);
                                layoutManager.setGapStrategy(
                                        StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
                                recStore.setLayoutManager(layoutManager);
                                recStore.setItemAnimator(new DefaultItemAnimator());
                                if (postAdapter != null && postAdapter.getItemCount() > 0) {
                                    recStore.setVisibility(View.VISIBLE);
                                    recStore.setAdapter(postAdapter);
                                }
                                else {
                                    recStore.setVisibility(View.GONE);
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
        if (view.getId()==R.id.imgStore||view.getId() == R.id.spinnerText)
        {
            spinner.performClick();
        }
        else if (view.getId()==R.id.imgLocation || view.getId() == R.id.txtLocation)
        {
            spinnerLoc.performClick();
        }
        else if (view.getId()==R.id.imgSearch)
        {
            loader.show();
            getStoreList(category_id,location_id);
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
        public PostAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.store_item, parent, false);
            return new PostAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(PostAdapter.MyViewHolder holder, int position) {
            try {
                holder.txtStoreName.setText(jsonArray.getJSONObject(position).getString("store_name")+lang);
                Picasso.get().load(jsonArray.getJSONObject(position).getString("store_logo"))
                        .placeholder(R.drawable.ic_image_black_24dp)
                        .into(holder.imgLogo);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            return jsonArray.length();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txtStoreName;
            ImageView imgLogo;

            public MyViewHolder(View itemView) {
                super(itemView);


                txtStoreName=itemView.findViewById(R.id.txtStoreName);
                imgLogo=itemView.findViewById(R.id.imgLogo);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        try {
                            StoreOffer storeOffer=new StoreOffer();
                            Bundle args = new Bundle();
                            args.putString("storeId",jsonArray.getJSONObject(getAdapterPosition()).getString("store_id") );
                             storeOffer.setArguments(args);
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.container,storeOffer);
                            ft.addToBackStack(null);
                            ft.commit();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//                        ft.replace(R.id.container, new StoreOffer());
//                        ft.addToBackStack(null);
//                        ft.commit();
                    }
                });

            }
        }


    }

}
