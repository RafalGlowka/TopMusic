package com.glowka.rafal.topmusic.presentation.flow.dashboard

import app.cash.turbine.test
import com.glowka.rafal.topmusic.domain.model.Country
import com.glowka.rafal.topmusic.domain.model.album
import com.glowka.rafal.topmusic.domain.repository.FakeMusicRepository
import com.glowka.rafal.topmusic.domain.repository.MusicRepository
import com.glowka.rafal.topmusic.domain.service.SnackBarService
import com.glowka.rafal.topmusic.domain.utils.EmptyParam
import com.glowka.rafal.topmusic.presentation.FlowSpec
import com.glowka.rafal.topmusic.presentation.architecture.businessFlow
import com.glowka.rafal.topmusic.presentation.architecture.screen
import com.glowka.rafal.topmusic.presentation.flow.dashboard.country.CountryDialogViewModelToFlowInterface
import com.glowka.rafal.topmusic.presentation.flow.dashboard.details.DetailsViewModelToFlowInterface
import com.glowka.rafal.topmusic.presentation.flow.dashboard.list.ListViewModelToFlowInterface
import com.glowka.rafal.topmusic.presentation.service.SnackBarServiceImpl
import com.glowka.rafal.topmusic.presentation.utils.FakeScreenNavigator
import com.glowka.rafal.topmusic.presentation.utils.emitScreenDialogEvent
import com.glowka.rafal.topmusic.presentation.utils.emitScreenEvent
import com.glowka.rafal.topmusic.presentation.utils.shouldBeHideScreenDialog
import com.glowka.rafal.topmusic.presentation.utils.shouldBeNavigationToScreen
import com.glowka.rafal.topmusic.presentation.utils.shouldBeNavigationToScreenDialog
import com.glowka.rafal.topmusic.presentation.utils.shouldBePopBackTo
import com.glowka.rafal.topmusic.presentation.utils.shouldBeStartActivity
import io.kotest.matchers.booleans.shouldBeTrue
import org.koin.core.module.Module

class DashboardFlowSpec : FlowSpec() {

  init {

    fun createFlow() = DashboardFlowImpl()
    val navigator = FakeScreenNavigator()

    it("starts with list screen and terminated on list screen event back") {
      val flow = createFlow()
      var flowTerminated = false
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

    it("Opens system browser if visit button was pressed on list screen") {
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

        awaitItem().shouldBeStartActivity()
      }
    }

    it("Shows country picker if Pick country was clicked on list screen") {
      val flow = createFlow()
      val country = Country.Angola

      navigator.navigationEvents.test {
        flow.start(navigator, EmptyParam.EMPTY) {}
        awaitItem().shouldBeNavigationToScreen(
          screen = DashboardFlow.Screens.List,
          param = EmptyParam.EMPTY
        ).emitScreenEvent(ListViewModelToFlowInterface.Event.ChangeCountry(country))

        awaitItem().shouldBeNavigationToScreenDialog(
          screenDialog = DashboardFlow.ScreenDialogs.Country,
          param = CountryDialogViewModelToFlowInterface.Param(selected = country)
        )
          .emitScreenDialogEvent(CountryDialogViewModelToFlowInterface.Event.CountryPicked(Country.Poland))

        awaitItem().shouldBeHideScreenDialog(
          screenDialog = DashboardFlow.ScreenDialogs.Country
        )
      }
    }
  }

  override fun Module.prepareKoinContext() {
    single<SnackBarService> {
      SnackBarServiceImpl()
    }

    single<MusicRepository> {
      FakeMusicRepository()
    }

    businessFlow(DashboardFlow.SCOPE_NAME) {
      screen(DashboardFlow.Screens.List)
    }
  }
}