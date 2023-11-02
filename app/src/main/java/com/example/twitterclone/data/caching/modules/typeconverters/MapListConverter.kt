package com.example.twitterclone.data.caching.modules.typeconverters

import androidx.room.TypeConverter
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

class MapListConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromMapList(mapList: List<Map<String, String>>?): String? {
        return gson.toJson(mapList)
    }

    @TypeConverter
    fun toMapList(json: String?): List<Map<String, String>>? {
        return gson.fromJson(json, object : TypeToken<List<Map<String, String>>>() {}.type)
    }

}