package com.bezatnew.bezat.api

import com.bezatnew.bezat.R
import com.bezatnew.bezat.activities.LoginActivity
import com.bezatnew.bezat.utils.PreferenceManager
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("userID")
    val userID: String?,
    @SerializedName("userInfo")
    val userInfo: UserInfo?,
    @SerializedName("error_msg")
    val errorMessage: String?
){
   fun  handleLogin(context: LoginActivity, onSuccess:()->Unit, onError:(errorMessage:String)->Unit){
       when(status){
           "failed"-> {
               onError(errorMessage ?: context.getString(R.string.someting_wrong))
           }
           "successful"->{
               PreferenceManager.instance.userInfo = userInfo
               if(userInfo?.deviceId!= null)
               PreferenceManager.instance.deviceId = userInfo.deviceId
               onSuccess()
           }
           "pending"->{
               //TODO()
               //context.finish()
               onError("This is yet to implement. This is mostly the OTP case!!")
           }
           "inactive"->{
               onError(errorMessage?:context.getString(R.string.someting_wrong))
           }
       }
    }
}