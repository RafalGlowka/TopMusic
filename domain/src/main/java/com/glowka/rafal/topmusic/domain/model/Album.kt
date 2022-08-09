package com.glowka.rafal.topmusic.domain.model

import com.glowka.rafal.topmusic.domain.utils.EMPTY

data class Album(
  val id: String = String.EMPTY,
  val artistName: String = String.EMPTY,
  val name: String = String.EMPTY,
  val releaseDate: String = String.EMPTY,
  val artworkUrl100: String = String.EMPTY,
  val genres: List<Genre> = emptyList(),
  val copyright: String = String.EMPTY,
  val url: String = String.EMPTY,
)
