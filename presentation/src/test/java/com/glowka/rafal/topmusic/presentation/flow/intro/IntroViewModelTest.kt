package com.glowka.rafal.topmusic.presentation.flow.intro

import com.glowka.rafal.topmusic.domain.model.Album
import com.glowka.rafal.topmusic.domain.service.SnackBarService
import com.glowka.rafal.topmusic.domain.usecase.InitLocalRepositoryUseCase
import com.glowka.rafal.topmusic.domain.usecase.RefreshAlbumsUseCase
import com.glowka.rafal.topmusic.domain.utils.EmptyParam
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

class IntroViewModelTest : ViewModelTest() {

  @MockK
  private lateinit var snackBarService: SnackBarService

  @MockK
  private lateinit var initLocalRepositoryUseCase: InitLocalRepositoryUseCase

  @MockK
  private lateinit var refreshAlbumsUseCase: RefreshAlbumsUseCase

  private lateinit var viewModel: IntroViewModelImpl

  @Before
  fun prepare() {
    initMocks()
    viewModel = IntroViewModelImpl(
      snackBarService = snackBarService,
      initLocalRepositoryUseCase = initLocalRepositoryUseCase,
      refreshAlbumsUseCase = refreshAlbumsUseCase,
    )
  }

  @After
  fun finish() {
    confirmVerified(snackBarService)
    confirmVerified(initLocalRepositoryUseCase)
    confirmVerified(refreshAlbumsUseCase)

    unmockkAll()
    clearAllMocks()
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun viewModelInitializationWithLocalStorage() = runViewModelTest {
    // Given
    val param = EmptyParam.EMPTY

    every { initLocalRepositoryUseCase.invoke(any()) } returnsSuccess true

    val eventRecorder =
      EventsRecorder<IntroViewModelToFlowInterface.Event>(IntroViewModelToFlowInterface.Event.Finished)
    viewModel.onScreenEvent = eventRecorder::listen

    // When
    viewModel.init(param)
    advanceUntilIdle()

    // Than
    eventRecorder.assert()
    verify { initLocalRepositoryUseCase.invoke(any()) }
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun viewModelInitializationWithoutLocalStorage() = runViewModelTest {
    // Given
    val ALBUM_ID = "123"
    val ALBUM_NAME = "name"
    val ALBUM_URL = "test.com/test/test.jpg"

    val album = Album(
      id = ALBUM_ID,
      name = ALBUM_NAME,
      url = ALBUM_URL,
    )

    val param = EmptyParam.EMPTY

    every { initLocalRepositoryUseCase.invoke(any()) } returnsSuccess false
    every { refreshAlbumsUseCase.invoke(any()) } returnsSuccess listOf(album)

    val eventRecorder =
      EventsRecorder<IntroViewModelToFlowInterface.Event>(IntroViewModelToFlowInterface.Event.Finished)
    viewModel.onScreenEvent = eventRecorder::listen

    // When
    viewModel.init(param)
    advanceUntilIdle()

    // Than
    eventRecorder.assert()
    verify { initLocalRepositoryUseCase.invoke(any()) }
    verify { refreshAlbumsUseCase.invoke(any()) }
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun viewModelInitializationError() = runViewModelTest {
    // Given
    val param = EmptyParam.EMPTY

    every { initLocalRepositoryUseCase.invoke(any()) } returnsSuccess false
    every { refreshAlbumsUseCase.invoke(any()) } returnsSuccess emptyList()
    every { snackBarService.showSnackBar(any(), any(), any(), any()) } returns Unit

    val eventRecorder = EventsRecorder<IntroViewModelToFlowInterface.Event>(null)
    viewModel.onScreenEvent = eventRecorder::listen

    // When
    viewModel.init(param)
    advanceUntilIdle()

    // Than
    eventRecorder.assertNoEvent()
    verify { initLocalRepositoryUseCase.invoke(any()) }
    verify { refreshAlbumsUseCase.invoke(any()) }
    verify { snackBarService.showSnackBar(any(), any(), any(), any()) }
  }

}