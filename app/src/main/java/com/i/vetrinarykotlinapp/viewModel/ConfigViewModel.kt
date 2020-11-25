package com.i.vetrinarykotlinapp.viewModel

import com.i.vetrinarykotlinapp.Resource
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.i.vetrinarykotlinapp.WebRepository
import com.i.vetrinarykotlinapp.model.Config
import com.i.vetrinarykotlinapp.model.Pet

class ConfigViewModel(webRepository: WebRepository) : ViewModel() {
    var pets: MutableLiveData<Resource<List<Pet>>>? = webRepository.getPetUrl()
    var configs: MutableLiveData<Resource<List<Config>>>? = webRepository.getConfigUrl()
}