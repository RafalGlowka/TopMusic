package com.glowka.rafal.topmusic.domain.model

data class Album(
  val id: String,
  val artistName: String,
  val name: String,
  val releaseDate: String,
  val artworkUrl100: String,
  val genres: List<Genre>,
  val copyright: String,
  val url: String,
  val countryCode: String,
)
