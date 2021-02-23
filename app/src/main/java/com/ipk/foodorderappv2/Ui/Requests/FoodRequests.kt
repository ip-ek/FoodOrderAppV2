package com.ipk.foodorderappv2.Ui.Requests

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.ipk.foodorderappv2.Adapters.FoodsAdapter
import com.ipk.foodorderappv2.Db.BasketDatabase
import com.ipk.foodorderappv2.Models.BasketFoods
import com.ipk.foodorderappv2.Models.Foods
import com.ipk.foodorderappv2.R
import com.ipk.foodorderappv2.Repository.BasketRepository
import com.ipk.foodorderappv2.Ui.Activities.BasketActivity
import com.ipk.foodorderappv2.Ui.Activities.DetailedFoodActivity
import com.ipk.foodorderappv2.Ui.Activities.MainActivity
import com.ipk.foodorderappv2.Ui.BasketViewModel
import com.ipk.foodorderappv2.Ui.BasketViewModelProviderFactory
import com.ipk.foodorderappv2.Util.Resource
import kotlinx.android.synthetic.main.activity_basket.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.card_design.*

class FoodRequests {
    private lateinit var sp: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor



    /*fun updateFab(context: Context, amount:Int){

        Log.d("amount fonk",amount.toString())

        if (amount==0){
            (context as MainActivity).fab_main.shrink()
        }else{
            (context as MainActivity).fab_main.text=amount.toString()+"${context.getString(R.string.TL)}"
            context.fab_main.extend()
        }
    } //update

    fun calculatePrice( orders:ArrayList<BasketFoods>):Int{
        var price=0
        for(i in 0 until orders.size){
            price+=(orders[i].yemek_fiyat.toInt()*orders[i].yemek_siparis_adet.toInt())
        }
        return price
    } //calculatePrice

    fun allOrders(mContext:Context): ArrayList<BasketFoods>{
        var basketList: ArrayList<BasketFoods> =ArrayList()
        (mContext as MainActivity).runViewBasket()

        mContext.viewModelBasket.foods.observe(mContext, Observer { response ->
            when(response){
                is Resource.Success->{
                    Log.e("takip", " basket adapter okey")

                    response.data?.let { basketResponse ->
                        Log.e("takip", "response list: ${response.data}")
                        if (basketResponse.basketFoods==null){
                            basketList=ArrayList<BasketFoods>()
                        }else{
                            basketList=basketResponse.basketFoods.toCollection(ArrayList())

                            updateFab(mContext, calculatePrice(basketList))
                        }
                    }
                }
                is Resource.Error->{
                    response.message?.let { message ->
                        Log.e("takip", "basket adapter error")
                    }
                }
                is Resource.Loading->{
                    //you can create a loading bar
                    Log.e("takip", "basket adapter loading")
                }
            }
        })

        return basketList
    }//allOrders*/

}