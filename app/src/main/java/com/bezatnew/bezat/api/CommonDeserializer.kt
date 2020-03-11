package com.bezatnew.bezat.api

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

class CommonDeserializer<T:Any>(val mClass:Class<T>):ResponseDeserializable<T> {
    override fun deserialize(content: String): T{
        return Gson().fromJson(content, mClass)
    }
}