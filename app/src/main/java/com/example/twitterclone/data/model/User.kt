package com.example.twitterclone.data.model

data class User(
    val name: String,
    val userId: String,
    val profilePic: String,
    val email:String,
    val bio: String,
    val noOfTweets: Long,
    val following : Int,
    val followers : Int,
    val tweets : List<String>
)
