package com.glowka.rafal.topmusic.presentation.architecture

interface ScreenInput

interface ScreenOutput

abstract class Screen<
    INPUT : ScreenInput,
    OUTPUT : ScreenOutput,
    >(
  val flowScopeName: String,
  val screenStructure: ScreenStructure<INPUT, OUTPUT, *>,
)

inline val Screen<*, *>.screenTag: String
  get() {
    return this::class.java.canonicalName?.takeIf { name -> name.isNotBlank() }
      ?: error("canonicalName is blank !")
  }

fun <
    INPUT : ScreenInput,
    OUTPUT : ScreenOutput
    > Screen<INPUT, OUTPUT>.flowDestination(
  param: INPUT?
) = FlowDestination(
  screen = this,
  param = param
)