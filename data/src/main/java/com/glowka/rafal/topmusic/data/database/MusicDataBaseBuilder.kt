package com.glowka.rafal.topmusic.data.database

import android.content.Context
import androidx.room.Room

object MusicDataBaseBuilder {
  fun create(applicationContext: Context) = Room.databaseBuilder(
    applicationContext,
    MusicDatabase::class.java, "musicDB"
  ).build()
}