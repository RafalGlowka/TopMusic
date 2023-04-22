package com.glowka.rafal.topmusic.presentation.flow.dashboard.details

import com.glowka.rafal.topmusic.domain.model.Album
import com.glowka.rafal.topmusic.presentation.architecture.BaseViewModel
import com.glowka.rafal.topmusic.presentation.architecture.ScreenEvent
import com.glowka.rafal.topmusic.presentation.architecture.ViewModelToFlowInterface
import com.glowka.rafal.topmusic.presentation.architecture.ViewModelToViewInterface
import com.glowka.rafal.topmusic.presentation.architecture.launch
import com.glowka.rafal.topmusic.presentation.flow.dashboard.details.DetailsViewModelToFlowInterface.Event
import com.glowka.rafal.topmusic.presentation.flow.dashboard.details.DetailsViewModelToFlowInterface.Param
import com.glowka.rafal.topmusic.presentation.flow.dashboard.details.DetailsViewModelToViewInterface.ViewState
import com.glowka.rafal.topmusic.presentation.flow.dashboard.details.DetailsViewModelToViewInterface.ViewEvents
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

interface DetailsViewModelToFlowInterface : ViewModelToFlowInterface<Param, Event> {
  data class Param(val album: Album)
  sealed class Event : ScreenEvent {
    object Back : Event()
    data class OpenURL(var url: String) : Event()
  }
}

interface DetailsViewModelToViewInterface : ViewModelToViewInterface<ViewState, ViewEvents> {
  data class ViewState(
    val album: Album
  )

  sealed class ViewEvents {
    object Close : ViewEvents()
    object OpenURL : ViewEvents()
  }
}

class DetailsViewModelImpl : DetailsViewModelToViewInterface, DetailsViewModelToFlowInterface,
  BaseViewModel<Param, Event, ViewState, ViewEvents>(
    backPressedEvent = Event.Back
  ) {

  override val viewState = MutableStateFlow(ViewState(Album()))

  lateinit var param: Param

  override fun init(param: Param) {
    this.param = param

    viewState.update { state -> state.copy(album = param.album) }
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