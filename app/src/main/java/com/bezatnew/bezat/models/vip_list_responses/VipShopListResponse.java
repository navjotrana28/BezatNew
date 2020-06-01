package com.bezatnew.bezat.models.vip_list_responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VipShopListResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("result")
    @Expose
    private List<VipShopListDetails> result = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<VipShopListDetails> getResult() {
        return result;
    }

    public void setResult(List<VipShopListDetails> result) {
        this.result = result;
    }
}
