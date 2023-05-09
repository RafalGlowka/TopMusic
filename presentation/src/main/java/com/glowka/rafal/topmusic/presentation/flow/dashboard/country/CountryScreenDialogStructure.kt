package com.glowka.rafal.topmusic.presentation.flow.dashboard.country

import com.glowka.rafal.topmusic.presentation.architecture.ScreenDialogStructure
import org.koin.core.scope.Scope

object CountryScreenDialogStructure :
  ScreenDialogStructure<CountryDialogViewModelToFlowInterface.Input,
      CountryDialogViewModelToFlowInterface.Output, CountryDialogViewModelToViewInterface>() {
  override val fragmentClass = CountryDialogFragment::class
  override fun Scope.viewModelCreator() = CountryDialogViewModelImpl()

}