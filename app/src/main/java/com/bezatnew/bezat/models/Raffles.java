package com.bezatnew.bezat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Raffles {

    @SerializedName("storeName")
    @Expose
    public String storeName;
    @SerializedName("storeName_ar")
    @Expose
    private String storeNameAr;
    @SerializedName("store_logo")
    @Expose
    private String storeLogo;
    @SerializedName("booking_id")
    @Expose
    private String bookingId;
    @SerializedName("raffles")
    @Expose
    private String raffles;
    @SerializedName("coupons")
    @Expose
    private String coupons;
    @SerializedName("bill_no")
    @Expose
    private String billNo;
    @SerializedName("bill_date")
    @Expose
    private String billDate;
    @SerializedName("purchase_amount")
    @Expose
    private String purchaseAmount;
    @SerializedName("totalCoupons")
    @Expose
    private Integer totalCoupons;
    @SerializedName("created_date")
    @Expose
    private String createdDate;

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

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getRaffles() {
        return raffles;
    }

    public void setRaffles(String raffles) {
        this.raffles = raffles;
    }

    public String getCoupons() {
        return coupons;
    }

    public void setCoupons(String coupons) {
        this.coupons = coupons;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(String purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    public Integer getTotalCoupons() {
        return totalCoupons;
    }

    public void setTotalCoupons(Integer totalCoupons) {
        this.totalCoupons = totalCoupons;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

}
