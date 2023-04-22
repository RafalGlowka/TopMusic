package com.glowka.rafal.topmusic.data.database

import androidx.room.TypeConverter
import com.glowka.rafal.topmusic.data.dso.GenreDso
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class GenresDataConverter {
    @TypeConverter
    fun fromGenresList(genres: List<GenreDso?>?): String? {
      if (genres == null) {
        return null
      }
      val gson = Gson()
      val type: Type = object : TypeToken<List<GenreDso?>?>() {}.type
      return gson.toJson(genres, type)
    }

    @TypeConverter
    fun toGenresList(genresString: String?): List<GenreDso>? {
      if (genresString == null) {
        return null
      }
      val gson = Gson()
      val type: Type = object : TypeToken<List<GenreDso?>?>() {}.type
      return gson.fromJson<List<GenreDso>>(genresString, type)
    }
}