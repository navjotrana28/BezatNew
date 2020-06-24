package com.bezatnew.bezat.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;

import com.bezatnew.bezat.ClientRetrofit;
import com.bezatnew.bezat.InstantAutoComplete;
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
    private InstantAutoComplete suggestion_box;
    private SearchResponseResult searchResponseResult = new SearchResponseResult();
    private SearchResponseData responseData = new SearchResponseData();
    private SearchView searchView;
    private ArrayList<String> category = new ArrayList<>();
    ArrayList<String> store_id = new ArrayList<>();
    public String lang = "";

    public Feedback() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        container.setClickable(true);
        if (SharedPrefs.getKey(getActivity(), "selectedlanguage").contains("ar")) {
            getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            lang = "_ar";
        } else {
            getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            lang = "";

        }
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);
        suggestion_box = view.findViewById(R.id.suggestionBox);
        suggestion_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suggestion_box.showDropDown();
            }
        });
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
                        if (lang.equals("_ar")) {
                            category.add(searchResponseResult.getResult().get(i).getStores().get(j).getStoreNameAr());
                        } else {
                            category.add(searchResponseResult.getResult().get(i).getStores().get(j).getStoreName());
                        }
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
            if (suggestion_box.getEditableText().toString().matches("")) {
                ContactUsDialog contactUsDialog = new ContactUsDialog(getString(R.string.please_select_retailer));
                contactUsDialog.show(getFragmentManager(), "ContactUs Dialog");
            } else if (text.getText().toString().matches("")) {
                ContactUsDialog contactUsDialog = new ContactUsDialog(getString(R.string.please_enter_feedback));
                contactUsDialog.show(getFragmentManager(), "ContactUs Dialog");
            } else if (ratingBar.getRating() == 0) {
                ContactUsDialog contactUsDialog = new ContactUsDialog(getString(R.string.please_rate_your_star));
                contactUsDialog.show(getFragmentManager(), "ContactUs Dialog");
            } else {
                FeedbackRequest request = new FeedbackRequest();
                request.setFeedback(text.getText().toString());
                request.setRatings(String.valueOf(ratingBar.getRating()));
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
        if (lang.equals("_ar")) {
            imgBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_back_rtl));
        }
        imgBack.setOnClickListener(view1 -> getActivity().onBackPressed());
    }

    private void feedbackToServer(FeedbackRequest request) {
        ClientRetrofit clientRetrofit = new ClientRetrofit();
        clientRetrofit.feedBackRequestApi(request, new FeedbackCallback() {
            @Override
            public void onSuccess(FeedbackResponse response) {
                ContactUsDialog contactUsDialog = new ContactUsDialog(getString(R.string.your_feedback_has_been_sent_successfully));
                contactUsDialog.show(getFragmentManager(), "ContactUs Dialog");
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
        button = view.findViewById(R.id.send_button);
    }

}
