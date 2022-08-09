package com.glowka.rafal.topmusic.data.database

import android.content.Context
import androidx.room.Room

object AlbumDataBaseBuilder {
  fun create(applicationContext: Context) = Room.databaseBuilder(
    applicationContext,
    AlbumDatabase::class.java, "albumDB"
  ).build()
}