package com.glowka.rafal.topmusic.presentation.flow.intro

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.glowka.rafal.topmusic.domain.service.SnackBarService
import com.glowka.rafal.topmusic.domain.usecase.InitLocalRepositoryUseCase
import com.glowka.rafal.topmusic.domain.usecase.RefreshAlbumsUseCase
import com.glowka.rafal.topmusic.domain.usecase.UseCaseResult
import com.glowka.rafal.topmusic.domain.utils.EmptyParam
import com.glowka.rafal.topmusic.domain.utils.collectUseCase
import com.glowka.rafal.topmusic.domain.utils.logD
import com.glowka.rafal.topmusic.domain.utils.mapSuccess
import com.glowka.rafal.topmusic.domain.utils.onMain
import com.glowka.rafal.topmusic.presentation.architecture.BaseViewModel
import com.glowka.rafal.topmusic.presentation.architecture.ScreenEvent
import com.glowka.rafal.topmusic.presentation.architecture.ViewModelToFlowInterface
import com.glowka.rafal.topmusic.presentation.architecture.ViewModelToViewInterface
import com.glowka.rafal.topmusic.presentation.architecture.launch
import com.glowka.rafal.topmusic.presentation.flow.intro.IntroViewModelToFlowInterface.Event
import com.glowka.rafal.topmusic.presentation.flow.intro.IntroViewModelToViewInterface.State
import com.glowka.rafal.topmusic.presentation.flow.intro.IntroViewModelToViewInterface.ViewEvents
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

interface IntroViewModelToFlowInterface : ViewModelToFlowInterface<EmptyParam, Event> {
  sealed class Event : ScreenEvent {
    object Finished : Event()
  }
}

interface IntroViewModelToViewInterface : ViewModelToViewInterface<State, ViewEvents> {
  data class State(
    val emptyState: EmptyParam = EmptyParam.EMPTY
  )

  sealed class ViewEvents {}
}

class IntroViewModelImpl(
  private val snackBarService: SnackBarService,
  private val initLocalRepositoryUseCase: InitLocalRepositoryUseCase,
  private val refreshAlbumsUseCase: RefreshAlbumsUseCase,
) : IntroViewModelToFlowInterface, IntroViewModelToViewInterface,
  BaseViewModel<EmptyParam, Event, State, ViewEvents>(
    backPressedEvent = null
  ) {

  var animation = false
  var data = false

  override fun init(param: EmptyParam) {
    initialDataFeatch()

    launch {
      delay(4000)
      animation = true
      if (data && animation) showNext()
    }
  }

  @OptIn(FlowPreview::class)
  private fun initialDataFeatch() {
    launch {
      initLocalRepositoryUseCase(param = EmptyParam.EMPTY).flatMapConcat { result ->
        logD("data init was: $result")
        if (result is UseCaseResult.Success<Boolean> && result.data) {
          flowOf(UseCaseResult.Success(true))
        } else {
          refreshAlbumsUseCase(param = EmptyParam.EMPTY).mapSuccess { list -> list.isNotEmpty() }
        }
      }.collectUseCase(
        onSuccess = { result ->
          onMain {
            if (result) {
              data = true
              if (data && animation) showNext()
            } else showError("Something went wrong.")
          }
        },
        onError = { error ->
          // Error decoding should be added to show user a call to action message.
          onMain {
            showError(error.message)
          }
        }
      )
    }
  }

  private fun showError(message: String) {
    snackBarService.showSnackBar(
      message = message,
      duration = Snackbar.LENGTH_INDEFINITE,
      actionLabel = "Retry",
      action = {
        initialDataFeatch()
      }
    )
  }

  private fun showNext() {
    sendEvent(event = Event.Finished)
  }

  override val state: MutableState<State> = mutableStateOf(State())

  override fun onViewEvent(event: ViewEvents) {
    // Nop
  }
}