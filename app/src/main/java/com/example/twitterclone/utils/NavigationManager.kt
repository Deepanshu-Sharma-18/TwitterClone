package com.example.twitterclone.utils

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.twitterclone.provider.AuthViewModel
import com.example.twitterclone.provider.MainViewModel
import com.example.twitterclone.screens.CreateComment
import com.example.twitterclone.screens.CreateTweet
import com.example.twitterclone.screens.EditProfile
import com.example.twitterclone.screens.FollowList
import com.example.twitterclone.screens.FollowingList
import com.example.twitterclone.screens.HomeScreen

import com.example.twitterclone.screens.ProfileScreen
import com.example.twitterclone.screens.SearchProfileScreen
import com.example.twitterclone.screens.SearchScreen
import com.example.twitterclone.screens.SignInScreen
import com.example.twitterclone.screens.SignUp
import com.example.twitterclone.screens.TweetDetail

@Composable
fun NavigationManager() {
    val navController = rememberNavController()
    val mainViewModel: MainViewModel = hiltViewModel<MainViewModel>()
    val authViewModel: AuthViewModel = hiltViewModel<AuthViewModel>()
    val user = authViewModel.currentuser
    Log.d("CurrentUser", "${user}")


    NavHost(
        navController = navController,
        startDestination = if (user == null) Screens.SignIN.name else Screens.HomeScreen.name
    ) {
        composable(Screens.HomeScreen.name) {
            mainViewModel.getProfile()
            HomeScreen(
                navController = navController,
                mainViewModel = mainViewModel,
                authViewModel = authViewModel
            )
        }
        composable(Screens.CreateTweet.name) {
            CreateTweet(navController = navController, mainViewModel = mainViewModel)
        }
        composable(Screens.SignIN.name) {
            SignInScreen(navController = navController, authViewModel = authViewModel)
        }
        composable(Screens.SignUp.name) {
            SignUp(navController = navController, authViewModel = authViewModel)
        }
        composable(Screens.SearchProfileScreen.name+"/{documentId}" ,
            arguments = listOf(navArgument("documentId"){
                type = NavType.StringType
            })
            ) {
          SearchProfileScreen(navController , mainViewModel , it.arguments?.getString("documentId")!! , authViewModel = authViewModel)
        }
        composable(Screens.ProfileScreen.name) {
            mainViewModel.getProfile()
            ProfileScreen(
                navController = navController,
                mainViewModel = mainViewModel,
                authViewModel = authViewModel
            )
        }
        composable(
            Screens.TweetDetail.name + "/{documentId}",
            arguments = listOf(navArgument("documentId") {
                type = NavType.StringType
            })
        ) {
            TweetDetail(
                mainViewModel = mainViewModel,
                authViewModel = authViewModel,
                documentId = it.arguments?.getString("documentId")!!,
                navController
            )
        }

        composable(
            Screens.CreateComment.name + "/{documentId}",
            arguments = listOf(navArgument("documentId") {
                type = NavType.StringType
            })
        ) {
            CreateComment(mainViewModel = mainViewModel, navController = navController , documentId = it.arguments?.getString("documentId")!! )
        }

        composable(Screens.SearchScreen.name){
            SearchScreen(mainViewModel,navController)
        }

        composable(Screens.EditProfile.name){
            EditProfile(mainViewModel = mainViewModel, navController = navController, authViewModel = authViewModel)
        }
        composable(Screens.Followers.name){
            FollowList(mainViewModel = mainViewModel, navController = navController, )
        }
        composable(Screens.Following.name){

            FollowingList(mainViewModel = mainViewModel, navController = navController)
        }



    }
}