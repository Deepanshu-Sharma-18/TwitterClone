package com.example.twitterclone.data.model

import com.google.firebase.firestore.FieldValue
import java.sql.Timestamp

data class CommentsModel(
    val content : String,
    val userId : String,
    val timestamp: FieldValue,
    val url : String
)