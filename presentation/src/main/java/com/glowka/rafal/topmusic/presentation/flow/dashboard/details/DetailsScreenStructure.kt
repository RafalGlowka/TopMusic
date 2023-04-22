package com.glowka.rafal.topmusic.presentation.flow.dashboard.details

import com.glowka.rafal.topmusic.presentation.architecture.ScreenStructure
import org.koin.core.scope.Scope

object DetailsScreenStructure : ScreenStructure<DetailsViewModelToFlowInterface.Param,
    DetailsViewModelToFlowInterface.Event, DetailsViewModelToFlowInterface,
    DetailsViewModelToViewInterface>() {
  override val fragmentClass = DetailsFragment::class
  override fun Scope.viewModelCreator() = DetailsViewModelImpl()
}