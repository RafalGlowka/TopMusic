package com.glowka.rafal.topmusic.presentation

import android.os.Bundle
import androidx.core.view.WindowCompat
import com.glowka.rafal.topmusic.domain.utils.EmptyParam
import com.glowka.rafal.topmusic.domain.utils.inject
import com.glowka.rafal.topmusic.presentation.architecture.BaseActivity
import com.glowka.rafal.topmusic.presentation.architecture.ScreenNavigator
import com.glowka.rafal.topmusic.presentation.flow.intro.IntroFlow
import com.glowka.rafal.topmusic.presentation.flow.intro.IntroResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {

  val scope = CoroutineScope(Dispatchers.Main)
  private val navigator: ScreenNavigator by inject()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)
    setContentView(R.layout.activity_main)

    if (savedInstanceState == null) {
      startMainFlow()
    }
    /*
     TODO: Checking if activity was no restored from state after process restart and we have
     fragment stack without proper scopes and objects in DI
     Warning !!
        It need to be solved before production release, for POC/chalange it's just not supported
        edge case.
     */
  }

  private fun startMainFlow() {
    val introFlow: IntroFlow by inject()
    scope.launch {
      introFlow.start(
        navigator = navigator,
        param = EmptyParam.EMPTY,
      ) { result ->
        when (result) {
          IntroResult.Terminated -> navigator.finishActivity()
        }
      }
    }
  }
}