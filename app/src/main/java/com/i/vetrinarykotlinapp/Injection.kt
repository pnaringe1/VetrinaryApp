package com.i.vetrinarykotlinapp

import okhttp3.OkHttpClient

object Injection {
    val okHttpClient: OkHttpClient by lazy { OkHttpClient() }
}