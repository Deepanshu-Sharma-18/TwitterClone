package com.example.twitterclone.screens.createTweet

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.twitterclone.R
import com.example.twitterclone.components.CircularPageIndicator
import com.example.twitterclone.components.isImageMimeType
import com.example.twitterclone.components.isVideoMimeType
import com.example.twitterclone.provider.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun CreateTweet(
    mainViewModel: MainViewModel,
    navController: NavController
) {
    val content = remember {
        mutableStateOf("")
    }
    val isImage = remember {
        mutableStateOf(true)
    }
    val scrollState = rememberScrollState()
    val characterLimit = 250

    var mediaUri = remember {
        mutableStateListOf<Pair<Uri, Boolean>>()
    }
    val context = LocalContext.current

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetMultipleContents()) { uri ->
            uri.forEach {
                mediaUri.add(Pair(it, it.isImageMimeType(context)))
            }
            Log.d("MEDIAURLSELECT", mediaUri.toList().toString())
        }
    val data = mainViewModel.data.observeAsState()



    if (data.value == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(60.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
    } else {

        Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .background(color = MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(0.dp)
            ) {
                Column(
                    Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    Divider(
                        thickness = 0.4.dp,
                        color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f),
                        modifier = Modifier.padding(horizontal = 1.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = MaterialTheme.colorScheme.background)
                            .padding(horizontal = 25.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        IconButton(onClick = { launcher.launch("*/*") }) {

                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.pick),
                                contentDescription = "menu",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(25.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(250.dp))
                        Text(
                            text = "${characterLimit - content.value.length}",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.W400,
                            color = MaterialTheme.colorScheme.secondary,

                            )
                    }
                }
            }
        }) {
            Surface(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .background(MaterialTheme.colorScheme.background)
                        .padding(top = 20.dp, bottom = 55.dp, start = 20.dp, end = 20.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = "close",
                                modifier = Modifier.size(25.dp),
                                tint = MaterialTheme.colorScheme.onBackground
                            )

                        }
                        Button(
                            onClick = {
                                GlobalScope.launch {

                                mainViewModel.postTweet(content = content.value, mediaUri)
                                }
                                navController.popBackStack()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            modifier = Modifier
                                .width(80.dp)
                                .height(35.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = "Tweet",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.W500,
                                color = MaterialTheme.colorScheme.background
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        AsyncImage(
                            model = mainViewModel.data.value!!["profilePic"],
                            contentDescription = "profile image",
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(shape = RoundedCornerShape(corner = CornerSize(50)))
                        )
                    }
                    TextField(
                        value = content.value,
                        onValueChange = {
                            if (it.length < characterLimit) {
                                content.value = it
                            }
                        },
                        placeholder = {
                            Text(
                                text = "What's happening?",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.W400,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        },
                        textStyle = TextStyle(
                            fontWeight = FontWeight.W400,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .padding(top = 10.dp, start = 25.dp),
                        maxLines = 25,
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            cursorColor = MaterialTheme.colorScheme.primary
                        ),

                        )
                    Spacer(modifier = Modifier.height(20.dp))

                    val mediaPager = rememberPagerState(initialPage = 0) {
                        mediaUri.size
                    }

                    val currentPage by rememberUpdatedState(mediaPager.currentPage)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 30.dp, end = 5.dp, bottom = 0.dp)
                    ) {

                        HorizontalPager(
                            state = mediaPager, pageSpacing = 10.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(shape = RoundedCornerShape(corner = CornerSize(10.dp)))
                        ) { page ->

                            if (mediaUri[page].second) {
                                val bitmap = remember { mutableStateOf<Bitmap?>(null) }

                                LaunchedEffect(Unit) {
                                    withContext(Dispatchers.IO) {
                                        val inputStream =
                                            context.contentResolver.openInputStream(mediaUri[page].first)
                                        bitmap.value =
                                            BitmapFactory.decodeStream(inputStream)
                                    }
                                }
                                isImage.value = true
                                bitmap.value?.let { btm ->
                                    Image(
                                        bitmap = btm.asImageBitmap(),
                                        contentDescription = null,
                                        contentScale = ContentScale.FillWidth,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            } else {
                                com.example.twitterclone.components.VideoPlayer(
                                    uri = mediaUri[page].first,
                                    link = null
                                )
                            }

                        }


                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    if (mediaUri.size > 0) {

                        CircularPageIndicator(
                            numberOfPages = mediaUri.size, currentPage = currentPage,
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterHorizontally)
                        )

                    }

                    Spacer(modifier = Modifier.height(20.dp))


                }
            }
        }
    }
}
