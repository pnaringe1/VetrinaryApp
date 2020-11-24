package com.i.vetrinarykotlinapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.i.vetrinarykotlinapp.WebRepository

class ConfigViewModelFactory( private val webRepository: WebRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ConfigViewModel(this.webRepository) as T
    }
}