package com.ipk.foodorderappv2.Ui.ViewModels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ipk.foodorderappv2.FoodsApplication
import com.ipk.foodorderappv2.Models.BasketResponse
import com.ipk.foodorderappv2.Models.Foods
import com.ipk.foodorderappv2.Models.FoodsResponse
import com.ipk.foodorderappv2.Repository.FoodsRepository
import com.ipk.foodorderappv2.Util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class FoodsViewModel(app: Application,
                     val foodsRepository: FoodsRepository)
    : AndroidViewModel(app) { //ui'dan data'yı ayırırsın

    val foods: MutableLiveData<Resource<FoodsResponse>> = MutableLiveData()
    val searchedFoods: MutableLiveData<Resource<FoodsResponse>> = MutableLiveData()
    val postInsertFoods: MutableLiveData<Resource<BasketResponse>> = MutableLiveData()

    init {
        getFoods()
    }

    fun getFoods() = viewModelScope.launch {
        safeFoodsCall()
    }

    fun searchedFoods(foodName:String)= viewModelScope.launch {
        safeSearchCall(foodName)

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
        safeInsertCall(yemek_id,yemek_adi,
                yemek_resim_adi,yemek_fiyat,yemek_siparis_adet)

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

    private suspend fun safeFoodsCall(){
        foods.postValue(Resource.Loading())
        try{
            if (hasInternetConection()){
                val response =foodsRepository.getFoods()
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

    private suspend fun safeInsertCall(yemek_id:String,
                                       yemek_adi: String,
                                       yemek_resim_adi:String,
                                       yemek_fiyat:String,
                                       yemek_siparis_adet:String){
        postInsertFoods.postValue(Resource.Loading())
        try{
            if (hasInternetConection()){
                val response =foodsRepository.postInsert(yemek_id,yemek_adi,
                        yemek_resim_adi,yemek_fiyat,yemek_siparis_adet)
                postInsertFoods.postValue(handleInsertResponse(response))
            } else{
                postInsertFoods.postValue(Resource.Error("No internet connection"))
            }
        }catch (t: Throwable){
            when(t){
                is IOException -> postInsertFoods.postValue(Resource.Error("Network Failure"))
                else -> postInsertFoods.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private suspend fun safeSearchCall(foodName:String){
        searchedFoods.postValue(Resource.Loading())
        try{
            if (hasInternetConection()){
                val response=foodsRepository.searchedFoods(foodName)
                searchedFoods.postValue(handleSearchedFoodsResponse(response))
            } else{
                searchedFoods.postValue(Resource.Error("No internet connection"))
            }
        }catch (t: Throwable){
            when(t){
                is IOException -> searchedFoods.postValue(Resource.Error("Network Failure"))
                else -> searchedFoods.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun hasInternetConection(): Boolean{
        val connectivityManager=getApplication<FoodsApplication>().getSystemService(
                Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            val activeNetwork=connectivityManager.activeNetwork?: return false
            val capabilities=connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when{
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }else{
            connectivityManager.activeNetworkInfo?.run {
                return when(type){
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}