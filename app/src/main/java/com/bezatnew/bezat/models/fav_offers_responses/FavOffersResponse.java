package com.bezatnew.bezat.models.fav_offers_responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FavOffersResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("result")
    @Expose
    private List<FavOffersDetails> result = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<FavOffersDetails> getResult() {
        return result;
    }

    public void setResult(List<FavOffersDetails> result) {
        this.result = result;
    }
}
