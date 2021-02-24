package com.ipk.foodorderappv2.Ui.ViewModels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ipk.foodorderappv2.Repository.FoodsRepository

class FoodsViewModelProviderFactory(val app:Application,
        val foodsRepository: FoodsRepository) :ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FoodsViewModel(app, foodsRepository) as T
    }
}