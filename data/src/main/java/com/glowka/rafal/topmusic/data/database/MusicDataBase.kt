package com.glowka.rafal.topmusic.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.glowka.rafal.topmusic.data.dao.AlbumDao
import com.glowka.rafal.topmusic.data.dao.ConfigDao
import com.glowka.rafal.topmusic.data.dso.AlbumDso
import com.glowka.rafal.topmusic.data.dso.ConfigDso

@Database(entities = [AlbumDso::class, ConfigDso::class], version = 2)
@TypeConverters(GenresDataConverter::class)
abstract class MusicDatabase : RoomDatabase() {
  abstract fun albumDao(): AlbumDao
  abstract fun configDao(): ConfigDao
}