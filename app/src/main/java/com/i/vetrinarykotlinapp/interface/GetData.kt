package com.i.vetrinarykotlinapp.`interface`

import androidx.lifecycle.MutableLiveData
import com.i.vetrinarykotlinapp.Resource
import com.i.vetrinarykotlinapp.model.Config
import com.i.vetrinarykotlinapp.model.Pet

interface GetData {

    fun getConfigUrl(): MutableLiveData<Resource<List<Config>>>
    fun getPetUrl(): MutableLiveData<Resource<List<Pet>>>
}