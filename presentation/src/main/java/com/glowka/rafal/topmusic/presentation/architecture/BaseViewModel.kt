package com.glowka.rafal.topmusic.presentation.architecture

import com.glowka.rafal.topmusic.domain.utils.CoroutineErrorHandler
import com.glowka.rafal.topmusic.domain.utils.inject
import com.glowka.rafal.topmusic.domain.utils.logE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.Closeable
import kotlin.coroutines.CoroutineContext

interface ViewModelToViewInterface<VIEWSTATE : Any, VIEWEVENT : Any> {
  val viewState: StateFlow<VIEWSTATE>
  fun onViewEvent(event: VIEWEVENT)
  fun onBackPressed(): Boolean
}

interface ViewModelToFlowInterface<INPUT : ScreenInput, OUTPUT : ScreenOutput> {
  var onScreenOutput: (OUTPUT) -> Unit

  fun onInput(input: INPUT)

  fun clear()
}

interface ViewModelInterface<
    INPUT : ScreenInput,
    OUTPUT : ScreenOutput,
    VIEWSTATE : Any,
    VIEWEVENT : Any
    > :
  ViewModelToViewInterface<VIEWSTATE, VIEWEVENT>,
  ViewModelToFlowInterface<INPUT, OUTPUT>

abstract class BaseViewModel<
    INPUT : ScreenInput,
    OUTPUT : ScreenOutput,
    VIEWSTATE : Any,
    VIEWEVENT : Any
    >(
  private val backPressedOutput: OUTPUT?
) :
  ViewModelInterface<INPUT, OUTPUT, VIEWSTATE, VIEWEVENT> {

  override lateinit var onScreenOutput: (OUTPUT) -> Unit

  //  var lifecycleOwner: LifecycleOwner? = null
  private var _viewModelScope: CloseableCoroutineScope? = null
  val viewModelScope: CoroutineScope
    get() {
      if (_viewModelScope == null) {
        _viewModelScope = CloseableCoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
      }
      return _viewModelScope!!
    }

  override fun onInput(input: INPUT) {
    logE("Function onInput(input) should be overrided")    
  }

  protected fun sendOutput(output: OUTPUT) {
    onScreenOutput(output)
  }

  override fun onBackPressed(): Boolean {
    return backPressedOutput?.let { event ->
      launch {
        sendOutput(event)
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