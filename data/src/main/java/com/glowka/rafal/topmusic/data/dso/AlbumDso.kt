package com.glowka.rafal.topmusic.data.dso

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "album")
data class AlbumDso(
  @PrimaryKey(autoGenerate = true) val uid: Int = 0,
  @ColumnInfo(name = "id")
  val id: String,
  @ColumnInfo(name = "artistName")
  val artistName: String,
  @ColumnInfo(name = "name")
  val name: String,
  @ColumnInfo(name = "releaseDate")
  val releaseDate: String,
  @ColumnInfo(name = "artworkUrl100")
  val artworkUrl100: String,
  val genres: List<GenreDso> = emptyList(),
  @ColumnInfo(name = "copyright")
  val copyright: String,
  @ColumnInfo(name = "url")
  val url: String,
  @ColumnInfo(name = "country")
  val countryCode: String
)