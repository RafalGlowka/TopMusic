package com.glowka.rafal.topmusic.modules

import com.glowka.rafal.topmusic.data.api.ListApi
import com.glowka.rafal.topmusic.data.mapper.AlbumDsoToAlbumMapper
import com.glowka.rafal.topmusic.data.mapper.AlbumDsoToAlbumMapperImpl
import com.glowka.rafal.topmusic.data.mapper.AlbumDtoToAlbumMapper
import com.glowka.rafal.topmusic.data.mapper.AlbumDtoToAlbumMapperImpl
import com.glowka.rafal.topmusic.data.mapper.AlbumToAlbumDsoMapper
import com.glowka.rafal.topmusic.data.mapper.AlbumToAlbumDsoMapperImpl
import com.glowka.rafal.topmusic.data.repository.MusicRepositoryImpl
import com.glowka.rafal.topmusic.domain.repository.MusicRepository
import com.glowka.rafal.topmusic.presentation.formatter.ReleaseDateFormatter
import com.glowka.rafal.topmusic.presentation.formatter.ReleaseDateFormatterImpl
import org.koin.dsl.module
import retrofit2.Retrofit

val musicModule = module {

  single<ListApi> {
    get<Retrofit>().create(ListApi::class.java)
  }

  factory<AlbumDtoToAlbumMapper> {
    AlbumDtoToAlbumMapperImpl()
  }

  factory<AlbumDsoToAlbumMapper> {
    AlbumDsoToAlbumMapperImpl()
  }

  factory<AlbumToAlbumDsoMapper> {
    AlbumToAlbumDsoMapperImpl()
  }

  single<MusicRepository> {
    MusicRepositoryImpl(
      listApi = get(),
      albumDtoToAlbumMapper = get(),
      albumDsoToAlbumMapper = get(),
      albumToAlbumDsoMapper = get(),
      musicDatabase = get(),
    )
  }

  factory<ReleaseDateFormatter> {
    ReleaseDateFormatterImpl()
  }

}