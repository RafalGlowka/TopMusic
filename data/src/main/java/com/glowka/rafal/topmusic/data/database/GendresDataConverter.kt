package com.glowka.rafal.topmusic.data.database

import androidx.room.TypeConverter
import com.glowka.rafal.topmusic.data.dso.GenreDso
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class GendresDataConverter {
    @TypeConverter
    fun fromGendresList(gendres: List<GenreDso?>?): String? {
      if (gendres == null) {
        return null
      }
      val gson = Gson()
      val type: Type = object : TypeToken<List<GenreDso?>?>() {}.type
      return gson.toJson(gendres, type)
    }

    @TypeConverter
    fun toGendresList(gendresString: String?): List<GenreDso>? {
      if (gendresString == null) {
        return null
      }
      val gson = Gson()
      val type: Type = object : TypeToken<List<GenreDso?>?>() {}.type
      return gson.fromJson<List<GenreDso>>(gendresString, type)
    }
}