package com.bezat.bezat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CouponResult {


    @SerializedName("result")
    @Expose
    private CouponData result;

    public CouponData getResult() {
        return result;
    }

    public void setResult(CouponData result) {
        this.result = result;
    }

}
