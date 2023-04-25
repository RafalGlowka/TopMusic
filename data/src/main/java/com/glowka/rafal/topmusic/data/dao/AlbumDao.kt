package com.glowka.rafal.topmusic.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.glowka.rafal.topmusic.data.dso.AlbumDso

@Dao
interface AlbumDao {
  @Query("SELECT * FROM album WHERE country = :countryCode")
  fun getAllWithCountryCode(countryCode: String): List<AlbumDso>

  @Insert
  fun insertAll(vararg albums: AlbumDso)

  @Query("DELETE FROM album")
  fun deleteAll()

  @Query("DELETE FROM album WHERE country = :countryCode")
  fun deleteAlbumsWithCountryCode(countryCode: String)
}