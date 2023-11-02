package com.example.twitterclone.data.caching.modules

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.twitterclone.data.caching.models.CacheModel
import kotlinx.coroutines.flow.Flow

@Dao
interface CacheModule {
    @Query("DELETE from cache")
    suspend fun deleteCachedTweets()
    @Query("Select * from cache")
     fun getCachedTweets() : Flow<List<CacheModel>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCachedTweets(tweets : List<CacheModel>)
}