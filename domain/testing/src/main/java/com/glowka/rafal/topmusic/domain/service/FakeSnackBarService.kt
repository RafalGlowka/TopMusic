package com.glowka.rafal.topmusic.domain.service

import android.view.View
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class FakeSnackBarService : SnackBarService {

  data class SnakbarEvent(
    val message: String,
    val duration: Int,
    val actionLabel: String,
    val action: () -> Unit
  )

  private val _events = MutableSharedFlow<SnakbarEvent>()
  val events: SharedFlow<SnakbarEvent> = _events

  override fun attach(rootView: View) {}

  override fun showSnackBar(
    message: String,
    duration: Int,
    actionLabel: String,
    action: () -> Unit
  ) {
    MainScope().launch {
      _events.emit(
        SnakbarEvent(
          message = message,
          duration = duration,
          actionLabel = actionLabel,
          action = action
        )
      )
    }
  }
}