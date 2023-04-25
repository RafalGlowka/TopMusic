package com.glowka.rafal.topmusic.presentation.service

import android.content.Context
import android.widget.Toast
import com.glowka.rafal.topmusic.domain.architecture.TextResource
import com.glowka.rafal.topmusic.domain.service.ToastService
import com.glowka.rafal.topmusic.presentation.architecture.compose.asString

class ToastServiceImpl(
  val context: Context,
) : ToastService {
  override fun showMessage(message: TextResource) {
    Toast.makeText(context, message.asString(context.resources), Toast.LENGTH_LONG).show()
  }
}