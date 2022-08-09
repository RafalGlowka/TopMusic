package com.glowka.rafal.topmusic.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {

  fun create(): Retrofit {

    return Retrofit.Builder()
      .baseUrl("https://rss.applemarketingtools.com")
      .addConverterFactory(GsonConverterFactory.create())
      .build()
  }
}