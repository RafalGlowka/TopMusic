package com.glowka.rafal.topmusic.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.glowka.rafal.topmusic.data.dso.AlbumDso
import com.glowka.rafal.topmusic.domain.model.Album

@Dao
interface AlbumDao {
  @Query("SELECT * FROM album")
  fun getAll(): List<AlbumDso>

  @Insert
  fun insertAll(vararg albums: AlbumDso)

  @Query("DELETE FROM album")
  fun deleteAll()
}