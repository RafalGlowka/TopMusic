package com.glowka.rafal.topmusic.presentation.flow.dashboard.list

import com.glowka.rafal.topmusic.domain.utils.EmptyParam
import com.glowka.rafal.topmusic.presentation.architecture.ScreenStructure
import org.koin.core.scope.Scope

object ListScreenStructure : ScreenStructure<EmptyParam, ListViewModelToFlowInterface.Event,
    ListViewModelToFlowInterface, ListViewModelToViewInterface>() {
  override val fragmentClass = ListFragment::class
  override fun Scope.viewModelCreator() = ListViewModelImpl(
    musicRepository = get(),
    snackBarService = get(),
  )

}