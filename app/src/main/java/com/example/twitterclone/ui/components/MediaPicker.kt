package com.example.twitterclone.ui.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.withContext


@Composable
fun RequestContentPermission() {
    var mediaUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        mediaUri = uri
    }

    Column {
        Button(onClick = {
            launcher.launch("*/*") // Specify the MIME type to allow both images and videos
        }) {
            Icon(
                imageVector = Icons.Rounded.Menu,
                contentDescription = "menu",
                tint = Color(0xff1DA1F2),
                modifier = Modifier.size(30.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        mediaUri?.let { uri ->
            when {
                uri.isImageMimeType(context) -> {
                    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

                    LaunchedEffect(uri) {
                        withContext(Dispatchers.IO) {
                            val inputStream = context.contentResolver.openInputStream(uri)
                            bitmap.value = BitmapFactory.decodeStream(inputStream)
                        }
                    }

                    bitmap.value?.let { btm ->
                        Image(
                            bitmap = btm.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier.size(400.dp)
                        )
                    }
                }
                uri.isVideoMimeType(context) -> {
                    VideoPlayer(uri = uri, link = null)
                }
                else -> {
                    // Unsupported media type
                }
            }
        }
    }
}

// Extension functions to check MIME types
fun Uri.isImageMimeType(context : Context): Boolean {
    val mimeType = context.contentResolver.getType(this)
    return mimeType?.startsWith("image/") == true
}

fun Uri.isVideoMimeType(context : Context): Boolean {
    val mimeType = context.contentResolver.getType(this)
    return mimeType?.startsWith("video/") == true
}