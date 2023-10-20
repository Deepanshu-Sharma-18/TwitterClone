package com.example.twitterclone.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import coil.compose.AsyncImage
import com.example.twitterclone.components.TweetCard
import com.example.twitterclone.provider.authentication.AuthViewModel
import com.example.twitterclone.provider.MainViewModel
import com.example.twitterclone.ui.theme.RalewayFontFamily
import com.example.twitterclone.Navigation.Screens


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    mainViewModel: MainViewModel,
    navController: NavController
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

        Scaffold(modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .background(Color.White),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {

                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    val (image, edit, topPart) = createRefs()

                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(color = Color(0xff1DA1F2))
                        .constrainAs(topPart) {
                            top.linkTo(parent.top, 0.dp)
                            start.linkTo(parent.start, 0.dp)
                            bottom.linkTo(parent.bottom, 50.dp)

                        }) {

                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Card(
                                Modifier
                                    .size(35.dp)
                                    .clip(shape = RoundedCornerShape(corner = CornerSize(50)))
                                    .background(color = Color(0xff657786))
                                    .clickable {
                                        navController.popBackStack()
                                    }) {
                                Column (modifier = Modifier.fillMaxSize().background(color = Color(0xff657786)), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "back",
                                        modifier = Modifier.size(25.dp)
                                    )
                                }
                            }

                            Card(
                                Modifier
                                    .size(35.dp)
                                    .clip(shape = RoundedCornerShape(corner = CornerSize(50)))
                                    .background(color = Color(0xff657786))
                                    .clickable {
                                        mainViewModel.auth.signOut()
                                        val navOptions = NavOptions.Builder()
                                            .setPopUpTo(navController.graph.startDestinationRoute, inclusive = false)
                                            .build()
                                        navController.navigate(Screens.SignIN.name , navOptions)
                                    }) {
                                Column (modifier = Modifier.fillMaxSize().background(color = Color(0xff657786)), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                                    Icon(
                                        imageVector = Icons.Default.ExitToApp,
                                        contentDescription = "back",
                                        modifier = Modifier.size(25.dp)
                                    )
                                }
                            }
                        }
                    }

                    Box(modifier = Modifier.constrainAs(image) {
                        top.linkTo(parent.top, 100.dp)
                        start.linkTo(parent.start, 30.dp)
                        bottom.linkTo(parent.bottom, (10).dp)
                    }) {
                        AsyncImage(
                            model = data.value!!["profilePic"],
                            contentDescription = "profile pic",
                            modifier = Modifier
                                .size(100.dp)
                                .clip(shape = RoundedCornerShape(corner = CornerSize(50)))
                        )
                    }


                    Box(modifier = Modifier.constrainAs(edit) {
                        top.linkTo(parent.top, 160.dp)
                        end.linkTo(parent.end, (30).dp)
                        bottom.linkTo(parent.bottom, (0).dp)
                    }) {
                        OutlinedButton(
                            onClick = {

                                navController.navigate(Screens.EditProfile.name)
                            },
                            modifier = Modifier.background(color = Color.White)
                        ) {
                            Text(
                                text = "Edit profile",
                                fontWeight = FontWeight.W500,
                                color = Color(0xff14171A)
                            )
                        }
                    }

                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(15.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {


                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "${data.value!!["name"]}",
                        fontWeight = FontWeight.W700,
                        color = Color(0xff14171A),

                        fontSize = 25.sp
                    )
                    Text(
                        text = "@${data.value!!["userId"]}",
                        fontWeight = FontWeight.W400,
                        color = Color(0xff657786)
                    )
                    Spacer(modifier = Modifier.height(30.dp))

                    Text(
                        text = "${data.value!!["bio"]}",
                        fontWeight = FontWeight.W400,
                        color = Color(0xff657786),
                        fontFamily = RalewayFontFamily,
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.height(15.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Spacer(modifier = Modifier.width(0.dp))
                        Text(text = buildAnnotatedString {
                            withStyle(
                                SpanStyle(
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            ) {
                                append("${data.value!!["following"]}")
                            }
                            withStyle(
                                SpanStyle(
                                    fontWeight = FontWeight.W400,
                                    color = Color(0xff657786),
                                    fontSize = 17.sp
                                )
                            ) {
                                append(" Following")
                            }
                        }, modifier = Modifier.clickable {
                            navController.navigate(Screens.Following.name)
                        })
                        Spacer(modifier = Modifier.width(15.dp))
                        Text(text = buildAnnotatedString {
                            withStyle(
                                SpanStyle(
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            ) {
                                append("0")
                            }
                            withStyle(
                                SpanStyle(
                                    fontWeight = FontWeight.W400,
                                    color = Color(0xff657786),
                                    fontSize = 17.sp
                                )
                            ) {
                                append(" Followers")
                            }
                        },Modifier.clickable {
                            navController.navigate(Screens.Followers.name)
                        })
                    }
                    Spacer(modifier = Modifier.height(25.dp))
                    Text(
                        text = "Tweets",
                        fontWeight = FontWeight.W700,
                        color = Color(0xff14171A),
                        fontSize = 19.sp,
                        modifier = Modifier.padding(horizontal = 15.dp)
                    )
                    Spacer(modifier = Modifier.height(7.dp))

                    for (i in 1..mainViewModel.tweetCount) {
                        TweetCard(
                            "${authViewModel.currentuser!!.uid}$i",
                            navController = navController,
                            mainViewModel = mainViewModel,
                            authViewModel = authViewModel
                        )
                    }
                }
            }
        }
    }
}