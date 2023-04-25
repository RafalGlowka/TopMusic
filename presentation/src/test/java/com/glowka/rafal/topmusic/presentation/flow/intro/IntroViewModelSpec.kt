package com.glowka.rafal.topmusic.presentation.flow.intro

import app.cash.turbine.test
import com.glowka.rafal.topmusic.domain.architecture.TextResource
import com.glowka.rafal.topmusic.domain.model.album
import com.glowka.rafal.topmusic.domain.repository.FakeMusicRepository
import com.glowka.rafal.topmusic.domain.service.FakeSnackBarService
import com.glowka.rafal.topmusic.domain.utils.EmptyParam
import com.glowka.rafal.topmusic.presentation.ViewModelSpec
import com.glowka.rafal.topmusic.presentation.utils.testScreenEvents
import com.google.android.material.snackbar.Snackbar
import io.kotest.matchers.shouldBe
import java.io.IOException
import com.glowka.rafal.topmusic.presentation.R

class IntroViewModelSpec : ViewModelSpec() {

  init {
    val snackBarService = FakeSnackBarService()
    val musicRepository = FakeMusicRepository()
    fun createViewModel(): IntroViewModelImpl {
      val viewModel = IntroViewModelImpl(
        snackBarService = snackBarService,
        musicRepository = musicRepository,
      )
      viewModel.init(param = EmptyParam.EMPTY)
      return viewModel
    }

    describe("screen activation") {

      it("do not call backend, but waits 4 seconds if list was initialized from local storage") {
        val viewModel = createViewModel()
        viewModel.testScreenEvents {
          musicRepository.setInitResponse(Result.success(true))
          musicRepository.setReloadResponse(Result.failure(IOException("connection error")))

          viewModel.onViewEvent(IntroViewModelToViewInterface.ViewEvents.ActiveScreen)
          expectNoEvents()
          advanceTimeBy(3000)
          expectNoEvents()
          advanceTimeBy(1100)
          awaitItem() shouldBe IntroViewModelToFlowInterface.Event.Finished
        }
      }

      it("call backend if not initialized from local storage") {
        val viewModel = createViewModel()
        musicRepository.setInitResponse(Result.success(false))
        musicRepository.setReloadResponse(Result.success(listOf(album())), 10000)
        viewModel.testScreenEvents {
          viewModel.onViewEvent(IntroViewModelToViewInterface.ViewEvents.ActiveScreen)
          advanceTimeBy(5000)
          expectNoEvents()
          advanceTimeBy(6000)
          awaitItem() shouldBe IntroViewModelToFlowInterface.Event.Finished
        }
      }

      it("shows error if initialization and calling be fails") {
        val viewModel = createViewModel()
        musicRepository.setInitResponse(Result.failure(IllegalStateException("initialization error")))
        musicRepository.setReloadResponse(Result.failure(IOException("connection error")))

        snackBarService.events.test {
          viewModel.onViewEvent(IntroViewModelToViewInterface.ViewEvents.ActiveScreen)
          awaitItem().run {
            message shouldBe TextResource.of("connection error")
            duration shouldBe Snackbar.LENGTH_INDEFINITE
            actionLabel shouldBe TextResource.of(R.string.retry)
          }
        }
      }

      it("call repository again if retry action on snackbar is clicked") {
        val viewModel = createViewModel()
        musicRepository.setInitResponse(Result.failure(IllegalStateException("initialization error")))
        musicRepository.setReloadResponse(Result.failure(IOException("connection error")))

        snackBarService.events.test {
          viewModel.onViewEvent(IntroViewModelToViewInterface.ViewEvents.ActiveScreen)
          var snackbarEvent = awaitItem()
          snackbarEvent.run {
            message shouldBe TextResource.of("connection error")
            duration shouldBe Snackbar.LENGTH_INDEFINITE
            actionLabel shouldBe TextResource.of(R.string.retry)
          }

          musicRepository.setInitResponse(Result.success(true))
          musicRepository.initializing.test {
            snackbarEvent.action?.let { action -> action() }
            awaitItem()
          }

          expectNoEvents()
        }
      }

      it("shows error if getting data from BE fails") {
        val viewModel = createViewModel()
        musicRepository.setInitResponse(Result.success(false))
        musicRepository.setReloadResponse(Result.failure(IOException("connection error")))

        snackBarService.events.test {
          viewModel.onViewEvent(IntroViewModelToViewInterface.ViewEvents.ActiveScreen)
          awaitItem().run {
            message shouldBe TextResource.of("connection error")
            duration shouldBe Snackbar.LENGTH_INDEFINITE
            actionLabel shouldBe TextResource.of(R.string.retry)
          }
        }
      }

      it("call backend again if retry action on snackbar is clicked") {
        val viewModel = createViewModel()
        musicRepository.setInitResponse(Result.success(false))
        musicRepository.setReloadResponse(Result.failure(IOException("connection error")))

        snackBarService.events.test {
          viewModel.onViewEvent(IntroViewModelToViewInterface.ViewEvents.ActiveScreen)

          var snackbarEvent = awaitItem()
          snackbarEvent.run {
            message shouldBe TextResource.of("connection error")
            duration shouldBe Snackbar.LENGTH_INDEFINITE
            actionLabel shouldBe TextResource.of(R.string.retry)
          }

          musicRepository.setReloadResponse(Result.success(listOf(album())))
          musicRepository.reloading.test {
            snackbarEvent.action?.let { action -> action() }
            awaitItem()
          }

          expectNoEvents()
        }
      }


      it("shows error with retry if response from backend is empty") {
        val viewModel = createViewModel()
        musicRepository.setInitResponse(Result.success(false))
        musicRepository.setReloadResponse(Result.success(emptyList()))

        snackBarService.events.test {
          viewModel.onViewEvent(IntroViewModelToViewInterface.ViewEvents.ActiveScreen)

          var snackbarEvent = awaitItem()
          snackbarEvent.run {
            message shouldBe TextResource.of(R.string.something_went_wrong)
            duration shouldBe Snackbar.LENGTH_INDEFINITE
            actionLabel shouldBe TextResource.of(R.string.retry)
          }

          musicRepository.setReloadResponse(Result.success(listOf(album())))
          musicRepository.reloading.test {
            snackbarEvent.action?.let { action -> action() }
            awaitItem()
          }

          expectNoEvents()
        }
      }
    }
  }

}