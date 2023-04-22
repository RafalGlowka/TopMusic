package com.glowka.rafal.topmusic.data.api

import com.glowka.rafal.topmusic.data.dto.ListResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

//https://rss.applemarketingtools.com/api/v2/us/music/most-played/10/albums.json

enum class Country(val countryCode: String) {
  Angola("ao"),
  UnitedStates("us"),
  Poland("pl"),
  UnitedKingdom("gb"),
  France("fr"),
  Germany("de")
}

interface ListApi {

  @GET("/api/v2/{countryCode}/music/most-played/100/albums.json")
  suspend fun getAlbums(@Path("countryCode") countryCode: String): ListResponseDto
}