package com.ipk.foodorderappv2.Models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(
        tableName = "yemekler"
)
data class Yemekler(
    @PrimaryKey(autoGenerate = true)
    var food_id:Int?=null,
    @SerializedName("yemek_adi")
    val yemek_adi: String,
    @SerializedName("yemek_fiyat")
    val yemek_fiyat: String,
    @SerializedName("yemek_id")
    val yemek_id: String,
    @SerializedName("yemek_resim_adi")
    val yemek_resim_adi: String
)