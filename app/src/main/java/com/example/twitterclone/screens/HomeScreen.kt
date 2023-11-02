package com.example.twitterclone.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.twitterclone.R
import com.example.twitterclone.components.TweetCard
import com.example.twitterclone.provider.viewModels.authentication.AuthViewModel
import com.example.twitterclone.provider.viewModels.appViewModel.MainViewModel
import com.example.twitterclone.Navigation.Screens
import com.example.twitterclone.caching.CacheModel
import com.example.twitterclone.caching.CacheUserModel
import com.google.firebase.Timestamp
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import java.lang.Thread.sleep

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class, DelicateCoroutinesApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun HomeScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    mainViewModel: MainViewModel
) {

    val scrollState = rememberScrollState()
    val data = mainViewModel.data.observeAsState()

    val topAppBarScrollBehavior =  TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        rememberTopAppBarState()
    )

    val isLoading = remember{
        mutableStateOf(false)
    }




        if (data.value == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(modifier = Modifier.size(30.dp), color =  MaterialTheme.colorScheme.primary)
            }
        } else {
            val following = mainViewModel.getFollowing()
                .collectAsState(initial = null)
            val userData = mainViewModel.data

            Log.d("DATAPROFILEHome", "$userData")
            Scaffold(
                topBar = {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = MaterialTheme.colorScheme.background)
                                .padding(vertical = 5.dp, horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            IconButton(onClick = { navController.navigate(Screens.ProfileScreen.name) }) {
                                AsyncImage(
                                    model = data.value!!["profilePic"],
                                    contentDescription = "profile image",
                                    contentScale = ContentScale.FillWidth,
                                    modifier = Modifier
                                        .size(35.dp)
                                        .clip(shape = RoundedCornerShape(corner = CornerSize(50)))
                                )
                            }

                            AsyncImage(
                                model = "https://cdn-icons-png.flaticon.com/512/733/733579.png?w=740&t=st=1688284770~exp=1688285370~hmac=4749b7d8b4f365320a235d7098ffbdd6ca7f5082d5f90d5d19940594a0bdc3f1",
                                contentDescription = "image-twitter",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .size(30.dp)

                            )

                            IconButton(onClick = {
                                navController.navigate(Screens.TrendingScreen.name)
                            }) {
                                Image(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.moments),
                                    contentDescription = "sparkling",
                                    modifier = Modifier
                                        .size(20.dp)
                                )
                            }
                        }
                        Divider(
                            thickness = 0.4.dp,
                            color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f),
                            modifier = Modifier.padding(horizontal = 1.dp)
                        )
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { navController.navigate(Screens.CreateTweet.name) },
                        contentColor = MaterialTheme.colorScheme.background,
                        containerColor = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(corner = CornerSize(50)),
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.tweet),
                            contentDescription = "add",
                            modifier = Modifier.size(23.dp)
                        )
                    }
                },
                bottomBar = {

                    BottomAppBar(
                        contentPadding = PaddingValues(0.dp),
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                    ) {

                        Column(verticalArrangement = Arrangement.Top) {
                            Divider(
                                thickness = 0.4.dp,
                                color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f),
                                modifier = Modifier.padding(horizontal = 1.dp)
                            )
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 40.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.home),
                                    contentDescription = "home",
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clickable {
                                            navController.navigate(Screens.HomeScreen.name)
                                        },
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.search),
                                    contentDescription = "search",
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clickable {
                                            navController.navigate(Screens.SearchScreen.name)
                                        },
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.alert),
                                    contentDescription = "notification",
                                    modifier = Modifier.size(20.dp),
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.messagehome),
                                    contentDescription = "Mail",
                                    modifier = Modifier.size(20.dp),
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                            }


                        }
                    }
                }) {

                Log.d("TESTHOMESCREEN" , data.value!!["following"].toString() + mainViewModel.tweetCount.toString())

                if ( data.value!!["following"].toString() == "0" && mainViewModel.tweetCount == 0L){
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = MaterialTheme.colorScheme.background),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "No Tweets", style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.W500
                        ))
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = "Follow People to view tweets", style = TextStyle(
                            fontSize = 17.sp,
                            fontWeight = FontWeight.W300
                        ))


                    }
                }else{

                    val tweets by mainViewModel.getTweetsHomeScreen(following)
                        .collectAsState(initial = null)

                    if (tweets == null || isLoading.value) {


                        Log.d("TWEETSSTATUS" , tweets.toString())
                        Log.d("TWEETSSTATUS" , tweets?.documents.toString())
                    } else {


                        val tweetsCacheList = mutableListOf<CacheModel>()


                        LaunchedEffect(key1 = Unit, block = {
                            val listOfTweets = tweets!!

                            synchronized(listOfTweets){

                                for(tweet in listOfTweets){
                                    var tp = tweet.data!!["timestamp"] as Timestamp
                                    val date = tp.toDate()
                                    val list = tweet.data!!["url"] as List<Map<String, String>>
                                    val commentNo = tweet.data!!["commentNo"] as Long
                                    val likesCount = tweet.data!!["likesCount"] as Long
                                    tweetsCacheList.add(
                                        CacheModel(
                                            tweetId = tweet.data!!["tweetId"].toString(),
                                            name = tweet.data!!["name"].toString(),
                                            commentNo = commentNo.toInt() ,
                                            content = tweet.data!!["content"].toString(),
                                            likesCount = likesCount.toInt(),
                                            retweeted = tweet.data!!["retweeted"].toString() == "true",
                                            retweets = tweet.data!!["retweets"] as Long,
                                            timestamp = date,
                                            url = list,
                                            userId = tweet.data!!["userId"].toString()
                                        )
                                    )

                                    Log.d("TWEETSCACHE" , tweetsCacheList.toList().toString())
                                    GlobalScope.launch (Dispatchers.IO) {

                                        mainViewModel.saveCacheTweets(tweetsCacheList)
                                    }
                                }
                            }

                            val followersCache = userData!!.value!!["followers"] as Long
                            val followingCache = userData!!.value!!["followers"] as Long

                            val cacheUserModel = CacheUserModel(
                                name = userData!!.value!!["name"].toString(),
                                userId = userData!!.value!!["userId"].toString(),
                                profilePic = userData!!.value!!["profilePic"].toString(),
                                email = userData!!.value!!["email"].toString(),
                                bio = userData!!.value!!["bio"].toString(),
                                followers = followersCache.toInt(),
                                following = followingCache.toInt(),
                                noOfTweets = userData!!.value!!["noOfTweets"] as Long
                            )

                            GlobalScope.launch(Dispatchers.IO) {
                                mainViewModel.insertCacheUser(
                                    cacheUserModel
                                )
                            }
                        })


                        val pullRefreshState = rememberPullRefreshState(
                            refreshing = isLoading.value,
                            onRefresh = {
                                isLoading.value = true
                                mainViewModel.getTweetsHomeScreen(following)
                                GlobalScope.launch(Dispatchers.Main) {
                                    sleep(3000L)
                                }
                                isLoading.value = false
                            }
                        )


                        Box(modifier = Modifier.fillMaxWidth()){
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .pullRefresh(pullRefreshState)
                                    .verticalScroll(scrollState)
                                    .background(color = MaterialTheme.colorScheme.background)
                                    .padding(top = 58.dp, start = 3.dp, bottom = 50.dp)
                            ) {


                                for (tweet in tweets!!) {
                                    TweetCard(
                                        documentId = tweet.id,
                                        mainViewModel = mainViewModel,
                                        navController = navController,
                                        authViewModel = authViewModel
                                    )


                                }
                            }
                                PullRefreshIndicator(
                                    refreshing = isLoading.value,
                                    state = pullRefreshState,
                                    modifier = Modifier.align(Alignment.TopCenter),
                                    contentColor = MaterialTheme.colorScheme.primary,
                                    backgroundColor = MaterialTheme.colorScheme.onBackground,
                                )
                        }

                    }

                }
        }
    }
}