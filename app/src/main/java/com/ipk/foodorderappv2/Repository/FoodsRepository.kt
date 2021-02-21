package com.ipk.foodorderappv2.Repository

import com.ipk.foodorderappv2.Api.RetrofitInstance
import com.ipk.foodorderappv2.Db.FoodsDatabase

class FoodsRepository(val db:FoodsDatabase) {
    suspend fun getFoods()=
            RetrofitInstance.api.apiGetFoods()

    suspend fun searchedFoods(foodName:String)=
            RetrofitInstance.api.apiGetSearchedFoods(foodName)

}