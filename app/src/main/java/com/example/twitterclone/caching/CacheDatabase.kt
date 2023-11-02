package com.example.twitterclone.caching

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.util.Date


@Database(entities = [CacheModel::class , CacheUserModel::class] , version = 3 , exportSchema = false)
@TypeConverters(CacheDatabase::class , MapListConverter::class)
abstract class CacheDatabase : RoomDatabase() {

    abstract fun dao() : CacheModule

    abstract fun daoUser() : CacheUserModule

    companion object{
        @TypeConverter
        @JvmStatic
        fun fromTimestamp(value: Long?): Date? {
            return value?.let { Date(it) }
        }

        @TypeConverter
        @JvmStatic
        fun dateToTimestamp(date: Date?): Long? {
            return if (date == null) null else date.getTime()
        }


    }
}