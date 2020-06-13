package com.bezatnew.bezat.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bezatnew.bezat.ClientRetrofit;
import com.bezatnew.bezat.R;
import com.bezatnew.bezat.adapter.SearchAdapter;
import com.bezatnew.bezat.adapter.SearchVerticalAdapter;
import com.bezatnew.bezat.interfaces.CategoryId;
import com.bezatnew.bezat.interfaces.SearchRetaierInterface;
import com.bezatnew.bezat.interfaces.SearchRetailerCallback;
import com.bezatnew.bezat.models.searchRetailerResponses.SearchResponseData;
import com.bezatnew.bezat.models.searchRetailerResponses.SearchResponseResult;
import com.bezatnew.bezat.models.searchRetailerResponses.SearchRetailerStore;
import com.bezatnew.bezat.utils.SharedPrefs;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchRetailer extends Fragment {
    SearchAdapter adapter;
    LinearLayoutManager layoutManager;
    ImageView imgBack;
    private RecyclerView recyclerViewHorizontal, recyclerViewVertical;
    private SearchVerticalAdapter verticalAdapter;
    private View progressBar;
    String categoryId = "";
    private SearchResponseResult searchResponseResult = new SearchResponseResult();
    private SearchResponseData responseData = new SearchResponseData();
    private SearchView searchView;
    private List<SearchRetailerStore> retailerData;
    public String lang = "";

    public SearchRetailer() {
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.fragment_search_retailer, container, false);
        recyclerViewHorizontal = view.findViewById(R.id.recyclerView_horizontal);
        recyclerViewVertical = view.findViewById(R.id.recyclerView_vertical);
        progressBar = view.findViewById(R.id.progress_bar_search);
        imgBack = view.findViewById(R.id.img_back);
        if(lang.equals("_ar")){
            imgBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back_rtl));
        }
        searchView = view.findViewById(R.id.search_view);
        setUpRecyclerView();
        setUpRecyclerViewVertical();
        loadSeachData();
        onCLickBackButton();
            initSearchView();
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = searchView.findViewById(id);
        textView.setHintTextColor(Color.rgb(105, 105, 105));
        textView.setTextColor(Color.rgb(105, 105, 105));
        textView.setGravity(Gravity.CENTER);
//        int searchIconId = searchView.getContext().getResources().getIdentifier("android:id/search_button",null, null);
//        ImageView searchIcon = (ImageView) searchView.findViewById(searchIconId);
//        searchIcon.setImageResource(R.drawable.ic_search_black_24dp);

        return view;
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

    private void onCLickBackButton() {
        imgBack.setOnClickListener(v -> getActivity().onBackPressed());
    }

    private void setUpRecyclerViewVertical() {
        verticalAdapter = new SearchVerticalAdapter(getActivity(), responseData.getStores(), new CategoryId() {
            @Override
            public void onSuccess(String categoryId) {
                try {
                    StoreOffer storeOffer = new StoreOffer();
                    Bundle args = new Bundle();
                    args.putString("storeId", categoryId);
                    storeOffer.setArguments(args);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.container, storeOffer);
                    ft.addToBackStack(null);
                    ft.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerViewVertical.setLayoutManager(layoutManager);
        recyclerViewVertical.setAdapter(verticalAdapter);
    }

    private void loadSeachData() {
        ClientRetrofit clientRetrofit = new ClientRetrofit();
        clientRetrofit.SearchRetailerResult(new SearchRetaierInterface() {
            @Override
            public void onSuccess(SearchResponseResult responseResult) {
                retailerData = responseResult.getResult().get(0).getStores();
                searchResponseResult = responseResult;
                adapter.setDatumList(responseResult);
                adapter.notifyDataSetChanged();
                verticalAdapter.setDatumList(responseResult.getResult().get(0).getStores());
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });
    }

    private void setUpRecyclerView() {
        adapter = new SearchAdapter(getActivity(), searchResponseResult, new SearchRetailerCallback() {
            @Override
            public void onClickHorizonView(int pos) {
                retailerData = searchResponseResult.getResult().get(pos).getStores();
                verticalAdapter.setDatumList(searchResponseResult.getResult().get(pos).getStores());
            }
        });
        layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        recyclerViewHorizontal.setLayoutManager(layoutManager);
        recyclerViewHorizontal.setAdapter(adapter);
    }

    public void getAfterQuery(List<SearchRetailerStore> searchableData, String query) {
        CompositeDisposable disposable = new CompositeDisposable();
        Observable.just(searchableData)
                .flatMap(Observable::fromIterable)
                .filter(searchRetailerStore -> (searchRetailerStore.getStoreName() + lang).toLowerCase().contains(query.toLowerCase()))
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<SearchRetailerStore>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(List<SearchRetailerStore> searchRetailerStores) {
                        verticalAdapter.setDatumList(searchRetailerStores);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });

    }
}
