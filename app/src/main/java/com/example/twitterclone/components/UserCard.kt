package com.example.twitterclone.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun UserCard(result: MutableMap<String, Any>) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xff1DA1F2))
            .height(100.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .background(Color(0xff1DA1F2))
                .padding(10.dp)
        ) {
            AsyncImage(
                model = result["profilePic"],
                contentDescription = "profile pic",
                contentScale = ContentScale.FillWidth,

                modifier = Modifier
                    .size(80.dp)
                    .clip(shape = RoundedCornerShape(corner = CornerSize(50)))
            )
            Spacer(modifier = Modifier.width(15.dp))
            Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start) {
                Text(
                    text = "${result!!["name"]}",
                    fontWeight = FontWeight.W700,
                    color = Color(0xff14171A),
                    fontSize = 25.sp
                )
                Text(
                    text = "@${result!!["userId"]}",
                    fontWeight = FontWeight.W400,
                    color = Color(0xff657786)
                )
            }
        }
    }
}