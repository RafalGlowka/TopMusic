package com.glowka.rafal.topmusic.presentation.flow.dashboard.country

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.glowka.rafal.topmusic.presentation.architecture.BaseBottomSheetDialogFragment
import com.glowka.rafal.topmusic.presentation.flow.dashboard.country.CountryDialogViewModelToViewInterface.ViewEvents
import com.glowka.rafal.topmusic.presentation.flow.dashboard.country.CountryDialogViewModelToViewInterface.ViewState

class CountryDialogFragment :
  BaseBottomSheetDialogFragment<ViewState, ViewEvents, CountryDialogViewModelToViewInterface>() {

  override val content: @Composable () -> Unit = {
    val viewState by viewModel.viewState.collectAsState()
    CountryScreenDialog(
      viewState = viewState,
      onViewEvent = viewModel::onViewEvent
    )
  }
}