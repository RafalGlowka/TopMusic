package com.glowka.rafal.topmusic.application

import android.app.Application
import com.glowka.rafal.topmusic.domain.utils.logD
import com.glowka.rafal.topmusic.modules.modulesList
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TopMusicApplication : Application() {

  override fun onCreate() {
    super.onCreate()
    initDI()
  }

  private fun initDI() {
    startKoin {
      androidLogger()
      androidContext(this@TopMusicApplication)
      modules(modulesList)
      createEagerInstances()
    }
    logD("Koin initialized")
  }
}