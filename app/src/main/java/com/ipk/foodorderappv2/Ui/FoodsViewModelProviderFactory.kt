package com.ipk.foodorderappv2.Ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ipk.foodorderappv2.Repository.FoodsRepository

class FoodsViewModelProviderFactory(val foodsRepository: FoodsRepository) :ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FoodsViewModel(foodsRepository) as T
    }
}