package com.i.vetrinarykotlinapp.activity

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.i.vetrinarykotlinapp.Constants
import com.i.vetrinarykotlinapp.R

/**
 * A Class which is used to Load webView
 */
class WebViewActivity : AppCompatActivity() {

    private lateinit var wv1: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        wv1 = findViewById(R.id.webview)
        wv1.webViewClient = MyBrowser()
        val url: String? = intent.getStringExtra(Constants.CONTENT_URL)


        wv1.settings.loadsImagesAutomatically = true
        wv1.settings.javaScriptEnabled = true
        wv1.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        if (url != null) {
            wv1.loadUrl(url)
        }
    }


    private class MyBrowser : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }
    }
}
