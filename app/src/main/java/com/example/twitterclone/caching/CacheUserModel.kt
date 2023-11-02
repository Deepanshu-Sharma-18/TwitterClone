package com.example.twitterclone.caching

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class CacheUserModel(
    @ColumnInfo
    val name: String,
    @PrimaryKey(autoGenerate = false)
    val userId: String,
    @ColumnInfo
    val profilePic: String,
    @ColumnInfo
    val email:String,
    @ColumnInfo
    val bio: String,
    @ColumnInfo
    val noOfTweets: Long,
    @ColumnInfo
    val following : Int,
    @ColumnInfo
    val followers : Int
)
