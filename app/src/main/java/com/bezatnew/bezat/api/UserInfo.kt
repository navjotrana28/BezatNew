package com.bezatnew.bezat.api


import com.google.gson.annotations.SerializedName

data class UserInfo(
    @SerializedName("address")
    val address: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("country_ar")
    val countryAr: String,
    @SerializedName("country_id")
    val countryId: String,
    @SerializedName("device_id")
    val deviceId: String,
    @SerializedName("dob")
    val dob: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("language_id")
    val languageId: String,
    @SerializedName("language_name")
    val languageName: String,
    @SerializedName("personalized_status")
    val personalizedStatus: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("phone_code")
    val phoneCode: String,
    @SerializedName("push_notification_status")
    val pushNotificationStatus: String,
    @SerializedName("user_code")
    val userCode: String,
    @SerializedName("user_name")
    val userName: String,
    @SerializedName("user_type")
    val userType: String
)