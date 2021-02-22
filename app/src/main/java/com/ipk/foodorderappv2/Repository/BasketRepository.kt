package com.ipk.foodorderappv2.Repository

import com.ipk.foodorderappv2.Api.RetrofitInstance
import com.ipk.foodorderappv2.Db.BasketDatabase
import com.ipk.foodorderappv2.Models.BasketFoods
import retrofit2.http.Field

class BasketRepository(val db: BasketDatabase) {
    suspend fun getBasket()= RetrofitInstance.api.apiGetBasket()
    suspend fun postDelete(id:String)= RetrofitInstance.api.apiDeleteFromBasket(id)

    suspend fun upsert(basketFoods: BasketFoods)=db.getBasketDao().upsert(basketFoods)

    fun getSavedBasket()= db.getBasketDao().getAllBasket()

    suspend fun deleteFood(basketFoods: BasketFoods)=db.getBasketDao().deleteBasketFood(basketFoods)

}