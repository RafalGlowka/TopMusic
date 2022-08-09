package com.glowka.rafal.topmusic.presentation.flow.intro

import com.glowka.rafal.topmusic.domain.utils.EmptyParam
import com.glowka.rafal.topmusic.presentation.architecture.BaseFlow
import com.glowka.rafal.topmusic.presentation.architecture.Screen
import com.glowka.rafal.topmusic.presentation.flow.dashboard.DashboardFlow
import com.glowka.rafal.topmusic.presentation.flow.dashboard.DashboardResult
import com.glowka.rafal.topmusic.presentation.utils.exhaustive

sealed class IntroResult {
  object Terminated : IntroResult()
}

class IntroFlowImpl(
  val dashboardFlow: DashboardFlow,
) :
  BaseFlow<EmptyParam, IntroResult>(flowScopeName = IntroFlow.SCOPE_NAME), IntroFlow {

  override fun onStart(param: EmptyParam): Screen<*, *, *> {
    showScreen(
      screen = IntroFlow.Screens.Start,
      param = EmptyParam.EMPTY,
      onEvent = ::onStartEvent
    )
    return IntroFlow.Screens.Start
  }

  private fun onStartEvent(event: IntroViewModelToFlowInterface.Event) {
    when (event) {
      IntroViewModelToFlowInterface.Event.Finished -> showDashboard()
    }.exhaustive
  }

  private fun showDashboard() {
    dashboardFlow.start(navigator = navigator, param = EmptyParam.EMPTY) { result ->
      when (result) {
        DashboardResult.Terminated -> finish(result = IntroResult.Terminated)
      }.exhaustive
    }
  }

}