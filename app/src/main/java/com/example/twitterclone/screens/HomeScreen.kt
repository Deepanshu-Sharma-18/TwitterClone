package com.example.twitterclone.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.twitterclone.R
import com.example.twitterclone.components.TweetCard
import com.example.twitterclone.provider.authentication.AuthViewModel
import com.example.twitterclone.provider.MainViewModel
import com.example.twitterclone.Navigation.Screens
import kotlinx.coroutines.DelicateCoroutinesApi

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, DelicateCoroutinesApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    mainViewModel: MainViewModel
) {

    val scrollState = rememberScrollState()
    val data = mainViewModel.data.observeAsState()

    if (data.value == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(modifier = Modifier.size(60.dp), color = Color(0xff1DA1F2))
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
                            .background(color = Color.White)
                            .padding(vertical = 10.dp, horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(onClick = { navController.navigate(Screens.ProfileScreen.name) }) {
                            AsyncImage(
                                model = data.value!!["profilePic"],
                                contentDescription = "profile image",
                                contentScale = ContentScale.FillWidth,
                                modifier = Modifier
                                    .size(40.dp)
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
                            authViewModel.auth.signOut()
                            navController.navigate(Screens.SignIN.name)
                        }) {
                            Image(
                                imageVector = ImageVector.vectorResource(id = R.drawable.moments),
                                contentDescription = "sparkling",
                                modifier = Modifier
                                    .size(25.dp)
                            )
                        }
                    }
                    Divider(
                        thickness = 1.dp,
                        color = Color(0x2AAAB8C2),
                        modifier = Modifier.padding(horizontal = 1.dp)
                    )
                }
            }, floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate(Screens.CreateTweet.name) },
                    contentColor = Color.White,
                    containerColor = Color(0xff1DA1F2),
                    shape = RoundedCornerShape(corner = CornerSize(50))
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.tweet),
                        contentDescription = "add",
                        modifier = Modifier.size(27.dp)
                    )
                }
            },
            bottomBar = {

                BottomAppBar(
                    contentPadding = PaddingValues(0.dp),
                    containerColor = Color.White,
                    contentColor = Color(0xffAAB8C2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {

                    Column (verticalArrangement = Arrangement.Top){
                        Divider(
                            thickness = 1.dp,
                            color = Color(0x2AAAB8C2),
                            modifier = Modifier.padding(horizontal = 1.dp)
                        )
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 30.dp, vertical = 13.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.home),
                                contentDescription = "home",
                                modifier = Modifier
                                    .size(23.dp)
                                    .clickable {
                                        navController.navigate(Screens.HomeScreen.name)
                                    },
                                tint = Color.Black
                            )
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.search),
                                contentDescription = "search",
                                modifier = Modifier
                                    .size(23.dp)
                                    .clickable {
                                        navController.navigate(Screens.SearchScreen.name)
                                    },
                                tint = Color.Black
                            )
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.alert),
                                contentDescription = "notification",
                                modifier = Modifier.size(23.dp),
                                tint = Color.Black
                            )
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.messagehome),
                                contentDescription = "Mail",
                                modifier = Modifier.size(23.dp),
                                tint = Color.Black
                            )
                        }


                    }
                }
            }) {


            val tweets by mainViewModel.getTweetsHomeScreen(following)
                .collectAsState(initial = null)
            if (tweets == null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.tertiary),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(60.dp),
                        color = Color(0xff1DA1F2)
                    )
                }
            } else {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .background(color =  MaterialTheme.colorScheme.tertiary)
                        .padding(top = 60.dp, start = 3.dp, bottom = 60.dp)
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


            }

        }
    }
}