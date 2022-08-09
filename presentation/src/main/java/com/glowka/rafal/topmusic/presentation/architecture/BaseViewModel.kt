package com.glowka.rafal.topmusic.presentation.architecture

import androidx.compose.runtime.MutableState
import androidx.lifecycle.LifecycleOwner
import com.glowka.rafal.topmusic.domain.utils.CoroutineErrorHandler
import com.glowka.rafal.topmusic.domain.utils.EmptyParam
import com.glowka.rafal.topmusic.domain.utils.inject
import com.glowka.rafal.topmusic.domain.utils.logE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.Closeable
import kotlin.coroutines.CoroutineContext

interface ViewModelToViewInterface<VIEWSTATE : Any, VIEWEVENT : Any> {
  val state: MutableState<VIEWSTATE>
  fun onViewEvent(event: VIEWEVENT)
  fun onBackPressed(): Boolean
}

interface ViewModelToFlowInterface<PARAM : Any, EVENT : ScreenEvent> {
  var onScreenEvent: (EVENT) -> Unit

  fun init(param: PARAM)

  fun clear()
}

interface ViewModelInterface<PARAM : Any, EVENT : ScreenEvent, VIEWSTATE : Any, VIEWEVENT : Any> :
  ViewModelToViewInterface<VIEWSTATE, VIEWEVENT>,
  ViewModelToFlowInterface<PARAM, EVENT>

abstract class BaseViewModel<PARAM : Any, EVENT : ScreenEvent, VIEWSTATE : Any, VIEWEVENT : Any>(
  private val backPressedEvent: EVENT?
) :
  ViewModelInterface<PARAM, EVENT, VIEWSTATE, VIEWEVENT> {

  override lateinit var onScreenEvent: (EVENT) -> Unit

//  var lifecycleOwner: LifecycleOwner? = null
  private var _viewModelScope: CloseableCoroutineScope? = null
  val viewModelScope: CoroutineScope
    get() {
      if (_viewModelScope == null) {
        _viewModelScope = CloseableCoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
      }
      return _viewModelScope!!
    }

  override fun init(param: PARAM) {
    if (param !is EmptyParam) logE("Function init(param) should be overrided")
  }

  protected fun sendEvent(event: EVENT) {
    onScreenEvent(event)
  }

  override fun onBackPressed(): Boolean {
    return backPressedEvent?.let { event ->
      launch {
        sendEvent(event)
      }
      true
    } ?: false
  }

  override fun clear() {
    _viewModelScope?.close()
    _viewModelScope = null
  }
}

internal class CloseableCoroutineScope(context: CoroutineContext) : Closeable, CoroutineScope {
  override val coroutineContext: CoroutineContext = context

  override fun close() {
    coroutineContext.cancel()
  }
}

fun BaseViewModel<*, *, *, *>.launch(
  context: CoroutineContext? = null,
  start: CoroutineStart = CoroutineStart.DEFAULT,
  block: suspend CoroutineScope.() -> Unit
): Job {
  var coroutineContext = context
  if (coroutineContext == null) {
    val coroutineErrorHandler: CoroutineErrorHandler by inject()
    coroutineContext = coroutineErrorHandler
  }
  return viewModelScope.launch(coroutineContext, start, block)
}