package com.example.twitterclone.caching

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date


@Entity(tableName = "cache")
data class CacheModel(
    @PrimaryKey(autoGenerate = false)
    val tweetId : String,
    @ColumnInfo
    val commentNo : Int,
    @ColumnInfo
    val content : String,
    @ColumnInfo
    val likesCount : Int,
    @ColumnInfo
    val timestamp : Date,
    @ColumnInfo
    val url : List<Map<String, String>>,
    @ColumnInfo
    val userId: String,
    @ColumnInfo
    val retweeted : Boolean,
    @ColumnInfo
    val name : String?,
    @ColumnInfo
    val retweets : Long,
)