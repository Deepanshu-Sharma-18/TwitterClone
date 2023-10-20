package com.example.twitterclone.screens.Authentication.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.twitterclone.provider.authentication.AuthViewModel
import com.example.twitterclone.Navigation.Screens

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SignUp(
    authViewModel: AuthViewModel,
    navController: NavController
) {

    val username = remember {
        mutableStateOf("")
    }
    val password = remember {
        mutableStateOf("")
    }
    val userId = remember {
        mutableStateOf("")
    }
    val name = remember {
        mutableStateOf("")
    }
    val scrollState = rememberScrollState()

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val interactionSource = remember {
        MutableInteractionSource()
    }


    Scaffold(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(20.dp)
            .clickable (interactionSource = interactionSource , indication = null) {
                keyboardController?.hide()
                focusManager.clearFocus()
            },
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            AsyncImage(
                model = "https://cdn-icons-png.flaticon.com/512/733/733579.png?w=740&t=st=1688284770~exp=1688285370~hmac=4749b7d8b4f365320a235d7098ffbdd6ca7f5082d5f90d5d19940594a0bdc3f1",
                contentDescription = "image-twitter",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(60.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Create New Account",
                fontSize = 35.sp,
                fontWeight = FontWeight.W400,
                color = MaterialTheme.colorScheme.secondary,

                )
            Spacer(modifier = Modifier.height(80.dp))

            OutlinedTextField(
                value = userId.value,
                onValueChange = { userId.value = it },
                label = { Text(text = "UserId") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.Transparent,
                    unfocusedBorderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                    focusedBorderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                    cursorColor = MaterialTheme.colorScheme.secondary,
                    textColor = MaterialTheme.colorScheme.secondary,
                    focusedLabelColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                    unfocusedLabelColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = name.value,
                onValueChange = { name.value = it },
                label = { Text(text = "Name") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.Transparent,
                    unfocusedBorderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                    focusedBorderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                    cursorColor = MaterialTheme.colorScheme.secondary,
                    textColor = MaterialTheme.colorScheme.secondary,
                    focusedLabelColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                    unfocusedLabelColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                )
            )

            Spacer(modifier = Modifier.height(40.dp))


            OutlinedTextField(
                value = username.value,
                onValueChange = { username.value = it },
                label = { Text(text = "Email") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.Transparent,
                    unfocusedBorderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                    focusedBorderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                    cursorColor = MaterialTheme.colorScheme.secondary,
                    textColor = MaterialTheme.colorScheme.secondary,
                    focusedLabelColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                    unfocusedLabelColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text(text = "Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.Transparent,
                    unfocusedBorderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                    focusedBorderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                    cursorColor = MaterialTheme.colorScheme.secondary,
                    textColor = MaterialTheme.colorScheme.secondary,
                    focusedLabelColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                    unfocusedLabelColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                )
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    authViewModel.signUp(
                        name.value,
                        userId.value,
                        username.value,
                        password.value,
                        navController,
                        context
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "Create Account",
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W400,
                        color = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = { navController.navigate(Screens.SignIN.name) }
            ) {
                Text(
                    text = "Already have an account? Sign In",
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.W500,
                        color = MaterialTheme.colorScheme.primary
                    )
                )

            }

        }
    }


}