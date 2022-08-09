package com.glowka.rafal.topmusic.data.mapper

import com.glowka.rafal.topmusic.data.dso.AlbumDso
import com.glowka.rafal.topmusic.data.dso.GenreDso
import com.glowka.rafal.topmusic.domain.model.Album

interface AlbumToAlbumDsoMapper : Mapper<Album, AlbumDso>

class AlbumToAlbumDsoMapperImpl() : AlbumToAlbumDsoMapper {

 override fun invoke(data: Album): AlbumDso {
  return AlbumDso(
   id = data.id,
   name = data.name,
   artistName = data.artistName,
   releaseDate = data.releaseDate,
   artworkUrl100 = data.artworkUrl100,
   genres = data.genres.map { genre ->
    GenreDso(
     id = genre.id,
     name = genre.name
    )
   },
   url = data.url,
   copyright = data.copyright
  )
 }

}
