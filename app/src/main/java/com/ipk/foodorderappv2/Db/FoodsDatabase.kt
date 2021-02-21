package com.ipk.foodorderappv2.Db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ipk.foodorderappv2.Models.Yemekler

@Database(
    entities = [Yemekler::class],
    version =1
)
abstract class FoodsDatabase: RoomDatabase() {
    abstract fun getFoodsDao(): FoodsDao

    companion object{
        @Volatile
        private var instance:FoodsDatabase?=null
        private  val LOCK=Any()

        operator fun invoke(context:Context) = instance ?: synchronized(LOCK){
            instance ?: createDatabase(context).also{ instance = it}
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                FoodsDatabase::class.java,
                "foods_db.db"
            ).build()
    }
}