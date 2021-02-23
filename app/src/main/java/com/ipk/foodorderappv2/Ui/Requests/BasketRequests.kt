package com.ipk.foodorderappv2.Ui.Requests

import android.content.Context
import android.util.Log
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.ipk.foodorderappv2.Models.BasketFoods
import com.ipk.foodorderappv2.R
import com.ipk.foodorderappv2.Ui.Activities.BasketActivity
import com.ipk.foodorderappv2.Util.Resource
import kotlinx.android.synthetic.main.activity_basket.*
import kotlinx.android.synthetic.main.activity_main.*

class BasketRequests {

    fun allOrders(mContext:Context): ArrayList<BasketFoods>{
        var basketList: ArrayList<BasketFoods> =ArrayList()
        (mContext as BasketActivity).runViewModel()

        mContext.viewModel.foods.observe(mContext, Observer { response ->
            when(response){
                is Resource.Success->{
                    Log.e("takip", " basket adapter okey")

                    response.data?.let { basketResponse ->
                        Log.e("takip", "response list: ${basketResponse.basketFoods}")
                        if (basketResponse.basketFoods==null){
                            val foodList=ArrayList<BasketFoods>()
                            mContext.basketAdapter.submitList(foodList)
                            mContext.btn_basket.text = mContext.getString(R.string.empty_basket)
                        }else{
                            val foodList=listConcat(basketResponse.basketFoods)
                            basketList=foodList.toCollection(ArrayList())
                            Log.e("takip", "size of List: ${foodList.size}")
                            mContext.basketAdapter.submitList(foodList)
                            mContext.btn_basket.text = "${calculatePrice(foodList)} ${mContext.getString(R.string.TL)}"

                        }
                    }
                }
                is Resource.Error->{
                    response.message?.let { message ->
                        Snackbar.make(mContext.btn_basket, "An error occured: $message", Snackbar.LENGTH_LONG).show()
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
    }//allOrders

    fun listConcat(orders: List<BasketFoods>):List<BasketFoods>{
        var tmpList:ArrayList<BasketFoods>
        tmpList= ArrayList()
        var flag:Boolean
        for(i in 0 until orders.size){
            flag=false
            tmpList.forEach { each->
                if (each.yemek_id.equals(orders[i].yemek_id)){
                    each.yemek_siparis_adet=(each.yemek_siparis_adet.toInt()+orders[i].yemek_siparis_adet.toInt()).toString()
                    flag=true
                }
            }
            if (!flag){
                tmpList.add(orders[i])
            }
        }
        return tmpList
    } //listConcat

    fun calculatePrice(orders: List<BasketFoods>):Int{
        var price=0
        for(i in 0 until orders.size){
            price+=(orders[i].yemek_fiyat.toInt()*orders[i].yemek_siparis_adet.toInt())
        }
        return price
    } //calculatePrice

    fun deleteFromBasket(mContext:Context, id:String){
        (mContext as BasketActivity).viewModel.postDeleteFoods(id)
        mContext.viewModel.postDeleteFoods.observe(mContext, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    Log.e("takip", " delete ${response.data} ")
                    response.data?.let { basketResponse ->
                        //pass
                    }

                    Log.e("takip", "deleteeeeeeeeeeeeeeeeeeeeeeeee")
                    //allOrders(mContext)
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        Snackbar.make(mContext.btn_basket, "An error occured: $message", Snackbar.LENGTH_LONG).show()
                        Log.e("takip", "delete adapter error")
                    }
                }
                is Resource.Loading -> {
                    //you can create a loading bar
                    Log.e("takip", "delete adapter loading")
                }
            }
        })
    }


    fun deleteAllBasket(mContext:Context){
        var foodList=allOrders(mContext)
        for(i in 0 until foodList.size){
            deleteFromBasket(mContext, foodList[i].yemek_id)
        }
    }

}