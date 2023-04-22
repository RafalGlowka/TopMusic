package com.glowka.rafal.topmusic.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.glowka.rafal.topmusic.data.dao.AlbumDao
import com.glowka.rafal.topmusic.data.dso.AlbumDso

@Database(entities = [AlbumDso::class], version = 1)
@TypeConverters(GenresDataConverter::class)
abstract class AlbumDatabase : RoomDatabase() {
  abstract fun albumDao(): AlbumDao
}