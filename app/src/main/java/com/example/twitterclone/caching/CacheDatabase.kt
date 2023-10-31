package com.example.twitterclone.caching

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.util.Date


@Database(entities = [CacheModel::class] , version = 1)
@TypeConverters(CacheDatabase::class)
abstract class CacheDatabase : RoomDatabase() {

    abstract fun dao() : CacheModule

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