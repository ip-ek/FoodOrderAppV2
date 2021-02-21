package com.ipk.foodorderappv2.Ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipk.foodorderappv2.Models.BasketResponse
import com.ipk.foodorderappv2.Models.FoodsResponse
import com.ipk.foodorderappv2.Repository.BasketRepository
import com.ipk.foodorderappv2.Util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class BasketViewModel(val basketRepository: BasketRepository): ViewModel() {
    val foods: MutableLiveData<Resource<BasketResponse>> = MutableLiveData()

    init {
        getFoods()
    }

    fun getFoods() = viewModelScope.launch {
        foods.postValue(Resource.Loading())
        val response =basketRepository.getBasket()
        Log.e("takip", "resres: ${basketRepository.getBasket().body()}")
        foods.postValue(handleFoodsResponse(response))
    }

    private fun handleFoodsResponse(response: Response<BasketResponse>): Resource<BasketResponse> {
        if(response.isSuccessful){
            Log.e("takip", "resres: ${response.body()}")
            response.body()?.let { resultResponse->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}