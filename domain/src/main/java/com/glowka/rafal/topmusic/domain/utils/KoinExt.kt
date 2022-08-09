package com.glowka.rafal.topmusic.domain.utils

import org.koin.core.context.GlobalContext
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier

inline fun <reified T : Any> inject(
  qualifier: Qualifier? = null,
  mode: LazyThreadSafetyMode = LazyThreadSafetyMode.SYNCHRONIZED,
  noinline parameters: ParametersDefinition? = null
): Lazy<T> =
  lazy(mode) {
    val koin = GlobalContext.get()
    koin.get<T>(qualifier, parameters)
  }