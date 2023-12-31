package com.example.twitterclone.provider.caching

import android.content.Context
import androidx.room.Room
import com.example.twitterclone.data.caching.CacheDatabase
import com.example.twitterclone.data.caching.modules.CacheModule
import com.example.twitterclone.data.caching.modules.CacheUserModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Singleton
    @Provides
    fun cacheProvider (database: CacheDatabase) : CacheModule {
        return database.dao()
    }

    @Singleton
    @Provides
    fun cacheUserProvider (database: CacheDatabase) : CacheUserModule {
        return database.daoUser()
    }

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): CacheDatabase =
        Room.databaseBuilder(
            context,
            CacheDatabase::class.java,
            "todoDb"
        ).fallbackToDestructiveMigration().build()
}