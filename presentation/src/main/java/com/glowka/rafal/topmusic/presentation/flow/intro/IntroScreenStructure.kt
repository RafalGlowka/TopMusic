package com.glowka.rafal.topmusic.presentation.flow.intro

import com.glowka.rafal.topmusic.presentation.architecture.ScreenStructure
import org.koin.core.scope.Scope

object IntroScreenStructure :
  ScreenStructure<IntroViewModelToFlowInterface.Input, IntroViewModelToFlowInterface.Output,
      IntroViewModelToViewInterface>() {
  override val fragmentClass = IntroFragment::class
  override fun Scope.viewModelCreator() = IntroViewModelImpl(
    musicRepository = get(),
    snackBarService = get(),
  )
}