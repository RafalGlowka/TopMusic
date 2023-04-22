package com.glowka.rafal.topmusic.presentation.flow.dashboard

import android.content.Intent
import app.cash.turbine.test
import com.glowka.rafal.topmusic.domain.model.album
import com.glowka.rafal.topmusic.domain.utils.EmptyParam
import com.glowka.rafal.topmusic.presentation.FlowSpec
import com.glowka.rafal.topmusic.presentation.flow.dashboard.details.DetailsViewModelToFlowInterface
import com.glowka.rafal.topmusic.presentation.flow.dashboard.list.ListViewModelToFlowInterface
import com.glowka.rafal.topmusic.presentation.utils.FakeScreenNavigator
import com.glowka.rafal.topmusic.presentation.utils.emitScreenEvent
import com.glowka.rafal.topmusic.presentation.utils.shouldBeNavigationToScreen
import com.glowka.rafal.topmusic.presentation.utils.shouldBePopBackTo
import com.glowka.rafal.topmusic.presentation.utils.shouldBeStartActivity
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe

class DashboardFlowSpec : FlowSpec() {

  init {

    fun createFlow() = DashboardFlowImpl()
    val navigator = FakeScreenNavigator()

    var flowTerminated = false

    it("starts with list screen and terminated on list screen event back") {
      val flow = createFlow()
      navigator.navigationEvents.test {
        flow.start(navigator, EmptyParam.EMPTY) { result ->
          when (result) {
            DashboardResult.Terminated -> flowTerminated = true
          }
        }
        awaitItem().shouldBeNavigationToScreen(
          screen = DashboardFlow.Screens.List,
          param = EmptyParam.EMPTY
        ).emitScreenEvent(ListViewModelToFlowInterface.Event.Back)
        flowTerminated.shouldBeTrue()
      }
    }

    it("Shows details screen if list screen emit ShowDetails event") {
      val album = album()
      val flow = createFlow()

      navigator.navigationEvents.test {
        flow.start(navigator, EmptyParam.EMPTY) {}
        awaitItem().shouldBeNavigationToScreen(
          screen = DashboardFlow.Screens.List,
          param = EmptyParam.EMPTY
        ).emitScreenEvent(ListViewModelToFlowInterface.Event.ShowDetails(album))

        awaitItem().shouldBeNavigationToScreen(
          screen = DashboardFlow.Screens.Details,
          param = DetailsViewModelToFlowInterface.Param(album = album)
        ).emitScreenEvent(DetailsViewModelToFlowInterface.Event.Back)

        awaitItem().shouldBePopBackTo(screen = DashboardFlow.Screens.List)
      }
    }

    it("Opens system browser it visit button was pressed on Dashboard screen") {
      val album = album()
      val flow = createFlow()
      val url = "www.wp.pl/test"

      navigator.navigationEvents.test {
        flow.start(navigator, EmptyParam.EMPTY) {}
        awaitItem().shouldBeNavigationToScreen(
          screen = DashboardFlow.Screens.List,
          param = EmptyParam.EMPTY
        ).emitScreenEvent(ListViewModelToFlowInterface.Event.ShowDetails(album))

        awaitItem().shouldBeNavigationToScreen(
          screen = DashboardFlow.Screens.Details,
          param = DetailsViewModelToFlowInterface.Param(album = album)
        ).emitScreenEvent(DetailsViewModelToFlowInterface.Event.OpenURL(url))

        awaitItem().shouldBeStartActivity().run {
          println(this)
          data shouldBe ""
        }
      }
    }
  }
}