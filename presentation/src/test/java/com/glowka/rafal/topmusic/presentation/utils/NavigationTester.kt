package com.glowka.rafal.topmusic.presentation.utils

import android.content.Intent
import com.glowka.rafal.topmusic.domain.utils.logE
import com.glowka.rafal.topmusic.domain.utils.pop
import com.glowka.rafal.topmusic.domain.utils.push
import com.glowka.rafal.topmusic.presentation.architecture.Screen
import com.glowka.rafal.topmusic.presentation.architecture.ScreenDialog
import com.glowka.rafal.topmusic.presentation.architecture.ScreenEvent
import com.glowka.rafal.topmusic.presentation.architecture.ScreenNavigator
import com.glowka.rafal.topmusic.presentation.architecture.ViewModelToFlowInterface
import org.junit.Assert
import org.koin.core.scope.Scope
import java.lang.AssertionError

data class ScreenNavigationCount<
    PARAM : Any,
    EVENT : ScreenEvent,
    VIEWMODEL_TO_FLOW : ViewModelToFlowInterface<PARAM, EVENT>,
    SCREEN : Screen<PARAM, EVENT, VIEWMODEL_TO_FLOW>
    >(
  val screen: SCREEN,
  val param: PARAM,
  val onEvent: (EVENT) -> Unit,
  var count: Int = 1,
  var visCount: Int = 1,
  var verified: Int = 0
)

data class ScreenDialogNavigationCount<PARAM : Any, EVENT : ScreenEvent>(
  val screenDialog: ScreenDialog<PARAM, EVENT, *>,
  val param: PARAM,
  val onEvent: (EVENT) -> Unit,
  var count: Int = 1,
  var verified: Int = 0,
)

class NavigationTester : ScreenNavigator {

  val screens = mutableListOf<ScreenNavigationCount<*, *, *, *>>()
  val screenDialogs = mutableListOf<ScreenDialogNavigationCount<*, *>>()

  val screenStack = arrayListOf<Screen<*, *, *>>()

  override fun <PARAM : Any, EVENT : ScreenEvent, VIEWMODEL_TO_FLOW : ViewModelToFlowInterface<PARAM, EVENT>> push(
    scope: Scope,
    screen: Screen<PARAM, EVENT, VIEWMODEL_TO_FLOW>,
    param: PARAM,
    onEvent: (EVENT) -> Unit
  ) {
    screenStack.push(screen)
    var sc = screens.firstOrNull { screenCount -> screenCount.screen == screen }
    if (sc == null) {
      sc = ScreenNavigationCount(screen, param, onEvent)
      screens.add(sc)
    } else {
      sc.visCount += 1
      sc.count += 1
    }
  }

  private fun hideScreen(screen: Screen<*, *, *>) {
    var sc = screens.firstOrNull { screenCount -> screenCount.screen == screen }
    if (sc == null) {
      logE("Screen not found")
    } else {
      sc.visCount--
    }
  }

  override fun popBack(screen: Screen<*, *, *>) {
    if (screenStack.isNotEmpty()) {
      for (i in screenStack.size - 1 downTo 0) {
        if (screenStack[i] == screen) {
          for (i2 in screenStack.size - 1 downTo i)
            screenStack.pop()?.let { sc -> hideScreen(sc) }
          break
        }
      }
    }

  }

  override fun popBackTo(screen: Screen<*, *, *>) {
    if (screenStack.isNotEmpty()) {
      for (i in screenStack.size - 1 downTo 0) {
        if (screenStack[i] == screen) {
          for (i2 in screenStack.size - 1 downTo i + 1)
            screenStack.pop()?.let { sc -> hideScreen(sc) }
          break
        }
      }
    }
  }

  override fun <PARAM : Any, EVENT : ScreenEvent, VIEWMODEL_TO_FLOW : ViewModelToFlowInterface<PARAM, EVENT>> showDialog(
    scope: Scope,
    screenDialog: ScreenDialog<PARAM, EVENT, VIEWMODEL_TO_FLOW>,
    param: PARAM,
    onEvent: (EVENT) -> Unit
  ) {
    var scd =
      screenDialogs.firstOrNull { screenDialogCount -> screenDialogCount.screenDialog == screenDialog }
    if (scd == null) {
      scd = ScreenDialogNavigationCount(screenDialog, param, onEvent)
      screenDialogs.add(scd)
    } else {
      scd.count += 1
    }
  }

  override fun hideDialog(screen: ScreenDialog<*, *, *>) {
  }

  override fun startActivity(intent: Intent) {
  }

  override fun finishActivity() {
  }

  fun <
      PARAM : Any,
      EVENT : ScreenEvent,
      VIEWMODEL_TO_FLOW : ViewModelToFlowInterface<PARAM, EVENT>,
      SCREEN : Screen<PARAM, EVENT, VIEWMODEL_TO_FLOW>
      > emitScreenEvent(
    screen : SCREEN,
    event: EVENT
  ) {
    var sc = screens.firstOrNull { screenCount -> screenCount.screen == screen }
    if (sc == null) {
      logE("emitScreenEvent - Screen $screen not found")
    } else {
      (sc as? ScreenNavigationCount<PARAM, EVENT, VIEWMODEL_TO_FLOW, SCREEN>)?.let { scc ->
        scc.onEvent(event)
      } ?: logE("emitScreenEvent - event type is incorrect !?")
    }
  }

  fun verify(screen: Screen<*, *, *>, count: Int = 1) {
    var sc = screens.firstOrNull { screenCount -> screenCount.screen == screen }
    if (sc == null) {
      logE("verify - Screen $screen not found")
    } else {
      sc.verified += count
    }
  }

  fun verify(screenDialog: ScreenDialog<*, *, *>, count: Int = 1) {
    var scd = screenDialogs.firstOrNull { screenCount -> screenCount.screenDialog == screenDialog }
    if (scd == null) {
      logE("verify - ScreenDialog $screenDialog not found")
    } else {
      scd.verified += count
    }
  }

  fun assert() {
    screens.forEach { screen ->
      try {
        Assert.assertEquals(screen.verified, screen.count)
      } catch(t : AssertionError) {
        println("${screen.screen} has problem with verify")
        throw t
      }
    }
    screenDialogs.forEach { screenDialog ->
      try {
        Assert.assertEquals(screenDialog.verified, screenDialog.count)
      } catch(t : AssertionError) {
        println("${screenDialog.screenDialog} has problem with verify")
        throw t
      }
    }
  }
}
