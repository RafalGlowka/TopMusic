package com.glowka.rafal.topmusic.presentation.flow.dashboard.list

import com.glowka.rafal.topmusic.presentation.architecture.ScreenStructure
import org.koin.core.scope.Scope

object ListScreenStructure : ScreenStructure<ListViewModelToFlowInterface.Input,
    ListViewModelToFlowInterface.Output, ListViewModelToViewInterface>() {
  override val fragmentClass = ListFragment::class
  override fun Scope.viewModelCreator() = ListViewModelImpl(
    musicRepository = get(),
    snackBarService = get(),
  )

}