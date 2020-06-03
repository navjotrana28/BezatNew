package com.bezatnew.bezat.fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bezatnew.bezat.ClientRetrofit;
import com.bezatnew.bezat.R;
import com.bezatnew.bezat.adapter.FavOffersAdapter;
import com.bezatnew.bezat.interfaces.CategoryId;
import com.bezatnew.bezat.interfaces.FavOffersListInterface;
import com.bezatnew.bezat.models.fav_offers_responses.FavOffersDetails;
import com.bezatnew.bezat.models.fav_offers_responses.FavOffersResponse;
import com.bezatnew.bezat.utils.Loader;
import com.bezatnew.bezat.utils.SharedPrefs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FavOffersList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FavOffersList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavOffersList extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ImageView imgBack;
    View rootView;
    Loader loader;
    String currentDate = "";
    private FavOffersAdapter verticalAdapter;
    LinearLayoutManager layoutManager;
    private RecyclerView recyclerViewVertical;
    public String lang = "";
    private SearchView searchView;
    private List<FavOffersDetails> retailerData;


    private OnFragmentInteractionListener mListener;

    public FavOffersList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VIPOffer.
     */
    // TODO: Rename and change types and number of parameters
    public static FavOffersList newInstance(String param1, String param2) {
        FavOffersList fragment = new FavOffersList();
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

        if (SharedPrefs.getKey(getActivity(), "selectedlanguage").contains("ar")) {
            getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            lang = "_ar";
        } else {
            getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            lang = "";
        }
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_fav_offers_lists, container, false);
        imgBack = rootView.findViewById(R.id.img_back);
        searchView = rootView.findViewById(R.id.search_view);
        recyclerViewVertical = rootView.findViewById(R.id.recyclerView_vertical);
        loader = new Loader(getContext());
        loader.show();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        currentDate = formatter.format(date);
        getVipOffer();
        initSearchView();
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = searchView.findViewById(id);
        textView.setHintTextColor(Color.rgb(105, 105, 105));
        textView.setTextColor(Color.rgb(105, 105, 105));
        textView.setGravity(Gravity.CENTER);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        return rootView;
    }

    private void getVipOffer() {
        ClientRetrofit clientRetrofit = new ClientRetrofit();
        clientRetrofit.getFavOffersList(SharedPrefs.getKey(getActivity(), "userId"), currentDate,
                new FavOffersListInterface() {
                    @Override
                    public void onSuccess(FavOffersResponse responseResult) {
                        retailerData = responseResult.getResult();
                        verticalAdapter = new FavOffersAdapter(getActivity(), responseResult.getResult(), new CategoryId() {
                            @Override
                            public void onSuccess(String categoryId) {
                                FavouriteOffer vipOffer = new FavouriteOffer();
                                Bundle args = new Bundle();
                                args.putString("storeId", categoryId);
                                vipOffer.setArguments(args);
                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.container, vipOffer);
                                ft.addToBackStack(null);
                                ft.commit();
                            }
                        });
                        layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                        recyclerViewVertical.setLayoutManager(layoutManager);
                        recyclerViewVertical.setAdapter(verticalAdapter);
                        loader.dismiss();
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void getAfterQuery(List<FavOffersDetails> searchableData, String query) {
        CompositeDisposable disposable = new CompositeDisposable();
        Observable.just(searchableData)
                .flatMap(Observable::fromIterable)
                .filter(favOffersDetails -> (favOffersDetails.getStoreName() + lang).toLowerCase().contains(query.toLowerCase()))
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<FavOffersDetails>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(List<FavOffersDetails> vipShopListDetails) {
                        verticalAdapter.setSearchList(vipShopListDetails);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });

    }


    private void initSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getAfterQuery(retailerData, newText);
                return false;
            }
        });
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
}
