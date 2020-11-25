package com.i.vetrinarykotlinapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.i.vetrinarykotlinapp.*
import com.i.vetrinarykotlinapp.utils.Util
import com.i.vetrinarykotlinapp.adapter.PetAdapter
import com.i.vetrinarykotlinapp.model.Pet
import com.i.vetrinarykotlinapp.viewModel.ConfigViewModel
import com.i.vetrinarykotlinapp.viewModel.ConfigViewModelFactory
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var btnChat: Button
    private lateinit var btnCall: Button
    private lateinit var textViewWorkingHours: TextView
    private lateinit var textViewErrorMessage: TextView

    private lateinit var configViewModel: ConfigViewModel
    private lateinit var petListDetails: RecyclerView
    private lateinit var webRepository: WebRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        btnChat = findViewById(R.id.btn_chat)
        btnCall = findViewById(R.id.btn_call)
        textViewWorkingHours = findViewById(R.id.textView_officeHrs)
        petListDetails = findViewById(R.id.recyclerView)
        textViewErrorMessage = findViewById(R.id.error_message)
        this.webRepository = WebRepository(Injection.okHttpClient)

        configViewModel = ViewModelProvider(
            this,
            ConfigViewModelFactory(webRepository)
        ).get(ConfigViewModel::class.java)

        btnChat.setOnClickListener {
            onBtnClicks()
        }
        btnCall.setOnClickListener {
            onBtnClicks()
        }
        setConfigDetails()
        setPetDetails()
    }

    /**
     * In this method gets the messages to user after call and chat button is clicked.
     */
    private fun onBtnClicks() {
        val workingHrs: String = textViewWorkingHours.text as String

        if (Util.isWorkingHours(workingHrs)) {
            Util.messageDialog(this, getString(R.string.message_outside_work_hrs))
        } else {
            Util.messageDialog(this, getString(R.string.message_within_work_hrs))
        }
    }

    /**
     * This method is used to set button which is true or false.
     * And set the working hours from json.
     */
    private fun setConfigDetails() {

        configViewModel.configs?.observe(this, {
            if (it.status == Resource.Status.SUCCESS) {
                Util.visibility(textViewWorkingHours, textViewErrorMessage)
                if (it.data?.get(0)?.isChatEnabled!! && it.data[0].isCallEnabled) {
                    btnChat.visibility = VISIBLE
                    btnCall.visibility = VISIBLE
                } else if (!it.data[0].isChatEnabled && !it.data[0].isCallEnabled) {
                    btnChat.visibility = GONE
                    btnCall.visibility = GONE
                } else if (it.data[0].isChatEnabled) {
                    btnChat.visibility = VISIBLE
                    btnCall.visibility = GONE
                } else if (it.data[0].isCallEnabled) {
                    btnChat.visibility = GONE
                    btnCall.visibility = VISIBLE
                }
                textViewWorkingHours.text =
                    this.getString(R.string.office_hrs, it.data[0].workHours)
            } else {
                Util.visibilityGone(textViewWorkingHours, textViewErrorMessage)
            }
        })
    }

    /**
     * This method is used to set pet list with icon and info of pets in recyclerView from json.
     * And get the info of pet in webView when clicked on pet.
     */
    private fun setPetDetails() {

        configViewModel.pets?.observe(this, { petlist ->
            if (petlist.status == Resource.Status.SUCCESS) {
                Util.visibility(textViewWorkingHours, textViewErrorMessage)
                petListDetails.layoutManager = LinearLayoutManager(application)
                val adapter = PetAdapter(petlist.data as ArrayList<Pet>) { item ->
                    val intent = Intent(applicationContext, WebViewActivity::class.java)
                    intent.putExtra(Constants.CONTENT_URL, item.contentUrl)
                    startActivity(intent)
                }
                petListDetails.adapter = adapter
            } else {
                Util.visibilityGone(textViewWorkingHours, textViewErrorMessage)
            }
        })
    }
}