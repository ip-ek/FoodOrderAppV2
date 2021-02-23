package com.ipk.foodorderappv2.Ui.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.ipk.foodorderappv2.Db.FoodsDatabase
import com.ipk.foodorderappv2.Models.Foods
import com.ipk.foodorderappv2.R
import com.ipk.foodorderappv2.Repository.FoodsRepository
import com.ipk.foodorderappv2.Ui.FoodsViewModel
import com.ipk.foodorderappv2.Ui.FoodsViewModelProviderFactory
import com.ipk.foodorderappv2.Ui.Requests.DetailedRequests
import com.ipk.foodorderappv2.Ui.Requests.FoodRequests
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detailed_food.*
import kotlinx.android.synthetic.main.activity_main.*

class DetailedFoodActivity : AppCompatActivity() {
    lateinit var viewModel: FoodsViewModel
    private lateinit var food: Foods

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_food)

        food=intent.getSerializableExtra("food") as Foods

        toolbar_title.setText(food.yemek_adi)
        setSupportActionBar(toolbar_main)

        val foodsRepository= FoodsRepository(FoodsDatabase(this))
        val viewModelProviderFactory= FoodsViewModelProviderFactory(application,foodsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(FoodsViewModel::class.java)

        detailed_price.text="${food.yemek_fiyat} ${this.getString(R.string.TL)}"
        val url2 = "http://kasimadalan.pe.hu/yemekler/resimler/${food.yemek_resim_adi}"
        Picasso.get().load(url2).into(detailed_image)
        totalUpdate()

        btn_up.setOnClickListener {
            detailed_count.text=(detailed_count.text.toString().toInt()+1).toString()
            totalUpdate()
        }

        btn_down.setOnClickListener {
            if(detailed_count.text!="1"){
                detailed_count.text=(detailed_count.text.toString().toInt()-1).toString()
                totalUpdate()
            }
        }

        btn_add.setOnClickListener {
            addToBasket(food, detailed_count.text.toString())
        }

    } //onCreate

    fun totalUpdate(){
        detailed_price_count.text="${detailed_count.text} x ${food.yemek_fiyat} ${this.getString(R.string.TL)}"
        var total=detailed_count.text.toString().toInt()*food.yemek_fiyat.toInt()
        detailed_total_price.text="${this.getString(R.string.total)}: ${total} ${this.getString(R.string.TL)}"
    } //totalUpdate

    fun addToBasket(food: Foods, count:String){
        DetailedRequests().addToBasket(this, food, count)
        finish()
    } //addToBasket*/
}