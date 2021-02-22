package com.ipk.foodorderappv2.Ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipk.foodorderappv2.Models.BasketResponse
import com.ipk.foodorderappv2.Models.Foods
import com.ipk.foodorderappv2.Models.FoodsResponse
import com.ipk.foodorderappv2.Repository.FoodsRepository
import com.ipk.foodorderappv2.Util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class FoodsViewModel(val foodsRepository: FoodsRepository): ViewModel() {
    val foods: MutableLiveData<Resource<FoodsResponse>> = MutableLiveData()
    val searchedFoods: MutableLiveData<Resource<FoodsResponse>> = MutableLiveData()
    val postInsertFoods: MutableLiveData<Resource<BasketResponse>> = MutableLiveData()

    init {
        getFoods()
    }

    fun getFoods() = viewModelScope.launch {
        foods.postValue(Resource.Loading())
        val response =foodsRepository.getFoods()
        //Log.e("takip", "resres: ${foodsRepository.getFoods().body()}")
        foods.postValue(handleFoodsResponse(response))
    }

    fun searchedFoods(foodName:String)= viewModelScope.launch {
        searchedFoods.postValue(Resource.Loading())
        val response=foodsRepository.searchedFoods(foodName)
        foods.postValue(handleSearchedFoodsResponse(response))
    }

    private fun handleFoodsResponse(response: Response<FoodsResponse>): Resource<FoodsResponse>{
        if(response.isSuccessful){
            //Log.e("takip", "resres: ${response.body()}")
            response.body()?.let { resultResponse->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchedFoodsResponse(response: Response<FoodsResponse>): Resource<FoodsResponse>{
        if(response.isSuccessful){
            //Log.e("takip", "serser: ${response.body()}")
            response.body()?.let { resultResponse->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun postInsertFoods(yemek_id:String,
                        yemek_adi: String,
                        yemek_resim_adi:String,
                        yemek_fiyat:String,
                        yemek_siparis_adet:String) = viewModelScope.launch {
        postInsertFoods.postValue(Resource.Loading())
        val response =foodsRepository.postInsert(yemek_id,yemek_adi,
                yemek_resim_adi,yemek_fiyat,yemek_siparis_adet)
        postInsertFoods.postValue(handleInsertResponse(response))
    }

    private fun handleInsertResponse(response: Response<BasketResponse>): Resource<BasketResponse> {
        if(response.isSuccessful){
            //Log.e("takip", "insert resres: ${response.body()}")
            response.body()?.let { resultResponse->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveFood(foods:Foods)= viewModelScope.launch {
        foodsRepository.upsert(foods)
    }

    fun getSavedFoods()=foodsRepository.getSavedFoods()

    fun deleteFood(foods: Foods)=viewModelScope.launch {
        foodsRepository.deleteFood(foods)
    }
}