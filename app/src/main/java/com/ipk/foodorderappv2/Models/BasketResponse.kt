package com.ipk.foodorderappv2.Models

import com.google.gson.annotations.SerializedName

data class BasketResponse(
    @SerializedName("sepet_yemekler")
    val basketFoods: List<BasketFoods>,
    @SerializedName("success")
    val success: Int
)