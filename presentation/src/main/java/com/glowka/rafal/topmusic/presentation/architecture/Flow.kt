package com.glowka.rafal.topmusic.presentation.architecture

import kotlinx.coroutines.*
import org.koin.core.qualifier.StringQualifier
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

data class FlowDestination<PARAM : Any, EVENT : ScreenEvent,
    VIEWMODEL_TO_FLOW : ViewModelToFlowInterface<PARAM, EVENT>>(
  val screen: Screen<PARAM, EVENT, VIEWMODEL_TO_FLOW>,
  val param: PARAM
)

data class FlowDialogDestination<PARAM : Any, EVENT : ScreenEvent,
    VIEWMODEL_TO_FLOW : ViewModelToFlowInterface<PARAM, EVENT>>(
  val screen: ScreenDialog<PARAM, EVENT, VIEWMODEL_TO_FLOW>,
  val param: PARAM
)

interface Flow<FLOW_PARAM, FLOW_RESULT : Any> {
  val flowScopeName: String
  fun start(navigator: ScreenNavigator, param: FLOW_PARAM, onResult: (FLOW_RESULT) -> Unit)
  fun finish(result: FLOW_RESULT)
}

abstract class BaseFlow<FLOW_PARAM, FLOW_RESULT : Any>(override val flowScopeName: String) :
  Flow<FLOW_PARAM, FLOW_RESULT> {

  protected lateinit var navigator: ScreenNavigator
  private lateinit var onResult: (FLOW_RESULT) -> Unit

  private var _flowScope: CloseableCoroutineScope? = null
  val flowScope: CoroutineScope
    get() {
      if (_flowScope == null) {
        _flowScope = CloseableCoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
      }
      return _flowScope!!
    }

  private var firstScreen: Screen<*, *, *>? = null

  abstract fun onStart(param: FLOW_PARAM): Screen<*, *, *>

  override fun start(
    navigator: ScreenNavigator,
    param: FLOW_PARAM,
    onResult: (FLOW_RESULT) -> Unit
  ) {
    this.navigator = navigator
    this.onResult = onResult
    firstScreen = onStart(param = param)
  }

  fun <PARAM : Any, EVENT : ScreenEvent, VIEWMODEL_TO_FLOW : ViewModelToFlowInterface<PARAM, EVENT>> showScreen(
    screen: Screen<PARAM, EVENT, VIEWMODEL_TO_FLOW>,
    param: PARAM,
    onEvent: (EVENT) -> Unit
  ): Screen<PARAM, EVENT, VIEWMODEL_TO_FLOW> {
    navigator.push(createScope(), screen, param, onEvent)
    return screen
  }

  fun <PARAM : Any, EVENT : ScreenEvent, VIEWMODEL_TO_FLOW : ViewModelToFlowInterface<PARAM, EVENT>> showScreenDialog(
    screen: ScreenDialog<PARAM, EVENT, VIEWMODEL_TO_FLOW>,
    param: PARAM,
    onEvent: (EVENT) -> Unit
  ): ScreenDialog<PARAM, EVENT, VIEWMODEL_TO_FLOW> {
    navigator.showDialog(createScope(), screen, param, onEvent)
    return screen
  }

  fun <PARAM : Any, EVENT : ScreenEvent, VIEWMODEL_TO_FLOW : ViewModelToFlowInterface<PARAM, EVENT>> hideScreenDialog(
    screen: ScreenDialog<PARAM, EVENT, VIEWMODEL_TO_FLOW>,
  ) {
    navigator.hideDialog(screen)
  }

  fun switchBackTo(screen: Screen<*, *, *>) {
    navigator.popBackTo(screen = screen)
  }

  override fun finish(result: FLOW_RESULT) {
    onResult(result)
    _flowScope?.close()
    closeScope()
  }
}

@Suppress("UNCHECKED_CAST")
fun <VIEWMODEL_TO_FLOW : ViewModelToFlowInterface<out Any, out ScreenEvent>>
    BaseFlow<*, *>.getViewModelToFlow(
  screen: Screen<*, *, VIEWMODEL_TO_FLOW>
): VIEWMODEL_TO_FLOW {
  val qualifier = StringQualifier(screen.screenTag)
  val scope = createScope()
  return scope.get<ViewModelToFlowInterface<*, *>>(qualifier = qualifier) as? VIEWMODEL_TO_FLOW
    ?: throw IllegalStateException("Missing ${screen.screenTag} in the scope $flowScopeName")
}

@Suppress("UNCHECKED_CAST")
fun <VIEWMODEL_TO_FLOW : ViewModelToFlowInterface<out Any, out ScreenEvent>>
    BaseFlow<*, *>.getViewModelToFlow(
  screen: ScreenDialog<*, *, VIEWMODEL_TO_FLOW>
): VIEWMODEL_TO_FLOW {
  val qualifier = StringQualifier(screen.screenTag)
  val scope = createScope()
  return scope.get<ViewModelToFlowInterface<*, *>>(qualifier = qualifier) as? VIEWMODEL_TO_FLOW
    ?: throw IllegalStateException("Missing ${screen.screenTag} in the scope $flowScopeName")
}

fun BaseFlow<*, *>.launch(
  context: CoroutineContext = EmptyCoroutineContext,
  start: CoroutineStart = CoroutineStart.DEFAULT,
  block: suspend CoroutineScope.() -> Unit
): Job = flowScope.launch(context, start, block)
