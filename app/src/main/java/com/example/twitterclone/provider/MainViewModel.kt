package com.example.twitterclone.provider

import android.content.ContentValues
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.twitterclone.model.CommentsModel
import com.example.twitterclone.model.TweetModel
import com.example.twitterclone.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import okhttp3.internal.wait
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _data = MutableLiveData<MutableMap<String, Any>?>(null)
    val data: LiveData<MutableMap<String, Any>?> get() = _data

    val auth = FirebaseAuth.getInstance()
    val store = Firebase.firestore
    var currentUser = auth.currentUser
    val storage = FirebaseStorage.getInstance()
    var tweetCount = 0L


    fun getProfile() {

        val documentRef = store.collection("users").document(currentUser!!.uid)
        Log.d("DATAPROFILE", currentUser!!.uid)
        documentRef
            .get()
            .addOnSuccessListener {

                _data.value = it.data!!
                tweetCount = data.value!!["noOfTweets"] as Long
                Log.d("DATAPROFILE", "${it.data}")
                Log.d("DATAPROFILE", "${tweetCount}")

            }
            .addOnFailureListener { exception ->
                Log.d("DATAPROFILE", "Error getting documents.", exception)

            }

    }

    fun postTweet(content: String?, mediaUri: Uri?, isImage: Boolean) {

        var downloadUri = ""
        tweetCount += 1
        // Get a reference to the storage location where you want to upload the file
        if (mediaUri != null) {
            val storageRef = storage.reference

            var fileRef = storageRef.child("images")
            if (isImage) {

                fileRef =
                    storageRef.child("images/${currentUser!!.uid}${tweetCount}.jpg") // Specify the desired storage location and file name
            } else {
                fileRef = storageRef.child("video/${currentUser!!.uid}${tweetCount}.mp4")
            }
            // Create a reference to the file in Firebase Storage

            // Upload the file to Firebase Storage
            val uploadTask = fileRef.putFile(mediaUri)
            val urlTask = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                fileRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    downloadUri = task.result.toString()
                    val tweet = TweetModel(
                        content = content ?: "",
                        userId = data.value!!["userId"].toString(),
                        tweetId = tweetCount.toString(),
                        likesCount = 0L,
                        timestamp = FieldValue.serverTimestamp(),
                        url = downloadUri,
                        commentNo = 0,
                        retweeted = false,
                        name = null,
                        retweets = 0

                    )
                    val documentRef =
                        store.collection("tweets").document("${currentUser!!.uid}${tweetCount}")

                    documentRef.set(tweet)
                        .addOnSuccessListener {
                            Log.d("SUCCESS", "success tweet gg")
                        }
                        .addOnFailureListener { e ->
                            Log.w(ContentValues.TAG, "Error adding document", e)
                        }
                    Log.d("DOWNLOADURL", "$downloadUri")
                }
            }
        }

        if (mediaUri == null) {

            val tweet = TweetModel(
                content = content ?: "",
                userId = data.value!!["userId"].toString(),
                tweetId = tweetCount.toString(),
                likesCount = 0L,
                timestamp = FieldValue.serverTimestamp(),
                url = "",
                commentNo = 0,
                retweeted = false,
                name = null,
                retweets = 0

            )
            val documentRef =
                store.collection("tweets").document("${currentUser!!.uid}${tweetCount}")

            documentRef.set(tweet)
                .addOnSuccessListener {
                    Log.d("SUCCESS", "success tweet gg")
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error adding document", e)
                }
        }


        val user = User(
            name = data.value!!["name"].toString(),
            email = data.value!!["email"].toString(),
            bio = data.value!!["bio"].toString(),
            profilePic = data.value!!["profilePic"].toString(),
            userId = data.value!!["userId"].toString(),
            noOfTweets = tweetCount,
            followers = 0,
            following = 0
        )
        val documentRef = store.collection("users").document(Firebase.auth.currentUser!!.uid)

        documentRef.set(user)
            .addOnSuccessListener {

            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }

    }


    fun getTweet(documentId: String): Flow<MutableMap<String, Any>?> {
        val documentRef = store.collection("tweets").document(documentId)
        return documentRef.snapshots().map { it.data }
    }

    fun Likes(documentId: String): Flow<QuerySnapshot> {
        return store.collection("tweets").document(documentId).collection("likes").snapshots()
    }

    fun updateProfile(name : String ,  url : Uri? , bio : String){

        val ref = store.collection("users").document(currentUser!!.uid)

        var dwndurl = ""
        if (url != null){

            val storageRef = storage.reference
            val fileRef = storageRef.child("images/${currentUser!!.uid}.jpg")

            // Upload the file to Firebase Storage
            val uploadTask = fileRef.putFile(url)
            val urlTask = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                fileRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful){
                    dwndurl = task.result.toString()
                    ref.update(
                        mutableMapOf<String,Any>(
                            "name" to name,

                            "bio" to bio,
                            "profilePic" to dwndurl
                        )
                    )
                }
                return@addOnCompleteListener
            }
        }else{

            ref.update(
                mutableMapOf<String,Any>(
                    "name" to name,

                    "bio" to bio,
                    "profilePic" to dwndurl
                )
            ).addOnSuccessListener {
                return@addOnSuccessListener
            }
        }

    }




    suspend fun updateLike(documentId: String, initalValue: Long) {

        val docRef2 = store.collection("tweets").document(documentId).collection("likes")
            .document(currentUser!!.uid)
        val documentRef = store.collection("tweets").document(documentId)

        if (docRef2.get().await().exists()) {
            docRef2.get().addOnSuccessListener {
                    docRef2.delete()

                    documentRef.update(
                        mutableMapOf<String, Any>(
                            "likesCount" to initalValue - 1
                        )
                    )
            }
        } else {
            docRef2.set(
                mutableMapOf<String, Any>(
                    "hasVoted" to true
                )
            )

            documentRef.update(
                mutableMapOf<String, Any>(
                    "likesCount" to initalValue + 1
                )
            )
        }
    }
     fun getRetweet(documentId: String): Flow<MutableMap<String, Any>?>  {

        return store.collection("tweets").document(documentId).snapshots().map { it.data }
    }

     fun updateRetweet(documentId: String, initalValue: Long) {

        val docRef2 = store.collection("tweets").document(documentId)
        docRef2.update(mutableMapOf<String,Any>(
            "retweets" to initalValue+1
        ))

         // ek naya field ref bana jo prev doc to refer kerega n uska snapshot retweeted true vala pe dikhega
    }

    fun retweet(documentId: String){
        tweetCount+=1
        val docRef = store.collection("tweets").document(documentId)
        val newRef = store.collection("tweets").document("${currentUser!!.uid}${tweetCount}")
        newRef.set(
            mutableMapOf<String,Any>(
                "documentId" to documentId,
                "retweeted" to true,
                "timestamp" to FieldValue.serverTimestamp(),
                "name" to data.value!!["name"].toString(),
                "userId" to data.value!!["userId"].toString()
            )
        )

        val documentRef = store.collection("users").document(Firebase.auth.currentUser!!.uid)
        documentRef.update(
            mutableMapOf<String,Any>(
                "noOfTweets" to tweetCount
            )
        )

    }

    suspend fun postComment(
        documentId: String,
        content: String?,
        mediaUri: Uri?,
        isImage: Boolean
    ) {

        var downloadUri = ""
        // Get a reference to the storage location where you want to upload the file
        if (mediaUri != null) {
            val storageRef = storage.reference

            var fileRef = storageRef.child("images")
            if (isImage) {

                fileRef =
                    storageRef.child("images/${currentUser!!.uid}$documentId.jpg") // Specify the desired storage location and file name
            } else {
                fileRef = storageRef.child("video/${currentUser!!.uid}$documentId.mp4")
            }
            // Create a reference to the file in Firebase Storage

            // Upload the file to Firebase Storage
            val uploadTask = fileRef.putFile(mediaUri)
            val urlTask = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                fileRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    downloadUri = task.result.toString()
                    val documentRef =
                        store.collection("tweets").document(documentId).collection("comments")
                            .document(currentUser!!.uid)
                    val comment = CommentsModel(
                        timestamp = FieldValue.serverTimestamp(),
                        userId = currentUser!!.uid,
                        content = content ?: "",
                        url = downloadUri
                    )
                    documentRef.set(comment)
                }
            }
        }

        if (mediaUri == null) {

            val documentRef = store.collection("tweets").document(documentId).collection("comments")
                .document(currentUser!!.uid)
            val comment = CommentsModel(
                timestamp = FieldValue.serverTimestamp(),
                userId = currentUser!!.uid,
                content = content ?: "",
                url = ""
            )
            documentRef.set(comment)
        }

        val documentRef2 = store.collection("tweets").document(documentId)
        val res = documentRef2.get().await()
        documentRef2.update(
            mutableMapOf<String, Any>(
                "commentNo" to (Integer.parseInt(res.data!!["commentNo"]!!.toString()) + 1)
            )
        )


    }

    fun getComments(documentId: String): Flow<QuerySnapshot> {
        val documentRef = store.collection("tweets").document(documentId).collection("comments")
        return documentRef.snapshots()
    }


    fun getUser(userId: String): Flow<MutableMap<String, Any>> {
        val documentRef = store.collection("users").document(userId)
        return documentRef.snapshots().map {
            it.data!!
        }
    }

    fun getTweetsHomeScreen(following: State<QuerySnapshot?>): Flow<QuerySnapshot> {

        val followedTweets = mutableListOf<String>()
        followedTweets.add(data.value!!["userId"].toString())


        if (following.value != null) {
            for (doc in following.value!!.documents) {
                followedTweets.add(doc.data!!["userId"].toString())
            }

        }


        val tweetsRef = store.collection("tweets")
            .orderBy("timestamp" , Query.Direction.ASCENDING)
            .whereIn("userId", followedTweets)
            .snapshots()


        return tweetsRef

    }

    suspend fun searchUserExist(userId: String): String? {
        val Ref = store.collection("users")
        val snapshot = Ref.whereEqualTo("userId", userId.toString()).limit(1).get().await()
        if (!snapshot.isEmpty) {
            val documentId = snapshot.documents[0].id
            return documentId
        }
        return null
    }

    fun follow(
        followingDocumentId: String,
        followersCount: Long,
        userId: String,
        documentId: String
    ) {


        val ref = store.collection("users").document(currentUser!!.uid).collection("following")
            .document(documentId)
        val res = ref.get().addOnSuccessListener {
            if (it.data == null) {

                val followersRef =
                    store.collection("users").document(followingDocumentId).collection("followers")
                        .document(currentUser!!.uid)
                val followingRef =
                    store.collection("users").document(currentUser!!.uid).collection("following")
                        .document(followingDocumentId)

                followersRef.set(
                    mutableMapOf(
                        "userId" to data.value!!["userId"]
                    )
                )
                followingRef.set(
                    mutableMapOf(
                        "userId" to userId
                    )
                )
                val dataRef = store.collection("users").document(followingDocumentId)
                val dataRef2 = store.collection("users").document(currentUser!!.uid)


                dataRef.update(
                    mutableMapOf<String, Any>(
                        "followers" to followersCount + 1
                    )
                )
                dataRef2.update(
                    mutableMapOf<String, Any>(
                        "following" to (Integer.parseInt(data.value!!["following"]!!.toString()) + 1)
                    )
                )
            } else {

                val followersRef = store.collection("users").document(followingDocumentId)
                    .collection("followers").document(currentUser!!.uid)
                val followingRef = store.collection("users").document(currentUser!!.uid)
                    .collection("following").document(followingDocumentId)

                followersRef.delete()
                followingRef.delete()

                val dataRef = store.collection("users").document(followingDocumentId)
                val dataRef2 = store.collection("users").document(currentUser!!.uid)

                dataRef.update(
                    mutableMapOf<String, Any>(
                        "followers" to followersCount - 1
                    )
                )
                dataRef2.update(
                    mutableMapOf<String, Any>(
                        "following" to (Integer.parseInt(data.value!!["following"]!!.toString()) - 1)
                    )
                )

            }
        }

    }

    fun isFollowing(documentId: String): Flow<DocumentSnapshot> {
        val ref = store.collection("users").document(currentUser!!.uid).collection("following")
            .document(documentId)
        val res = ref.snapshots()
        return res
    }

    fun getFollowers(): Flow<QuerySnapshot> {

        val followersRef = store.collection("users").document(currentUser!!.uid).collection("followers")
        return followersRef.snapshots()
    }

    fun getFollowing(): Flow<QuerySnapshot> {
        val followingRef = store.collection("users").document(currentUser!!.uid).collection("following")
        return followingRef.snapshots()
    }

}
