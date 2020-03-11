package com.bezatnew.bezat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CouponData {

    @SerializedName("raffles")
    @Expose
    private List<Raffles> raffles = null;

    public List<Raffles> getRaffles() {
        return raffles;
    }

    public void setRaffles(List<Raffles> raffles) {
        this.raffles = raffles;
    }

}
