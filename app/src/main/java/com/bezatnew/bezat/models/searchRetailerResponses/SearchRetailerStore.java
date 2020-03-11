package com.bezatnew.bezat.models.searchRetailerResponses;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchRetailerStore {

    @SerializedName("store_id")
    @Expose
    private String storeId;
    @SerializedName("store_name")
    @Expose
    private String storeName;
    @SerializedName("store_logo")
    @Expose
    private String storeLogo;
    @SerializedName("store_image")
    @Expose
    private String storeImage;
    @SerializedName("store_banner")
    @Expose
    private String storeBanner;
    @SerializedName("store_name_ar")
    @Expose
    private String storeNameAr;

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
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

    public String getStoreImage() {
        return storeImage;
    }

    public void setStoreImage(String storeImage) {
        this.storeImage = storeImage;
    }

    public String getStoreBanner() {
        return storeBanner;
    }

    public void setStoreBanner(String storeBanner) {
        this.storeBanner = storeBanner;
    }

    public String getStoreNameAr() {
        return storeNameAr;
    }

    public void setStoreNameAr(String storeNameAr) {
        this.storeNameAr = storeNameAr;
    }

}
