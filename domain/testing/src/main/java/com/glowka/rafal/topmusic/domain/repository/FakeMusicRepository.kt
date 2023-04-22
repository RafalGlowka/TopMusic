package com.glowka.rafal.topmusic.domain.repository

import com.glowka.rafal.topmusic.domain.model.Album
import com.glowka.rafal.topmusic.domain.usecase.UseCaseResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach

class FakeMusicRepository : MusicRepository {

  private val _initializing = MutableSharedFlow<Unit>()
  val initializing: SharedFlow<Unit> = _initializing

  private val _reloading = MutableSharedFlow<Unit>()
  val reloading: SharedFlow<Unit> = _reloading

  private var initResponse: UseCaseResult<Boolean> = UseCaseResult.error("")
  private var initDelayMs = 0L
  private var reloadResponse: UseCaseResult<List<Album>> =
    UseCaseResult.error("Missing response")
  private var reloadDelayMs = 0L

  override val albums = MutableStateFlow<List<Album>>(emptyList())

  override fun init(): Flow<UseCaseResult<Boolean>> {
    return flowOf(initResponse).onEach { _ ->
      _initializing.emit(Unit)
      delay(initDelayMs)
    }
  }

  override fun reloadFromBackend(): Flow<UseCaseResult<List<Album>>> {
    return flowOf(reloadResponse).onEach { result ->
      _reloading.emit(Unit)
      delay(reloadDelayMs)
      if (result is UseCaseResult.Success<List<Album>>) {
        albums.emit(result.data)
      }
    }
  }

  fun setInitResponse(
    initResponse: UseCaseResult<Boolean>,
    delayMs: Long = 0L,
  ) {
    this.initResponse = initResponse
    this.initDelayMs = delayMs
  }

  fun setReloadResponse(
    reloadResponse: UseCaseResult<List<Album>>,
    delayMs: Long = 0L,
  ) {
    this.reloadResponse = reloadResponse
    this.reloadDelayMs = delayMs
  }
}