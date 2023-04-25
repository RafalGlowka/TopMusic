package com.glowka.rafal.topmusic.presentation.flow.dashboard.country

import com.glowka.rafal.topmusic.domain.model.Country
import com.glowka.rafal.topmusic.presentation.architecture.BaseViewModel
import com.glowka.rafal.topmusic.presentation.architecture.ScreenEvent
import com.glowka.rafal.topmusic.presentation.architecture.ViewModelToFlowInterface
import com.glowka.rafal.topmusic.presentation.architecture.ViewModelToViewInterface
import com.glowka.rafal.topmusic.domain.architecture.TextResource
import com.glowka.rafal.topmusic.presentation.flow.dashboard.country.CountryDialogViewModelToFlowInterface.Event
import com.glowka.rafal.topmusic.presentation.flow.dashboard.country.CountryDialogViewModelToFlowInterface.Param
import com.glowka.rafal.topmusic.presentation.flow.dashboard.country.CountryDialogViewModelToViewInterface.ViewEvents
import com.glowka.rafal.topmusic.presentation.flow.dashboard.country.CountryDialogViewModelToViewInterface.ViewState
import com.glowka.rafal.topmusic.presentation.model.nameResId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

interface CountryDialogViewModelToFlowInterface : ViewModelToFlowInterface<Param, Event> {
  data class Param(val selected: Country)
  sealed class Event : ScreenEvent {
    data class CountryPicked(val country: Country) : Event()
    object Back : Event()
  }
}

interface CountryDialogViewModelToViewInterface : ViewModelToViewInterface<ViewState, ViewEvents> {
  sealed class ViewEvents {
    data class PickCountry(val position: Int) : ViewEvents()
    object Back : ViewEvents()
  }

  data class ViewState(
    val selectedIndex: Int = 0,
    val items: List<TextResource> = emptyList(),
  )
}

class CountryDialogViewModelImpl : CountryDialogViewModelToFlowInterface,
  CountryDialogViewModelToViewInterface,
  BaseViewModel<Param, Event, ViewState, ViewEvents>(
    backPressedEvent = Event.Back
  ) {
  override val viewState = Country.values().let { countries ->
    MutableStateFlow(
      ViewState(
        selectedIndex = countries.indexOf(Country.UnitedStates),
        items = countries.map { country ->
          TextResource.of(country.nameResId())
        },
      )
    )
  }

  override fun init(param: Param) {
    viewState.update { state ->
      state.copy(selectedIndex = Country.values().indexOf(param.selected))
    }
  }

  override fun onViewEvent(event: ViewEvents) {
    when (event) {
      is ViewEvents.PickCountry -> {
        sendEvent(
          event = Event.CountryPicked(
            country = Country.values()[event.position],
          )
        )
      }
      ViewEvents.Back -> {
        sendEvent(Event.Back)
      }
    }
  }
}