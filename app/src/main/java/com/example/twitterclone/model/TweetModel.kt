package com.example.twitterclone.model

import com.google.firebase.firestore.FieldValue
import java.net.URI

data class TweetModel(
    val commentNo : Int,
    val content:String,
    val likesCount:Long,
    val timestamp: Any,
    val tweetId: String,
    val url: String,
    val userId: String,
    val retweeted : Boolean,
    val name : String?,
    val retweets : Long,
)
