package com.ipk.foodorderappv2.Ui.Activities

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.ipk.foodorderappv2.Adapters.FoodsAdapter
import com.ipk.foodorderappv2.Db.FoodsDatabase
import com.ipk.foodorderappv2.Models.Foods
import com.ipk.foodorderappv2.R
import com.ipk.foodorderappv2.Repository.FoodsRepository
import com.ipk.foodorderappv2.Ui.FoodsViewModel
import com.ipk.foodorderappv2.Ui.FoodsViewModelProviderFactory
import com.ipk.foodorderappv2.Util.Resource
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    lateinit var viewModel: FoodsViewModel
    lateinit var foodsAdapter: FoodsAdapter
    private lateinit var foodList: ArrayList<Foods>
    private lateinit var sp: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sp = getSharedPreferences("FoodAppSh", Context.MODE_PRIVATE)
        editor=sp.edit()

        toolbar_main.title=this.getString(R.string.app_name)
        setSupportActionBar(toolbar_main)

        val foodsRepository=FoodsRepository(FoodsDatabase(this))
        val viewModelProviderFactory= FoodsViewModelProviderFactory(foodsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(FoodsViewModel::class.java)

        setupRv()
        allFoods()

        fab_main.setOnClickListener{
            startActivity(Intent(this@MainActivity, BasketActivity::class.java))
        }

    } //onCreate

    private fun setupRv(){
        foodsAdapter= FoodsAdapter(this@MainActivity)
        rv_main.apply {
            adapter=foodsAdapter
            layoutManager=LinearLayoutManager(this@MainActivity)
        }
    }//setupRv

    fun allFoods(){
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
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
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

    fun searchFoods(){
        viewModel.searchedFoods.observe(this, Observer { response ->
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
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        Log.e("takip", "foods adapter error")
                    }
                }
                is Resource.Loading -> {
                    //you can create a loading bar
                    Log.e("takip", "foods adapter loading")
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
        Log.d("takip gönderilen arama", p0)
        searcedFoods(p0)
        return true
    } //onQueryTextSubmit

    override fun onQueryTextChange(p0: String): Boolean {
        Log.d("takip harf girdikce", p0)
        searcedFoods(p0)
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

    fun updateAdapter(){
        var checkedItem = sp.getInt("listItem", 0)

        when(checkedItem){
            0 -> {//default
                //pass
            }
            1 -> foodList.sortBy { it.yemek_fiyat } //cheap to expensive
            2 -> foodList.sortByDescending { it.yemek_fiyat } //expensive to cheap
            3 -> foodList.sortBy { it.yemek_adi } //A to Z
            4 -> foodList.sortByDescending { it.yemek_adi } //Z to A
        }

        foodsAdapter.differ.submitList(foodList)
    } //updateAdapter

    fun searcedFoods(src:String){
        //searchFoods()
        //viewModel.searchedFoods(src)

    }

}