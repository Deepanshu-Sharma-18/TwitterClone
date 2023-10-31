package com.example.twitterclone.screens.cacheScreen.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.twitterclone.Navigation.Screens
import com.example.twitterclone.R
import com.example.twitterclone.caching.CacheModel
import com.example.twitterclone.components.CircularPageIndicator
import com.example.twitterclone.components.RetweetCard
import com.example.twitterclone.components.VideoPlayer
import com.google.firebase.Timestamp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Composable
fun CacheTweetCard( tweet : CacheModel) {

    Surface(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            Modifier
                .fillMaxWidth()

                .clickable {

                }
                .background(color = MaterialTheme.colorScheme.background)
                .padding(vertical = 0.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top) {
            Divider(
                thickness = 0.4.dp,
                color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f),
                modifier = Modifier.padding(horizontal = 1.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            if (tweet.retweeted == true) {
                Row(
                    modifier = Modifier.padding(horizontal = 50.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.retweet),
                        contentDescription = "retweet"
                    )
                    Spacer(modifier = Modifier.width(7.dp))
                    Text(
                        text = "${tweet.name} Retweeted",
                        fontWeight = FontWeight.W400,
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 15.sp
                    )
                }





            }


                val date = tweet.timestamp
                val currentDate = LocalDateTime.now()
                val targetDate = LocalDateTime.of(
                    2023,
                    date.month + 1,
                    date.date,
                    date.hours,
                    date.minutes
                )
                Log.d("DateTarget", targetDate.toString())
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.background)
                        .padding(vertical = 10.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "profile pic",
                        contentScale = ContentScale.Inside,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(shape = RoundedCornerShape(corner = CornerSize(50)))
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = "${tweet.name}",
                                fontWeight = FontWeight.W600,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 16.sp
                            )

                            if (true) {
                                Spacer(modifier = Modifier.width(5.dp))
                                Surface(
                                    shape = RoundedCornerShape(
                                        corner = CornerSize(
                                            50
                                        )
                                    ),
                                    modifier = Modifier.size(18.dp),
                                    color = MaterialTheme.colorScheme.primary
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Check,
                                        contentDescription = "check",
                                        Modifier.size(10.dp),
                                        tint = Color.White
                                    )
                                }

                            }

                            Spacer(modifier = Modifier.width(10.dp))

                        }

                        Spacer(modifier = Modifier.height(5.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Text(
                                    text = "@",
                                    fontWeight = FontWeight.W400,
                                    color = MaterialTheme.colorScheme.secondary,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = "${tweet.userId}",
                                    fontWeight = FontWeight.W400,
                                    color = MaterialTheme.colorScheme.secondary,
                                    fontSize = 15.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.DateRange,
                                    contentDescription = "",
                                    Modifier.size(14.dp),
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = "${
                                        ChronoUnit.DAYS.between(
                                            targetDate,
                                            currentDate
                                        )
                                    }d",
                                    fontWeight = FontWeight.W400,
                                    color = MaterialTheme.colorScheme.secondary,
                                    fontSize = 15.sp
                                )

                            }
                        }


                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = tweet.content,
                            fontWeight = FontWeight.W400,
                            color = MaterialTheme.colorScheme.onTertiary,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(25.dp))

                        if(tweet.url > 0){
                            Box(modifier = Modifier
                                .fillMaxWidth()
                                .height(350.dp)){
                                Text(text = "Could Not Load Image" , style = TextStyle(
                                    fontSize = 15.sp
                                ), modifier = Modifier.align(Alignment.Center))
                            }
                        }

                        Spacer(modifier = Modifier.height(25.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = MaterialTheme.colorScheme.background),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Box(modifier = Modifier.width(60.dp)) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(color = MaterialTheme.colorScheme.background),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start
                                ) {

                                    Icon(imageVector = ImageVector.vectorResource(id = R.drawable.reply),
                                        contentDescription = "list",
                                        tint = MaterialTheme.colorScheme.onBackground,
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clickable {

                                            })
                                    Spacer(modifier = Modifier.width(7.dp))
                                    Text(
                                        text = tweet.commentNo.toString(),
                                        fontWeight = FontWeight.W400,
                                        color = MaterialTheme.colorScheme.onBackground,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                            Box(modifier = Modifier.width(60.dp)) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(color = MaterialTheme.colorScheme.background),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start
                                ) {

                                    Icon(imageVector = ImageVector.vectorResource(
                                        id = R.drawable.likesolid
                                    ),
                                        contentDescription = "likes",
                                        tint =  MaterialTheme.colorScheme.onBackground,
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clickable {

                                            })
                                    Spacer(modifier = Modifier.width(7.dp))
                                    Text(
                                        text = tweet.likesCount.toString(),
                                        fontWeight = FontWeight.W400,
                                        color = MaterialTheme.colorScheme.onBackground,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                            Box(modifier = Modifier.width(60.dp)) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(color = MaterialTheme.colorScheme.background),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start
                                ) {

                                    Icon(imageVector = ImageVector.vectorResource(id = R.drawable.retweet),
                                        contentDescription = "retweet",
                                        tint = MaterialTheme.colorScheme.onBackground,
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clickable {


                                            })
                                    Spacer(modifier = Modifier.width(7.dp))
                                    Text(
                                        text = tweet.retweets.toString(),
                                        fontWeight = FontWeight.W400,
                                        color = MaterialTheme.colorScheme.onBackground,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                            Box(modifier = Modifier.width(60.dp)) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(color = MaterialTheme.colorScheme.background),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start
                                ) {

                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = "list",
                                        tint = MaterialTheme.colorScheme.onBackground,
                                        modifier = Modifier.size(20.dp)
                                    )

                                }
                            }

                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

    }
}