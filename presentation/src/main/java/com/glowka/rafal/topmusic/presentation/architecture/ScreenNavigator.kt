package com.glowka.rafal.topmusic.presentation.architecture

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.glowka.rafal.topmusic.presentation.utils.setLightTextColor
import com.glowka.rafal.topmusic.presentation.utils.setStatusBarBackgroundColor
import org.koin.core.qualifier.StringQualifier
import org.koin.core.scope.Scope

interface ScreenNavigator {
  fun <
      INPUT : ScreenInput,
      OUTPUT : ScreenOutput,
      > push(
    scope: Scope,
    screen: Screen<INPUT, OUTPUT>,
    param: INPUT?,
    onScreenOutput: (OUTPUT) -> Unit
  )

  fun popBack(screen: Screen<*, *>)
  fun popBackTo(screen: Screen<*, *>)
  fun <
      INPUT : ScreenInput,
      OUTPUT : ScreenOutput,
      > showDialog(
    scope: Scope,
    screenDialog: ScreenDialog<INPUT, OUTPUT>,
    param: INPUT?,
    onScreenOutput: (OUTPUT) -> Unit
  )

  fun hideDialog(screen: ScreenDialog<*, *>)
  fun startActivity(intent: Intent)
  fun finishActivity()
}

interface FragmentActivityAttachment {
  fun attach(fm: FragmentActivity)
  fun detach()
}

class FragmentNavigatorImpl(val containerId: Int) : FragmentActivityAttachment,
  ScreenNavigator {

  private var fragmentActivity: FragmentActivity? = null
  private var waitingOperation: (() -> Unit)? = null

  override fun attach(fm: FragmentActivity) {
    fragmentActivity = fm
    waitingOperation?.invoke()
    waitingOperation = null
  }

  override fun detach() {
    fragmentActivity = null
  }

  override fun <INPUT : ScreenInput, OUTPUT : ScreenOutput> push(
    scope: Scope,
    screen: Screen<INPUT, OUTPUT>,
    param: INPUT?,
    onScreenOutput: (OUTPUT) -> Unit
  ) {
    initFlowDestination(scope, FlowDestination(screen = screen, param = param), onScreenOutput)

    val fragmentTag = screen.screenTag
    val fm = fragmentActivity
    if (fm == null) {
      waitingOperation = {
        push(scope = scope, screen = screen, param = param, onScreenOutput = onScreenOutput)
      }
    } else {
      fm.supportFragmentManager.commit {
        val arguments = Bundle().apply {
          putString(BaseFragment.ARG_SCOPE, screen.flowScopeName)
          putString(BaseFragment.ARG_SCREEN_TAG, fragmentTag)
        }
        replace(containerId, screen.screenStructure.fragmentClass.java, arguments, fragmentTag)
        addToBackStack(fragmentTag)
      }
    }
  }

  override fun popBack(screen: Screen<*, *>) {
    val fm = fragmentActivity
    if (fm == null) {
      waitingOperation = {
        popBack(screen = screen)
      }
    } else {
      fm.supportFragmentManager.popBackStack(
        screen.screenTag,
        FragmentManager.POP_BACK_STACK_INCLUSIVE
      )
    }
  }

  override fun popBackTo(screen: Screen<*, *>) {
    val fm = fragmentActivity
    if (fm == null) {
      waitingOperation = {
        popBackTo(screen = screen)
      }
    } else {
      fm.supportFragmentManager.popBackStack(screen.screenTag, 0)
    }
  }

  override fun <INPUT : ScreenInput, OUTPUT : ScreenOutput> showDialog(
    scope: Scope,
    screen: ScreenDialog<INPUT, OUTPUT>,
    param: INPUT?,
    onScreenOutput: (OUTPUT) -> Unit
  ) {
    initFlowDestination(
      scope = scope,
      flowDestination = FlowDialogDestination(screen = screen, param = param),
      onScreenOUTPUT = onScreenOutput
    )

    val fragmentTag = screen.screenTag
    val fm = fragmentActivity
    if (fm == null) {
      waitingOperation = {
        showDialog(scope = scope, screen = screen, param = param, onScreenOutput = onScreenOutput)
      }
    } else {
      if (screen.screenStructure.lightTextColor != null) {
        fm.setLightTextColor(screen.screenStructure.lightTextColor)
      }
      if (screen.screenStructure.statusBarColor != null) {
        fm.setStatusBarBackgroundColor(screen.screenStructure.statusBarColor)
      }
      fm.supportFragmentManager.commit {
        val arguments = Bundle().apply {
          putString(BaseFragment.ARG_SCOPE, screen.flowScopeName)
          putString(BaseFragment.ARG_SCREEN_TAG, fragmentTag)
        }
        add(screen.screenStructure.fragmentClass.java, arguments, fragmentTag)
//        addToBackStack(fragmentTag)
      }
    }
  }

  override fun hideDialog(screen: ScreenDialog<*, *>) {
    val fm = fragmentActivity
    if (fm == null) {
      waitingOperation = {
        hideDialog(screen = screen)
      }
    } else {
      val dialogFragment = fm.supportFragmentManager.fragments.getScreenDialogFragment(
        scopeName = screen.flowScopeName,
        screenTag = screen.screenTag

      )
      dialogFragment?.dismiss()
    }
  }

  override fun startActivity(intent: Intent) {
    val fm = fragmentActivity
    if (fm == null) {
      waitingOperation = {
        startActivity(intent)
      }
    } else {
      fm.startActivity(intent)
    }
  }

  override fun finishActivity() {
    val fm = fragmentActivity
    if (fm == null) {
      waitingOperation = {
        finishActivity()
      }
    } else {
      fm.finish()
    }
  }

}

@Suppress("UNCHECKED_CAST")
fun <INPUT : ScreenInput, OUTPUT : ScreenOutput> ScreenNavigator.initFlowDestination(
  scope: Scope,
  flowDestination: FlowDestination<INPUT, OUTPUT>,
  onScreenOUTPUT: (OUTPUT) -> Unit,
) {
  val qualifier = StringQualifier(flowDestination.screen.screenTag)
  val viewModelToFlow =
    scope.get<ViewModelToFlowInterface<*, *>>(qualifier = qualifier) as? ViewModelToFlowInterface<INPUT, OUTPUT>
    ?: throw IllegalStateException("Missing ${flowDestination.screen.screenTag} in the scope ${scope.id}")
  flowDestination.param?.let { input ->
    viewModelToFlow.onInput(input = input)
  }
  viewModelToFlow.onScreenOutput = onScreenOUTPUT
}

@Suppress("UNCHECKED_CAST")
fun <INPUT : ScreenInput, OUTPUT : ScreenOutput> ScreenNavigator.initFlowDestination(
  scope: Scope,
  flowDestination: FlowDialogDestination<INPUT, OUTPUT>,
  onScreenOUTPUT: (OUTPUT) -> Unit,
) {
  val qualifier = StringQualifier(flowDestination.screen.screenTag)
  val viewModelToFlow =
    scope.get<ViewModelToFlowInterface<*, *>>(qualifier = qualifier) as? ViewModelToFlowInterface<INPUT, OUTPUT>
    ?: throw IllegalStateException("Missing ${flowDestination.screen.screenTag} in the scope ${scope.id}")
  flowDestination.param?.let { input ->
    viewModelToFlow.onInput(input = input)
  }
  viewModelToFlow.onScreenOutput = onScreenOUTPUT
}

/**
 * Find the proper fragment in the stack.
 */
fun List<Fragment>.getScreenDialogFragment(scopeName: String, screenTag: String): DialogFragment? =
  firstOrNull { fragment ->
    fragment is DialogFragment &&
        fragment.arguments?.getString(BaseDialogFragment.ARG_SCOPE) == scopeName &&
        fragment.arguments?.getString(BaseDialogFragment.ARG_SCREEN_TAG) == screenTag
  } as? DialogFragment