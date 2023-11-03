package com.example.twitterclone.Navigation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.twitterclone.provider.viewModels.authentication.AuthViewModel
import com.example.twitterclone.provider.viewModels.appViewModel.MainViewModel
import com.example.twitterclone.ui.screens.comment.CreateComment
import com.example.twitterclone.ui.screens.createTweet.CreateTweet
import com.example.twitterclone.ui.screens.profile.SubScreens.EditProfile
import com.example.twitterclone.ui.screens.profile.SubScreens.FollowList
import com.example.twitterclone.ui.screens.profile.SubScreens.FollowingList
import com.example.twitterclone.ui.screens.Authentication.ui.ForgotPasswordScreen
import com.example.twitterclone.ui.screens.HomeScreen

import com.example.twitterclone.ui.screens.profile.ProfileScreen
import com.example.twitterclone.ui.screens.search.SearchProfileScreen
import com.example.twitterclone.ui.screens.search.SearchScreen
import com.example.twitterclone.ui.screens.Authentication.ui.SignInScreen
import com.example.twitterclone.ui.screens.Authentication.ui.SignUp
import com.example.twitterclone.ui.screens.trending.TrendingScreen
import com.example.twitterclone.ui.screens.tweetDetail.TweetDetail
import com.example.twitterclone.ui.screens.cacheScreen.CacheScreen
import com.example.twitterclone.ui.screens.cacheScreen.CachedUserProfile
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
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

            if(mainViewModel.isInternetAvailable(context = LocalContext.current)){
                GlobalScope.launch {

                    mainViewModel.getProfile()
                }
                HomeScreen(
                    navController = navController,
                    mainViewModel = mainViewModel,
                    authViewModel = authViewModel
                )
            }else{
                CacheScreen(navController = navController , mainViewModel = mainViewModel )
            }
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
        composable(
            Screens.SearchProfileScreen.name+"/{documentId}" ,
            arguments = listOf(navArgument("documentId"){
                type = NavType.StringType
            })
            ) {
          SearchProfileScreen(navController , mainViewModel , it.arguments?.getString("documentId")!! , authViewModel = authViewModel)
        }
        composable(Screens.ProfileScreen.name) {

            if(mainViewModel.isInternetAvailable(context = LocalContext.current)){

                GlobalScope.launch {

                    mainViewModel.getProfile()
                }

                ProfileScreen(
                    navController = navController,
                    mainViewModel = mainViewModel,
                    authViewModel = authViewModel
                )
            }else{
                CachedUserProfile(mainViewModel = mainViewModel, navController = navController)
            }
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

        composable(Screens.TrendingScreen.name){
            TrendingScreen(mainViewModel , navController , authViewModel)
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

        composable(Screens.ForgotPassword.name){
            ForgotPasswordScreen(navController , authViewModel)
        }




    }
}