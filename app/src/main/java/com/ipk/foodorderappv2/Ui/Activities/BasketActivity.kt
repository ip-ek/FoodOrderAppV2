package com.ipk.foodorderappv2.Ui.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ipk.foodorderappv2.Adapters.BasketAdapter
import com.ipk.foodorderappv2.Db.BasketDatabase
import com.ipk.foodorderappv2.Models.BasketFoods
import com.ipk.foodorderappv2.R
import com.ipk.foodorderappv2.Repository.BasketRepository
import com.ipk.foodorderappv2.Ui.BasketViewModel
import com.ipk.foodorderappv2.Ui.BasketViewModelProviderFactory
import com.ipk.foodorderappv2.Util.Resource
import kotlinx.android.synthetic.main.activity_basket.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.card_basket.*

class BasketActivity : AppCompatActivity() {
    lateinit var viewModel: BasketViewModel
    lateinit var basketAdapter: BasketAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basket)

        toolbar_basket.title=this.getString(R.string.basket)
        setSupportActionBar(toolbar_basket)

        val basketRepository=BasketRepository(BasketDatabase(this))
        val viewModelProviderFactory= BasketViewModelProviderFactory(basketRepository)
        viewModel=ViewModelProvider(this, viewModelProviderFactory).get(BasketViewModel::class.java)

        setupRv()
        allOrders()

        //allOrders()

    } //onCreate

    private fun setupRv(){
        basketAdapter= BasketAdapter(this@BasketActivity)
        rv_basket.apply {
            adapter=basketAdapter
            layoutManager=LinearLayoutManager(this@BasketActivity)
        }
    }

    fun allOrders(){
        viewModel.foods.observe(this, Observer { response ->
            when(response){
                is Resource.Success->{
                    Log.e("takip", " basket adapter okey")

                    response.data?.let { basketResponse ->
                        if (basketResponse.basketFoods==null){
                            btn_basket.text = this.getString(R.string.empty_basket)
                        //Toast.makeText(this, "Sepet Boş", Toast.LENGTH_SHORT).show()
                        }else{
                            val foodList=listConcat(basketResponse.basketFoods)
                            basketAdapter.differ.submitList(foodList)
                            btn_basket.text = "${calculatePrice(foodList)} ${this.getString(R.string.TL)}"
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

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.basket_delete_menu,menu)
        return super.onCreateOptionsMenu(menu)
    } //onCreateOptionsMenu

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.del_basket -> {
                Log.d("takip", "sepetin silinmesi seçildi")
                val ad= AlertDialog.Builder(this@BasketActivity)
                ad.setTitle("Silme İsteği")
                ad.setMessage("Tüm sepet silinsin mi?")
                ad.setIcon(R.drawable.delete_icon)
                ad.setPositiveButton("Evet"){ d,i ->
                    //Snackbar.make(basket_delete, "Sepet silindi.", Snackbar.LENGTH_SHORT).show()
                    //deleteBasket()
                }
                ad.setNegativeButton("Hayır"){ d,i ->
                    //Snackbar.make(basket_delete, "Silme işlemi iptal edildi!", Snackbar.LENGTH_SHORT).show()
                }
                ad.create().show()
            }
            else -> {
                Log.e("eroor", "Menu item hatası")
            }
        }
        return super.onOptionsItemSelected(item)
    } //onOptionsItemSelected

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
    }

    fun calculatePrice(orders: List<BasketFoods>):Int{
        var price=0
        for(i in 0 until orders.size){
            price+=(orders[i].yemek_fiyat.toInt()*orders[i].yemek_siparis_adet.toInt())
            Log.e("takip", "yemek fiyat:${orders[i].yemek_fiyat}\nyemek adet${orders[i].yemek_siparis_adet}\nTutar:${price}")
        }
        return price
    } //calculatePrice


/*
   fun deleteBasket(){
        Log.d("takip lanet", foodList.size.toString())
        for(i in 0 until foodList.size){
            deleteFromBasket(foodList[i])
            Log.d("takip lanet", "say")
        }
        foodList=ArrayList() //isteklerin karşılanması zaman almaktadır
        adapter= BasketFoodsAdapter(this@BasketActivity, foodList)
        rv_basket.adapter=adapter
        btn_basket.text = this.getString(R.string.empty_basket)
    } //deleteBasket

    fun deleteFromBasket(food: BasketFoods){
        val url=this.getString(R.string.deleteFromBasket)
        val req= object : StringRequest(Request.Method.POST,url, Response.Listener { res ->
            Log.d("takip sil cevap", res)

        }, Response.ErrorListener { Log.d("Takip sil","hata") }){
            override fun getParams(): MutableMap<String, String> {
                val params=HashMap<String,String>()
                params["yemek_id"]=food.yemek_id.toString()
                return params
            }
        }
        Volley.newRequestQueue(this).add(req)

    } //deleteFromBasket*/
}