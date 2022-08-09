package com.glowka.rafal.topmusic.data.repository

import com.glowka.rafal.topmusic.data.api.ListApi
import com.glowka.rafal.topmusic.data.dao.AlbumDao
import com.glowka.rafal.topmusic.data.database.AlbumDatabase
import com.glowka.rafal.topmusic.data.dso.AlbumDso
import com.glowka.rafal.topmusic.data.dso.GenreDso
import com.glowka.rafal.topmusic.data.dto.AlbumDto
import com.glowka.rafal.topmusic.data.dto.GenreDto
import com.glowka.rafal.topmusic.data.dto.ListFeedDto
import com.glowka.rafal.topmusic.data.dto.ListResponseDto
import com.glowka.rafal.topmusic.data.mapper.AlbumDsoToAlbumMapper
import com.glowka.rafal.topmusic.data.mapper.AlbumDtoToAlbumMapper
import com.glowka.rafal.topmusic.data.mapper.AlbumToAlbumDsoMapper
import com.glowka.rafal.topmusic.data.utils.firstData
import com.glowka.rafal.topmusic.domain.model.Album
import com.glowka.rafal.topmusic.domain.model.Genre
import com.glowka.rafal.topmusic.domain.repository.MusicRepository
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class MusicRepositoryTest {

  @MockK
  private lateinit var listApi: ListApi

  @MockK
  private lateinit var albumDtoToAlbumMapper: AlbumDtoToAlbumMapper

  @MockK
  private lateinit var albumDsoToAlbumMapper: AlbumDsoToAlbumMapper

  @MockK
  private lateinit var albumToAlbumDsoMapper: AlbumToAlbumDsoMapper

  @MockK
  private lateinit var albumDatabase: AlbumDatabase

  lateinit var repository: MusicRepository

  @Before
  fun prepare() {
    MockKAnnotations.init(this)
    repository = MusicRepositoryImpl(
      listApi = listApi,
      albumDtoToAlbumMapper = albumDtoToAlbumMapper,
      albumDsoToAlbumMapper = albumDsoToAlbumMapper,
      albumToAlbumDsoMapper = albumToAlbumDsoMapper,
      albumDatabase = albumDatabase,
    )
  }

  @After
  fun finish() {
    confirmVerified(
      listApi,
      albumDtoToAlbumMapper,
      albumDsoToAlbumMapper,
      albumToAlbumDsoMapper,
      albumDatabase,
    )
    unmockkAll()
    clearAllMocks()
  }

  @Test
  fun checkingInitialization() = runBlocking {
    // Given
    val ALBUM_ID = "124"
    val ALBUM_NAME = "test name"
    val ALBUM_ARTIS_NAME = "artist test"
    val ALBUM_RELEASE_DATE = "artist test"
    val ALBUM_ARTWORK_URL = "artwork url"
    val ALBUM_URL = "test url"
    val ALBUM_GENSE_1_ID = "album gense 1 id"
    val ALBUM_GENSE_1_NAME = "album gense 1 name"
    val ALBUM_GENSE_2_ID = "album gense 2 id"
    val ALBUM_GENSE_2_NAME = "album gense 2 name"
    val COPYRIGHT = "copyright test"

    val albumDso = AlbumDso(
      id = ALBUM_ID,
      name = ALBUM_NAME,
      artistName = ALBUM_ARTIS_NAME,
      releaseDate = ALBUM_RELEASE_DATE,
      artworkUrl100 = ALBUM_ARTWORK_URL,
      genres = listOf(
        GenreDso(ALBUM_GENSE_1_ID, ALBUM_GENSE_1_NAME),
        GenreDso(ALBUM_GENSE_2_ID, ALBUM_GENSE_2_NAME)
      ),
      url = ALBUM_URL,
      copyright = COPYRIGHT,
    )

    val album = Album(
      id = ALBUM_ID,
      name = ALBUM_NAME,
      artistName = ALBUM_ARTIS_NAME,
      releaseDate = ALBUM_RELEASE_DATE,
      artworkUrl100 = ALBUM_ARTWORK_URL,
      genres = listOf(
        Genre(ALBUM_GENSE_1_ID, ALBUM_GENSE_1_NAME),
        Genre(ALBUM_GENSE_2_ID, ALBUM_GENSE_2_NAME)
      ),
      url = ALBUM_URL,
      copyright = COPYRIGHT,
    )

    every { albumDatabase.albumDao().getAll() } returns listOf(albumDso)
    every { albumDsoToAlbumMapper.invoke(albumDso) } returns album

    // When
    val response = repository.init().firstData()

    // Then
    Assert.assertEquals(true, response)
    verify { albumDatabase.albumDao().getAll() }
    verify { albumDsoToAlbumMapper.invoke(albumDso) }
  }

  @Test
  fun checkingRefreshList() = runBlocking {
    // Given
    val ALBUM_ID = "124"
    val ALBUM_NAME = "test name"
    val ALBUM_ARTIS_NAME = "artist test"
    val ALBUM_RELEASE_DATE = "artist test"
    val ALBUM_ARTWORK_URL = "artwork url"
    val ALBUM_URL = "test url"
    val ALBUM_GENSE_1_ID = "album gense 1 id"
    val ALBUM_GENSE_1_NAME = "album gense 1 name"
    val ALBUM_GENSE_2_ID = "album gense 2 id"
    val ALBUM_GENSE_2_NAME = "album gense 2 name"
    val COPYRIGHT = "copyright test"

    val albumDto = AlbumDto(
      id = ALBUM_ID,
      name = ALBUM_NAME,
      artistName = ALBUM_ARTIS_NAME,
      releaseDate = ALBUM_RELEASE_DATE,
      artworkUrl100 = ALBUM_ARTWORK_URL,
      genres = listOf(
        GenreDto(ALBUM_GENSE_1_ID, ALBUM_GENSE_1_NAME),
        GenreDto(ALBUM_GENSE_2_ID, ALBUM_GENSE_2_NAME)
      ),
      url = ALBUM_URL,
    )

    val albumDso = AlbumDso(
      id = ALBUM_ID,
      name = ALBUM_NAME,
      artistName = ALBUM_ARTIS_NAME,
      releaseDate = ALBUM_RELEASE_DATE,
      artworkUrl100 = ALBUM_ARTWORK_URL,
      genres = listOf(
        GenreDso(ALBUM_GENSE_1_ID, ALBUM_GENSE_1_NAME),
        GenreDso(ALBUM_GENSE_2_ID, ALBUM_GENSE_2_NAME)
      ),
      url = ALBUM_URL,
      copyright = COPYRIGHT,
    )

    val album = Album(
      id = ALBUM_ID,
      name = ALBUM_NAME,
      artistName = ALBUM_ARTIS_NAME,
      releaseDate = ALBUM_RELEASE_DATE,
      artworkUrl100 = ALBUM_ARTWORK_URL,
      genres = listOf(
        Genre(ALBUM_GENSE_1_ID, ALBUM_GENSE_1_NAME),
        Genre(ALBUM_GENSE_2_ID, ALBUM_GENSE_2_NAME)
      ),
      url = ALBUM_URL,
      copyright = COPYRIGHT,
    )

    coEvery { listApi.getAlbums() } returns ListResponseDto(
      ListFeedDto(listOf(albumDto), COPYRIGHT)
    )
    every { albumDtoToAlbumMapper.invoke(any()) } returns album
    val albumDao: AlbumDao = mockk()
    every { albumDao.deleteAll() } returns Unit
    every { albumDatabase.albumDao() } returns albumDao
    every { albumToAlbumDsoMapper.invoke(album) } returns albumDso
    every { albumDao.insertAll(any()) } returns Unit

    // When
    val response = repository.refreshListFromNetwork().firstData()

    // Then
    Assert.assertEquals(listOf(album), response)
    coVerify { listApi.getAlbums() }
    verify { albumDtoToAlbumMapper.invoke(any()) }
    verify { albumDao.deleteAll() }
    verify { albumDatabase.albumDao() }
    verify { albumToAlbumDsoMapper.invoke(album) }
    verify { albumDao.insertAll(any()) }
  }

}