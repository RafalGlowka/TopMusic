package com.glowka.rafal.topmusic.presentation.flow.intro

import com.glowka.rafal.topmusic.domain.architecture.TextResource
import com.glowka.rafal.topmusic.domain.repository.MusicRepository
import com.glowka.rafal.topmusic.domain.service.SnackBarService
import com.glowka.rafal.topmusic.presentation.R
import com.glowka.rafal.topmusic.presentation.architecture.BaseViewModel
import com.glowka.rafal.topmusic.presentation.architecture.ScreenInput
import com.glowka.rafal.topmusic.presentation.architecture.ScreenOutput
import com.glowka.rafal.topmusic.presentation.architecture.ViewModelToFlowInterface
import com.glowka.rafal.topmusic.presentation.architecture.ViewModelToViewInterface
import com.glowka.rafal.topmusic.presentation.architecture.launch
import com.glowka.rafal.topmusic.presentation.flow.intro.IntroViewModelToFlowInterface.Input
import com.glowka.rafal.topmusic.presentation.flow.intro.IntroViewModelToFlowInterface.Output
import com.glowka.rafal.topmusic.presentation.flow.intro.IntroViewModelToViewInterface.State
import com.glowka.rafal.topmusic.presentation.flow.intro.IntroViewModelToViewInterface.ViewEvents
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow

interface IntroViewModelToFlowInterface : ViewModelToFlowInterface<Input, Output> {
  sealed interface Input : ScreenInput
  sealed interface Output : ScreenOutput {
    object Finished : Output
  }
}

interface IntroViewModelToViewInterface : ViewModelToViewInterface<State, ViewEvents> {
  class State
  sealed interface ViewEvents {
    object ActiveScreen : ViewEvents
  }
}

class IntroViewModelImpl(
  private val snackBarService: SnackBarService,
  private val musicRepository: MusicRepository,
) : IntroViewModelToFlowInterface, IntroViewModelToViewInterface,
  BaseViewModel<Input, Output, State, ViewEvents>(
    backPressedOutput = null
  ) {

  var animation = false
  var data = false

  @OptIn(FlowPreview::class)
  private fun loadDataFromStorageOrGetFromBackend() {
    launch {
      musicRepository.initWithLocalStorage()
        .recover { false }
        .mapCatching { result ->
          data = result
          if (!result) {
            musicRepository.reloadFromBackend()
              .map { list -> list.isNotEmpty() }
              .getOrThrow()
          } else {
            true
          }
        }
        .onFailure { error ->
          showError(error.message?.let { message -> TextResource.of(message) }
            ?: TextResource.Companion.of(R.string.initialization_error))
        }
        .onSuccess { result ->
          if (result) {
            if (animation) showNext()
          } else showError(TextResource.Companion.of(R.string.something_went_wrong))
        }
    }
  }

  private fun showError(message: TextResource) {
    snackBarService.showSnackBar(
      message = message,
      duration = Snackbar.LENGTH_INDEFINITE,
      actionLabel = TextResource.of(R.string.retry),
      action = {
        loadDataFromStorageOrGetFromBackend()
      }
    )
  }

  private fun showNext() {
    sendOutput(output = Output.Finished)
  }

  override val viewState = MutableStateFlow(State())

  override fun onViewEvent(event: ViewEvents) {
    when (event) {
      ViewEvents.ActiveScreen -> {
        loadDataFromStorageOrGetFromBackend()

        launch {
          delay(MIN_SHOW_TIME_MS)
          animation = true
          if (data) showNext()
        }
      }
    }
  }

  companion object {
    const val MIN_SHOW_TIME_MS = 4000L
  }
}