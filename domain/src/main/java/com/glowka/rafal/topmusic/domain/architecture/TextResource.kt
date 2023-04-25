package com.glowka.rafal.topmusic.domain.architecture

import androidx.annotation.StringRes
import com.glowka.rafal.topmusic.domain.utils.EMPTY
import java.util.*

sealed interface TextResource {
  class ResourceTextResource internal constructor(@StringRes val messageResId: Int) : TextResource {
    override fun toString(): String {
      return "messageResId = $messageResId"
    }

    override fun equals(other: Any?): Boolean {
      return other is ResourceTextResource && other.messageResId == messageResId
    }

    override fun hashCode(): Int {
      return Objects.hashCode(messageResId)
    }
  }

  class StringTextResource internal constructor(val message: String) : TextResource {
    override fun toString(): String {
      return "message = $message"
    }

    override fun equals(other: Any?): Boolean {
      return other is StringTextResource && other.message == message
    }

    override fun hashCode(): Int {
      return Objects.hashCode(message)
    }
  }

  companion object {
    fun of(@StringRes resId: Int): TextResource = ResourceTextResource(resId)
    fun of(message: String): TextResource = StringTextResource(message)
    val EMPTY: TextResource = StringTextResource(String.EMPTY)
  }
}