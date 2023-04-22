package com.glowka.rafal.topmusic.presentation.flow.intro

import androidx.compose.runtime.Composable
import com.glowka.rafal.topmusic.presentation.architecture.BaseFragment
import com.glowka.rafal.topmusic.presentation.flow.intro.IntroViewModelToViewInterface.State
import com.glowka.rafal.topmusic.presentation.flow.intro.IntroViewModelToViewInterface.ViewEvents

class IntroFragment : BaseFragment<State, ViewEvents, IntroViewModelToViewInterface>() {

  override val content: @Composable () -> Unit = {
    IntroScreen()
  }

}