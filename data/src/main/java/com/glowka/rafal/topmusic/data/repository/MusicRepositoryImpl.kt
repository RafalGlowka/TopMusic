package com.glowka.rafal.topmusic.data.repository

import com.glowka.rafal.topmusic.data.api.ListApi
import com.glowka.rafal.topmusic.data.database.AlbumDatabase
import com.glowka.rafal.topmusic.data.mapper.AlbumDsoToAlbumMapper
import com.glowka.rafal.topmusic.data.mapper.AlbumDtoToAlbumMapper
import com.glowka.rafal.topmusic.data.mapper.AlbumToAlbumDsoMapper
import com.glowka.rafal.topmusic.domain.model.Album
import com.glowka.rafal.topmusic.domain.repository.MusicRepository
import com.glowka.rafal.topmusic.domain.usecase.UseCaseResult
import com.glowka.rafal.topmusic.domain.utils.EMPTY
import com.glowka.rafal.topmusic.domain.utils.logD
import com.glowka.rafal.topmusic.domain.utils.logE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class MusicRepositoryImpl(
  val listApi: ListApi,
  val albumDtoToAlbumMapper: AlbumDtoToAlbumMapper,
  val albumDsoToAlbumMapper: AlbumDsoToAlbumMapper,
  val albumToAlbumDsoMapper: AlbumToAlbumDsoMapper,
  val albumDatabase: AlbumDatabase,
) : MusicRepository {

  private var albums = listOf<Album>()

  override fun init(): Flow<UseCaseResult<Boolean>> =
    getAlbumsFromLocalStore().map { result -> UseCaseResult.Success(result) }

  @OptIn(FlowPreview::class)
  override fun refreshListFromNetwork(): Flow<UseCaseResult<List<Album>>> =
    flow<UseCaseResult<List<Album>>> {
//      logD("making data call")
      val data = listApi.getAlbums()
//      logD("data: $data")
      val copyright = data.feed?.copyright?: String.EMPTY
      val newList = data.feed?.results?.map { albumDto ->
        albumDtoToAlbumMapper(albumDto to copyright)
      }
      emit(UseCaseResult.Success(newList?.filterNotNull() ?: emptyList()))
    }.catch { throwable ->
//      logE("refreshFromServer error:", throwable)
      emit(UseCaseResult.Error(message = throwable.message ?: "Parser error"))
    }.flatMapConcat { result ->
      if (result is UseCaseResult.Success<List<Album>> && result.data.isNotEmpty()) {
        albums = result.data
        saveAlbumsToLocalStore(result.data).map { result }
      } else {
        flowOf(result)
      }
    }

  override fun getAlbums(): Flow<UseCaseResult<List<Album>>> = flowOf(UseCaseResult.Success(albums))

  private fun getAlbumsFromLocalStore(): Flow<Boolean> = flow {
    albums = albumDatabase.albumDao().getAll().map { albumDso ->
      albumDsoToAlbumMapper(albumDso)
    }
    emit(albums.isNotEmpty())
  }.flowOn(Dispatchers.IO)

  private fun saveAlbumsToLocalStore(list: List<Album>): Flow<Boolean> = flow {
    albumDatabase.albumDao().apply {
      deleteAll()
      insertAll(*(list.map { album ->
        albumToAlbumDsoMapper(album)
      }.toTypedArray()))
    }
    emit(true)
  }.flowOn(Dispatchers.IO)
}