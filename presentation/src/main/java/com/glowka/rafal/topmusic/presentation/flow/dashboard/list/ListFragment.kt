package com.glowka.rafal.topmusic.presentation.flow.dashboard.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.glowka.rafal.topmusic.presentation.architecture.BaseFragment
import com.glowka.rafal.topmusic.presentation.flow.dashboard.list.ListViewModelToViewInterface.ViewEvents
import com.glowka.rafal.topmusic.presentation.flow.dashboard.list.ListViewModelToViewInterface.ViewState

class ListFragment : BaseFragment<ViewState, ViewEvents, ListViewModelToViewInterface>() {

  override val content: @Composable () -> Unit = {
    val viewState by viewModel.viewState.collectAsState()
    ListScreen(
      viewState = viewState,
      onViewEvent = viewModel::onViewEvent
    )
  }
}