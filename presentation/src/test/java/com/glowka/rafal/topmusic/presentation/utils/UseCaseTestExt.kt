package com.glowka.rafal.topmusic.presentation.utils

import com.glowka.rafal.topmusic.domain.usecase.UseCaseResult
import io.mockk.MockKAdditionalAnswerScope
import io.mockk.MockKStubScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

infix fun <DATA : Any, B> MockKStubScope<Flow<UseCaseResult<DATA>>, B>.returnsSuccess(
  data: DATA
): MockKAdditionalAnswerScope<Flow<UseCaseResult<DATA>>, B> {
  return returns(flowOf(UseCaseResult.Success(data)))
}

infix fun <DATA : Any, B> MockKStubScope<Flow<UseCaseResult<DATA>>, B>.returnsError(
  message: String
): MockKAdditionalAnswerScope<Flow<UseCaseResult<DATA>>, B> {
  return returns(flowOf(UseCaseResult.Error(message)))
}