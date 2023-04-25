package com.glowka.rafal.topmusic.domain.service

import android.view.View
import com.glowka.rafal.topmusic.domain.architecture.TextResource
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class FakeSnackBarService : SnackBarService {
  data class SnakbarEvent(
    val message: TextResource,
    val duration: Int,
    val actionLabel: TextResource?,
    val action: (() -> Unit)?,
  )

  private val _events = MutableSharedFlow<SnakbarEvent>()
  val events: SharedFlow<SnakbarEvent> = _events

  override fun attach(rootView: View) {}

  override fun showSnackBar(
    message: TextResource,
    duration: Int,
    actionLabel: TextResource?,
    action: (() -> Unit)?
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