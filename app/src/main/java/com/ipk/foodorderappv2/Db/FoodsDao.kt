package com.ipk.foodorderappv2.Db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ipk.foodorderappv2.Models.Yemekler

@Dao
interface FoodsDao {
    //başta çekip kaydederken lazım
    @Insert(onConflict = OnConflictStrategy.REPLACE) // already exist
    suspend fun upsert(foods: Yemekler): Long //id returns

    @Query("SELECT * FROM yemekler")
    //livedata changes
    fun getAllFoods():LiveData<List<Yemekler>>

    @Delete
    suspend fun deleteFood(foods: Yemekler)

}