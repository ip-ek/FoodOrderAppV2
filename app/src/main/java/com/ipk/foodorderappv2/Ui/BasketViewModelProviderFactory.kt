package com.ipk.foodorderappv2.Ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ipk.foodorderappv2.Repository.BasketRepository
import com.ipk.foodorderappv2.Repository.FoodsRepository

class BasketViewModelProviderFactory(val app: Application,
                                     val basketRepository: BasketRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BasketViewModel(app, basketRepository) as T
    }
}