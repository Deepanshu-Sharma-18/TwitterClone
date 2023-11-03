package com.example.twitterclone.ui.components

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.exoplayer2.analytics.AnalyticsListener
import io.sanghun.compose.video.RepeatMode
import io.sanghun.compose.video.controller.VideoPlayerControllerConfig
import io.sanghun.compose.video.uri.VideoPlayerMediaItem

@Composable
fun VideoPlayer(uri: Uri? , link : String?) {


    if (uri != null){
        io.sanghun.compose.video.VideoPlayer(
            mediaItems = listOf(
                VideoPlayerMediaItem.StorageMediaItem(
                    storageUri = uri
                )
            ),
            handleLifecycle = false,
            autoPlay = false,
            usePlayerController = true,
            enablePip = false,
            handleAudioFocus = false,
            controllerConfig = VideoPlayerControllerConfig(
                showSpeedAndPitchOverlay = false,
                showSubtitleButton = false,
                showCurrentTimeAndTotalTime = true,
                showBufferingProgress = true,
                showForwardIncrementButton = true,
                showBackwardIncrementButton = true,
                showBackTrackButton = false,
                showNextTrackButton = false,
                showRepeatModeButton = false,
                controllerShowTimeMilliSeconds = 5_000,
                controllerAutoShow = true,
                showFullScreenButton = false
            ),
            volume = 0.5f,  // volume 0.0f to 1.0f
            repeatMode = RepeatMode.NONE,       // or RepeatMode.ALL, RepeatMode.ONE
            onCurrentTimeChanged = { // long type, current player time (millisec)
                Log.e("CurrentTime", it.toString())
            },
            playerInstance = { // ExoPlayer instance (Experimental)

            },
            modifier = Modifier
                .fillMaxWidth().height(350.dp),
        )
    }else{
        io.sanghun.compose.video.VideoPlayer(
            mediaItems = listOf(
                VideoPlayerMediaItem.NetworkMediaItem(
                    url = link!!
                )
            ),
            handleLifecycle = false,
            autoPlay = false,
            usePlayerController = true,
            enablePip = false,
            handleAudioFocus = true,
            controllerConfig = VideoPlayerControllerConfig(
                showSpeedAndPitchOverlay = false,
                showSubtitleButton = false,
                showCurrentTimeAndTotalTime = true,
                showBufferingProgress = false,
                showForwardIncrementButton = true,
                showBackwardIncrementButton = true,
                showBackTrackButton = false,
                showNextTrackButton = false,
                showRepeatModeButton = false,
                controllerShowTimeMilliSeconds = 5_000,
                controllerAutoShow = true,
                showFullScreenButton = false
            ),
            volume = 0.5f,  // volume 0.0f to 1.0f
            repeatMode = RepeatMode.NONE,       // or RepeatMode.ALL, RepeatMode.ONE
            onCurrentTimeChanged = { // long type, current player time (millisec)
            },
            playerInstance = { // ExoPlayer instance (Experimental)

            },
            modifier = Modifier
                .fillMaxSize().height(350.dp),
        )
    }
}