package com.glowka.rafal.topmusic.presentation.flow.intro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.glowka.rafal.topmusic.presentation.R
import com.glowka.rafal.topmusic.presentation.architecture.BaseFragment
import com.glowka.rafal.topmusic.presentation.flow.intro.IntroViewModelToViewInterface.State
import com.glowka.rafal.topmusic.presentation.flow.intro.IntroViewModelToViewInterface.ViewEvents
import com.glowka.rafal.topmusic.presentation.style.FontSize
import com.glowka.rafal.topmusic.presentation.style.Fonts
import com.google.accompanist.systemuicontroller.rememberSystemUiController


class IntroFragment : BaseFragment<State, ViewEvents, IntroViewModelToViewInterface>() {

  override fun ComposeView.renderState(viewModelState: MutableState<State>) {
    setContent {
      val systemUiController = rememberSystemUiController()
      val useDarkIcons = MaterialTheme.colors.isLight

      MaterialTheme {
        systemUiController.setSystemBarsColor(
          color = Color.Transparent,
          darkIcons = useDarkIcons
        )
        Column(
          modifier = Modifier.padding(10.dp),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally,
        ) {
          val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.music))
          LottieAnimation(
            modifier = Modifier
              .fillMaxWidth(0.5f)
              .fillMaxHeight(0.4f),
            composition = composition,
            iterations = LottieConstants.IterateForever,
          )
          Text(
            text = getString(R.string.app_name),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = FontSize.title,
            fontFamily = Fonts.Bold
          )
        }
      }
    }
  }

}