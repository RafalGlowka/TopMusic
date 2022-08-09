package com.glowka.rafal.topmusic.presentation.architecture

abstract class ScreenDialog<
    PARAM : Any,
    EVENT : ScreenEvent,
    VIEWMODEL_TO_FLOW : ViewModelToFlowInterface<PARAM, EVENT>
    >(
  val flowScopeName: String,
  val screenStructure: ScreenDialogStructure<PARAM, EVENT, VIEWMODEL_TO_FLOW, *>,
)

inline val ScreenDialog<*, *, *>.screenTag: String
  get() {
    return this::class.java.canonicalName?.takeIf { name -> name.isNotBlank() }
      ?: error("canonicalName is blank !")
  }

fun <PARAM : Any, EVENT : ScreenEvent> ScreenDialog<
    PARAM, EVENT, ViewModelToFlowInterface<PARAM, EVENT>
    >.flowDestination(param: PARAM) = FlowDialogDestination(
  screen = this,
  param = param
)