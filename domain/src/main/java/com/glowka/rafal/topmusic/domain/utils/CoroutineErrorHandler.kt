package com.glowka.rafal.topmusic.domain.utils

import com.glowka.rafal.topmusic.domain.service.ToastService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

class CoroutineErrorHandler(
  val toastService: ToastService,
) : AbstractCoroutineContextElement(CoroutineExceptionHandler), CoroutineExceptionHandler {
  override fun handleException(context: CoroutineContext, exception: Throwable) {
    exception.message?.let { message ->
      toastService.showMessage(message)
    }
    logE("Coroutine exception", exception)
  }
}