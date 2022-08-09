package com.glowka.rafal.topmusic.domain.utils

interface StringResolver {
  operator fun invoke(resId: Int): String
  operator fun invoke(resId: Int, vararg args: Any): String
}