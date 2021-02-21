package com.ipk.foodorderappv2.Repository

import com.ipk.foodorderappv2.Api.RetrofitInstance
import com.ipk.foodorderappv2.Db.BasketDatabase

class BasketRepository(val db: BasketDatabase) {
    suspend fun getBasket()= RetrofitInstance.api.apiGetBasket()

}