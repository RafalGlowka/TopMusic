package com.glowka.rafal.topmusic.domain.service

import com.glowka.rafal.topmusic.domain.architecture.TextResource

interface ToastService {
  fun showMessage(message: TextResource)
}