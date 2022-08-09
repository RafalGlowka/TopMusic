package com.glowka.rafal.topmusic.domain.repository

import com.glowka.rafal.topmusic.domain.model.Album
import com.glowka.rafal.topmusic.domain.usecase.UseCaseResult
import kotlinx.coroutines.flow.Flow

interface MusicRepository {
  fun init(): Flow<UseCaseResult<Boolean>>
  fun refreshListFromNetwork(): Flow<UseCaseResult<List<Album>>>
  fun getAlbums(): Flow<UseCaseResult<List<Album>>>
}