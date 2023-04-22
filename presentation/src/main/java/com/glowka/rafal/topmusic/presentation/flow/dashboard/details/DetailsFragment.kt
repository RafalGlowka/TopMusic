package com.glowka.rafal.topmusic.presentation.flow.dashboard.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.glowka.rafal.topmusic.presentation.architecture.BaseFragment
import com.glowka.rafal.topmusic.presentation.flow.dashboard.details.DetailsViewModelToViewInterface.ViewState
import com.glowka.rafal.topmusic.presentation.flow.dashboard.details.DetailsViewModelToViewInterface.ViewEvents

class DetailsFragment :
  BaseFragment<ViewState, ViewEvents, DetailsViewModelToViewInterface>() {

  override val content: @Composable () -> Unit = {
    val viewState by viewModel.viewState.collectAsState()
    DetailsScreen(
      viewState = viewState,
      onViewEvent = viewModel::onViewEvent
    )
  }
}

