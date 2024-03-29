package com.glowka.rafal.topmusic.presentation.flow.dashboard

import com.glowka.rafal.topmusic.domain.utils.EmptyParam
import com.glowka.rafal.topmusic.presentation.architecture.Flow
import com.glowka.rafal.topmusic.presentation.architecture.Screen
import com.glowka.rafal.topmusic.presentation.architecture.ScreenDialog
import com.glowka.rafal.topmusic.presentation.architecture.ScreenDialogStructure
import com.glowka.rafal.topmusic.presentation.architecture.ScreenInput
import com.glowka.rafal.topmusic.presentation.architecture.ScreenOutput
import com.glowka.rafal.topmusic.presentation.architecture.ScreenStructure
import com.glowka.rafal.topmusic.presentation.flow.dashboard.country.CountryDialogViewModelToFlowInterface
import com.glowka.rafal.topmusic.presentation.flow.dashboard.country.CountryScreenDialogStructure
import com.glowka.rafal.topmusic.presentation.flow.dashboard.details.DetailsScreenStructure
import com.glowka.rafal.topmusic.presentation.flow.dashboard.details.DetailsViewModelToFlowInterface
import com.glowka.rafal.topmusic.presentation.flow.dashboard.list.ListScreenStructure
import com.glowka.rafal.topmusic.presentation.flow.dashboard.list.ListViewModelToFlowInterface

@Suppress("MaxLineLength")
interface DashboardFlow : Flow<EmptyParam, DashboardResult> {

  companion object {
    const val SCOPE_NAME = "Dashboard"
  }

  sealed class Screens<INPUT : ScreenInput, OUTPUT : ScreenOutput>(
    screenStructure: ScreenStructure<INPUT, OUTPUT, *>
  ) : Screen<INPUT, OUTPUT>(
    flowScopeName = SCOPE_NAME,
    screenStructure = screenStructure
  ) {

    object List :
      Screens<ListViewModelToFlowInterface.Input, ListViewModelToFlowInterface.Output>(
        ListScreenStructure
      )

    object Details :
      Screens<DetailsViewModelToFlowInterface.Input, DetailsViewModelToFlowInterface.Output>(
        DetailsScreenStructure
      )
  }

  sealed class ScreenDialogs<INPUT : ScreenInput, OUTPUT : ScreenOutput>(
    screenStructure: ScreenDialogStructure<INPUT, OUTPUT, *>
  ) :
    ScreenDialog<INPUT, OUTPUT>(
      flowScopeName = SCOPE_NAME,
      screenStructure = screenStructure,
    ) {
    object Country :
      ScreenDialogs<CountryDialogViewModelToFlowInterface.Input, CountryDialogViewModelToFlowInterface.Output>(
        CountryScreenDialogStructure
      )
  }
}
