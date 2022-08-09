package com.glowka.rafal.topmusic.presentation.flow.dashboard.list

import androidx.compose.runtime.mutableStateOf
import com.glowka.rafal.topmusic.domain.model.Album
import com.glowka.rafal.topmusic.domain.usecase.GetAlbumsUseCase
import com.glowka.rafal.topmusic.domain.usecase.RefreshAlbumsUseCase
import com.glowka.rafal.topmusic.domain.utils.EMPTY
import com.glowka.rafal.topmusic.domain.utils.EmptyParam
import com.glowka.rafal.topmusic.domain.utils.StringResolver
import com.glowka.rafal.topmusic.domain.utils.collectUseCase
import com.glowka.rafal.topmusic.domain.utils.logD
import com.glowka.rafal.topmusic.presentation.R
import com.glowka.rafal.topmusic.presentation.architecture.BaseViewModel
import com.glowka.rafal.topmusic.presentation.architecture.ScreenEvent
import com.glowka.rafal.topmusic.presentation.architecture.ViewModelToFlowInterface
import com.glowka.rafal.topmusic.presentation.architecture.ViewModelToViewInterface
import com.glowka.rafal.topmusic.presentation.architecture.launch
import com.glowka.rafal.topmusic.presentation.flow.dashboard.list.ListViewModelToFlowInterface.Event
import com.glowka.rafal.topmusic.presentation.flow.dashboard.list.ListViewModelToViewInterface.State
import com.glowka.rafal.topmusic.presentation.flow.dashboard.list.ListViewModelToViewInterface.ViewEvents
import com.glowka.rafal.topmusic.presentation.utils.exhaustive

interface ListViewModelToFlowInterface : ViewModelToFlowInterface<EmptyParam, Event> {
  sealed class Event : ScreenEvent {
    data class ShowDetails(val album: Album) : Event()
    object Back : Event()
  }

  fun refresh()
}

interface ListViewModelToViewInterface : ViewModelToViewInterface<State, ViewEvents> {
  sealed class ViewEvents {
    data class PickedAlbum(val album: Album) : ViewEvents()
    object RefreshList : ViewEvents()
  }

  data class State(
    val errorMessage: String = String.EMPTY,
    val isRefreshing: Boolean = false,
    val items: List<Album> = emptyList()
  )
}

class ListViewModelImpl(
  private val stringResolver: StringResolver,
  private val refreshAlbumsUseCase: RefreshAlbumsUseCase,
  private val getAlbumsUseCase: GetAlbumsUseCase,
) : ListViewModelToViewInterface, ListViewModelToFlowInterface,
  BaseViewModel<EmptyParam, Event, State, ViewEvents>(
    backPressedEvent = Event.Back
  ) {
  override val state = mutableStateOf(State())

  override fun init(param: EmptyParam) {
    launch {
      state.value = state.value.copy(
        isRefreshing = true
      )
      getAlbumsUseCase(EmptyParam.EMPTY).collectUseCase(
        onSuccess = { list ->
          updateList(list)
        }
      )
    }
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
    }.exhaustive
  }

  private fun updateList(albums: List<Album>) {
    logD("updateList ${albums.size}")
    if (albums.isEmpty()) {
      val errorMessage = stringResolver(R.string.list_is_empty)
      state.value = state.value.copy(
        items = emptyList(),
        errorMessage = errorMessage,
        isRefreshing = false,
      )
    } else {
      state.value = state.value.copy(
        errorMessage = String.EMPTY,
        items = albums,
        isRefreshing = false
      )
    }
  }

  override fun refresh() {
    launch {
      state.value = state.value.copy(
        isRefreshing = true
      )
      refreshAlbumsUseCase(param = EmptyParam.EMPTY).collectUseCase(
        onSuccess = { list ->
          updateList(list)
        }
      )
    }
  }

}