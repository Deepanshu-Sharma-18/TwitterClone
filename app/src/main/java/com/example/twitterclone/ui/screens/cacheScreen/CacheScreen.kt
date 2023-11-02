package com.example.twitterclone.ui.screens.cacheScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.twitterclone.Navigation.Screens
import com.example.twitterclone.R
import com.example.twitterclone.provider.viewModels.appViewModel.MainViewModel
import com.example.twitterclone.ui.screens.cacheScreen.components.CacheTweetCard
import java.time.format.TextStyle

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CacheScreen(navController: NavController , mainViewModel: MainViewModel) {
    val data by mainViewModel.getCachedTweets().collectAsState(initial = null)
    val profile by mainViewModel.getCachedUser().collectAsState(initial = null)

    val scrollState = rememberScrollState()

    if(data == null || profile == null){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(modifier = Modifier.size(30.dp), color = MaterialTheme.colorScheme.primary)
        }
    }else{
        Scaffold (
            modifier = Modifier.fillMaxSize(),
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
                            IconButton(onClick = {
                                navController.navigate(Screens.ProfileScreen.name)
                            }) {
                                AsyncImage(
                                    model = profile!!.profilePic.toString(),
                                    contentDescription = "profile image",
                                    contentScale = ContentScale.FillWidth,
                                    modifier = Modifier
                                        .size(35.dp)
                                        .clip(shape = RoundedCornerShape(corner = CornerSize(50)))
                                )
                            }

//                            Text(
//                                text = "Twitter",
//                                fontWeight = FontWeight.W600,
//                                color = MaterialTheme.colorScheme.onBackground,
//                                fontSize = 16.sp
//                            )

                            AsyncImage(
                                model = "https://cdn-icons-png.flaticon.com/512/733/733579.png?w=740&t=st=1688284770~exp=1688285370~hmac=4749b7d8b4f365320a235d7098ffbdd6ca7f5082d5f90d5d19940594a0bdc3f1",
                                contentDescription = "image-twitter",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .size(30.dp)

                            )


                            IconButton(onClick = {

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

                                    },
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.search),
                                contentDescription = "search",
                                modifier = Modifier
                                    .size(20.dp)
                                    .clickable {

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
            },
            floatingActionButton = {
                                   Box(
                                       modifier = Modifier.width(180.dp).height(30.dp)
                                           .clip(shape = RoundedCornerShape(corner = CornerSize(20.dp)))
                                           .background(MaterialTheme.colorScheme.primary)
                                   ) {
                                       Text(text = "No Internet", style = androidx.compose.ui.text.TextStyle(
                                            fontSize = 14.sp,
                                           fontWeight = FontWeight.W600
                                       ),
                                           modifier = Modifier.align(Alignment.Center)
                                       )
                                   }
            },
            floatingActionButtonPosition = FabPosition.Center
        ){

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(top = 58.dp, start = 3.dp, bottom = 50.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ){
                
                for(tweet in data!!){
                    CacheTweetCard(tweet = tweet)
                }



            }

        }
    }
}