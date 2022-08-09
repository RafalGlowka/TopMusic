package com.glowka.rafal.topmusic.presentation.architecture

import com.glowka.rafal.topmusic.domain.utils.logTag
import org.koin.android.ext.android.getKoin
import org.koin.core.definition.Definition
import org.koin.core.error.NoBeanDefFoundException
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.ScopeDSL
import org.koin.dsl.binds


fun BaseFragment<*, *, *>.getScopeName() = arguments?.getString(BaseFragment.ARG_SCOPE)
fun BaseFragment<*, *, *>.getScreenTag() = arguments?.getString(BaseFragment.ARG_SCREEN_TAG)
fun BaseDialogFragment<*, *, *>.getScopeName() = arguments?.getString(BaseDialogFragment.ARG_SCOPE)
fun BaseDialogFragment<*, *, *>.getScreenTag() =
  arguments?.getString(BaseDialogFragment.ARG_SCREEN_TAG)

fun <VMSTATE : Any, VEVENTS : Any, VM : ViewModelToViewInterface<VMSTATE, VEVENTS>>
    BaseFragment<VMSTATE, VEVENTS, VM>.injectViewModel(): Lazy<VM> {
  return lazy(LazyThreadSafetyMode.NONE) {
    val scopeName =
      getScopeName() ?: throw IllegalArgumentException("Missing scopeName for ${this::logTag}")
    val screenTag =
      getScreenTag() ?: throw IllegalArgumentException("Missing screenTag for ${this::logTag}")
    val scope = getKoin().getScopeOrNull(scopeName)
      ?: throw RuntimeException("Missing scope $scopeName definition in Di")
    val qualifier = StringQualifier(screenTag)

    val viewModel = try {
      scope.get(
        clazz = BaseViewModel::class,
        qualifier = qualifier,
        parameters = null
      ) as? BaseViewModel<*, *, *, *>
        ?: throw TypeCastException("Incorrect ViewModel base type")
    } catch (error: NoBeanDefFoundException) {
      throw java.lang.RuntimeException("Missing viewModel for screen $screenTag in scope $scopeName")
    }

//    viewModel.lifecycleOwner = this

    @Suppress("UNCHECKED_CAST")
    return@lazy viewModel as? VM ?: throw TypeCastException("Incorrect ViewModel type")
  }
}

fun <VMSTATE : Any, VEVENTS : Any, VM : ViewModelToViewInterface<VMSTATE, VEVENTS>>
    BaseDialogFragment<VMSTATE, VEVENTS, VM>.injectViewModel(): Lazy<VM> {
  return lazy(LazyThreadSafetyMode.NONE) {
    val scopeName =
      getScopeName() ?: throw IllegalArgumentException("Missing scopeName for ${this::logTag}")
    val screenTag =
      getScreenTag() ?: throw IllegalArgumentException("Missing screenTag for ${this::logTag}")
    val scope = getKoin().getScopeOrNull(scopeName)
      ?: throw RuntimeException("Missing scope $scopeName definition in Di")
    val qualifier = StringQualifier(screenTag)

    val viewModel = try {
      scope.get(
        clazz = BaseViewModel::class,
        qualifier = qualifier,
        parameters = null
      ) as? BaseViewModel<*, *, *, *>
        ?: throw TypeCastException("Incorrect ViewModel base type")
    } catch (error: NoBeanDefFoundException) {
      throw java.lang.RuntimeException("Missing viewModel for screen $screenTag in scope $scopeName")
    }

//    viewModel.lifecycleOwner = this

    @Suppress("UNCHECKED_CAST")
    return@lazy viewModel as? VM ?: throw TypeCastException("Incorrect ViewModel type")
  }
}


fun <PARAM : Any, EVENT : ScreenEvent,
    VIEWMODEL_TO_FLOW : ViewModelToFlowInterface<PARAM, EVENT>> ScopeDSL.screenViewModel(
  screen: Screen<PARAM, EVENT, VIEWMODEL_TO_FLOW>,
  definition: Definition<ViewModelInterface<PARAM, EVENT, *, *>>
) {
  scoped(
    qualifier = named(screen.screenTag),
    definition = definition,
  ) binds arrayOf(ViewModelToFlowInterface::class, BaseViewModel::class)
}

fun <PARAM : Any, EVENT : ScreenEvent,
    VIEWMODEL_TO_FLOW : ViewModelToFlowInterface<PARAM, EVENT>> ScopeDSL.screen(
  screen: Screen<PARAM, EVENT, VIEWMODEL_TO_FLOW>
) {
  scoped(
    qualifier = named(screen.screenTag),
    definition = {
      with(screen.screenStructure) {
        viewModelCreator()
      }
    }) binds arrayOf(ViewModelToFlowInterface::class, BaseViewModel::class)
}

fun <PARAM : Any, EVENT : ScreenEvent,
    VIEWMODEL_TO_FLOW : ViewModelToFlowInterface<PARAM, EVENT>> ScopeDSL.screenDialog(
  screen: ScreenDialog<PARAM, EVENT, VIEWMODEL_TO_FLOW>
) {
  scoped(
    qualifier = named(screen.screenTag),
    definition = {
      with(screen.screenStructure) {
        viewModelCreator()
      }
    }) binds arrayOf(ViewModelToFlowInterface::class, BaseViewModel::class)
}