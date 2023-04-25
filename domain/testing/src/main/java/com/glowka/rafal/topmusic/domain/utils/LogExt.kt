package com.glowka.rafal.topmusic.domain.utils

import android.util.Log
import io.mockk.every
import io.mockk.mockkStatic

fun mockLogs() {
  mockkStatic(Log::class)
  every { Log.v(any(), any()) } returns 0
  every { Log.d(any(), any()) } returns 0
  every { Log.i(any(), any()) } returns 0
  every { Log.e(any(), any()) } returns 0
}