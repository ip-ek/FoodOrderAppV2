package com.ipk.foodorderappv2.Ui.ViewModels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ipk.foodorderappv2.Repository.BasketRepository

class BasketViewModelProviderFactory(val app: Application,
                                     val basketRepository: BasketRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BasketViewModel(app, basketRepository) as T
    }
}