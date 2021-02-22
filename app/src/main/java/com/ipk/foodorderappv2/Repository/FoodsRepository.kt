package com.ipk.foodorderappv2.Repository

import com.ipk.foodorderappv2.Api.RetrofitInstance
import com.ipk.foodorderappv2.Db.FoodsDatabase
import com.ipk.foodorderappv2.Models.Foods

class FoodsRepository(val db:FoodsDatabase) {
    suspend fun getFoods()=
            RetrofitInstance.api.apiGetFoods()

    suspend fun searchedFoods(foodName:String)=
            RetrofitInstance.api.apiGetSearchedFoods(foodName)

    suspend fun postInsert(yemek_id:String,
                           yemek_adi: String,
                           yemek_resim_adi:String,
                           yemek_fiyat:String,
                           yemek_siparis_adet:String)=
            RetrofitInstance.api.apiAddToBasket(yemek_id,yemek_adi,
                    yemek_resim_adi,yemek_fiyat,yemek_siparis_adet)

    suspend fun upsert(foods:Foods)=db.getFoodsDao().upsert(foods)

    fun getSavedFoods()= db.getFoodsDao().getAllFoods()

    suspend fun deleteFood(foods: Foods)=db.getFoodsDao().deleteFood(foods)

}