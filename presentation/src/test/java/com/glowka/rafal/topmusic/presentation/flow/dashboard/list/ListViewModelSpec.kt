package com.glowka.rafal.topmusic.presentation.flow.dashboard.list

import app.cash.turbine.test
import com.glowka.rafal.topmusic.domain.model.album
import com.glowka.rafal.topmusic.domain.repository.FakeMusicRepository
import com.glowka.rafal.topmusic.domain.usecase.UseCaseResult
import com.glowka.rafal.topmusic.domain.utils.EmptyParam
import com.glowka.rafal.topmusic.presentation.ViewModelSpec
import com.glowka.rafal.topmusic.presentation.utils.testScreenEvents
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.collect

class ListViewModelSpec : ViewModelSpec() {

  init {
    val musicRepository = FakeMusicRepository()
    val viewModel = ListViewModelImpl(
      musicRepository = musicRepository
    )

    describe("initialization") {

      it("gets initial list from repository") {
        viewModel.viewState.test {
          awaitItem().items.shouldBeEmpty()

          val album = album()
          musicRepository.setReloadResponse(UseCaseResult.success(listOf(album)))
          musicRepository.reloadFromBackend().collect()
          expectNoEvents()

          viewModel.init(EmptyParam.EMPTY)

          awaitItem().items shouldBe listOf(album)
        }
      }
    }

    val album = album(id = "1234565")
    val album2 = album(id = "4321", name = "Manna z nieba")

    it("refreshes list when repository state is changed") {
      viewModel.viewState.test {
        awaitItem()

        musicRepository.setReloadResponse(UseCaseResult.success(listOf(album, album2)))

        musicRepository.reloadFromBackend().collect()

        awaitItem().items shouldBe listOf(album, album2)
      }

    }

    describe("View events") {

      it("refresh list state on refresh event") {
        val album3 = album("album3")
        musicRepository.setReloadResponse(UseCaseResult.success(listOf(album3)))

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
          musicRepository.setReloadResponse(UseCaseResult.success(listOf(album4)), 1000)

          viewModel.onViewEvent(ListViewModelToViewInterface.ViewEvents.RefreshList)
          awaitItem().isRefreshing.shouldBeTrue()
          advanceTimeBy(2000)
          awaitItem().isRefreshing.shouldBeFalse()
        }
      }

      it("emits ShowDetails event when album is picked") {
        val album4 = album(id="4")
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
    }
  }
}