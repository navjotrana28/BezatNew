package com.bezatnew.bezat.models.vip_list_responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VipShopListDetails {

        @SerializedName("store_id")
        @Expose
        private String storeId;
        @SerializedName("store_name")
        @Expose
        private String storeName;
        @SerializedName("store_name_ar")
        @Expose
        private String storeNameAr;
        @SerializedName("store_logo")
        @Expose
        private String storeLogo;
        @SerializedName("store_image")
        @Expose
        private String storeImage;

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

        public String getStoreNameAr() {
            return storeNameAr;
        }

        public void setStoreNameAr(String storeNameAr) {
            this.storeNameAr = storeNameAr;
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

    }