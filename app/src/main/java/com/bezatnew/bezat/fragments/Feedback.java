package com.bezatnew.bezat.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.fragment.app.Fragment;

import com.bezatnew.bezat.ClientRetrofit;
import com.bezatnew.bezat.R;
import com.bezatnew.bezat.api.feedbackResponse.FeedbackRequest;
import com.bezatnew.bezat.api.feedbackResponse.FeedbackResponse;
import com.bezatnew.bezat.interfaces.FeedbackCallback;
import com.bezatnew.bezat.interfaces.SearchRetaierInterface;
import com.bezatnew.bezat.models.searchRetailerResponses.SearchResponseData;
import com.bezatnew.bezat.models.searchRetailerResponses.SearchResponseResult;
import com.bezatnew.bezat.utils.SharedPrefs;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class Feedback extends Fragment {
    EditText text;
    private RatingBar ratingBar;
    Button button;
    ImageView imgBack;
    private AutoCompleteTextView suggestion_box;
    private SearchResponseResult searchResponseResult = new SearchResponseResult();
    private SearchResponseData responseData = new SearchResponseData();
    private SearchView searchView;
    private ArrayList<String> category = new ArrayList<>();
    ArrayList<String> store_id = new ArrayList<>();
    private String lang = "";

    public Feedback() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (SharedPrefs.getKey(getActivity(),"selectedlanguage").contains("ar")) {
            getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            lang="_ar";
        } else {
            getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            lang="";
        }
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);
        suggestion_box = view.findViewById(R.id.suggestionBox);
        apiCallForSuggestionViewData();
        addViews(view);
        onCLickSendBtn();
        onClickBackButton(view);

        return view;
    }

    private void apiCallForSuggestionViewData() {
        ClientRetrofit clientRetrofit = new ClientRetrofit();
        clientRetrofit.SearchRetailerResult(new SearchRetaierInterface() {
            @Override
            public void onSuccess(SearchResponseResult responseResult) {
                searchResponseResult = responseResult;
                for (int i = 0; i < searchResponseResult.getResult().size(); i++) {
                    for (int j = 0; j < searchResponseResult.getResult().get(i).getStores().size(); j++) {
                        category.add(searchResponseResult.getResult().get(i).getStores().get(j).getStoreName());
                        store_id.add(searchResponseResult.getResult().get(i).getStores().get(j).getStoreId());
                    }
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()),
                        android.R.layout.simple_spinner_dropdown_item, category);
                suggestion_box.setAdapter(arrayAdapter);
            }

            @Override
            public void onFailure(Throwable e) {
            }
        });
    }

    private void onCLickSendBtn() {
        button.setOnClickListener(v -> {
            if (text.getText().toString().matches("")){
                Toast.makeText(getActivity(), "fill the form first!", Toast.LENGTH_SHORT).show();
            }
            else {
                FeedbackRequest request = new FeedbackRequest();
                request.setFeedback(text.getText().toString());
                request.setRatings(String.valueOf(ratingBar.getNumStars()));
                request.setUserId(SharedPrefs.getKey(getActivity(), "userId"));
                request.setRetailerId(getRetailerId());
                feedbackToServer(request);
            }
        });
    }

    private String getRetailerId() {
        for (int i = 0; i < category.size(); i++) {
            if (suggestion_box.getEditableText().toString().equals(category.get(i))) {
                return store_id.get(i);
            }
        }
        return null;
    }

    private void onClickBackButton(View view) {
        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(view1 -> getActivity().onBackPressed());
    }

    private void feedbackToServer(FeedbackRequest request) {
        ClientRetrofit clientRetrofit = new ClientRetrofit();
        clientRetrofit.feedBackRequestApi(request, new FeedbackCallback() {
            @Override
            public void onSuccess(FeedbackResponse response) {
                Toast.makeText(getContext(), response.getStatus()+"! Your feebback has been sent sucessfully", Toast.LENGTH_LONG).show();
                getActivity().onBackPressed();
            }

            @Override
            public void onFailure(Throwable e) {

            }
        });

    }

    private void addViews(View view) {
        text = view.findViewById(R.id.edit_text);
        ratingBar = view.findViewById(R.id.rating_bar);
//        ratingBar.setRating(5);
        button = view.findViewById(R.id.send_button);

    }

}
