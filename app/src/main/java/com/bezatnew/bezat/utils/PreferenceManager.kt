package com.bezatnew.bezat.utils

import android.app.Activity
import android.content.Context
import com.bezatnew.bezat.MyApp
import com.bezatnew.bezat.api.UserInfo
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import java.util.*

@Suppress("DEPRECATION")
class PreferenceManager {
private constructor()
    companion object {
        var singleton:PreferenceManager? = null
    val instance:PreferenceManager
    get(){
        if(singleton == null) singleton = PreferenceManager()
        return singleton!!
    }
    }
    var deviceId:String
    get() {
        var toReturn = getString(keyDeviceId, MyApp.MyApp?.applicationContext)
        if (toReturn == null)
            toReturn = FirebaseInstanceId.getInstance().token.toString()                        //UUID.randomUUID().toString()
        setString(keyDeviceId, toReturn, MyApp.MyApp?.applicationContext)
        return toReturn
    }
    set(value) {
        setString(keyDeviceId, value, MyApp.MyApp?.applicationContext)
    }
    fun logOut(context: Context?){
        context?.getSharedPreferences(preferenceKey,Activity.MODE_PRIVATE)?.edit()?.apply {
            remove(keyDeviceId)
            remove(keyUserInfo)
        }?.apply()
    }
    private val keyDeviceId="KEY_DEVICE_ID"
    private val keyUserInfo="USER_INFO"
    private val preferenceKey = "PREFERENCES"
    var userInfo: UserInfo?
        get() {
            var toReturn = getString(keyUserInfo, MyApp.MyApp?.applicationContext)
            return Gson().fromJson(toReturn,UserInfo::class.java)
        }
        set(value) {
            setString(keyUserInfo, Gson().toJson(value), MyApp.MyApp?.applicationContext)
        }

    fun getString(key:String, context: Context?):String?{
        return context?.getSharedPreferences(preferenceKey,Activity.MODE_PRIVATE)?.getString(key,null)
    }
    fun setString(key:String, value:String, context: Context?){
        context?.getSharedPreferences(preferenceKey,Activity.MODE_PRIVATE)?.edit()?.putString(key,value)?.apply()
    }
}