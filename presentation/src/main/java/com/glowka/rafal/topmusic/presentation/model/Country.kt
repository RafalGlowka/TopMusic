package com.glowka.rafal.topmusic.presentation.model

import com.glowka.rafal.topmusic.domain.model.Country
import com.glowka.rafal.topmusic.presentation.R

fun Country.nameResId(): Int {
  return when (this) {
    Country.Angola -> R.string.country_ao
    Country.UnitedStates -> R.string.country_us
    Country.Poland -> R.string.country_pl
    Country.UnitedKingdom -> R.string.country_gb
    Country.France -> R.string.country_fr
    Country.Germany -> R.string.country_de
  }
}