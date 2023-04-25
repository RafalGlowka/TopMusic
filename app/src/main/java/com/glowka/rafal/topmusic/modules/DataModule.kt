package com.glowka.rafal.topmusic.modules

import com.glowka.rafal.topmusic.data.database.MusicDataBaseBuilder
import com.glowka.rafal.topmusic.data.remote.JSONSerializer
import com.glowka.rafal.topmusic.data.remote.JSONSerializerImpl
import com.glowka.rafal.topmusic.data.remote.OKHttpClientBuilder
import com.glowka.rafal.topmusic.data.remote.RetrofitBuilder
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

  factory {
    OKHttpClientBuilder.create()
  }

  single<JSONSerializer> {
    JSONSerializerImpl()
  }

  single {
    RetrofitBuilder.create()
  }

  single {
    MusicDataBaseBuilder.create(androidContext())
  }

}