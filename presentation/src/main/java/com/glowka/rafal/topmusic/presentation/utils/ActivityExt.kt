package com.glowka.rafal.topmusic.presentation.utils

import android.app.Activity

fun Activity.setLightTextColor(lightStatusBarTextColor: Boolean = true) {
  if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
    if (lightStatusBarTextColor) {
      window?.decorView?.windowInsetsController?.setSystemBarsAppearance(
        0,
        android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
      )
    } else {
      window?.decorView?.windowInsetsController?.setSystemBarsAppearance(
        android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
        android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
      )
    }
  } else @Suppress("DEPRECATION") {
    val visibilityFlagState = window?.decorView?.systemUiVisibility?.and(
      android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    ) ?: 0
    if (lightStatusBarTextColor) {
      if (visibilityFlagState > 0) {
        window?.decorView?.apply {
          systemUiVisibility -= android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
      }
    } else {
      if (visibilityFlagState == 0) {
        window?.decorView?.apply {
          systemUiVisibility += android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
      }
    }
  }
}

fun Activity.setStatusBarBackgroundColor(color: Int) {
  window.statusBarColor = color
}