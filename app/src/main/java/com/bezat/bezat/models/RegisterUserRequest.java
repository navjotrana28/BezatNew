package com.bezat.bezat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterUserRequest {
    @SerializedName("password")
    @Expose
    public String password;

    @SerializedName("mobile_code")
    @Expose
    public String mobile_code;

    @SerializedName("phone")
    @Expose
    public String phone;
}
