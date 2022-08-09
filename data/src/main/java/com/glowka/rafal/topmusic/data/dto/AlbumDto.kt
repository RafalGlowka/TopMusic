package com.glowka.rafal.topmusic.data.dto

import com.google.gson.annotations.SerializedName

data class AlbumDto(
  @SerializedName("id")
  val id: String?,
  @SerializedName("artistName")
  val artistName: String?,
  @SerializedName("name")
  val name: String?,
  @SerializedName("releaseDate")
  val releaseDate: String?,
  @SerializedName("artworkUrl100")
  val artworkUrl100: String?,
  @SerializedName("genres")
  val genres: List<GenreDto>?,
  @SerializedName("url")
  val url: String?,
)