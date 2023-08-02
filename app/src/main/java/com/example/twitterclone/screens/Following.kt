package com.example.twitterclone.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.twitterclone.components.UserCard
import com.example.twitterclone.provider.MainViewModel

@Composable
fun FollowingList(mainViewModel: MainViewModel , navController: NavController) {
    val follow by mainViewModel.getFollowing().collectAsState(initial = null)
    val scrollState = rememberScrollState()
    if (follow == null ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(modifier = Modifier.size(60.dp), color = Color(0xff1DA1F2))
        }
    }else{
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(vertical = 0.dp, horizontal = 10.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .background(Color.White)
            ) {
                Spacer(modifier = Modifier.height(40.dp))
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "close",
                        modifier = Modifier.size(25.dp),
                        tint = Color.Black
                    )

                }
                Spacer(modifier = Modifier.height(40.dp))
                LazyColumn(content = {
                    items(follow!!.documents.size) {
                        Spacer(modifier = Modifier.height(5.dp))
                        UserCard(result = follow!!.documents[it].data!!)
                    }
                }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(40.dp))
            }


        }
    }
}