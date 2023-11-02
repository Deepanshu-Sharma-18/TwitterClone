package com.example.twitterclone.caching

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
@Dao
interface CacheUserModule {
     @Query("Select * from user")
     fun getCacheUser() : Flow<CacheUserModel>

     @Query("DELETE from user")
     suspend fun deleteCacheUser()

     @Insert
     suspend fun insertUserCache(cacheUserModel: CacheUserModel)
}