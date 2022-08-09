package com.glowka.rafal.topmusic.presentation.architecture

import org.koin.core.scope.Scope
import kotlin.reflect.KClass

abstract class ScreenDialogStructure<
    PARAM : Any,
    EVENT : ScreenEvent,
    VIEWMODEL_TO_FLOW : ViewModelToFlowInterface<PARAM, EVENT>,
    VIEWMODEL_TO_VIEW : ViewModelToViewInterface<*, *>,
    >(
  val statusBarColor: Int? = null,
  val lightTextColor: Boolean? = null,
) {
  abstract val fragmentClass: KClass<out BaseDialogFragment<*, *, VIEWMODEL_TO_VIEW>>
  abstract fun Scope.viewModelCreator(): ViewModelInterface<PARAM, EVENT, *, *>
}