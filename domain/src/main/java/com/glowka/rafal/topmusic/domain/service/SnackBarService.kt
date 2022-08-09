package com.glowka.rafal.topmusic.domain.service

import android.view.View

interface SnackBarService {
  fun attach(rootView: View) // This should be separated to some ActivityAwareService.
  fun showSnackBar(message: String, duration: Int, actionLabel: String, action: () -> Unit)
}