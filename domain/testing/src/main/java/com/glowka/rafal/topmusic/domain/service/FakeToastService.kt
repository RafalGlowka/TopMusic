package com.glowka.rafal.topmusic.domain.service

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class FakeToastService : ToastService {

  private val _events = MutableSharedFlow<String>()
  val events: SharedFlow<String> = _events

  override fun showMessage(message: String) {
    MainScope().launch {
      _events.emit(message)
    }
  }
}