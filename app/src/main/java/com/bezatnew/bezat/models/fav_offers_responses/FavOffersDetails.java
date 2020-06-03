package com.bezatnew.bezat.models.fav_offers_responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FavOffersDetails {

    @SerializedName("store_id")
    @Expose
    private String storeId;
    @SerializedName("store_image")
    @Expose
    private String storeImage;
    @SerializedName("store_name_ar")
    @Expose
    private String storeNameAr;
    @SerializedName("store_name")
    @Expose
    private String storeName;
    @SerializedName("store_logo")
    @Expose
    private String storeLogo;

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreImage() {
        return storeImage;
    }

    public void setStoreImage(String storeImage) {
        this.storeImage = storeImage;
    }

    public String getStoreNameAr() {
        return storeNameAr;
    }

    public void setStoreNameAr(String storeNameAr) {
        this.storeNameAr = storeNameAr;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreLogo() {
        return storeLogo;
    }

    public void setStoreLogo(String storeLogo) {
        this.storeLogo = storeLogo;
    }

}