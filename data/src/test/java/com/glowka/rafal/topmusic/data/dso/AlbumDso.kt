package com.glowka.rafal.topmusic.data.dso

import com.glowka.rafal.topmusic.domain.model.Country

fun albumDso(
  id: String = "1",
  name: String = "Karoce",
  artistName: String = "Adam Nowak",
  releaseDate: String = "28.02.2000",
  artworkUrl: String = "https://secret.music.com/Karoce.jpg",
  genres: List<GenreDso> = listOf(genreDso()),
  url: String = "http://https://secret.music.com/Karoce",
  copyright: String = "Nowak - All rights reserved",
  countryCode: String = Country.UnitedStates.countryCode,
) = AlbumDso(
  id = id,
  name = artistName,
  artistName = artistName,
  releaseDate = releaseDate,
  artworkUrl100 = artworkUrl,
  genres = genres,
  url = url,
  copyright = copyright,
  countryCode = countryCode,
)
