package com.ipk.foodorderappv2.Ui.Requests

class FoodRequests {

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