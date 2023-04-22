package com.glowka.rafal.topmusic.presentation.architecture.compose

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.glowka.rafal.topmusic.domain.utils.EMPTY

sealed interface Text {
  @Composable
  fun asString(): String

  private data class ResourceText(@StringRes val messageResId: Int) : Text {
    @Composable
    override fun asString() = stringResource(id = messageResId)
  }

  private data class StringText(val message: String) : Text {
    @Composable
    override fun asString() = message
  }

  companion object {
    fun of(@StringRes resId: Int): Text = ResourceText(resId)
    fun of(message: String): Text = StringText(message)
    val EMPTY: Text = StringText(String.EMPTY)
  }
}