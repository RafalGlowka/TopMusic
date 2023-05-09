package com.glowka.rafal.topmusic.presentation.flow.dashboard.details

import com.glowka.rafal.topmusic.presentation.architecture.ScreenStructure
import org.koin.core.scope.Scope

object DetailsScreenStructure : ScreenStructure<DetailsViewModelToFlowInterface.Input,
    DetailsViewModelToFlowInterface.Output, DetailsViewModelToViewInterface>() {
  override val fragmentClass = DetailsFragment::class
  override fun Scope.viewModelCreator() = DetailsViewModelImpl()
}