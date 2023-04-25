package com.glowka.rafal.topmusic.data.mapper

import com.glowka.rafal.topmusic.data.dto.AlbumDto
import com.glowka.rafal.topmusic.data.dto.GenreDto
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.property.forAll

class AlbumDtoToAlbumMapperSpec : DescribeSpec() {
  init {

    val mapper = AlbumDtoToAlbumMapperImpl()

    it("maps received data") {

      @Suppress("MaxLineLength")
      forAll(100) { givenAlbumId: String, givenAlbumName: String, givenArtistName: String, givenReleaseDate: String, givenArtworkUrl: String, givenAlbumUrl: String, givenGense1Id: String, givenGense1Name: String, givenGense2Id: String, givenGense2Name: String, givenCopyright: String, givenCountryCode: String ->
        val dataIn = AlbumData(
          album = AlbumDto(
            id = givenAlbumId,
            name = givenAlbumName,
            artistName = givenArtistName,
            releaseDate = givenReleaseDate,
            artworkUrl100 = givenArtworkUrl,
            genres = listOf(
              GenreDto(givenGense1Id, givenGense1Name),
              GenreDto(givenGense2Id, givenGense2Name)
            ),
            url = givenAlbumUrl
          ),
          copyright = givenCopyright,
          countryCode = givenCountryCode
        )

        // When
        val response = mapper(dataIn)

        response.shouldNotBeNull().run {
          id shouldBe givenAlbumId
          name shouldBe givenAlbumName
          artistName shouldBe givenArtistName
          releaseDate shouldBe givenReleaseDate
          artworkUrl100 shouldBe givenArtworkUrl
          url shouldBe givenAlbumUrl
          genres.shouldHaveSize(2)
          genres[0].shouldNotBeNull().run {
            id shouldBe givenGense1Id
            name shouldBe givenGense1Name
          }
          genres[1].shouldNotBeNull().run {
            id shouldBe givenGense2Id
            name shouldBe givenGense2Name
          }
          copyright shouldBe givenCopyright
          countryCode shouldBe givenCountryCode
        }

        true
      }
    }
  }
}