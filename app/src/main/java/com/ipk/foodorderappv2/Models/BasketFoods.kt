package com.ipk.foodorderappv2.Models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "sepet_yemekler"
)
data class BasketFoods(
    @PrimaryKey(autoGenerate = true)
    var basket_id:Int? = null,
    val yemek_adi: String,
    val yemek_fiyat: String,
    val yemek_id: String,
    val yemek_resim_adi: String,
    val yemek_siparis_adet: String
)