package com.glowka.rafal.topmusic.domain.service

import android.view.View
import com.glowka.rafal.topmusic.domain.architecture.TextResource

interface SnackBarService {
  fun attach(rootView: View) // This should be separated to some ActivityAwareService.
  fun showSnackBar(
    message: TextResource,
    duration: Int,
    actionLabel: TextResource? = null,
    action: (() -> Unit)? = null,
  )
}