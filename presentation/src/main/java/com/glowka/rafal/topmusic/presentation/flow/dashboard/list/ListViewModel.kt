package com.glowka.rafal.topmusic.presentation.flow.dashboard.list

import com.glowka.rafal.topmusic.domain.architecture.TextResource
import com.glowka.rafal.topmusic.domain.model.Album
import com.glowka.rafal.topmusic.domain.model.Country
import com.glowka.rafal.topmusic.domain.repository.MusicRepository
import com.glowka.rafal.topmusic.domain.service.SnackBarService
import com.glowka.rafal.topmusic.domain.utils.EmptyParam
import com.glowka.rafal.topmusic.domain.utils.logD
import com.glowka.rafal.topmusic.presentation.R
import com.glowka.rafal.topmusic.presentation.architecture.BaseViewModel
import com.glowka.rafal.topmusic.presentation.architecture.ScreenEvent
import com.glowka.rafal.topmusic.presentation.architecture.ViewModelToFlowInterface
import com.glowka.rafal.topmusic.presentation.architecture.ViewModelToViewInterface
import com.glowka.rafal.topmusic.presentation.architecture.launch
import com.glowka.rafal.topmusic.presentation.flow.dashboard.list.ListViewModelToFlowInterface.Event
import com.glowka.rafal.topmusic.presentation.flow.dashboard.list.ListViewModelToViewInterface.ViewEvents
import com.glowka.rafal.topmusic.presentation.flow.dashboard.list.ListViewModelToViewInterface.ViewState
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

interface ListViewModelToFlowInterface : ViewModelToFlowInterface<EmptyParam, Event> {
  sealed class Event : ScreenEvent {
    data class ShowDetails(val album: Album) : Event()
    data class ChangeCountry(val country: Country) : Event()
    object Back : Event()
  }

  fun refresh()
  fun setCountry(country: Country)
}

interface ListViewModelToViewInterface : ViewModelToViewInterface<ViewState, ViewEvents> {
  sealed class ViewEvents {
    data class PickedAlbum(val album: Album) : ViewEvents()
    object RefreshList : ViewEvents()
    object PickCountry : ViewEvents()
  }

  data class ViewState(
    val errorMessage: TextResource = TextResource.EMPTY,
    val isRefreshing: Boolean = false,
    val country: Country = Country.UnitedStates,
    val items: List<Album> = emptyList()
  )
}

class ListViewModelImpl(
  private val musicRepository: MusicRepository,
  private val snackBarService: SnackBarService,
) : ListViewModelToViewInterface, ListViewModelToFlowInterface,
  BaseViewModel<EmptyParam, Event, ViewState, ViewEvents>(
    backPressedEvent = Event.Back
  ) {
  override val viewState = MutableStateFlow(ViewState())

  override fun init(param: EmptyParam) {
    musicRepository.albums
      .onEach { albums -> updateList(albums) }
      .launchIn(viewModelScope)
    musicRepository.country
      .onEach { country ->
        viewState.update { state -> state.copy(country = country) }
      }
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
      ViewEvents.PickCountry -> {
        sendEvent(Event.ChangeCountry(country = viewState.value.country))
      }
    }
  }

  private fun updateList(albums: List<Album>) {
    logD("updateList ${albums.size}")
    if (albums.isEmpty()) {
      val errorMessage = TextResource.of(R.string.list_is_empty)
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
          errorMessage = TextResource.EMPTY,
          items = albums,
          isRefreshing = false
        )
      }
    }
  }

  override fun refresh() {
    launch {
      viewState.update { state -> state.copy(isRefreshing = true) }
      musicRepository.reloadFromBackend()
        .onFailure { error ->
          showError(error.message?.let { message -> TextResource.of(message) }
            ?: TextResource.Companion.of(R.string.connection_problem))
        }
      viewState.update { state -> state.copy(isRefreshing = false) }
    }
  }

  override fun setCountry(country: Country) {
    viewModelScope.launch {
      viewState.update { state -> state.copy(isRefreshing = true) }
      musicRepository.changeCountryWithLocalStorage(country)
        .recover { false }
        .onSuccess { result ->
          if (!result) {
            musicRepository.reloadFromBackend()
              .onFailure { error ->
                showError(error.message?.let { message -> TextResource.of(message) }
                  ?: TextResource.Companion.of(R.string.connection_problem))
              }
          }
        }
      viewState.update { state -> state.copy(isRefreshing = false) }
    }
  }

  private fun showError(message: TextResource) {
    snackBarService.showSnackBar(
      message = message,
      duration = Snackbar.LENGTH_LONG,
    )
  }

}