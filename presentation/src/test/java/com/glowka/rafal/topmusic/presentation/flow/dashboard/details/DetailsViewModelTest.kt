package com.glowka.rafal.topmusic.presentation.flow.dashboard.details

import com.glowka.rafal.topmusic.domain.model.Album
import com.glowka.rafal.topmusic.presentation.ViewModelTest
import com.glowka.rafal.topmusic.presentation.utils.EventsRecorder
import io.mockk.clearAllMocks
import io.mockk.confirmVerified
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.After
import org.junit.Before
import org.junit.Test

class DetailsViewModelTest : ViewModelTest() {

  private lateinit var viewModel: DetailsViewModelImpl

  @Before
  fun prepare() {
    initMocks()
    viewModel = DetailsViewModelImpl()
  }

  @After
  fun finish() {
    confirmVerified(toastService)

    unmockkAll()
    clearAllMocks()
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun viewModelOnOpenUrlEvent() = runViewModelTest {
    // Given
    val ALBUM_ID = "123"
    val ALBUM_NAME = "name"
    val ALBUM_URL = "test.com/test/test.jpg"

    val album = Album(
      id = ALBUM_ID,
      name = ALBUM_NAME,
      url = ALBUM_URL,
    )

    val eventRecorder = EventsRecorder<DetailsViewModelToFlowInterface.Event>(
      DetailsViewModelToFlowInterface.Event.OpenURL(ALBUM_URL)
    )
    viewModel.onScreenEvent = eventRecorder::listen

    viewModel.init(DetailsViewModelToFlowInterface.Param(album))
    // When
    viewModel.onViewEvent(DetailsViewModelToViewInterface.ViewEvents.OpenURL)
    advanceUntilIdle()
    println("test1")

    // Than
    eventRecorder.assert()
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun viewModelOnCloseEvent() = runViewModelTest {
    // Given
    val ALBUM_ID = "123"
    val ALBUM_NAME = "name"
    val ALBUM_URL = "test.com/test/test.jpg"

    val album = Album(
      id = ALBUM_ID,
      name = ALBUM_NAME,
      url = ALBUM_URL,
    )

    val eventRecorder =
      EventsRecorder<DetailsViewModelToFlowInterface.Event>(DetailsViewModelToFlowInterface.Event.Back)
    viewModel.onScreenEvent = eventRecorder::listen

    viewModel.init(DetailsViewModelToFlowInterface.Param(album))
    // When
    viewModel.onViewEvent(DetailsViewModelToViewInterface.ViewEvents.Close)
    advanceUntilIdle()

    // Than
    eventRecorder.assert()
  }

}