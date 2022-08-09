package com.glowka.rafal.topmusic.domain.utils

import com.glowka.rafal.topmusic.domain.usecase.UseCaseResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

fun <DATA_IN : Any, DATA_OUT : Any> UseCaseResult<DATA_IN>.mapSuccess(
  mapping: (DATA_IN) -> DATA_OUT
): UseCaseResult<DATA_OUT> {
  return when (this) {
    is UseCaseResult.Error -> this
    is UseCaseResult.Success<DATA_IN> -> {
      try {
        val dataOut = mapping(this.data)
        UseCaseResult.Success<DATA_OUT>(data = dataOut)
      } catch (t: Throwable) {
//        logE("mapping problem", t)
        UseCaseResult.Error(message = t.message ?: "mapping exception")
      }
    }
  }
}

fun <DATA_IN : Any, DATA_OUT : Any> Flow<UseCaseResult<DATA_IN>>.mapSuccess(
  mapping: (DATA_IN) -> DATA_OUT
): Flow<UseCaseResult<DATA_OUT>> {
  return this.map { result -> result.mapSuccess(mapping) }
}

suspend fun <DATA : Any?> Flow<UseCaseResult<DATA>>.collectUseCase(
  onSuccess: (suspend (result: DATA) -> Unit),
  onError: (suspend (UseCaseResult.Error) -> Unit)? = null,
) {
  this.collect { result ->
    when (result) {
      is UseCaseResult.Error -> {
        onError?.invoke(result)
      }
      is UseCaseResult.Success<DATA> -> {
        onSuccess(result.data)
      }
    }
    Unit
  }
}

suspend fun <T> onMain(block: suspend CoroutineScope.() -> T): T =
  withContext(Dispatchers.Main.immediate, block)

suspend fun <T> onIO(block: suspend CoroutineScope.() -> T): T = withContext(Dispatchers.IO, block)