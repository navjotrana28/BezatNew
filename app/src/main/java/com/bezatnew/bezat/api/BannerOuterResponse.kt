package com.bezatnew.bezat.api


import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class BannerOuterResponse(
    @SerializedName("result")
    val result: List<Result>
)
{
    class Deserializer:ResponseDeserializable<BannerOuterResponse>{
        override fun deserialize(content: String): BannerOuterResponse? {
            return Gson().fromJson(content,BannerOuterResponse::class.java)
        }
    }
}