package com.glowka.rafal.topmusic.data.utils

import com.glowka.rafal.topmusic.domain.usecase.UseCaseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

suspend fun <DATA : Any> Flow<UseCaseResult<DATA>>.firstData(): DATA? {

  return when (val response = this.first()) {
    is UseCaseResult.Success<DATA> -> {
      response.data
    }
    else -> null
  }
}