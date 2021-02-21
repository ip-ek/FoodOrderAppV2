package com.ipk.foodorderappv2.Models

data class BasketResponse(
    val basketFoods: List<BasketFoods>,
    val success: Int
)