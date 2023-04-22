package com.glowka.rafal.topmusic.presentation.flow.dashboard.list

import com.glowka.rafal.topmusic.domain.model.Album
import com.glowka.rafal.topmusic.domain.repository.MusicRepository
import com.glowka.rafal.topmusic.domain.utils.EmptyParam
import com.glowka.rafal.topmusic.domain.utils.collectUseCase
import com.glowka.rafal.topmusic.domain.utils.logD
import com.glowka.rafal.topmusic.presentation.R
import com.glowka.rafal.topmusic.presentation.architecture.BaseViewModel
import com.glowka.rafal.topmusic.presentation.architecture.ScreenEvent
import com.glowka.rafal.topmusic.presentation.architecture.ViewModelToFlowInterface
import com.glowka.rafal.topmusic.presentation.architecture.ViewModelToViewInterface
import com.glowka.rafal.topmusic.presentation.architecture.compose.Text
import com.glowka.rafal.topmusic.presentation.architecture.launch
import com.glowka.rafal.topmusic.presentation.flow.dashboard.list.ListViewModelToFlowInterface.Event
import com.glowka.rafal.topmusic.presentation.flow.dashboard.list.ListViewModelToViewInterface.ViewEvents
import com.glowka.rafal.topmusic.presentation.flow.dashboard.list.ListViewModelToViewInterface.ViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

interface ListViewModelToFlowInterface : ViewModelToFlowInterface<EmptyParam, Event> {
  sealed class Event : ScreenEvent {
    data class ShowDetails(val album: Album) : Event()
    object Back : Event()
  }

  fun refresh()
}

interface ListViewModelToViewInterface : ViewModelToViewInterface<ViewState, ViewEvents> {
  sealed class ViewEvents {
    data class PickedAlbum(val album: Album) : ViewEvents()
    object RefreshList : ViewEvents()
  }

  data class ViewState(
    val errorMessage: Text = Text.EMPTY,
    val isRefreshing: Boolean = false,
    val items: List<Album> = emptyList()
  )
}

class ListViewModelImpl(
  private val musicRepository: MusicRepository,
) : ListViewModelToViewInterface, ListViewModelToFlowInterface,
  BaseViewModel<EmptyParam, Event, ViewState, ViewEvents>(
    backPressedEvent = Event.Back
  ) {
  override val viewState = MutableStateFlow(ViewState())

  override fun init(param: EmptyParam) {
    musicRepository.albums
      .onEach { albums -> updateList(albums) }
      .launchIn(viewModelScope)
  }

  override fun onViewEvent(event: ViewEvents) {
    when (event) {
      is ViewEvents.PickedAlbum -> {
        sendEvent(
          event = Event.ShowDetails(
            album = event.album,
          )
        )
      }
      ViewEvents.RefreshList -> {
        refresh()
      }
    }
  }

  private fun updateList(albums: List<Album>) {
    logD("updateList ${albums.size}")
    if (albums.isEmpty()) {
      val errorMessage = Text.of(R.string.list_is_empty)
      viewState.update { state ->
        state.copy(
          items = emptyList(),
          errorMessage = errorMessage,
          isRefreshing = false,
        )
      }
    } else {
      viewState.update { state ->
        state.copy(
          errorMessage = Text.EMPTY,
          items = albums,
          isRefreshing = false
        )
      }
    }
  }

  override fun refresh() {
    launch {
      viewState.update { state -> state.copy(isRefreshing = true) }
      musicRepository.reloadFromBackend().collectUseCase(
        onSuccess = {
          viewState.update { state -> state.copy(isRefreshing = false) }
        },
        onError = {
          viewState.update { state -> state.copy(isRefreshing = false) }
        }
      )
    }
  }

}