package com.glowka.rafal.topmusic.data.mapper

import com.glowka.rafal.topmusic.data.dto.AlbumDto
import com.glowka.rafal.topmusic.data.dto.GenreDto
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class AlbumDtoToAlbumMapperTest {

  lateinit var mapper: AlbumDtoToAlbumMapper

  @Before
  fun prepare() {
    mapper = AlbumDtoToAlbumMapperImpl()
  }

  @After
  fun finish() {
  }

  @Test
  fun parseTest() {
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

    val dataIn = Pair(
      AlbumDto(
        id = ALBUM_ID,
        name = ALBUM_NAME,
        artistName = ALBUM_ARTIS_NAME,
        releaseDate = ALBUM_RELEASE_DATE,
        artworkUrl100 = ALBUM_ARTWORK_URL,
        genres = listOf(
          GenreDto(ALBUM_GENSE_1_ID, ALBUM_GENSE_1_NAME),
          GenreDto(ALBUM_GENSE_2_ID, ALBUM_GENSE_2_NAME)
        ),
        url = ALBUM_URL
      ), COPYRIGHT
    )

    // When
    val response = mapper(dataIn)

    // Then
    Assert.assertEquals(ALBUM_ID, response!!.id)
    Assert.assertEquals(ALBUM_NAME, response.name)
    Assert.assertEquals(ALBUM_ARTIS_NAME, response.artistName)
    Assert.assertEquals(ALBUM_RELEASE_DATE, response.releaseDate)
    Assert.assertEquals(ALBUM_ARTWORK_URL, response.artworkUrl100)
    Assert.assertEquals(ALBUM_URL, response.url)
    Assert.assertEquals(ALBUM_GENSE_1_ID, response.genres[0].id)
    Assert.assertEquals(ALBUM_GENSE_1_NAME, response.genres[0].name)
    Assert.assertEquals(ALBUM_GENSE_2_ID, response.genres[1].id)
    Assert.assertEquals(ALBUM_GENSE_2_NAME, response.genres[1].name)
    Assert.assertEquals(COPYRIGHT, response.copyright)
  }

}