package com.ipk.foodorderappv2.Api

import com.ipk.foodorderappv2.Models.BasketResponse
import com.ipk.foodorderappv2.Models.FoodsResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST

interface FoodsApi {

    @GET("tum_sepet_yemekler.php")
    suspend fun apiGetFoods(): Response<FoodsResponse> //suspend for coroutine

    @POST("tum_yemekler_arama.php")
    suspend fun apiGetSearchedFoods(@Field("yemek_adi") yemek_adi:String): Response<FoodsResponse>

    @POST("insert_sepet_yemek.php")
    suspend fun apiAddToBasket(@Field("yemek_id") yemek_id:String,
                               @Field("yemek_adi") yemek_adi: String,
                               @Field("yemek_resim_adi") yemek_resim_adi:String,
                               @Field("yemek_fiyat") yemek_fiyat:String,
                               @Field("yemek_siparis_adet") yemek_siparis_adet:String)

    @GET("tum_sepet_yemekler.php")
    suspend fun apiGetBasket(): Response<BasketResponse>

    @POST("delete_sepet_yemek.php")
    suspend fun apiDeleteFromBasket(@Field("yemek_id") yemek_id:String)

}