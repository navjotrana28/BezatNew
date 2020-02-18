package com.bezat.bezat.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bezat.bezat.ClientRetrofit;
import com.bezat.bezat.R;
import com.bezat.bezat.adapter.SearchAdapter;
import com.bezat.bezat.adapter.SearchVerticalAdapter;
import com.bezat.bezat.interfaces.CategoryId;
import com.bezat.bezat.interfaces.SearchRetaierInterface;
import com.bezat.bezat.interfaces.SearchRetailerCallback;
import com.bezat.bezat.models.searchRetailerResponses.SearchResponseData;
import com.bezat.bezat.models.searchRetailerResponses.SearchResponseResult;
import org.json.JSONException;

import java.util.List;

import io.reactivex.Observable;


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

    public SearchRetailer() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_retailer, container, false);
        recyclerViewHorizontal = view.findViewById(R.id.recyclerView_horizontal);
        recyclerViewVertical = view.findViewById(R.id.recyclerView_vertical);
        progressBar = view.findViewById(R.id.progress_bar_search);
        imgBack = view.findViewById(R.id.img_back);
        searchView = view.findViewById(R.id.search_view);
        setUpRecyclerView();
        setUpRecyclerViewVertical();
        loadSeachData();
        onCLickBackButton();
        initSearchView();
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
                return false;
            }
        });
    }

    private void onCLickBackButton() {
        imgBack.setOnClickListener(v -> getActivity().onBackPressed());
    }

    private void setUpRecyclerViewVertical() {
        verticalAdapter = new SearchVerticalAdapter(getActivity(), responseData, new CategoryId() {
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
                searchResponseResult = responseResult;
                adapter.setDatumList(responseResult);
                adapter.notifyDataSetChanged();
                verticalAdapter.setDatumList(responseResult.getResult().get(0));
                verticalAdapter.notifyDataSetChanged();
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
                verticalAdapter.setDatumList(searchResponseResult.getResult().get(pos));
                verticalAdapter.notifyDataSetChanged();
            }
        });
        layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        recyclerViewHorizontal.setLayoutManager(layoutManager);
        recyclerViewHorizontal.setAdapter(adapter);
    }

//    private Observable<List<SearchResponseData>> getAfterQuery(List<SearchResponseData> searchableData, String query) {
//        return Observable.just(searchableData)
//                .
//    }
}
