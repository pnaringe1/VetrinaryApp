package com.i.vetrinarykotlinapp

import androidx.lifecycle.MutableLiveData
import com.i.vetrinarykotlinapp.`interface`.GetData
import com.i.vetrinarykotlinapp.model.Config
import com.i.vetrinarykotlinapp.model.Pet
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class WebRepository : GetData {
    private val client: OkHttpClient = Injection.okHttpClient

    /**
     * Fetch the Settings from CONFIG_URL using OkHttpClient
     * Get the data from Json.
     */
    override fun getConfigUrl(): MutableLiveData<Resource<List<Config>>> {
        val configData = MutableLiveData<Resource<List<Config>>>()
        val get: Request = Request.Builder()
            .url(Constants.CONFIG_URL)
            .build()
        client.newCall(get).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException) {
                configData.postValue(Resource.error(Constants.ERROR))
                e.printStackTrace()
            }
            override fun onResponse(call: Call?, response: Response) {
                if (response.isSuccessful) {
                    val myResponse: String = response.body()!!.string()
                    val json = JSONObject(myResponse)
                    val user = Config()
                    user.workHours =
                        json.getJSONObject(Constants.SETTINGS).getString(Constants.WORK_HOURS)
                    user.isChatEnabled =
                        json.getJSONObject(Constants.SETTINGS)
                            .getBoolean(Constants.IS_CHAT_ENABLED)
                    user.isCallEnabled =
                        json.getJSONObject(Constants.SETTINGS)
                            .getBoolean(Constants.IS_CALL_ENABLED)
                    configData.postValue(Resource.success(listOf(user)))
                } else {
                    configData.postValue(Resource.error(Constants.NO_DATA_FOUND))

                }
            }
        })
        return configData
    }

    /*
       *
       * Loads data of Pet from PET_URL using OkHttpClient.
       * Get the data from Json.
        */
    override fun getPetUrl(): MutableLiveData<Resource<List<Pet>>> {
        val petData = MutableLiveData<Resource<List<Pet>>>()
        val request = Request.Builder()
            .url(Constants.PET_URL)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                petData.postValue(Resource.error(Constants.ERROR))
                e.printStackTrace()

            }
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val strResponse = response.body()!!.string()
                    val jsonContact = JSONObject(strResponse)
                    val jsonarrayInfo: JSONArray = jsonContact.getJSONArray(Constants.PETS)
                    val size: Int = jsonarrayInfo.length()
                    val petslist: ArrayList<Pet> = ArrayList()
                    for (i in 0 until size) {
                        val jsonObject: JSONObject = jsonarrayInfo.getJSONObject(i)
                        val model = Pet()
                        model.title = jsonObject.getString(Constants.TITLE)
                        model.imgUrl = jsonObject.getString(Constants.IMAGE_URL)
                        model.contentUrl = jsonObject.getString(Constants.CONTENT_URL)
                        petslist.add(model)
                        petData.postValue(Resource.success(petslist))
                    }
                } else {
                    petData.postValue(Resource.error(Constants.NO_DATA_FOUND))
                }
            }
        })
        return petData
    }
}