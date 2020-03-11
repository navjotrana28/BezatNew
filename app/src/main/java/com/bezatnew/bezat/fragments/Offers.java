package com.bezatnew.bezat.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.bezatnew.bezat.R;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Offers.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Offers#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Offers extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView recOffer;
    ImageView imgBack;

    private OnFragmentInteractionListener mListener;
    View rootView;
    public Offers() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Offers.
     */
    // TODO: Rename and change types and number of parameters
    public static Offers newInstance(String param1, String param2) {
        Offers fragment = new Offers();
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
        rootView=inflater.inflate(R.layout.fragment_offers, container, false);
        recOffer=rootView.findViewById(R.id.recOffer);
        imgBack=rootView.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        String response=getArguments().getString("storeArray");
        try {
            JSONArray jsonArray=new JSONArray(response);
            PostAdapter postAdapter = new PostAdapter(jsonArray);
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
                    .inflate(R.layout.offers_item, parent, false);
            return new PostAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(PostAdapter.MyViewHolder holder, int position) {
            try {
                holder.offer_descp.setText(jsonArray.getJSONObject(position).getString("offer_descp"));
                holder.discount_price.setText("BD "+jsonArray.getJSONObject(position).getString("discount_price"));
                holder.actual_price.setText("Origanal Price:BD "+jsonArray.getJSONObject(position).getString("actual_price"));
                Picasso.get().load(jsonArray.getJSONObject(position).getString("offer_img"))
                        .into(holder.offer_img);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        @Override
        public int getItemCount() {
            return jsonArray.length();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView offer_descp,discount_price,actual_price;
            ImageView offer_img;

            public MyViewHolder(View itemView) {
                super(itemView);


                offer_descp=itemView.findViewById(R.id.offer_descp);
                discount_price=itemView.findViewById(R.id.discount_price);
                actual_price=itemView.findViewById(R.id.actual_price);
                offer_img=itemView.findViewById(R.id.offer_img);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            OfferDetails offerDetails=new OfferDetails();
                            Bundle args = new Bundle();
                            args.putString("offerId",jsonArray.getJSONObject(getAdapterPosition()).getString("offer_id") );
                            offerDetails.setArguments(args);
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.container,offerDetails);
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
