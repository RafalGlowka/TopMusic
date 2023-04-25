package com.glowka.rafal.topmusic.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.glowka.rafal.topmusic.data.dso.ConfigDso

@Dao
interface ConfigDao {
  @Query("SELECT * FROM config WHERE key = :key")
  fun getAll(key: String): List<ConfigDso>

  @Insert
  fun insertAll(vararg configs: ConfigDso)

  @Query("DELETE FROM config")
  fun deleteAll()
}