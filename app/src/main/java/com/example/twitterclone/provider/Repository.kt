package com.example.twitterclone.provider

import com.example.twitterclone.caching.CacheModel
import com.example.twitterclone.caching.CacheModule
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(private val cacheModule: CacheModule) {

    suspend fun deleteCachedTweets() = cacheModule.deleteCachedTweets()
    fun getCachedTweets() : Flow<List<CacheModel>> = cacheModule.getCachedTweets()
    suspend fun insertCachedTweets(tweets : List<CacheModel>) = cacheModule.insertCachedTweets(tweets)
}