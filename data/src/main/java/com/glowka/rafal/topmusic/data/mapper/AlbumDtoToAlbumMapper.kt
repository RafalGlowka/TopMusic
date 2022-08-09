package com.glowka.rafal.topmusic.data.mapper

import com.glowka.rafal.topmusic.data.dto.AlbumDto
import com.glowka.rafal.topmusic.domain.model.Album
import com.glowka.rafal.topmusic.domain.model.Genre
import com.glowka.rafal.topmusic.domain.utils.EMPTY

interface AlbumDtoToAlbumMapper : Mapper<Pair<AlbumDto?, String>, Album?>

class AlbumDtoToAlbumMapperImpl() : AlbumDtoToAlbumMapper {

 override fun invoke(data: Pair<AlbumDto?, String>): Album? {
  val (album, copyright) = data
  album?.id ?: return null
  album.name ?: return null
  album.artistName ?: return null
  album.releaseDate ?: return null
  album.artworkUrl100 ?: return null
  album.url ?: return null

  return Album(
   id = album.id,
   name = album.name,
   artistName = album.artistName,
   releaseDate = album.releaseDate,
   artworkUrl100 = album.artworkUrl100.replace("100x100", "512x512"),
   genres = album.genres?.map { genreDto ->
     Genre(genreDto.id ?: String.EMPTY, genreDto.name ?: String.EMPTY)
   } ?: emptyList(),
   copyright = copyright,
   url = album.url
  )
 }

}
