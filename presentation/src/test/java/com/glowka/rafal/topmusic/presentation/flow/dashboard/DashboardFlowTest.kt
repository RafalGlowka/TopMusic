package com.glowka.rafal.topmusic.presentation.flow.dashboard

import com.glowka.rafal.topmusic.domain.model.Album
import com.glowka.rafal.topmusic.domain.utils.EmptyParam
import com.glowka.rafal.topmusic.presentation.FlowTest
import com.glowka.rafal.topmusic.presentation.flow.dashboard.list.ListViewModelToFlowInterface
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

class DashboardFlowTest : FlowTest() {

  private lateinit var flow: DashboardFlow

  @Before
  fun prepare() {
    initMocks()
    flow = DashboardFlowImpl()
  }

  @After
  fun finish() {
    confirmVerified(toastService)

    unmockkAll()
    clearAllMocks()
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun showingStartScreen() = runFlowTest {
    // Given

    // When
    flow.start(navigationTester, EmptyParam.EMPTY) {}
    advanceUntilIdle()
    println("test1")

    // Than
    navigationTester.verify(DashboardFlow.Screens.List)
    navigationTester.assert()
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun hidingStartScreen() = runFlowTest {
    // Given

    // When
    flow.start(navigationTester, EmptyParam.EMPTY) {}
    navigationTester.emitScreenEvent(
      DashboardFlow.Screens.List,
      ListViewModelToFlowInterface.Event.Back
    )
    advanceUntilIdle()
    println("test1")

    // Than
    navigationTester.verify(DashboardFlow.Screens.List)
    navigationTester.assert()
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun showingDetailsStartScreen() = runFlowTest {
    // Given
    val ALBUM_ID = "123"
    val ALBUM_NAME = "name"
    val ALBUM_URL = "test.com/test/test.jpg"

    val album = Album(
      id = ALBUM_ID,
      name = ALBUM_NAME,
      url = ALBUM_URL,
    )

    // When
    flow.start(navigationTester, EmptyParam.EMPTY) {}
    navigationTester.emitScreenEvent(
      DashboardFlow.Screens.List,
      ListViewModelToFlowInterface.Event.ShowDetails(album)
    )
    advanceUntilIdle()
    println("test1")

    // Than
    navigationTester.verify(DashboardFlow.Screens.List)
    navigationTester.verify(DashboardFlow.Screens.Details)
    navigationTester.assert()
  }

}