package com.glowka.rafal.topmusic.data.dto

import com.google.gson.annotations.SerializedName

data class ListResponseDto(

  @SerializedName("feed")
  val feed: ListFeedDto?
)
