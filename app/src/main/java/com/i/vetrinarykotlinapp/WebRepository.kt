package com.i.vetrinarykotlinapp

import androidx.lifecycle.MutableLiveData
import com.i.vetrinarykotlinapp.model.Config
import com.i.vetrinarykotlinapp.model.Pet
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class WebRepository {

    private var petslist: ArrayList<Pet> = ArrayList()
    var configdata: MutableLiveData<Resource<List<Config>>>? = null
    var petData: MutableLiveData<Resource<List<Pet>>>? = null
    private val client: OkHttpClient = OkHttpClient()

    fun getConfigUrl(): MutableLiveData<Resource<List<Config>>>? {
        if (configdata == null) {
            configdata = MutableLiveData<Resource<List<Config>>>()
            loadConfigUrl()
        }
        return configdata
    }

    fun getPetUrl(): MutableLiveData<Resource<List<Pet>>>? {
        if (petData == null) {
            petData = MutableLiveData<Resource<List<Pet>>>()
            loadPets()
        }
        return petData
    }

    /*
    *
    * Loads data of Pet from PET_URL using OkHttpClient.
    * Get the data from Json.
     */
    private fun loadPets() {

        val request = Request.Builder()
            .url(Constants.PET_URL)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                petData?.postValue(Resource.error(Constants.ERROR))
                e.printStackTrace()

            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val strResponse = response.body()!!.string()
                    val jsonContact = JSONObject(strResponse)
                    val jsonarrayInfo: JSONArray = jsonContact.getJSONArray(Constants.PETS)
                    val size: Int = jsonarrayInfo.length()
                    petslist = ArrayList()
                    for (i in 0 until size) {
                        val jsonObject: JSONObject = jsonarrayInfo.getJSONObject(i)
                        val model = Pet()
                        model.title = jsonObject.getString(Constants.TITLE)
                        model.imgUrl = jsonObject.getString(Constants.IMAGE_URL)
                        model.contentUrl = jsonObject.getString(Constants.CONTENT_URL)
                        petslist.add(model)
                        petData?.postValue(Resource.success(petslist))
                    }
                } else {
                    petData?.postValue(Resource.error(Constants.NO_DATA_FOUND))
                }
            }

        })

    }

    /**
     * Fetch the Settings from CONFIG_URL using OkHttpClient
     * Get the data from Json.
     */
    private fun loadConfigUrl() {

        val get: Request = Request.Builder()
            .url(Constants.CONFIG_URL)
            .build()
        client.newCall(get).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException) {
                configdata?.postValue(Resource.error(Constants.ERROR))
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
                        json.getJSONObject(Constants.SETTINGS).getBoolean(Constants.IS_CHAT_ENABLED)
                    user.isCallEnabled =
                        json.getJSONObject(Constants.SETTINGS).getBoolean(Constants.IS_CALL_ENABLED)
                    configdata?.postValue(Resource.success(listOf(user)))
                } else {
                    configdata?.postValue(Resource.error(Constants.NO_DATA_FOUND))

                }
            }
        })

    }
}


