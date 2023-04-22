package com.glowka.rafal.topmusic.domain.repository

import com.glowka.rafal.topmusic.domain.model.Album
import com.glowka.rafal.topmusic.domain.usecase.UseCaseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface MusicRepository {

  val albums: StateFlow<List<Album>>
  fun init(): Flow<UseCaseResult<Boolean>>
  fun reloadFromBackend(): Flow<UseCaseResult<List<Album>>>
}