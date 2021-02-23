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
import com.ipk.foodorderappv2.Ui.Requests.BasketRequests
import com.ipk.foodorderappv2.Util.Resource
import kotlinx.android.synthetic.main.activity_basket.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.card_basket.*

class BasketActivity : AppCompatActivity() {
    lateinit var viewModel: BasketViewModel
    lateinit var basketAdapter: BasketAdapter
    lateinit var basketList: ArrayList<BasketFoods>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basket)

        toolbar_basket.title=this.getString(R.string.basket)
        setSupportActionBar(toolbar_basket)

        runViewModel()

        setupRv()
        basketList=BasketRequests().allOrders(this)

        //allOrders()

    } //onCreate

    fun runViewModel(){
        val basketRepository=BasketRepository(BasketDatabase(this))
        val viewModelProviderFactory= BasketViewModelProviderFactory(application, basketRepository)
        viewModel=ViewModelProvider(this, viewModelProviderFactory).get(BasketViewModel::class.java)
    }

    private fun setupRv(){
        basketAdapter= BasketAdapter(this@BasketActivity)
        rv_basket.apply {
            adapter=basketAdapter
            layoutManager=LinearLayoutManager(this@BasketActivity)
        }
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
                    BasketRequests().deleteAllBasket(this)
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
}