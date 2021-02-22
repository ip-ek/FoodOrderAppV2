package com.ipk.foodorderappv2.Api

import com.ipk.foodorderappv2.Models.BasketResponse
import com.ipk.foodorderappv2.Models.FoodsResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface FoodsApi {

    @GET("tum_yemekler.php")
    suspend fun apiGetFoods(): Response<FoodsResponse> //suspend for coroutine

    @FormUrlEncoded
    @POST("tum_yemekler_arama.php")
    suspend fun apiGetSearchedFoods(@Field("yemek_adi") yemek_adi:String): Response<FoodsResponse>

    @FormUrlEncoded
    @POST("insert_sepet_yemek.php")
    suspend fun apiAddToBasket(@Field("yemek_id") yemek_id:String,
                               @Field("yemek_adi") yemek_adi: String,
                               @Field("yemek_resim_adi") yemek_resim_adi:String,
                               @Field("yemek_fiyat") yemek_fiyat:String,
                               @Field("yemek_siparis_adet") yemek_siparis_adet:String): Response<BasketResponse>

    @GET("tum_sepet_yemekler.php")
    suspend fun apiGetBasket(): Response<BasketResponse>

    @FormUrlEncoded
    @POST("delete_sepet_yemek.php")
    suspend fun apiDeleteFromBasket(@Field("yemek_id") yemek_id:String) :Response<BasketResponse>

}