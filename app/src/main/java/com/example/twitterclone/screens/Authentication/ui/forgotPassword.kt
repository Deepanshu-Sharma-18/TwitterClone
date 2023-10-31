package com.example.twitterclone.screens.Authentication.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.twitterclone.provider.viewModels.authentication.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ForgotPasswordScreen(navController: NavController, authViewModel: AuthViewModel) {

    val scrollState = rememberScrollState()
    val interactionSource = remember{
        MutableInteractionSource()
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val username = remember{
        mutableStateOf("")
    }
    val visible = remember{
        mutableStateOf(false)
    }
    val context = LocalContext.current
    Scaffold {
           paddingValues ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(20.dp)
                .verticalScroll(scrollState)
                .clickable(interactionSource = interactionSource, indication = null) {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }, verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally
        ){

            Spacer(modifier = Modifier.height(120.dp))
            
            Text(
                text="Forgot Password",
                fontSize = 35.sp,
                fontWeight = FontWeight.W400,
                color = MaterialTheme.colorScheme.onBackground,
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
            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    authViewModel.sendPasswordReset(email = username.value , context)
                    navController.popBackStack()
                          },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Reset Password" ,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W400,
                        color = Color.White
                    )
                )
            }
        }
    }
}