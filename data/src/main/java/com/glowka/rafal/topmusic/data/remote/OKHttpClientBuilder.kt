package com.glowka.rafal.topmusic.data.remote

import okhttp3.OkHttpClient

object OKHttpClientBuilder {
  fun create(): OkHttpClient {
    val builder = OkHttpClient.Builder()
    return builder.build()
  }
}