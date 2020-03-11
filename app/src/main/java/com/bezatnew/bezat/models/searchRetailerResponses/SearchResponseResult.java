package com.bezatnew.bezat.models.searchRetailerResponses;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResponseResult {

    @SerializedName("result")
    @Expose
    private List<SearchResponseData> result = null;

    public List<SearchResponseData> getResult() {
        return result;
    }

    public void setResult(List<SearchResponseData> result) {
        this.result = result;
    }

}
