package com.glowka.rafal.topmusic.presentation.utils

import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.test
import com.glowka.rafal.topmusic.presentation.architecture.BaseViewModel
import com.glowka.rafal.topmusic.presentation.architecture.ScreenEvent
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class ViewModelScreenEventsMonitor<EVENT : ScreenEvent>(viewModel: BaseViewModel<*, EVENT, *, *>) {
  private val _events = MutableSharedFlow<EVENT>()
  val events: SharedFlow<EVENT> = _events

  init {
    viewModel.onScreenEvent = { event ->
      MainScope().launch {
        _events.emit(event)
      }
    }
  }
}

suspend fun <EVENT : ScreenEvent> BaseViewModel<*, EVENT, *, *>.testScreenEvents(
  validate: suspend ReceiveTurbine<EVENT>.() -> Unit
) {
  ViewModelScreenEventsMonitor(this).events.test(validate = validate)
}
