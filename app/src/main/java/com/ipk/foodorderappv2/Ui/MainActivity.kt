package com.ipk.foodorderappv2.Ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.ipk.foodorderappv2.Adapters.FoodsAdapter
import com.ipk.foodorderappv2.Db.FoodsDatabase
import com.ipk.foodorderappv2.R
import com.ipk.foodorderappv2.Repository.FoodsRepository
import com.ipk.foodorderappv2.Util.Resource
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    lateinit var viewModel: FoodsViewModel
    lateinit var foodsAdapter: FoodsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar_main.title=this.getString(R.string.app_name)
        setSupportActionBar(toolbar_main)

        val foodsRepository=FoodsRepository(FoodsDatabase(this))
        val viewModelProviderFactory= FoodsViewModelProviderFactory(foodsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(FoodsViewModel::class.java)

        setupRv()
        //Log.e("takip", "helloooo")

        viewModel.foods.observe(this, Observer { response ->
            when(response){
                is Resource.Success->{
                    Log.e("takip", "hebele ${response.data}")

                    response.data?.let { foodsResponse ->
                        foodsAdapter.differ.submitList(foodsResponse.yemekler)
                    }

                    Log.e("takip", "obaaaaaaaa")
                }
                is Resource.Error->{
                    response.message?.let { message ->
                        Log.e("takip", "foods adapter error")
                    }
                }
                is Resource.Loading->{
                    //you can create a loading bar
                    Log.e("takip", "foods adapter loading")
                }
            }

        }) //lifeCycleowner??


    }
    private fun setupRv(){
        foodsAdapter= FoodsAdapter(this@MainActivity)
        rv_main.apply {
            adapter=foodsAdapter
            layoutManager=LinearLayoutManager(this@MainActivity)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_search_menu,menu)

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
                //showAlert()
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
        Log.d("takip gönderilen arama",p0)
        //searcedFoods(p0)
        return true
    } //onQueryTextSubmit

    override fun onQueryTextChange(p0: String): Boolean {
        Log.d("takip harf girdikce",p0)
       // searcedFoods(p0)
        return true
    } //onQueryTextChange

}