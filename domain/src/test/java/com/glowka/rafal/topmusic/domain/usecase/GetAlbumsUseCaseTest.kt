package com.glowka.rafal.topmusic.domain.usecase

import com.glowka.rafal.topmusic.domain.model.Album
import com.glowka.rafal.topmusic.domain.repository.MusicRepository
import com.glowka.rafal.topmusic.domain.utils.EmptyParam
import com.glowka.rafal.topmusic.domain.utils.firstData
import com.glowka.rafal.topmusic.domain.utils.returnsSuccess
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetAlbumsUseCaseTest {

  @MockK
  private lateinit var musicRepository: MusicRepository

  lateinit var useCase: GetAlbumsUseCase

  @Before
  fun prepare() {
    MockKAnnotations.init(this)
    useCase = GetAlbumsUseCaseImpl(
      musicRepository = musicRepository
    )
  }

  @After
  fun finish() {
    confirmVerified(musicRepository)

    unmockkAll()
    clearAllMocks()
  }

  @Test
  fun useCaseIsCallingRepositories() = runBlocking {
    // Given
    val params = EmptyParam.EMPTY

    val list =
      listOf(Album(id = "1", name = "album test 1"), Album(id = "2", name = "album test 2"))
    every { musicRepository.getAlbums() } returnsSuccess list

    // When
    val response = useCase(param = params).firstData()

    // Than
    Assert.assertEquals(list, response)
    verify { musicRepository.getAlbums() }
  }
}