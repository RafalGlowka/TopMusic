package com.glowka.rafal.topmusic.data.dto

import com.google.gson.annotations.SerializedName

data class ListFeedDto(
  @SerializedName("results")
  val results: List<AlbumDto?>?,
  @SerializedName("copyright")
  val copyright: String?,
)