package com.glowka.rafal.topmusic.data.mapper

import com.glowka.rafal.topmusic.data.dso.AlbumDso
import com.glowka.rafal.topmusic.domain.model.Album
import com.glowka.rafal.topmusic.domain.model.Genre
import com.glowka.rafal.topmusic.domain.utils.EMPTY

interface AlbumDsoToAlbumMapper : Mapper<AlbumDso, Album>

class AlbumDsoToAlbumMapperImpl() : AlbumDsoToAlbumMapper {

 override fun invoke(data: AlbumDso): Album {
  return Album(
   id = data.id,
   name = data.name,
   artistName = data.artistName,
   releaseDate = data.releaseDate,
   artworkUrl100 = data.artworkUrl100,
   genres = data.genres.map { genreDso ->
    Genre(
     id = genreDso.id ?: String.EMPTY,
     name = genreDso.name ?: String.EMPTY
    )
   },
   copyright = data.copyright,
   url = data.url,
   countryCode = data.countryCode
  )
 }

}
