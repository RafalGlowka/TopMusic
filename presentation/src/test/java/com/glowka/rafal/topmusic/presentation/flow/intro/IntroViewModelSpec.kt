package com.glowka.rafal.topmusic.presentation.flow.intro

import app.cash.turbine.test
import com.glowka.rafal.topmusic.domain.model.album
import com.glowka.rafal.topmusic.domain.repository.FakeMusicRepository
import com.glowka.rafal.topmusic.domain.service.FakeSnackBarService
import com.glowka.rafal.topmusic.domain.usecase.UseCaseResult
import com.glowka.rafal.topmusic.domain.utils.EmptyParam
import com.glowka.rafal.topmusic.presentation.ViewModelSpec
import com.glowka.rafal.topmusic.presentation.utils.testScreenEvents
import com.google.android.material.snackbar.Snackbar
import io.kotest.matchers.shouldBe

class IntroViewModelSpec : ViewModelSpec() {

  init {
    val snackBarService = FakeSnackBarService()
    val musicRepository = FakeMusicRepository()
    fun createViewModel() = IntroViewModelImpl(
      snackBarService = snackBarService,
      musicRepository = musicRepository,
    )

    describe("initialization") {
      musicRepository.setReloadResponse(UseCaseResult.success(listOf(album())), 10000)

      it("do not call backend, but waits 4 seconds if list was initialized from local storage") {
        val viewModel = createViewModel()
        viewModel.testScreenEvents {
          musicRepository.setInitResponse(UseCaseResult.success(true))

          viewModel.init(param = EmptyParam.EMPTY)
          expectNoEvents()
          advanceTimeBy(3000)
          expectNoEvents()
          advanceTimeBy(1100)
          awaitItem() shouldBe IntroViewModelToFlowInterface.Event.Finished
        }
      }

      it("call backend if not initialized from local storage") {
        val viewModel = createViewModel()
        musicRepository.setInitResponse(UseCaseResult.success(false))
        viewModel.testScreenEvents {
          viewModel.init(param = EmptyParam.EMPTY)
          advanceTimeBy(5000)
          expectNoEvents()
          advanceTimeBy(6000)
          awaitItem() shouldBe IntroViewModelToFlowInterface.Event.Finished
        }
      }

      it("shows error if initialization and calling be fails") {
        val viewModel = createViewModel()
        musicRepository.setInitResponse(UseCaseResult.error("initialization error"))
        musicRepository.setReloadResponse(UseCaseResult.error("connection error"))

        snackBarService.events.test {
          viewModel.init(param = EmptyParam.EMPTY)
          awaitItem().run {
            message shouldBe "connection error"
            duration shouldBe Snackbar.LENGTH_INDEFINITE
            actionLabel shouldBe "Retry"
          }
        }
      }

      it("call repository again if retry action on snackbar is clicked") {
        val viewModel = createViewModel()
        musicRepository.setInitResponse(UseCaseResult.error("initialization error"))
        musicRepository.setReloadResponse(UseCaseResult.error("connection error"))

        snackBarService.events.test {
          viewModel.init(param = EmptyParam.EMPTY)

          var snackbarEvent = awaitItem()
          snackbarEvent.run {
            message shouldBe "connection error"
            duration shouldBe Snackbar.LENGTH_INDEFINITE
            actionLabel shouldBe "Retry"
          }

          musicRepository.setInitResponse(UseCaseResult.success(true))
          musicRepository.initializing.test {
            snackbarEvent.action()
            awaitItem()
          }

          expectNoEvents()
        }
      }

      it("shows error if getting data from BE fails") {
        val viewModel = createViewModel()
        musicRepository.setInitResponse(UseCaseResult.success(false))
        musicRepository.setReloadResponse(UseCaseResult.error("connection error"))

        snackBarService.events.test {
          viewModel.init(param = EmptyParam.EMPTY)
          awaitItem().run {
            message shouldBe "connection error"
            duration shouldBe Snackbar.LENGTH_INDEFINITE
            actionLabel shouldBe "Retry"
          }
        }
      }

      it("call backend again if retry action on snackbar is clicked") {
        val viewModel = createViewModel()
        musicRepository.setInitResponse(UseCaseResult.success(false))
        musicRepository.setReloadResponse(UseCaseResult.error("connection error"))

        snackBarService.events.test {
          viewModel.init(param = EmptyParam.EMPTY)

          var snackbarEvent = awaitItem()
          snackbarEvent.run {
            message shouldBe "connection error"
            duration shouldBe Snackbar.LENGTH_INDEFINITE
            actionLabel shouldBe "Retry"
          }

          musicRepository.setReloadResponse(UseCaseResult.success(listOf(album())))
          musicRepository.reloading.test {
            snackbarEvent.action()
            awaitItem()
          }

          expectNoEvents()
        }
      }


      it("shows error with retry if response from backend is empty") {
        val viewModel = createViewModel()
        musicRepository.setInitResponse(UseCaseResult.success(false))
        musicRepository.setReloadResponse(UseCaseResult.success(emptyList()))

        snackBarService.events.test {
          viewModel.init(param = EmptyParam.EMPTY)

          var snackbarEvent = awaitItem()
          snackbarEvent.run {
            message shouldBe "Something went wrong."
            duration shouldBe Snackbar.LENGTH_INDEFINITE
            actionLabel shouldBe "Retry"
          }

          musicRepository.setReloadResponse(UseCaseResult.success(listOf(album())))
          musicRepository.reloading.test {
            snackbarEvent.action()
            awaitItem()
          }

          expectNoEvents()
        }
      }
    }
  }

}