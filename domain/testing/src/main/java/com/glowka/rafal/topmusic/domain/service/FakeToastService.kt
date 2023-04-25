package com.glowka.rafal.topmusic.domain.service

import com.glowka.rafal.topmusic.domain.architecture.TextResource
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class FakeToastService : ToastService {

  private val _events = MutableSharedFlow<TextResource>()
  val events: SharedFlow<TextResource> = _events

  override fun showMessage(message: TextResource) {
    MainScope().launch {
      _events.emit(message)
    }
  }
}