package com.glowka.rafal.topmusic.domain.utils

import app.cash.turbine.test
import com.glowka.rafal.topmusic.domain.usecase.UseCaseResult
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import kotlinx.coroutines.flow.Flow

infix fun <T, U : T> UseCaseResult<T>.shouldBeSuccess(expected: U?) {
  shouldBeTypeOf<UseCaseResult.Success<T>>().data shouldBe expected
}

infix fun <T> UseCaseResult<T>.shouldBeError(expectedMessage: String) {
  shouldBeTypeOf<UseCaseResult.Error>().message shouldBe expectedMessage
}

fun <T, U : T> UseCaseResult<T>.shouldBeSuccess(): T {
  return shouldBeTypeOf<UseCaseResult.Success<T>>().data
}

fun <T> UseCaseResult<T>.shouldBeError(): String {
  return shouldBeTypeOf<UseCaseResult.Error>().message
}

suspend infix fun <T, U : T> Flow<UseCaseResult<T>>.shouldBeSuccess(expected: U?) {
  this.test {
    awaitItem() shouldBeSuccess expected
    awaitComplete()
  }
}

suspend infix fun <T> Flow<UseCaseResult<T>>.shouldBeError(expectedMessage: String) {
  this.test {
    awaitItem() shouldBeError expectedMessage
    awaitComplete()
  }
}