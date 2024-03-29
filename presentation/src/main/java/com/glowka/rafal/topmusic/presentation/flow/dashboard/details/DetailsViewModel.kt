package com.glowka.rafal.topmusic.presentation.flow.dashboard.details

import com.glowka.rafal.topmusic.domain.model.Album
import com.glowka.rafal.topmusic.presentation.architecture.BaseViewModel
import com.glowka.rafal.topmusic.presentation.architecture.ScreenInput
import com.glowka.rafal.topmusic.presentation.architecture.ScreenOutput
import com.glowka.rafal.topmusic.presentation.architecture.ViewModelToFlowInterface
import com.glowka.rafal.topmusic.presentation.architecture.ViewModelToViewInterface
import com.glowka.rafal.topmusic.presentation.architecture.launch
import com.glowka.rafal.topmusic.presentation.flow.dashboard.details.DetailsViewModelToFlowInterface.Input
import com.glowka.rafal.topmusic.presentation.flow.dashboard.details.DetailsViewModelToFlowInterface.Output
import com.glowka.rafal.topmusic.presentation.flow.dashboard.details.DetailsViewModelToViewInterface.ViewEvents
import com.glowka.rafal.topmusic.presentation.flow.dashboard.details.DetailsViewModelToViewInterface.ViewState
import kotlinx.coroutines.flow.MutableStateFlow

interface DetailsViewModelToFlowInterface : ViewModelToFlowInterface<Input, Output> {

  sealed interface Input : ScreenInput {
    data class Init(val album: Album) : Input
  }

  sealed interface Output : ScreenOutput {
    object Back : Output
    data class OpenURL(var url: String) : Output
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
  BaseViewModel<Input, Output, ViewState, ViewEvents>(
    backPressedOutput = Output.Back
  ) {

  override lateinit var viewState: MutableStateFlow<ViewState>

  lateinit var album: Album

  override fun onInput(input: Input) {
    when (input) {
      is Input.Init -> {
        this.album = input.album
        viewState = MutableStateFlow(ViewState(album))
      }
    }
  }

  override fun onViewEvent(event: ViewEvents) {
    when (event) {
      ViewEvents.Close -> {
        launch {
          sendOutput(output = Output.Back)
        }
      }

      ViewEvents.OpenURL -> {
        launch {
          sendOutput(output = Output.OpenURL(album.url))
        }
      }
    }

  }

}