package com.glowka.rafal.topmusic.domain.usecase

import kotlinx.coroutines.flow.Flow

sealed class UseCaseResult<out DATA : Any?> {
  data class Error(val message: String) : UseCaseResult<Nothing>()
  data class Success<out DATA : Any?>(val data: DATA) : UseCaseResult<DATA>()

  companion object {
    fun <DATA : Any?> success(data: DATA) = Success(data)
    fun error(message: String) = Error(message)
  }
}

interface UseCase<PARAM, RESULT> {
  operator fun invoke(
    param: PARAM,
  ): Flow<UseCaseResult<RESULT>>
}