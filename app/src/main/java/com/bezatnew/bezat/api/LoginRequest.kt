package com.bezatnew.bezat.api


import com.bezatnew.bezat.activities.LoginActivity
import com.bezatnew.bezat.activities.RegistrationActivity
import com.bezatnew.bezat.utils.Loader
import com.bezatnew.bezat.utils.PreferenceManager
import com.bezatnew.bezat.utils.URLS
import com.github.kittinunf.fuel.httpPost
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("device_id")
    val deviceId: String = PreferenceManager.instance.deviceId,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    val os: String
) {
    fun generate() = Gson().toJson(this)
    fun login(context: LoginActivity, onsuccess: (LoginResponse) -> Unit, onError: () -> Unit) {
        val loader = Loader(context)
        loader.show()
        URLS.LOGIN_URL.httpPost(
            listOf(
                "device_id" to deviceId,
                "email" to email,
                "password" to password,
                "os" to os
            )
        ).responseObject(CommonDeserializer(LoginResponse::class.java)) { _, _, result ->
            if (loader.isShowing)
                loader.dismiss()
            val (response, error) = result
            if (error == null && response != null) {

                onsuccess(response)
            } else {
                onError()
            }
        }
    }

    fun loginReg(context: RegistrationActivity, onsuccess: (LoginResponse) -> Unit, onError: () -> Unit) {
        val loader = Loader(context)
        loader.show()
        URLS.LOGIN_URL.httpPost(
            listOf(
                "device_id" to deviceId,
                "email" to email,
                "password" to password,
                "os" to os
            )
        ).responseObject(CommonDeserializer(LoginResponse::class.java)) { _, _, result ->
            if (loader.isShowing)
                loader.dismiss()
            val (response, error) = result
            if (error == null && response != null) {

                onsuccess(response)
            } else {
                onError()
            }
        }
    }

}