package com.bezat.bezat.interfaces;


import com.bezat.bezat.models.searchRetailerResponses.SearchResponseResult;

public interface SearchRetaierInterface {

    void onSuccess(SearchResponseResult responseResult);

    void onFailure(Throwable e);

}
