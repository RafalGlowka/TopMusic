package com.glowka.rafal.topmusic.presentation.architecture

interface ScreenEvent

abstract class Screen<
    PARAM : Any,
    EVENT : ScreenEvent,
    VIEWMODEL_TO_FLOW : ViewModelToFlowInterface<PARAM, EVENT>
    >(
  val flowScopeName: String,
  val screenStructure: ScreenStructure<PARAM, EVENT, VIEWMODEL_TO_FLOW, *>,
)

inline val Screen<*, *, *>.screenTag: String
  get() {
    return this::class.java.canonicalName?.takeIf { name -> name.isNotBlank() }
      ?: error("canonicalName is blank !")
  }

fun <PARAM : Any, EVENT : ScreenEvent> Screen<PARAM, EVENT, ViewModelToFlowInterface<PARAM, EVENT>>.flowDestination(
  param: PARAM
) = FlowDestination(
  screen = this,
  param = param
)