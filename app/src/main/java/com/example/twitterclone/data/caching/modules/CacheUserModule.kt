package com.example.twitterclone.data.caching.modules

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.twitterclone.data.caching.models.CacheUserModel
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