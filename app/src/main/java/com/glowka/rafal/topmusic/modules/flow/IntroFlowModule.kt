package com.glowka.rafal.topmusic.modules.flow

import com.glowka.rafal.topmusic.presentation.architecture.businessFlow
import com.glowka.rafal.topmusic.presentation.architecture.screen
import com.glowka.rafal.topmusic.presentation.flow.intro.IntroFlow
import com.glowka.rafal.topmusic.presentation.flow.intro.IntroFlowImpl
import org.koin.dsl.module

val introFeatureModule = module {

  single<IntroFlow> {
    IntroFlowImpl(dashboardFlow = get())
  }

  businessFlow(
    scopeName = IntroFlow.SCOPE_NAME,
  ) {
    screen(screen = IntroFlow.Screens.Start)
  }

}