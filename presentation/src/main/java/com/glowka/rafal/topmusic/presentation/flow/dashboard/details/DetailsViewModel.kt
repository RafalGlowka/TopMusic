package com.glowka.rafal.topmusic.presentation.flow.dashboard.details

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.glowka.rafal.topmusic.domain.model.Album
import com.glowka.rafal.topmusic.presentation.architecture.BaseViewModel
import com.glowka.rafal.topmusic.presentation.architecture.ScreenEvent
import com.glowka.rafal.topmusic.presentation.architecture.ViewModelToFlowInterface
import com.glowka.rafal.topmusic.presentation.architecture.ViewModelToViewInterface
import com.glowka.rafal.topmusic.presentation.architecture.launch
import com.glowka.rafal.topmusic.presentation.flow.dashboard.details.DetailsViewModelToFlowInterface.Event
import com.glowka.rafal.topmusic.presentation.flow.dashboard.details.DetailsViewModelToFlowInterface.Param
import com.glowka.rafal.topmusic.presentation.flow.dashboard.details.DetailsViewModelToViewInterface.State
import com.glowka.rafal.topmusic.presentation.flow.dashboard.details.DetailsViewModelToViewInterface.ViewEvents

interface DetailsViewModelToFlowInterface : ViewModelToFlowInterface<Param, Event> {
  data class Param(val album: Album)
  sealed class Event : ScreenEvent {
    object Back : Event()
    data class OpenURL(var url: String) : Event()
  }
}

interface DetailsViewModelToViewInterface : ViewModelToViewInterface<State, ViewEvents> {
  data class State(
    val album: Album
  )

  sealed class ViewEvents {
    object Close : ViewEvents()
    object OpenURL : ViewEvents()
  }
}

class DetailsViewModelImpl(
) : DetailsViewModelToViewInterface, DetailsViewModelToFlowInterface,
  BaseViewModel<Param, Event, State, ViewEvents>(
    backPressedEvent = Event.Back
  ) {

  override val state: MutableState<State> = mutableStateOf(State(Album()))

  lateinit var param: Param

  override fun init(param: Param) {
    this.param = param

    state.value = state.value.copy(
      album = param.album
    )
  }

  override fun onViewEvent(event: ViewEvents) {
    when (event) {
      ViewEvents.Close -> {
        launch {
          sendEvent(event = Event.Back)
        }
      }
      ViewEvents.OpenURL -> {
        launch {
          sendEvent(event = Event.OpenURL(param.album.url))
        }
      }
    }

  }

}