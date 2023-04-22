package com.glowka.rafal.topmusic.data.dto

fun albumDto(
  id: String = "2",
  name: String = "Drzewo",
  artistName: String = "Anna Kania",
  releaseDate: String = "20.08.2005",
  artworkUrl: String = "https://secret.music.com/Drzewo.jpg",
  genres: List<GenreDto> = listOf(genreDto()),
  url: String = "http://https://secret.music.com/Drzewo",
) = AlbumDto(
  id = id,
  name = artistName,
  artistName = artistName,
  releaseDate = releaseDate,
  artworkUrl100 = artworkUrl,
  genres = genres,
  url = url,
)
