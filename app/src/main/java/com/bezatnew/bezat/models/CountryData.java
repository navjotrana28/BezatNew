package com.bezatnew.bezat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CountryData {


    @SerializedName("result")
    @Expose
    private List<CountryResult> result = null;

    public List<CountryResult> getResult() {
        return result;
    }

    public void setResult(List<CountryResult> result) {
        this.result = result;
    }
}
