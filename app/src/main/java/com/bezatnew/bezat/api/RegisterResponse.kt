package com.bezatnew.bezat.api


import android.content.Context
import com.bezatnew.bezat.R
import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("userID")
    val userID: String?,
    @SerializedName("userInfo")
    val userInfo: RegisterUserInfo?,
    @SerializedName("error_msg") val errorMessage:String?
) {
    fun evaluate(context: Context,onSuccess:()->Unit,onFail:(String)->Unit) {
        when(status){
            "success"->
            {
                onSuccess()
            }
            else->{
                onFail(errorMessage?:context.getString(R.string.someting_wrong))
            }
        }
    }
}