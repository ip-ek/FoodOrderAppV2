package com.ipk.foodorderappv2.Models

import com.google.gson.annotations.SerializedName

data class FoodsResponse(
    @SerializedName("yemekler")
    val foods: List<Foods>,
    @SerializedName("success")
    val success: Int
)