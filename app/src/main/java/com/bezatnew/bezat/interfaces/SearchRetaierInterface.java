package com.bezatnew.bezat.interfaces;


import com.bezatnew.bezat.models.searchRetailerResponses.SearchResponseResult;

public interface SearchRetaierInterface {

    void onSuccess(SearchResponseResult responseResult);

    void onFailure(Throwable e);

}
