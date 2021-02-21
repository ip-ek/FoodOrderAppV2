package com.ipk.foodorderappv2.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ipk.foodorderappv2.Models.Yemekler
import com.ipk.foodorderappv2.R
import com.ipk.foodorderappv2.Util.Constants.Companion.PICS_URL
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_design.view.*

class FoodsAdapter(var mContext:Context) :RecyclerView.Adapter<FoodsAdapter.CardHolder>(){

    inner class CardHolder(view: View):RecyclerView.ViewHolder(view)

    private val differCallBack=object :DiffUtil.ItemCallback<Yemekler>(){
        override fun areItemsTheSame(oldItem: Yemekler, newItem: Yemekler): Boolean {
            return  oldItem.yemek_id==newItem.yemek_id
        }

        override fun areContentsTheSame(oldItem: Yemekler, newItem: Yemekler): Boolean {
            return oldItem==newItem
        }
    }

    val differ= AsyncListDiffer(this, differCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        return CardHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.card_design,
                parent,
                false
            )
        )

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        val food = differ.currentList[position]
        Log.e("takip","adapter ${differ.currentList}")
        holder.itemView.apply {
            val url2 = "${PICS_URL}${food.yemek_resim_adi}"
            Picasso.get().load(url2).into(card_food_img)

            card_food_price.text="${food.yemek_fiyat} ${mContext.getString(R.string.TL)}"
            card_food_name.text="${food.yemek_adi}"

            foods_card.setOnClickListener {
                if(card_detail.visibility==View.GONE){
                    tw_count.text="1"
                    card_detail.visibility=View.VISIBLE
                }else{
                    card_detail.visibility=View.GONE
                }
            }
            openners(holder, food)
        }
    }

    fun openners(holder: CardHolder, food: Yemekler){
        holder.itemView.apply {
            btn_min.setOnClickListener {
                if(tw_count.text!="1"){
                    tw_count.text=(tw_count.text.toString().toInt()-1).toString()
                }
            }

            btn_plus.setOnClickListener {
                tw_count.text=(tw_count.text.toString().toInt()+1).toString()
            }

            /*btn_add_open.setOnClickListener {
                addToBasket(holder, food,tw_count.text.toString())
            }*/

            /*btn_more.setOnClickListener {
                val intent= Intent(mContext, DetailedFoodActivity::class.java)
                intent.putExtra("food", food)
                mContext.startActivity(intent)
                card_detail.visibility=View.GONE
            }*/
        }

    } //openners

}