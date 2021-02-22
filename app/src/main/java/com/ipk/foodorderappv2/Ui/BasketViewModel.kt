package com.ipk.foodorderappv2.Ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipk.foodorderappv2.Models.BasketFoods
import com.ipk.foodorderappv2.Models.BasketResponse
import com.ipk.foodorderappv2.Models.Foods
import com.ipk.foodorderappv2.Models.FoodsResponse
import com.ipk.foodorderappv2.Repository.BasketRepository
import com.ipk.foodorderappv2.Util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class BasketViewModel(val basketRepository: BasketRepository): ViewModel() {
    val foods: MutableLiveData<Resource<BasketResponse>> = MutableLiveData()
    val costFoods:MutableLiveData<Resource<BasketResponse>> = MutableLiveData()
    val postDeleteFoods: MutableLiveData<Resource<BasketResponse>> = MutableLiveData()

    init {
        getFoods()
        costFoods()
    }

    fun getFoods() = viewModelScope.launch {
        foods.postValue(Resource.Loading())
        val response =basketRepository.getBasket()
        Log.e("takip", "getFoods")
        foods.postValue(handleFoodsResponse(response))
    }

    private fun handleFoodsResponse(response: Response<BasketResponse>): Resource<BasketResponse> {
        if(response.isSuccessful){
            Log.e("takip", "get resres: ${response.body()}")
            response.body()?.let { resultResponse->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun costFoods() = viewModelScope.launch {
        costFoods.postValue(Resource.Loading())
        val response =basketRepository.getBasket()
        Log.e("takip", "getFoods")
        costFoods.postValue(handleFoodsResponse(response))
    }

    private fun handleCostFoodsResponse(response: Response<BasketResponse>): Resource<BasketResponse> {
        if(response.isSuccessful){
            Log.e("takip", "get resres: ${response.body()}")
            response.body()?.let { resultResponse->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun postDeleteFoods(id:String) = viewModelScope.launch {
        postDeleteFoods.postValue(Resource.Loading())
        val response =basketRepository.postDelete(id)
        postDeleteFoods.postValue(handleDeleteResponse(response))
    }

    private fun handleDeleteResponse(response: Response<BasketResponse>): Resource<BasketResponse> {
        if(response.isSuccessful){
            Log.e("takip", "delete resres: ${response.body()}")
            getFoods()
            response.body()?.let { resultResponse->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


    fun saveBasketFood(basketFoods: BasketFoods)= viewModelScope.launch {
        basketRepository.upsert(basketFoods)
    }

    fun getSavedBasketFoods()=basketRepository.getSavedBasket()

    fun deleteBasketFood(basketFoods: BasketFoods)=viewModelScope.launch {
        basketRepository.deleteFood(basketFoods)
    }
}