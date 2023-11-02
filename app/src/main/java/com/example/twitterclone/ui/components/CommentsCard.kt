package com.example.twitterclone.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.twitterclone.provider.viewModels.appViewModel.MainViewModel

@Composable
fun CommentsCard(result : MutableMap<String , Any> , mainViewModel: MainViewModel) {

    val user by mainViewModel.getUser(result["userId"].toString()).collectAsState(initial = null)
    if ( user == null){
        Column(modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(modifier= Modifier.size(60.dp), color = MaterialTheme.colorScheme.primary)
        }
    }
    else{

        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(vertical = 0.dp), horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                Divider(
                    thickness = 0.4.dp,
                    color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5F),
                    modifier = Modifier.padding(horizontal = 1.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.background)
                        .padding(vertical = 20.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Start
                ) {
                    AsyncImage(
                        model = user!!["profilePic"],
                        contentDescription = "profile pic",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(shape = RoundedCornerShape(corner = CornerSize(50)))
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.fillMaxWidth().background(color = MaterialTheme.colorScheme.background)
                    ) {
                        Text(
                            text = "${user!!["name"]}",
                            fontWeight = FontWeight.W700,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "@${user!!["userId"]}",
                            fontWeight = FontWeight.W400,
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "${result["content"]}",
                            fontWeight = FontWeight.W400,
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(25.dp))
                        if (result["url"] != null){}
                        else{
                            if (result["url"].toString().contains("images")){
                                AsyncImage(

                                    model = result["url"],
                                    contentDescription = "post-image",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(end = 15.dp)
                                        .clip(shape = RoundedCornerShape(corner = CornerSize(20.dp))),
                                    contentScale = ContentScale.FillWidth
                                )
                            }
                            else{
                                VideoPlayer(uri = null, link = result["url"].toString())

                            }
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                }
            }
        }
    }
}