package com.glowka.rafal.topmusic.presentation.architecture

abstract class ScreenDialog<
    INPUT : ScreenInput,
    OUTPUT : ScreenOutput
    >(
  val flowScopeName: String,
  val screenStructure: ScreenDialogStructure<INPUT, OUTPUT, *>,
)

inline val ScreenDialog<*, *>.screenTag: String
  get() {
    return this::class.java.canonicalName?.takeIf { name -> name.isNotBlank() }
      ?: error("canonicalName is blank !")
  }

fun <
    INPUT : ScreenInput,
    OUTPUT : ScreenOutput
    > ScreenDialog<INPUT, OUTPUT>.flowDestination(param: INPUT?) = FlowDialogDestination(
  screen = this,
  param = param
)