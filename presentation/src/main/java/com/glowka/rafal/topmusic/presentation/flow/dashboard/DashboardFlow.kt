package com.glowka.rafal.topmusic.presentation.flow.dashboard

import com.glowka.rafal.topmusic.domain.utils.EmptyParam
import com.glowka.rafal.topmusic.presentation.architecture.Flow
import com.glowka.rafal.topmusic.presentation.architecture.Screen
import com.glowka.rafal.topmusic.presentation.architecture.ScreenEvent
import com.glowka.rafal.topmusic.presentation.architecture.ScreenStructure
import com.glowka.rafal.topmusic.presentation.architecture.ViewModelToFlowInterface
import com.glowka.rafal.topmusic.presentation.flow.dashboard.details.DetailsScreenStructure
import com.glowka.rafal.topmusic.presentation.flow.dashboard.details.DetailsViewModelToFlowInterface
import com.glowka.rafal.topmusic.presentation.flow.dashboard.list.ListScreenStructure
import com.glowka.rafal.topmusic.presentation.flow.dashboard.list.ListViewModelToFlowInterface

@Suppress("MaxLineLength")
interface DashboardFlow : Flow<EmptyParam, DashboardResult> {

  companion object {
    const val SCOPE_NAME = "Dashboard"
  }

  sealed class Screens<PARAM : Any, EVENT : ScreenEvent,
      VIEWMODEL_TO_FLOW : ViewModelToFlowInterface<PARAM, EVENT>>(screenStructure: ScreenStructure<PARAM, EVENT, VIEWMODEL_TO_FLOW, *>) :
    Screen<PARAM, EVENT, VIEWMODEL_TO_FLOW>(
      flowScopeName = SCOPE_NAME,
      screenStructure = screenStructure
    ) {

    object List :
      Screens<EmptyParam, ListViewModelToFlowInterface.Event, ListViewModelToFlowInterface>(
        ListScreenStructure
      )

    object Details :
      Screens<DetailsViewModelToFlowInterface.Param, DetailsViewModelToFlowInterface.Event, DetailsViewModelToFlowInterface>(
        DetailsScreenStructure
      )
  }
}
