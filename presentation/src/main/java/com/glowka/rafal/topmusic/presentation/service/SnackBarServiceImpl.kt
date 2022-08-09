package com.glowka.rafal.topmusic.presentation.service

import android.view.View
import com.glowka.rafal.topmusic.domain.service.SnackBarService
import com.glowka.rafal.topmusic.domain.utils.logE
import com.google.android.material.snackbar.Snackbar

class SnackBarServiceImpl() : SnackBarService {
  private var rootView: View? = null

  override fun attach(rootView: View) {
    this.rootView = rootView
  }

  override fun showSnackBar(
    message: String,
    duration: Int,
    actionLabel: String,
    action: () -> Unit
  ) {
    rootView?.let { rootView ->
      val snackBar = Snackbar.make(rootView, message, duration)
      snackBar.setAction(actionLabel) {
        action()
        snackBar.dismiss()
      }
      snackBar.show()
    } ?: logE("Missing rootView")
  }
}