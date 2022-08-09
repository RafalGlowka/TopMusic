package com.glowka.rafal.topmusic.presentation.formatter

import com.glowka.rafal.topmusic.domain.utils.EMPTY
import java.text.SimpleDateFormat

interface ReleaseDateFormatter : Formatter<String, String>

class ReleaseDateFormatterImpl : ReleaseDateFormatter {
  override fun format(data: String): String {
    try {
      val dateFormat = SimpleDateFormat("yyyy-MM-dd")
      val date = dateFormat.parse(data)
      date?: return String.EMPTY

      val dateFormat2 = SimpleDateFormat("MMMM dd, yyyy")

      return "Released ${dateFormat2.format(date)}"
    } catch (t: Throwable) {
      return String.EMPTY
    }
  }

}