package com.bezatnew.bezat.models.searchRetailerResponses;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResponseData {

    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("category_ar")
    @Expose
    private String categoryAr;
    @SerializedName("category_image")
    @Expose
    private String categoryImage;
    @SerializedName("stores")
    @Expose
    private List<SearchRetailerStore> stores = null;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryAr() {
        return categoryAr;
    }

    public void setCategoryAr(String categoryAr) {
        this.categoryAr = categoryAr;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public List<SearchRetailerStore> getStores() {
        return stores;
    }

    public void setStores(List<SearchRetailerStore> stores) {
        this.stores = stores;
    }

}
