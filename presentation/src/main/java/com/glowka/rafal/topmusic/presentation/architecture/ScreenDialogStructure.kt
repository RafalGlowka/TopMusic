package com.glowka.rafal.topmusic.presentation.architecture

import androidx.fragment.app.DialogFragment
import org.koin.core.scope.Scope
import kotlin.reflect.KClass

abstract class ScreenDialogStructure<
    INPUT : ScreenInput,
    OUTPUT : ScreenOutput,
    VIEWMODEL_TO_VIEW : ViewModelToViewInterface<*, *>,
    >(
  val statusBarColor: Int? = null,
  val lightTextColor: Boolean? = null,
) {
  abstract val fragmentClass: KClass<out DialogFragment>
  abstract fun Scope.viewModelCreator(): ViewModelInterface<INPUT, OUTPUT, *, *>
}