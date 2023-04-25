@file:Suppress("MaxLineLength")
package com.glowka.rafal.topmusic.presentation.utils

import android.content.Intent
import com.glowka.rafal.topmusic.domain.utils.pop
import com.glowka.rafal.topmusic.domain.utils.push
import com.glowka.rafal.topmusic.presentation.architecture.Screen
import com.glowka.rafal.topmusic.presentation.architecture.ScreenDialog
import com.glowka.rafal.topmusic.presentation.architecture.ScreenEvent
import com.glowka.rafal.topmusic.presentation.architecture.ScreenNavigator
import com.glowka.rafal.topmusic.presentation.architecture.ViewModelToFlowInterface
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import org.koin.core.scope.Scope

sealed interface NavigationEvent {
  sealed interface ScreenNavigationEvent : NavigationEvent {

    data class ShowScreen<
        PARAM : Any,
        EVENT : ScreenEvent,
        VIEWMODEL_TO_FLOW : ViewModelToFlowInterface<PARAM, EVENT>,
        SCREEN : Screen<PARAM, EVENT, VIEWMODEL_TO_FLOW>
        >(
      val screen: SCREEN,
      val param: PARAM,
      val onEvent: (EVENT) -> Unit,
    ) : ScreenNavigationEvent

    data class PopBack<
        PARAM : Any,
        EVENT : ScreenEvent,
        VIEWMODEL_TO_FLOW : ViewModelToFlowInterface<PARAM, EVENT>,
        SCREEN : Screen<PARAM, EVENT, VIEWMODEL_TO_FLOW>
        >(val screen: SCREEN) : ScreenNavigationEvent

    data class PopBackTo<
        PARAM : Any,
        EVENT : ScreenEvent,
        VIEWMODEL_TO_FLOW : ViewModelToFlowInterface<PARAM, EVENT>,
        SCREEN : Screen<PARAM, EVENT, VIEWMODEL_TO_FLOW>
        >(val screen: SCREEN) : ScreenNavigationEvent
  }

  sealed interface ScreenDialogNavigationEvent : NavigationEvent {
    data class ShowScreenDialog<PARAM : Any, EVENT : ScreenEvent,
        VIEWMODEL_TO_FLOW : ViewModelToFlowInterface<PARAM, EVENT>,
        SCREENDIALOG : ScreenDialog<PARAM, EVENT, VIEWMODEL_TO_FLOW>>(
      val screenDialog: SCREENDIALOG,
      val param: PARAM,
      val onEvent: (EVENT) -> Unit,
    ) : ScreenDialogNavigationEvent

    data class HideScreenDialog<
        PARAM : Any, EVENT : ScreenEvent,
        VIEWMODEL_TO_FLOW : ViewModelToFlowInterface<PARAM, EVENT>,
        SCREENDIALOG : ScreenDialog<PARAM, EVENT, VIEWMODEL_TO_FLOW>>(
      val screenDialog: SCREENDIALOG
    ) : ScreenDialogNavigationEvent
  }

  sealed interface ActivityNavigationEvent : NavigationEvent {

    data class StartActivity(val intent: Intent) : ActivityNavigationEvent

    object FinishActivity : ActivityNavigationEvent
  }
}

class FakeScreenNavigator : ScreenNavigator {

  private val _navigationEvents = MutableSharedFlow<NavigationEvent>()
  val navigationEvents: SharedFlow<NavigationEvent> = _navigationEvents

  val screenStack = arrayListOf<Screen<*, *, *>>()

  override fun <PARAM : Any, EVENT : ScreenEvent, VIEWMODEL_TO_FLOW : ViewModelToFlowInterface<PARAM, EVENT>> push(
    scope: Scope,
    screen: Screen<PARAM, EVENT, VIEWMODEL_TO_FLOW>,
    param: PARAM,
    onEvent: (EVENT) -> Unit
  ) {
    screenStack.push(screen)
    val event = NavigationEvent.ScreenNavigationEvent.ShowScreen(screen, param, onEvent)
    MainScope().launch {
      _navigationEvents.emit(event)
    }
  }

  override fun popBack(screen: Screen<*, *, *>) {
    if (screenStack.isNotEmpty()) {
      for (i in screenStack.size - 1 downTo 0) {
        if (screenStack[i] == screen) {
          for (i2 in screenStack.size - 1 downTo i)
            screenStack.pop()
          break
        }
      }
    }
    MainScope().launch {
      _navigationEvents.emit(NavigationEvent.ScreenNavigationEvent.PopBack(screen))
    }
  }

  override fun popBackTo(screen: Screen<*, *, *>) {
    if (screenStack.isNotEmpty()) {
      for (i in screenStack.size - 1 downTo 0) {
        if (screenStack[i] == screen) {
          for (i2 in screenStack.size - 1 downTo i + 1)
            screenStack.pop()
          break
        }
      }
    }
    MainScope().launch {
      _navigationEvents.emit(NavigationEvent.ScreenNavigationEvent.PopBackTo(screen))
    }
  }

  @Suppress("MaxLineLength")
  override fun <PARAM : Any, EVENT : ScreenEvent, VIEWMODEL_TO_FLOW : ViewModelToFlowInterface<PARAM, EVENT>> showDialog(
    scope: Scope,
    screenDialog: ScreenDialog<PARAM, EVENT, VIEWMODEL_TO_FLOW>,
    param: PARAM,
    onEvent: (EVENT) -> Unit
  ) {
    val scd =
      NavigationEvent.ScreenDialogNavigationEvent.ShowScreenDialog(screenDialog, param, onEvent)
    MainScope().launch {
      _navigationEvents.emit(scd)
    }
  }

  override fun hideDialog(screenDialog: ScreenDialog<*, *, *>) {
    val scd = NavigationEvent.ScreenDialogNavigationEvent.HideScreenDialog(screenDialog)
    MainScope().launch {
      _navigationEvents.emit(scd)
    }
  }

  override fun startActivity(intent: Intent) {
    MainScope().launch {
      _navigationEvents.emit(NavigationEvent.ActivityNavigationEvent.StartActivity(intent))
    }
  }

  override fun finishActivity() {
    MainScope().launch {
      _navigationEvents.emit(NavigationEvent.ActivityNavigationEvent.FinishActivity)
    }
  }
}

fun <PARAM : Any, EVENT : ScreenEvent, VIEWMODEL_TO_FLOW : ViewModelToFlowInterface<PARAM, EVENT>,
    SCREEN : Screen<PARAM, EVENT, VIEWMODEL_TO_FLOW>> NavigationEvent.shouldBeNavigationToScreen(
  screen: SCREEN,
  param: PARAM,
): NavigationEvent.ScreenNavigationEvent.ShowScreen<PARAM, EVENT, VIEWMODEL_TO_FLOW, SCREEN> {
  this.shouldBeTypeOf<NavigationEvent.ScreenNavigationEvent.ShowScreen<PARAM, EVENT, VIEWMODEL_TO_FLOW, SCREEN>>()
    .run {
      this.screen shouldBe screen
      this.param shouldBe param
    }
  return this
}

fun <PARAM : Any, EVENT : ScreenEvent, VIEWMODEL_TO_FLOW : ViewModelToFlowInterface<PARAM, EVENT>,
    SCREEN : Screen<PARAM, EVENT, VIEWMODEL_TO_FLOW>> NavigationEvent.ScreenNavigationEvent.ShowScreen<PARAM, EVENT, VIEWMODEL_TO_FLOW, SCREEN>.emitScreenEvent(
  event: EVENT
): NavigationEvent.ScreenNavigationEvent.ShowScreen<PARAM, EVENT, VIEWMODEL_TO_FLOW, SCREEN> {
  this.shouldBeTypeOf<NavigationEvent.ScreenNavigationEvent.ShowScreen<PARAM, EVENT, VIEWMODEL_TO_FLOW, SCREEN>>()
    .onEvent(event)
  return this
}

fun <PARAM : Any, EVENT : ScreenEvent, VIEWMODEL_TO_FLOW : ViewModelToFlowInterface<PARAM, EVENT>,
    SCREEN : Screen<PARAM, EVENT, VIEWMODEL_TO_FLOW>> NavigationEvent.shouldBePopBack(
  screen: SCREEN,
) {
  this.shouldBeTypeOf<NavigationEvent.ScreenNavigationEvent.PopBack<PARAM, EVENT, VIEWMODEL_TO_FLOW, SCREEN>>()
    .run {
      this.screen shouldBe screen
    }
}

fun <PARAM : Any, EVENT : ScreenEvent, VIEWMODEL_TO_FLOW : ViewModelToFlowInterface<PARAM, EVENT>,
    SCREEN : Screen<PARAM, EVENT, VIEWMODEL_TO_FLOW>> NavigationEvent.shouldBePopBackTo(
  screen: SCREEN,
) {
  this.shouldBeTypeOf<NavigationEvent.ScreenNavigationEvent.PopBackTo<PARAM, EVENT, VIEWMODEL_TO_FLOW, SCREEN>>()
    .run {
      this.screen shouldBe screen
    }
}

fun <PARAM : Any, EVENT : ScreenEvent, VIEWMODEL_TO_FLOW : ViewModelToFlowInterface<PARAM, EVENT>,
    SCREENDIALOG : ScreenDialog<PARAM, EVENT, VIEWMODEL_TO_FLOW>> NavigationEvent.shouldBeNavigationToScreenDialog(
  screenDialog: SCREENDIALOG,
  param: PARAM,
): NavigationEvent.ScreenDialogNavigationEvent.ShowScreenDialog<PARAM, EVENT, VIEWMODEL_TO_FLOW, SCREENDIALOG> {
  this.shouldBeTypeOf<NavigationEvent.ScreenDialogNavigationEvent.ShowScreenDialog<PARAM, EVENT, VIEWMODEL_TO_FLOW, SCREENDIALOG>>()
    .run {
      this.screenDialog shouldBe screenDialog
      this.param shouldBe param
    }
  return this
}

fun <PARAM : Any, EVENT : ScreenEvent, VIEWMODEL_TO_FLOW : ViewModelToFlowInterface<PARAM, EVENT>,
    SCREENDIALOG : ScreenDialog<PARAM, EVENT, VIEWMODEL_TO_FLOW>> NavigationEvent.ScreenDialogNavigationEvent.ShowScreenDialog<PARAM, EVENT, VIEWMODEL_TO_FLOW, SCREENDIALOG>.emitScreenDialogEvent(
  event: EVENT
): NavigationEvent.ScreenDialogNavigationEvent.ShowScreenDialog<PARAM, EVENT, VIEWMODEL_TO_FLOW, SCREENDIALOG> {
  this.shouldBeTypeOf<NavigationEvent.ScreenDialogNavigationEvent.ShowScreenDialog<PARAM, EVENT, VIEWMODEL_TO_FLOW, SCREENDIALOG>>()
    .onEvent(event)
  return this
}

fun <PARAM : Any, EVENT : ScreenEvent, VIEWMODEL_TO_FLOW : ViewModelToFlowInterface<PARAM, EVENT>,
    SCREENDIALOG : ScreenDialog<PARAM, EVENT, VIEWMODEL_TO_FLOW>> NavigationEvent.shouldBeHideScreenDialog(
  screenDialog: SCREENDIALOG,
) {
  this.shouldBeTypeOf<NavigationEvent.ScreenDialogNavigationEvent.HideScreenDialog<PARAM, EVENT, VIEWMODEL_TO_FLOW, SCREENDIALOG>>()
    .run {
      this.screenDialog shouldBe screenDialog
    }
}

fun NavigationEvent.shouldBeStartActivity() {
  this.shouldBeTypeOf<NavigationEvent.ActivityNavigationEvent.StartActivity>()
}

fun NavigationEvent.shouldBeFinishActivity() {
  this.shouldBeTypeOf<NavigationEvent.ActivityNavigationEvent.FinishActivity>()
}
