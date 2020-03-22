package com.bezatnew.bezat.api


import android.util.Log
import com.bezatnew.bezat.R
import com.bezatnew.bezat.activities.RegistrationActivity
import com.bezatnew.bezat.utils.Loader
import com.bezatnew.bezat.utils.URLS
import com.bezatnew.bezat.utils.toList
import com.github.kittinunf.fuel.httpPost
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("device_id")
    val deviceId: String,
    @SerializedName("dob")
    val dob: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("gender")
    val gender: String,
//    @SerializedName("mobile_code")
//    val mobileCode: String,
    @SerializedName("name")
    val name: String,
//    @SerializedName("password")
//    val password: String,
//    @SerializedName("phone")
//    val phone: String,
    @SerializedName("userID")
    val userID: String
){
    fun generate() = Gson().toJson(this)
    fun register(context:RegistrationActivity, onSuccess: (RegisterResponse)->Unit,onError:(String)->Unit){
        val dialog = Loader(context)
        dialog.show()
        URLS.COMPLETEREGISTER_URL.httpPost(generate().toList()).responseObject(CommonDeserializer(RegisterResponse::class.java)){_,_,result->
                dialog.dismiss()
            val(response, error) = result
            if(error == null) {
                if (response != null && response.status == "success") {
                    Log.d("RegisterResponse", response.toString())
                    onSuccess(response)
                }
                else
                {
                    dialog.dismiss()
                    onError(response?.errorMessage?:context.getString(R.string.someting_wrong))
                }
            }
            else {
                dialog.dismiss()
                onError(context.getString(R.string.someting_wrong))
                error.printStackTrace()
            }
        }
    }
}