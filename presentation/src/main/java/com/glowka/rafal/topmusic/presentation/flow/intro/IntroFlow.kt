package com.glowka.rafal.topmusic.presentation.flow.intro

import com.glowka.rafal.topmusic.domain.utils.EmptyParam
import com.glowka.rafal.topmusic.presentation.architecture.*


@Suppress("MaxLineLength")
interface IntroFlow : Flow<EmptyParam, IntroResult> {

  companion object {
    const val SCOPE_NAME = "Intro"
  }

  sealed class Screens<INPUT: ScreenInput, OUTPUT : ScreenOutput>(
    screenStructure: ScreenStructure<INPUT, OUTPUT, *>
  ) :
    Screen<INPUT, OUTPUT>(flowScopeName = SCOPE_NAME, screenStructure = screenStructure) {
    object Start : Screens<IntroViewModelToFlowInterface.Input, IntroViewModelToFlowInterface.Output>(
      screenStructure = IntroScreenStructure
    )
  }
}