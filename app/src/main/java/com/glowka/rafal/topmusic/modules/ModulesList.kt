package com.glowka.rafal.topmusic.modules

import com.glowka.rafal.topmusic.modules.flow.dashboardFeatureModule
import com.glowka.rafal.topmusic.modules.flow.introFeatureModule
import org.koin.core.module.Module

val modulesList = listOf<Module>(
  appModule,
  dataModule,
  musicModule,
  introFeatureModule,
  dashboardFeatureModule
)