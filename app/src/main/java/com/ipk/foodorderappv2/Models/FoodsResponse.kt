package com.ipk.foodorderappv2.Models

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class FoodsResponse(
    @SerializedName("yemekler")
    val yemekler: List<Yemekler>,
    @SerializedName("success")
    val success: Int
)