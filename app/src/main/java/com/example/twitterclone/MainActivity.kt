package com.example.twitterclone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.twitterclone.ui.theme.TwittercloneTheme
import com.example.twitterclone.Navigation.NavigationManager
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TwittercloneTheme {
                NavigationManager()
            }
        }
    }
}

