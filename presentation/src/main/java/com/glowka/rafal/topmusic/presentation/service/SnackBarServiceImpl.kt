package com.glowka.rafal.topmusic.presentation.service

import android.view.View
import com.glowka.rafal.topmusic.domain.architecture.TextResource
import com.glowka.rafal.topmusic.domain.service.SnackBarService
import com.glowka.rafal.topmusic.domain.utils.logE
import com.glowka.rafal.topmusic.presentation.architecture.compose.asString
import com.google.android.material.snackbar.Snackbar

class SnackBarServiceImpl() : SnackBarService {
  private var rootView: View? = null

  override fun attach(rootView: View) {
    this.rootView = rootView
  }

  override fun showSnackBar(
    message: TextResource,
    duration: Int,
    actionLabel: TextResource?,
    action: (() -> Unit)?,
  ) {
    rootView?.let { rootView ->
      val snackBar = Snackbar.make(rootView, message.asString(rootView.resources), duration)
      if (actionLabel != null && action != null) {
        snackBar.setAction(actionLabel.asString(rootView.resources)) {
          action()
          snackBar.dismiss()
        }
      }
      snackBar.show()
    } ?: logE("Missing rootView")
  }
}