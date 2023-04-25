package com.glowka.rafal.topmusic.presentation.flow.dashboard.list

import app.cash.turbine.test
import com.glowka.rafal.topmusic.domain.architecture.TextResource
import com.glowka.rafal.topmusic.domain.model.Country
import com.glowka.rafal.topmusic.domain.model.album
import com.glowka.rafal.topmusic.domain.repository.FakeMusicRepository
import com.glowka.rafal.topmusic.domain.service.FakeSnackBarService
import com.glowka.rafal.topmusic.domain.utils.EmptyParam
import com.glowka.rafal.topmusic.presentation.ViewModelSpec
import com.glowka.rafal.topmusic.presentation.utils.testScreenEvents
import com.google.android.material.snackbar.Snackbar
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import java.io.IOException

class ListViewModelSpec : ViewModelSpec() {

  init {
    val musicRepository = FakeMusicRepository()
    val snackBarService = FakeSnackBarService()
    fun createViewModel() = ListViewModelImpl(
      musicRepository = musicRepository,
      snackBarService = snackBarService,
    )

    var viewModel = createViewModel()

    describe("initialization") {

      it("start observing album data when init is called") {
        viewModel.viewState.test {
          awaitItem().items.shouldBeEmpty()

          val album = album()
          musicRepository.setReloadResponse(Result.success(listOf(album)))
          musicRepository.reloadFromBackend()
          expectNoEvents()

          viewModel.init(EmptyParam.EMPTY)

          awaitItem().items shouldBe listOf(album)
        }
      }

      it("start observing country data when init is called") {
        viewModel = createViewModel()
        viewModel.viewState.test {
          awaitItem().country shouldBe Country.UnitedStates
          musicRepository.setChangeCountryResponse(Result.success(true))
          musicRepository.changeCountryWithLocalStorage(Country.Poland)
          expectNoEvents()

          viewModel.init(EmptyParam.EMPTY)
          awaitItem() // first update is after subscription of albums
          awaitItem().country shouldBe Country.Poland
        }
      }
    }

    val album = album(id = "1234565")
    val album2 = album(id = "4321", name = "Manna z nieba")

    it("refreshes view state when albums are changed in repository") {
      viewModel.viewState.test {
        awaitItem()

        musicRepository.setReloadResponse(Result.success(listOf(album, album2)))

        musicRepository.reloadFromBackend()

        awaitItem().items shouldBe listOf(album, album2)
      }

    }

    describe("View events") {

      it("refresh list state on refresh event") {
        val album3 = album("album3")
        musicRepository.setReloadResponse(Result.success(listOf(album3)))

        viewModel.viewState.test {
          awaitItem().items shouldBe listOf(album, album2)

          viewModel.onViewEvent(ListViewModelToViewInterface.ViewEvents.RefreshList)

          awaitItem().items shouldBe listOf(album3)
        }
      }

      it("shows refreshing symbol during refresh action") {
        viewModel.viewState.test {
          awaitItem().isRefreshing.shouldBeFalse()
          val album4 = album()
          musicRepository.setReloadResponse(Result.success(listOf(album4)), 1000)

          viewModel.onViewEvent(ListViewModelToViewInterface.ViewEvents.RefreshList)
          awaitItem().isRefreshing.shouldBeTrue()
          advanceTimeBy(2000)
          awaitItem().isRefreshing.shouldBeFalse()
        }
      }

      it("emits ShowDetails event when album is picked") {
        val album4 = album(id = "4")
        viewModel.testScreenEvents {
          viewModel.onViewEvent(ListViewModelToViewInterface.ViewEvents.PickedAlbum(album4))
          awaitItem() shouldBe ListViewModelToFlowInterface.Event.ShowDetails(album4)
        }
      }

      it("emits Back event when system back action is called") {
        viewModel.testScreenEvents {
          viewModel.onBackPressed()
          awaitItem() shouldBe ListViewModelToFlowInterface.Event.Back
        }
      }

      it("emits pick country when pick country action was clicked") {
        viewModel.testScreenEvents {
          viewModel.onViewEvent(ListViewModelToViewInterface.ViewEvents.PickCountry)
          awaitItem() shouldBe ListViewModelToFlowInterface.Event.ChangeCountry(
            country = viewModel.viewState.value.country
          )
        }
      }
    }

    describe("Changing country") {
      it("do not call backend, if albums exists in local storage") {
        snackBarService.events.test {
          viewModel.viewState.test {
            awaitItem().country shouldBe Country.Poland

            musicRepository.setChangeCountryResponse(Result.success(true))
            musicRepository.setReloadResponse(Result.failure(IOException("connection error")))

            viewModel.setCountry(Country.UnitedKingdom)
            awaitItem().country shouldBe Country.UnitedKingdom
          }
          expectNoEvents()
        }
      }

      it("call backend if not exist in local storage") {
        snackBarService.events.test {
          viewModel.viewState.test {
            awaitItem().country shouldBe Country.UnitedKingdom

            musicRepository.setChangeCountryResponse(Result.success(false))
            musicRepository.setReloadResponse(Result.success(listOf(album(id = "1234"))))
            viewModel.setCountry(Country.Germany)
            awaitItem().run {
              country shouldBe Country.Germany
              items shouldBe listOf(album(id = "1234"))
            }
          }
          expectNoEvents()
        }
      }

      it("shows error if failing both - getting data from local storage and backend") {
        musicRepository.setChangeCountryResponse(Result.failure(IllegalStateException("initialization error")))
        musicRepository.setReloadResponse(Result.failure(IOException("connection error")))

        snackBarService.events.test {
          viewModel.viewState.test {
            awaitItem()
            viewModel.setCountry(Country.UnitedStates)
            awaitItem().country shouldBe Country.UnitedStates
          }
          awaitItem().run {
            message shouldBe TextResource.of("connection error")
            duration shouldBe Snackbar.LENGTH_LONG
            actionLabel.shouldBeNull()
          }
        }
      }

      it("shows error if getting data from backend fails") {
        musicRepository.setInitResponse(Result.success(false))
        musicRepository.setReloadResponse(Result.failure(IOException("connection error")))

        snackBarService.events.test {
          viewModel.viewState.test {
            awaitItem()
            viewModel.setCountry(Country.Poland)
            awaitItem().country shouldBe Country.Poland
          }
          awaitItem().run {
            message shouldBe TextResource.of("connection error")
            duration shouldBe Snackbar.LENGTH_LONG
            actionLabel.shouldBeNull()
          }
        }
      }
    }

    describe("issues reporting") {
      it("show error message in case of connection problems during refresh") {
        snackBarService.events.test {
          expectNoEvents()
          musicRepository.setReloadResponse(Result.failure(IOException("Connection problem")))

          viewModel.onViewEvent(ListViewModelToViewInterface.ViewEvents.RefreshList)

          awaitItem().run {
            message shouldBe TextResource.of("Connection problem")
          }

        }
      }
      it("shows error message in case od connection problems during country change") {
        snackBarService.events.test {
          expectNoEvents()
          musicRepository.setChangeCountryResponse(Result.success(false))
          musicRepository.setReloadResponse(Result.failure(IOException("Connection problem")))

          viewModel.setCountry(Country.Germany)

          awaitItem().run {
            message shouldBe TextResource.of("Connection problem")
          }
        }
      }
    }


  }
}