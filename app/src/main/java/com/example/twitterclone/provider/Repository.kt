package com.example.twitterclone.provider

import com.example.twitterclone.data.caching.models.CacheModel
import com.example.twitterclone.data.caching.modules.CacheModule
import com.example.twitterclone.data.caching.models.CacheUserModel
import com.example.twitterclone.data.caching.modules.CacheUserModule
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(private val cacheModule: CacheModule, private val cacheUserModule: CacheUserModule) {

    suspend fun deleteCachedTweets() = cacheModule.deleteCachedTweets()
    fun getCachedTweets() : Flow<List<CacheModel>> = cacheModule.getCachedTweets()
    suspend fun insertCachedTweets(tweets : List<CacheModel>) = cacheModule.insertCachedTweets(tweets)


    suspend fun deleteCacheUser() = cacheUserModule.deleteCacheUser()

    fun getCacheUser() : Flow<CacheUserModel> = cacheUserModule.getCacheUser()

    suspend fun insertCacheUser(cacheUserModel: CacheUserModel)  = cacheUserModule.insertUserCache(cacheUserModel)
}