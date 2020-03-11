package com.bezatnew.bezat.api

import android.os.Looper
import android.util.Log
import com.bezatnew.bezat.utils.URLS
import com.github.kittinunf.fuel.httpGet

class BannerOuterRequest {
    fun request(onSuccess:(BannerOuterResponse)->Unit,onError:()->Unit, url:String? = null){
        (url?:URLS.OUTER_URL).httpGet().responseObject(BannerOuterResponse.Deserializer()){
            _,_,result->
            val (response,error) = result
            if(error == null && response!=null) {
                Log.d("BannerOutput",response.toString())
                android.os.Handler(Looper.getMainLooper()).post {
                    onSuccess(response)
                }
            }
            else {
                Log.d("BannerOutput",error?.message)
                android.os.Handler(Looper.getMainLooper()).post {
                    onError
                }
            }
        }
    }
}