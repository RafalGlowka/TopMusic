package com.glowka.rafal.topmusic.data.api

import com.glowka.rafal.topmusic.data.dto.ListResponseDto
import retrofit2.http.GET

//https://rss.applemarketingtools.com/api/v2/us/music/most-played/10/albums.json

interface ListApi {

  @GET("/api/v2/us/music/most-played/100/albums.json")
  suspend fun getAlbums(): ListResponseDto
}