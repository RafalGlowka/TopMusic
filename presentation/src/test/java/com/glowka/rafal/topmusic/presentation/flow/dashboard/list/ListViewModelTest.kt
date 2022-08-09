package com.glowka.rafal.topmusic.presentation.flow.dashboard.list

import com.glowka.rafal.topmusic.domain.model.Album
import com.glowka.rafal.topmusic.domain.usecase.GetAlbumsUseCase
import com.glowka.rafal.topmusic.domain.usecase.RefreshAlbumsUseCase
import com.glowka.rafal.topmusic.presentation.ViewModelTest
import com.glowka.rafal.topmusic.presentation.utils.EventsRecorder
import com.glowka.rafal.topmusic.presentation.utils.returnsSuccess
import io.mockk.clearAllMocks
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.After
import org.junit.Before
import org.junit.Test

class ListViewModelTest : ViewModelTest() {

  @MockK
  private lateinit var refreshAlbumsUseCase: RefreshAlbumsUseCase

  @MockK
  private lateinit var getAlbumsUseCase: GetAlbumsUseCase

  private lateinit var viewModel: ListViewModelImpl

  @Before
  fun prepare() {
    initMocks()
    viewModel = ListViewModelImpl(
      refreshAlbumsUseCase = refreshAlbumsUseCase,
      getAlbumsUseCase = getAlbumsUseCase,
      stringResolver = stringResolver,
    )
  }

  @After
  fun finish() {
    confirmVerified(refreshAlbumsUseCase)
    confirmVerified(getAlbumsUseCase)
    confirmVerified(toastService)

    unmockkAll()
    clearAllMocks()
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun viewModelOnPickAlbumEvent() = runViewModelTest {
    // Given
    val ALBUM_ID = "123"
    val ALBUM_NAME = "name"
    val ALBUM_URL = "test.com/test/test.jpg"

    val album = Album(
      id = ALBUM_ID,
      name = ALBUM_NAME,
      url = ALBUM_URL,
    )

    val eventRecorder = EventsRecorder<ListViewModelToFlowInterface.Event>(
      ListViewModelToFlowInterface.Event.ShowDetails(album)
    )
    viewModel.onScreenEvent = eventRecorder::listen

//    viewModel.init(EmptyParam.EMPTY)
    // When
    viewModel.onViewEvent(ListViewModelToViewInterface.ViewEvents.PickedAlbum(album))
    advanceUntilIdle()
    println("test1")

    // Than
    eventRecorder.assert()
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun viewModelOnRefreshEvent() = runViewModelTest {
    // Given
    val ALBUM_ID = "123"
    val ALBUM_NAME = "name"
    val ALBUM_URL = "test.com/test/test.jpg"

    val album = Album(
      id = ALBUM_ID,
      name = ALBUM_NAME,
      url = ALBUM_URL,
    )

    every { refreshAlbumsUseCase.invoke(any()) } returnsSuccess listOf(album)

    // When
    viewModel.onViewEvent(ListViewModelToViewInterface.ViewEvents.RefreshList)
    advanceUntilIdle()

    // Than
    verify { refreshAlbumsUseCase.invoke(any()) }
  }

}