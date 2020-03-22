package com.bezatnew.bezat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterRequestResponse {
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("error_msg")
    @Expose
    public String error_msg;
    @SerializedName("userID")
    @Expose
    public Integer userID;
    @SerializedName("userInfo")
    @Expose
    public UserInfo userInfo;

    public class UserInfo {
        @SerializedName("phone_code")
        @Expose
        public String phoneCode;
        @SerializedName("phone")
        @Expose
        public String phone;
        @SerializedName("otp")
        @Expose
        public Integer otp;
    }
}
