package com.glowka.rafal.topmusic.domain.model

fun album(
  id: String = "1",
  artistName: String = "Jacek Mak",
  name: String = "album name",
  releaseDate: String = "1.01.1973",
  artworkUrl100: String = "https://artwork.com/album.jpg",
  genres: List<Genre> = emptyList(),
  copyright: String = "Some copyright frazes",
  url: String = "https:/supermusic.com/album",
) = Album(
  id = id,
  artistName = artistName,
  name = name,
  releaseDate = releaseDate,
  artworkUrl100 = artworkUrl100,
  genres = genres,
  copyright = copyright,
  url = url,
)
