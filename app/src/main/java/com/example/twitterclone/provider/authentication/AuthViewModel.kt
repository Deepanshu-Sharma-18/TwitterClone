package com.example.twitterclone.provider.authentication

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.twitterclone.model.User
import com.example.twitterclone.Navigation.Screens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {

    var auth = FirebaseAuth.getInstance()
    val store = Firebase.firestore
    var currentuser = auth.currentUser


    fun signIn( username: String, password: String, navController: NavController, ctx: Context) {


        auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "signInWithEmail:success")
                    currentuser = auth.currentUser
                    Log.d("CurrentUser", currentuser.toString())
                    navController.navigate(Screens.HomeScreen.name)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        ctx,
                        task.exception?.localizedMessage.toString(),
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
    }

    fun signUp(name: String,userId : String,username: String, password: String, navController: NavController, ctx: Context){

        auth.createUserWithEmailAndPassword(username, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    auth = FirebaseAuth.getInstance()
                    currentuser = Firebase.auth.currentUser
                    val user = User(
                        name = name,
                        email = username,
                        bio = "Edit Bio",
                        profilePic = "https://images.unsplash.com/photo-1522075469751-3a6694fb2f61?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1780&q=80",
                        userId = userId,
                        noOfTweets = 0,
                        followers = 0,
                        following = 0,
                        tweets = emptyList()
                    )
                    val documentRef = store.collection("users").document(Firebase.auth.currentUser!!.uid)

                    documentRef.set(user)
                        .addOnSuccessListener {
                            navController.navigate(Screens.HomeScreen.name)
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error adding document", e)
                        }


                    Log.d("CurrentUser", currentuser.toString())


                } else {
                    // If sign in fails, display a message to the user.

                    Toast.makeText(ctx, task.exception?.localizedMessage.toString(), Toast.LENGTH_SHORT).show()
                }
            }



    }

    fun signOut(navController: NavController){
        auth.signOut()
        currentuser = auth.currentUser
        navController.navigate(Screens.SignIN.name)
    }

    fun sendPasswordReset(email : String , ctx : Context){
        auth.sendPasswordResetEmail(email)
        Toast.makeText(ctx ,"Reset password link sent to the above mail" , Toast.LENGTH_SHORT).show()
    }

    fun updatedetails(navController: NavController,name : String){
        val profileUpdates = userProfileChangeRequest {

            displayName = name
            photoUri = Uri.parse("https://images.unsplash.com/photo-1522075469751-3a6694fb2f61?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1780&q=80")
        }

        auth.currentUser!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User profile updated.")
                    navController.navigate(Screens.HomeScreen.name)
                }
            }
    }

}