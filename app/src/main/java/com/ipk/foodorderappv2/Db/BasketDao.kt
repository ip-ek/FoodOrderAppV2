package com.ipk.foodorderappv2.Db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ipk.foodorderappv2.Models.BasketFoods

@Dao
interface BasketDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(basketfoods: BasketFoods): Long //id returns

    @Query("SELECT * FROM sepet_yemekler")
    //there is no data change so no need susend desc - lifedata
    fun getAllBasket(): LiveData<List<BasketFoods>>

    @Delete
    suspend fun deleteBasketFood(basketFoods: BasketFoods)
}