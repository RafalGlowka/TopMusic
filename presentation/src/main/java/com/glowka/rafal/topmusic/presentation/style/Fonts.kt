package com.glowka.rafal.topmusic.presentation.style

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.glowka.rafal.topmusic.presentation.R

object Fonts {
  val Regular = FontFamily(
    Font(R.font.regular)
  )
  val Bold = FontFamily(
    Font(R.font.bold),
  )
  val Medium = FontFamily(
    Font(R.font.medium),
  )
  val Semibold = FontFamily(
    Font(R.font.semibold_italic),
  )
}

object FontSize {
  val small: TextUnit = 12.sp
  val base: TextUnit = 16.sp
  val big: TextUnit = 18.sp
  val title: TextUnit = 34.sp
}