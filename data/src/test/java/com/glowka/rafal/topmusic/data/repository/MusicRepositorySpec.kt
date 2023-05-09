package com.glowka.rafal.topmusic.data.repository

import app.cash.turbine.test
import com.glowka.rafal.topmusic.data.api.ListApi
import com.glowka.rafal.topmusic.data.dao.AlbumDao
import com.glowka.rafal.topmusic.data.database.ConfigKeys
import com.glowka.rafal.topmusic.data.database.MusicDatabase
import com.glowka.rafal.topmusic.data.dso.ConfigDso
import com.glowka.rafal.topmusic.data.dso.albumDso
import com.glowka.rafal.topmusic.data.dto.ListFeedDto
import com.glowka.rafal.topmusic.data.dto.ListResponseDto
import com.glowka.rafal.topmusic.data.dto.albumDto
import com.glowka.rafal.topmusic.data.mapper.AlbumDsoToAlbumMapperImpl
import com.glowka.rafal.topmusic.data.mapper.AlbumDtoToAlbumMapperImpl
import com.glowka.rafal.topmusic.data.mapper.AlbumToAlbumDsoMapperImpl
import com.glowka.rafal.topmusic.domain.model.Country
import com.glowka.rafal.topmusic.domain.utils.mockLogs
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify

class MusicRepositorySpec : DescribeSpec() {

  init {
    mockLogs()

    val listApi: ListApi = mockk()
    val albumDtoToAlbumMapper = AlbumDtoToAlbumMapperImpl()
    val albumDsoToAlbumMapper = AlbumDsoToAlbumMapperImpl()
    val albumToAlbumDsoMapper = AlbumToAlbumDsoMapperImpl()
    val musicDatabase: MusicDatabase = mockk()
    fun createRepository() = MusicRepositoryImpl(
      listApi = listApi,
      albumDtoToAlbumMapper = albumDtoToAlbumMapper,
      albumDsoToAlbumMapper = albumDsoToAlbumMapper,
      albumToAlbumDsoMapper = albumToAlbumDsoMapper,
      musicDatabase = musicDatabase,
    )

    describe("Initialization") {
      it("reads data from database during initialization and notify that some albums were read") {
        val repository = createRepository()
        val countryCode = Country.Angola.countryCode
        val configDso = ConfigDso(key = ConfigKeys.COUNTRY_CODE, value = countryCode)
        val albumDso = albumDso(countryCode = countryCode)
        every {
          musicDatabase.configDao().getAll(ConfigKeys.COUNTRY_CODE)
        } returns listOf(configDso)
        every { musicDatabase.albumDao().getAllWithCountryCode(countryCode) } returns listOf(
          albumDso
        )

        repository.albums.test {
          awaitItem().size shouldBe 0

          repository.initWithLocalStorage() shouldBeSuccess true
          awaitItem().run {
            size shouldBe 1
            get(0).run {
              name shouldBe albumDso.name
              id shouldBe albumDso.id
              artistName shouldBe albumDso.artistName
              name shouldBe albumDso.name
              releaseDate shouldBe albumDso.releaseDate
              artworkUrl100 shouldBe albumDso.artworkUrl100
              genres.size shouldBe albumDso.genres.size
              for (i in genres.indices) {
                genres[i].id shouldBe albumDso.genres[i].id
                genres[i].name shouldBe albumDso.genres[i].name
              }
              copyright shouldBe albumDso.copyright
              url shouldBe albumDso.url
              countryCode shouldBe albumDso.countryCode
            }
          }
        }
        verify { musicDatabase.albumDao().getAllWithCountryCode(countryCode) }
      }

      it("notify that nothing was read from database during initialization if database is empty") {
        val repository = createRepository()
        val countryCode = Country.Angola.countryCode
        val configDso = ConfigDso(key = ConfigKeys.COUNTRY_CODE, value = countryCode)
        every {
          musicDatabase.configDao().getAll(ConfigKeys.COUNTRY_CODE)
        } returns listOf(configDso)
        every { musicDatabase.albumDao().getAllWithCountryCode(countryCode) } returns emptyList()

        repository.albums.test {
          awaitItem().size shouldBe 0

          repository.initWithLocalStorage() shouldBeSuccess false
          expectNoEvents()
        }

        verify { musicDatabase.albumDao().getAllWithCountryCode(countryCode) }
      }
    }


    it("checks data on BE when refresh is called") {
      val repository = createRepository()
      val albumDto = albumDto()
      val copyright = "Some copyringht note"

      coEvery { listApi.getAlbums(any()) } returns ListResponseDto(
        ListFeedDto(listOf(albumDto), copyright)
      )
      val albumDao: AlbumDao = mockk()
      every { albumDao.deleteAll() } returns Unit
      every { musicDatabase.albumDao() } returns albumDao
      every { albumDao.insertAll(any()) } returns Unit
      every { albumDao.deleteAlbumsWithCountryCode(any()) } returns Unit

      repository.reloadFromBackend().shouldBeSuccess().run {
        size shouldBe 1
        get(0).run {
          name shouldBe albumDto.name
          id shouldBe albumDto.id
          artistName shouldBe albumDto.artistName
          name shouldBe albumDto.name
          releaseDate shouldBe albumDto.releaseDate
          artworkUrl100 shouldBe albumDto.artworkUrl100
          genres.size shouldBe albumDto.genres?.size
          for (i in genres.indices) {
            genres[i].id shouldBe albumDto.genres?.get(i)?.id
            genres[i].name shouldBe albumDto.genres?.get(i)?.name
          }
          this.copyright shouldBe copyright
          url shouldBe albumDto.url
        }
      }

      coVerify { listApi.getAlbums(any()) }
      verify { albumDao.deleteAlbumsWithCountryCode(any()) }
      verify { musicDatabase.albumDao() }
      verify { albumDao.insertAll(any()) }
    }

    confirmVerified(
      listApi,
      musicDatabase,
    )
    unmockkAll()
    clearAllMocks()
  }

}