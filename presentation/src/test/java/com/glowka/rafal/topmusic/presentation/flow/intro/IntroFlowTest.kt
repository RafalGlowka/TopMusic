package com.glowka.rafal.topmusic.presentation.flow.intro

import com.glowka.rafal.topmusic.domain.utils.EmptyParam
import com.glowka.rafal.topmusic.presentation.FlowTest
import com.glowka.rafal.topmusic.presentation.flow.dashboard.DashboardFlow
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

class IntroFlowTest : FlowTest() {

  @MockK
  private lateinit var dashboardFlow: DashboardFlow

  private lateinit var flow: IntroFlow

  @Before
  fun prepare() {
    initMocks()
    flow = IntroFlowImpl(
      dashboardFlow = dashboardFlow
    )
  }

  @After
  fun finish() {
    confirmVerified(dashboardFlow)
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
    navigationTester.verify(IntroFlow.Screens.Start)
    navigationTester.assert()
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun hidingStartScreen() = runFlowTest {
    // Given

    every { dashboardFlow.start(navigationTester, any(), any()) } returns Unit
    // When
    flow.start(navigationTester, EmptyParam.EMPTY) {}
    navigationTester.emitScreenEvent(
      IntroFlow.Screens.Start,
      IntroViewModelToFlowInterface.Event.Finished
    )
    advanceUntilIdle()
    println("test1")

    // Than
    navigationTester.verify(IntroFlow.Screens.Start)
    verify { dashboardFlow.start(navigationTester, any(), any()) }
    navigationTester.assert()
  }


}