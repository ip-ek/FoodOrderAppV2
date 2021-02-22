package com.ipk.foodorderappv2.Ui.Requests

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import com.ipk.foodorderappv2.Models.Foods
import com.ipk.foodorderappv2.Ui.Activities.DetailedFoodActivity
import com.ipk.foodorderappv2.Ui.Activities.MainActivity
import com.ipk.foodorderappv2.Util.Resource

class DetailedRequests {

    fun addToBasket(context: Context, foods: Foods, count:String){

        (context as DetailedFoodActivity).viewModel.postInsertFoods(foods.yemek_id,foods.yemek_adi,
                foods.yemek_resim_adi,foods.yemek_fiyat,count)

        context.viewModel.postInsertFoods.observe(context, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    Log.e("takip", " insert okey ")
                    response.data?.let { basketResponse ->
                        //pass
                    }
                    Toast.makeText(context,"kaydedildi", Toast.LENGTH_SHORT).show()
                    //updateFabText()

                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        Log.e("takip", "insert adapter error")
                    }
                }
                is Resource.Loading -> {
                    //you can create a loading bar
                    Log.e("takip", "insert adapter loading")
                }
            }
        })

    }
}