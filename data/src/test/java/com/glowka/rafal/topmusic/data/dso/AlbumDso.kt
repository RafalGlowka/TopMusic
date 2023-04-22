package com.glowka.rafal.topmusic.data.dso

fun albumDso(
  id: String = "1",
  name: String = "Karoce",
  artistName: String = "Adam Nowak",
  releaseDate: String = "28.02.2000",
  artworkUrl: String = "https://secret.music.com/Karoce.jpg",
  genres: List<GenreDso> = listOf(genreDso()),
  url: String = "http://https://secret.music.com/Karoce",
  copyright: String = "Nowak - All rights reserved"
) = AlbumDso(
  id = id,
  name = artistName,
  artistName = artistName,
  releaseDate = releaseDate,
  artworkUrl100 = artworkUrl,
  genres = genres,
  url = url,
  copyright = copyright,
)
