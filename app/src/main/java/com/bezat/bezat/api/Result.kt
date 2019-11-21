package com.bezat.bezat.api

import com.google.gson.annotations.SerializedName


data class Result(
    @SerializedName("banner")
    val banner: String
)