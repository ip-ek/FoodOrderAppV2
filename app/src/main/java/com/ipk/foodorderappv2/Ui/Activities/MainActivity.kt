package com.ipk.foodorderappv2.Ui.Activities

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.ipk.foodorderappv2.Adapters.FoodsAdapter
import com.ipk.foodorderappv2.Db.FoodsDatabase
import com.ipk.foodorderappv2.Models.BasketFoods
import com.ipk.foodorderappv2.Models.Foods
import com.ipk.foodorderappv2.R
import com.ipk.foodorderappv2.Repository.FoodsRepository
import com.ipk.foodorderappv2.Ui.ViewModels.FoodsViewModel
import com.ipk.foodorderappv2.Ui.ViewModels.FoodsViewModelProviderFactory
import com.ipk.foodorderappv2.Util.Resource
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.card_design.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    lateinit var viewModel: FoodsViewModel
    lateinit var foodsAdapter: FoodsAdapter
    private lateinit var foodList: ArrayList<Foods>
    private lateinit var sp: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var basketList: ArrayList<BasketFoods>
    var amount =0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sp = getSharedPreferences("FoodAppSh", Context.MODE_PRIVATE)
        editor=sp.edit()

        toolbar_main.title=this.getString(R.string.app_name)
        setSupportActionBar(toolbar_main)

        val foodsRepository=FoodsRepository(FoodsDatabase(this))
        val viewModelProviderFactory= FoodsViewModelProviderFactory(application, foodsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(FoodsViewModel::class.java)

        setupRv()
        allFoods()
        updateFabText()

        fab_main.setOnClickListener{
            startActivity(Intent(this@MainActivity, BasketActivity::class.java))
        }

        //viewModel.saveFood(food)
        // in saved news
        /*viewModel.getSavedFoods().observe(this, Observer{ foods->
            foodsAdapter.differ.submitList(foods)
        })
        //delete in adapter
        val food=foodsAdapter.differ.currentList[position]
        viewModel.deleteFood(food)*/

    } //onCreate

    override fun onResume() {
        updateFabText()
        super.onResume()
    }

    private fun setupRv(){
        foodsAdapter= FoodsAdapter(this@MainActivity)
        rv_main.apply {
            adapter=foodsAdapter
            layoutManager=LinearLayoutManager(this@MainActivity)
        }
    }//setupRv

    fun addToBasket(foods: Foods, count:String){

        viewModel.postInsertFoods(foods.yemek_id,foods.yemek_adi,
                foods.yemek_resim_adi,foods.yemek_fiyat,count)

        viewModel.postInsertFoods.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    Log.e("takip", " insert okey ")
                    response.data?.let { basketResponse ->
                        //pass
                    }
                    Toast.makeText(this,"kaydedildi", Toast.LENGTH_SHORT).show()
                    updateFabText()
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        Snackbar.make(fab_main, "An error occured: $message", Snackbar.LENGTH_LONG).show()
                        Log.e("takip", "insert adapter error")
                    }
                }
                is Resource.Loading -> {
                    //you can create a loading bar
                    Log.e("takip", "insert adapter loading")
                }
            }
        })
    }

    fun searchFoods(){
        viewModel.searchedFoods.observe(this, Observer { response ->
            Log.e("takip", "searche girdi")
            when (response) {
                is Resource.Success -> {
                    response.data?.let { foodsResponse ->
                        Log.e("takip", " search ${response.data} ")
                        if (foodsResponse.foods == null) {
                            foodList = ArrayList<Foods>()
                        } else {
                            foodList = foodsResponse.foods.toCollection(ArrayList())
                        }
                        foodsAdapter.differ.submitList(foodList)
                        updateAdapter()
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        //Snackbar.make(fab_main, "An error occured: $message", Snackbar.LENGTH_LONG).show()
                        Log.e("takip", "search adapter error")
                    }
                }
                is Resource.Loading -> {
                    Log.e("takip", "search adapter loading")
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_search_menu, menu)

        val item = menu.findItem(R.id.action_search)
        val searchView = item.actionView as SearchView
        searchView.setOnQueryTextListener(this)

        return super.onCreateOptionsMenu(menu)
    } //onCreateOptionsMenu

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_search -> {
                Log.d("takip", "search seçildi")
            }
            R.id.action_list -> {
                Log.d("takip", "filtre seçildi")
                showAlert()
            }
            else -> {
                Log.e("eroor", "Menu item hatası")
            }
        }
        return super.onOptionsItemSelected(item)
    } //onOptionsItemSelected

    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    } //onBackPressed

    override fun onQueryTextSubmit(p0: String): Boolean {
        Log.d("search gönderilen arama", p0)
        try{
            searcedFoods(p0)
        }catch (e : Exception){
            e.printStackTrace()
        }

        return true
    } //onQueryTextSubmit

    override fun onQueryTextChange(p0: String): Boolean {
        Log.d("search harf girdikce", p0)
        try{
            searcedFoods(p0)
        }catch (e : Exception){
            e.printStackTrace()
        }
        return true
    } //onQueryTextChange

    fun showAlert(){
        val ad= AlertDialog.Builder(this@MainActivity)
        ad.setTitle("Sırala:")
        //ad.setIcon(R.drawable.filter_list)
        var items = arrayOf(this.getString(R.string.def), this.getString(R.string.fromCheaper), this.getString(
                R.string.toCheaper
        ),
                this.getString(R.string.A_to_Z), this.getString(R.string.Z_to_A))
        var checkedItem = sp.getInt("listItem", 0)

        ad.setSingleChoiceItems(items, checkedItem,
                DialogInterface.OnClickListener() { dialogInterface: DialogInterface, i: Int ->
                    when (i) {
                        0 -> editor.putInt("listItem", 0)
                        1 -> editor.putInt("listItem", 1)
                        2 -> editor.putInt("listItem", 2)
                        3 -> editor.putInt("listItem", 3)
                        4 -> editor.putInt("listItem", 4)
                    }
                })
        ad.setPositiveButton(this.getString(R.string.apply)){ d, i ->
            //Snackbar.make(toolbar_main, "Filtre Ayarlandı!", Snackbar.LENGTH_LONG).show()
            editor.commit()
            if (sp.getInt("listItem", 0)==0) allFoods()
            else updateAdapter()
        }
        ad.setNegativeButton(this.getString(R.string.cancel)){ d, i ->
            //Snackbar.make(toolbar_main, "Filtre Ayarlanmadı!", Snackbar.LENGTH_LONG).show()
        }
        ad.create().show()
    } //showAlert

    fun searcedFoods(src:String){
        viewModel.searchedFoods(src)
        foodList= ArrayList()
        updateAdapter()
        searchFoods()
    }

    fun allFoods(){
        foodList = ArrayList<Foods>()
        viewModel.foods.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    Log.e("takip", " foods adapter okey")
                    response.data?.let { foodsResponse ->
                        if (foodsResponse.foods == null) {
                            foodList = ArrayList()
                        } else {
                            foodList = foodsResponse.foods.toCollection(ArrayList())
                        }
                        foodsAdapter.differ.submitList(foodList)
                        updateAdapter()
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        Snackbar.make(fab_main, "An error occured: $message", Snackbar.LENGTH_LONG).show()
                        Log.e("takip", "foods adapter error")
                    }
                }
                is Resource.Loading -> {
                    //you can create a loading bar
                    Log.e("takip", "foods adapter loading")
                }
            }
        })

    }//allFoods

    fun updateAdapter(){
        sp = getSharedPreferences("FoodAppSh", Context.MODE_PRIVATE)
        editor=sp.edit()

        var checkedItem = sp.getInt("listItem", 0)

        when(checkedItem){
            0 -> {//default
                //pass
            }
            1 -> foodList.sortBy { it.yemek_fiyat.toInt() } //cheap to expensive
            2 -> foodList.sortByDescending { it.yemek_fiyat.toInt() } //expensive to cheap
            3 -> foodList.sortBy { it.yemek_adi } //A to Z
            4 -> foodList.sortByDescending { it.yemek_adi } //Z to A
        }

        foodsAdapter.differ.submitList(ArrayList<Foods>(foodList))
    } //updateAdapter

    fun updateFabText(){ //all orders
        val url="http://kasimadalan.pe.hu/yemekler/tum_sepet_yemekler.php"

        val req = StringRequest(Request.Method.GET, url, Response.Listener { res->
            Log.d("takip veri okuma: ", res)
            jsonParseBasket(res)
            amount=calculatePrice()
            update()
            Log.d("takip list:", basketList.size.toString())
            Log.d("amount",amount.toString())
        }, Response.ErrorListener { Log.d("takip hata: ", "Veri okuma") })

        Volley.newRequestQueue(this@MainActivity).add(req)

    } //allOrders

    fun jsonParseBasket(res:String){
        basketList= ArrayList()

        try {
            val jsonObj= JSONObject(res)
            val foods =jsonObj.getJSONArray("sepet_yemekler")

            for(i in 0 until foods.length()){
                val f=foods.getJSONObject(i)

                val yemek_id=f.getString("yemek_id")
                val yemek_adi=f.getString("yemek_adi")
                val yemek_resim_adi = f.getString("yemek_resim_adi")
                val yemek_fiyat=f.getString("yemek_fiyat")
                val yemek_siparis_adet=f.getString("yemek_siparis_adet")

                val food=BasketFoods(null, yemek_adi, yemek_fiyat, yemek_id, yemek_resim_adi,yemek_siparis_adet)
                basketList.add(food)
            }

        }catch (e: JSONException){
            Log.d("takip hata:","parse hatası")
            e.printStackTrace()
        }

    } //jsonParseBasket

    fun calculatePrice():Int{
        var price=0
        for(i in 0 until basketList.size){
            price+=basketList[i].yemek_fiyat.toInt()*basketList[i].yemek_siparis_adet.toInt()
        }
        return price
    } //calculatePrice

    fun update(){
        Log.d("amount fonk",amount.toString())

        if (amount==0){
            fab_main.shrink()
        }else{
            fab_main.text=amount.toString()+"${this.getString(R.string.TL)}"
            fab_main.extend()
        }
    } //update

}