package com.glowka.rafal.topmusic.presentation.flow.intro

import com.glowka.rafal.topmusic.domain.utils.EmptyParam
import com.glowka.rafal.topmusic.presentation.architecture.ScreenStructure
import org.koin.core.scope.Scope

object IntroScreenStructure : ScreenStructure<EmptyParam, IntroViewModelToFlowInterface.Event,
    IntroViewModelToFlowInterface, IntroViewModelToViewInterface>() {
  override val fragmentClass = IntroFragment::class
  override fun Scope.viewModelCreator() = IntroViewModelImpl(
    initLocalRepositoryUseCase = get(),
    refreshAlbumsUseCase = get(),
    snackBarService = get(),
  )
}