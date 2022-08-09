package com.glowka.rafal.topmusic.data.dto

import com.google.gson.annotations.SerializedName

class GenreDto(
  @SerializedName("genreId")
  val id: String?,
  @SerializedName("name")
  val name: String?,
)