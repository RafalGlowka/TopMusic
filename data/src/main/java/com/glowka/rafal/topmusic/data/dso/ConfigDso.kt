package com.glowka.rafal.topmusic.data.dso

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "config")
data class ConfigDso(
  @PrimaryKey(autoGenerate = true) val uid: Int = 0,
  @ColumnInfo(name = "key")
  val key: String,
  @ColumnInfo(name = "value")
  val value: String,
)