package com.bezatnew.bezat.api


import com.google.gson.annotations.SerializedName

data class RegisterUserInfo(
    @SerializedName("email")
    val email: String,
    @SerializedName("otp")
    val otp: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("push_notification_status")
    val pushNotificationStatus: String,
    @SerializedName("user_code")
    val userCode: String,
    @SerializedName("user_name")
    val userName: String,
    @SerializedName("user_type")
    val userType: String
)