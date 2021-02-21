package com.ipk.foodorderappv2.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ipk.foodorderappv2.Models.BasketFoods
import com.ipk.foodorderappv2.R
import com.ipk.foodorderappv2.Util.Constants
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_basket.view.*

class BasketAdapter(var mContext: Context) : RecyclerView.Adapter<BasketAdapter.CardHolder>() {

    inner class CardHolder(view: View):RecyclerView.ViewHolder(view)

    private val differCallBack=object : DiffUtil.ItemCallback<BasketFoods>(){
        override fun areItemsTheSame(oldItem: BasketFoods, newItem: BasketFoods): Boolean {
            return  oldItem.yemek_id==newItem.yemek_id
        }

        override fun areContentsTheSame(oldItem: BasketFoods, newItem: BasketFoods): Boolean {
            return oldItem==newItem
        }
    }

    val differ= AsyncListDiffer(this, differCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketAdapter.CardHolder {
        return CardHolder(
                LayoutInflater.from(mContext).inflate(
                        R.layout.card_basket,
                        parent,
                        false
                )
        )

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: BasketAdapter.CardHolder, position: Int) {
        val order = differ.currentList[position]
        Log.e("takip","adapter ${differ.currentList}")
        holder.itemView.apply {
            val url2 = "${Constants.PICS_URL}${order.yemek_resim_adi}"
            Picasso.get().load(url2).into(basket_food_img)

            basket_food_price.text="${order.yemek_fiyat.toInt()*order.yemek_siparis_adet.toInt()} ${mContext.getString(R.string.TL)}"
            card_basket_name.text="${order.yemek_adi}"
            card_basket_det_price.text="${order.yemek_siparis_adet} x ${order.yemek_fiyat} ${mContext.getString(R.string.TL)}"
            //basket_delete.
        }
    }
}