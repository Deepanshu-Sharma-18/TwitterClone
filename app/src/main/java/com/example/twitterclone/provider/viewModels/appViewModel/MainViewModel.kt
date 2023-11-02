package com.example.twitterclone.provider.viewModels.appViewModel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.provider.Settings.Global
import android.util.Log
import androidx.compose.runtime.State
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.twitterclone.caching.CacheModel
import com.example.twitterclone.caching.CacheUserModel
import com.example.twitterclone.model.CommentsModel
import com.example.twitterclone.model.TweetModel
import com.example.twitterclone.model.User
import com.example.twitterclone.provider.Repository
import com.google.firebase.Timestamp
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
import io.grpc.util.ForwardingLoadBalancer
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class MainViewModel @Inject constructor( private val repository: Repository) : ViewModel() {
    private val _data = MutableLiveData<MutableMap<String, Any>?>(null)
    val data: LiveData<MutableMap<String, Any>?> get() = _data

    val auth = FirebaseAuth.getInstance()
    val store = Firebase.firestore
    var currentUser = auth.currentUser
    val storage = FirebaseStorage.getInstance()
    var tweetCount = 0L


    suspend fun getProfile() {

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
            .await()
    }


    fun getCachedUser() = repository.getCacheUser()

    suspend fun insertCacheUser(cacheUserModel: CacheUserModel) {
        repository.deleteCacheUser()
        repository.insertCacheUser(cacheUserModel)
    }




    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            // For devices with versions below Android M
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }

    suspend fun postTweet(content: String?, mediaUri: List<Pair<Uri? , Boolean>>?) {

        var tweetCnt = tweetCount + 1

        val tweetsList = data.value!!["tweets"] as MutableList<String>

        var downloadUri = ""
        var mediaList = mutableListOf<Map<String,String>>()

        val seed = System.currentTimeMillis()
        val randomInt = Random(seed)
        // Get a reference to the storage location where you want to upload the file
        val storageRef = storage.reference
        if (!mediaUri.isNullOrEmpty()) {

            var fileRef = storageRef.child("images")

            for (i in 0..mediaUri.lastIndex-1){
                Log.d("TWEETSTATUS" , "here in loop $i")
                if (mediaUri[i].second) {

                    fileRef =
                        storageRef.child("${currentUser!!.uid}/${randomInt.nextInt(0,1000000)}.jpg") // Specify the desired storage location and file name
                } else {
                    fileRef = storageRef.child("${currentUser!!.uid}/${randomInt.nextInt(0,1000000)}.mp4")
                }

                val uploadTask = mediaUri[i].first?.let { fileRef.putFile(it) }

                val result = uploadTask?.await()

                if (result?.task?.isSuccessful == true){

                    mediaList.add(
                        hashMapOf(
                            "link" to result.storage.downloadUrl.await().toString() ,
                            "isImage" to mediaUri[i].second.toString()
                        )
                    )

                }
            }

            Log.d("TWEETSTATUS" , mediaList.toList().toString())

            if (mediaUri[mediaUri.lastIndex].second) {

                fileRef =
                    storageRef.child("${currentUser!!.uid}/${randomInt.nextInt(0,10000)}.jpg") // Specify the desired storage location and file name
            } else {
                fileRef = storageRef.child("${currentUser!!.uid}/${randomInt.nextInt(0,10000)}.mp4")
            }


            val newRand = randomInt.nextInt(10000000)
            tweetsList.add("${currentUser!!.uid}${newRand}")

            val uploadTask = mediaUri[mediaUri.lastIndex].first?.let { fileRef.putFile(it) }
            val urlTask = uploadTask?.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }

                fileRef.downloadUrl
            }?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    mediaList.add(hashMapOf(
                        "link" to task.result.toString(),
                        "isImage" to mediaUri[mediaUri.lastIndex].second.toString()
                    ))
                    Log.d("TWEETSTATUS", mediaList.toList().toString())

                    val tweet = TweetModel(
                        content = content ?: "",
                        userId = data.value!!["userId"].toString(),
                        tweetId = tweetCnt.toString(),
                        likesCount = 0L,
                        timestamp = FieldValue.serverTimestamp(),
                        url = mediaList,
                        commentNo = 0,
                        retweeted = false,
                        name = data.value!!["name"].toString(),
                        retweets = 0

                    )

                    val documentRef =
                        store.collection("tweets").document("${currentUser!!.uid}${newRand}")



                    documentRef.set(tweet)
                        .addOnSuccessListener {
                            Log.d("TWEETSTATUS", "success tweet gg")

                        }
                        .addOnFailureListener { e ->
                            Log.d("TWEETSTATUS", e.toString())
                        }

                }
            }
        }

        if (mediaUri.isNullOrEmpty()) {

            val tweet = TweetModel(
                content = content ?: "",
                userId = data.value!!["userId"].toString(),
                tweetId = tweetCnt.toString(),
                likesCount = 0L,
                timestamp = FieldValue.serverTimestamp(),
                url = emptyList(),
                commentNo = 0,
                retweeted = false,
                name = data.value!!["name"].toString(),
                retweets = 0

            )
            val newRand = randomInt.nextInt(10000000)
            val documentRef =
                store.collection("tweets").document("${currentUser!!.uid}${newRand}")

            documentRef.set(tweet)
                .await()
            tweetsList.add("${currentUser!!.uid}${newRand}")
        }


        val user = User(
            name = data.value!!["name"].toString(),
            email = data.value!!["email"].toString(),
            bio = data.value!!["bio"].toString(),
            profilePic = data.value!!["profilePic"].toString(),
            noOfTweets = tweetCnt,
            userId = data.value!!["userId"].toString(),
            followers = 0,
            following = 0,
            tweets = tweetsList
        )
        val documentRef = store.collection("users").document(Firebase.auth.currentUser!!.uid)

        documentRef.set(user)
            .await()

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


    fun deleteTweet(documentId: String){
        val docRef = store.collection("tweets").document(documentId)


        val tweetList = data.value!!["tweets"] as MutableList<String>

        tweetList.remove(documentId)

        val initalValue = tweetCount

        val userRef = store.collection("users").document(currentUser!!.uid)

        userRef.update(
            mutableMapOf(
                "tweets" to tweetList,
                "noOfTweets" to initalValue - 1
            )
        )

        docRef.delete()
        _data.value = null
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
            .orderBy("timestamp" , Query.Direction.DESCENDING)
            .whereIn("userId", followedTweets)
            .snapshots()

        return tweetsRef
    }

    suspend fun saveCacheTweets(tweetsCacheList : List<CacheModel>){
        repository.deleteCachedTweets()
        repository.insertCachedTweets(tweetsCacheList)
    }

    fun getCachedTweets() = repository.getCachedTweets()


    fun getTrendingTweets() :  Flow<QuerySnapshot>{
        val trendingRef = store.collection("tweets")
            .orderBy("likesCount" , Query.Direction.DESCENDING)
            .snapshots()

        return trendingRef
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
