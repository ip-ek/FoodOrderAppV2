package com.ipk.foodorderappv2.Ui.ViewModels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ipk.foodorderappv2.FoodsApplication
import com.ipk.foodorderappv2.Models.BasketFoods
import com.ipk.foodorderappv2.Models.BasketResponse
import com.ipk.foodorderappv2.Repository.BasketRepository
import com.ipk.foodorderappv2.Util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class BasketViewModel(app: Application,
                      val basketRepository: BasketRepository): AndroidViewModel(app) {
    val foods: MutableLiveData<Resource<BasketResponse>> = MutableLiveData()
    val costFoods:MutableLiveData<Resource<BasketResponse>> = MutableLiveData()
    val postDeleteFoods: MutableLiveData<Resource<BasketResponse>> = MutableLiveData()

    init {
        getFoods()
        costFoods()
    }

    fun getFoods() = viewModelScope.launch {
        foods.postValue(Resource.Loading())
        safeFoodsCall()
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
        safeCostFoods()
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
        safeDeleteCall(id)
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

    private suspend fun safeFoodsCall(){
        foods.postValue(Resource.Loading())
        try{
            if (hasInternetConection()){
                val response =basketRepository.getBasket()
                foods.postValue(handleFoodsResponse(response))
            } else{
                foods.postValue(Resource.Error("No internet connection"))
            }
        }catch (t: Throwable){
            when(t){
                is IOException -> foods.postValue(Resource.Error("Network Failure"))
                else -> foods.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private suspend fun safeDeleteCall(id:String){
        postDeleteFoods.postValue(Resource.Loading())
        try{
            if (hasInternetConection()){
                val response =basketRepository.postDelete(id)
                postDeleteFoods.postValue(handleDeleteResponse(response))
            } else{
                postDeleteFoods.postValue(Resource.Error("No internet connection"))
            }
        }catch (t: Throwable){
            when(t){
                is IOException -> postDeleteFoods.postValue(Resource.Error("Network Failure"))
                else -> postDeleteFoods.postValue(Resource.Error("Conversion Error"))
            }
        }
    }
    private suspend fun safeCostFoods(){
        costFoods.postValue(Resource.Loading())
        try{
            if (hasInternetConection()){
                val response =basketRepository.getBasket()
                costFoods.postValue(handleFoodsResponse(response))
            } else{
                costFoods.postValue(Resource.Error("No internet connection"))
            }
        }catch (t: Throwable){
            when(t){
                is IOException -> costFoods.postValue(Resource.Error("Network Failure"))
                else -> costFoods.postValue(Resource.Error("Conversion Error"))
            }
        }
    }


    private fun hasInternetConection(): Boolean{
        val connectivityManager=getApplication<FoodsApplication>().getSystemService(
                Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            val activeNetwork=connectivityManager.activeNetwork?: return false
            val capabilities=connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when{
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }else{
            connectivityManager.activeNetworkInfo?.run {
                return when(type){
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}