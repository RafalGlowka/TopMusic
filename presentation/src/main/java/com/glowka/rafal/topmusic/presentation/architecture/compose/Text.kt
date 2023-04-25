package com.glowka.rafal.topmusic.presentation.architecture.compose

import android.content.res.Resources
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.glowka.rafal.topmusic.domain.architecture.TextResource

@Composable
fun TextResource.asString(): String {
  return when (this) {
    is TextResource.ResourceTextResource -> stringResource(id = messageResId)
    is TextResource.StringTextResource -> message
  }
}

fun TextResource.asString(resources: Resources): String {
  return when (this) {
    is TextResource.ResourceTextResource -> resources.getString(messageResId)
    is TextResource.StringTextResource -> message
  }
}
