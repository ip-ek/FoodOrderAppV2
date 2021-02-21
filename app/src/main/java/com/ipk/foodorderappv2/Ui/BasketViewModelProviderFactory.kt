package com.ipk.foodorderappv2.Ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ipk.foodorderappv2.Repository.BasketRepository
import com.ipk.foodorderappv2.Repository.FoodsRepository

class BasketViewModelProviderFactory(val basketRepository: BasketRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BasketViewModel(basketRepository) as T
    }
}