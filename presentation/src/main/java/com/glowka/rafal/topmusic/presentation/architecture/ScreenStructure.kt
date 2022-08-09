package com.glowka.rafal.topmusic.presentation.architecture

import org.koin.core.scope.Scope
import kotlin.reflect.KClass

abstract class ScreenStructure<
    PARAM : Any,
    EVENT : ScreenEvent,
    VIEWMODEL_TO_FLOW : ViewModelToFlowInterface<PARAM, EVENT>,
    VIEWMODEL_TO_VIEW : ViewModelToViewInterface<*, *>,
    > {
  abstract val fragmentClass: KClass<out BaseFragment<*, *, VIEWMODEL_TO_VIEW>>
  abstract fun Scope.viewModelCreator(): ViewModelInterface<PARAM, EVENT, *, *>
}