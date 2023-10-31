package com.example.twitterclone.provider.caching

import android.content.Context
import androidx.room.Room
import com.example.twitterclone.caching.CacheDatabase
import com.example.twitterclone.caching.CacheModule
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
    fun provideAppDatabase(@ApplicationContext context: Context): CacheDatabase =
        Room.databaseBuilder(
            context,
            CacheDatabase::class.java,
            "todoDb"
        ).fallbackToDestructiveMigration().build()
}