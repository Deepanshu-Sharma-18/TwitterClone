package com.example.twitterclone.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.twitterclone.provider.authentication.AuthViewModel
import com.example.twitterclone.provider.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EditProfile(
    navController: NavController,
    mainViewModel: MainViewModel,
    authViewModel: AuthViewModel
) {
    val data = mainViewModel.data.observeAsState()

    if (data.value == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(modifier = Modifier.size(60.dp), color = MaterialTheme.colorScheme.primary)
        }
    } else {

        var mediaUri by remember { mutableStateOf<Uri?>(null) }
        val context = LocalContext.current

        val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            mediaUri = uri
        }

        val name = remember {
            mutableStateOf("${data.value!!["name"]}")
        }
        val username = remember {
            mutableStateOf("${data.value!!["userId"]}")
        }
        val bio = remember {
            mutableStateOf("${data.value!!["bio"]}")
        }
        val scrollState = rememberScrollState()
        Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Back",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.W400,
                        modifier = Modifier.padding(start = 15.dp)
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = "iconback",
                        tint =MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .size(25.dp)
                            .clickable {
                                navController.popBackStack()
                            })
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),

            )
        }) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .background(
                        MaterialTheme.colorScheme.background
                    )
                    .padding(horizontal = 15.dp, vertical = 50.dp)
            ) {
                Spacer(modifier = Modifier.height(50.dp))
                Text(
                    text = "Edit Profile",
                    fontWeight = FontWeight.W700,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 25.sp,
                    modifier = Modifier.padding(horizontal = 15.dp)
                )

                Spacer(modifier = Modifier.height(30.dp))
                OutlinedTextField(
                    value = name.value,
                    textStyle = TextStyle(
                        fontWeight = FontWeight.W400,
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 20.sp,
                    ),
                    onValueChange = { name.value = it },
                    label = { Text(text = "Name" ,
                        fontWeight = FontWeight.W400,
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 13.sp,
                    ) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = "Profile pic",
                    fontWeight = FontWeight.W600,
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 25.sp,
                    modifier = Modifier.padding(horizontal = 15.dp)
                )
                Row(horizontalArrangement = Arrangement.Center , modifier = Modifier.fillMaxWidth()) {

                        if (mediaUri == null){

                            AsyncImage(
                                model = data.value!!["profilePic"],
                                contentDescription = "profile pic",
                                contentScale = ContentScale.FillWidth,
                                modifier = Modifier
                                    .size(150.dp)
                                    .clip(shape = RoundedCornerShape(corner = CornerSize(50)))
                                    .clickable {
                                        launcher.launch("*/*")
                                    }
                            )
                        }
                        else{
                            mediaUri?.let { uri ->
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
                                    contentScale = ContentScale.FillWidth,
                                    modifier = Modifier
                                        .size(150.dp)
                                        .clip(shape = RoundedCornerShape(corner = CornerSize(50)))
                                        .clickable {
                                            launcher.launch("*/*")
                                        }
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Bio",
                    fontWeight = FontWeight.W600,
                    color =MaterialTheme.colorScheme.secondary,
                    fontSize = 25.sp,
                    modifier = Modifier.padding(horizontal = 15.dp)
                )
                OutlinedTextField(
                    value = bio.value,
                    onValueChange = {
                        bio.value = it
                    },
                    placeholder = {
                        Text(
                            text = "Bio",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.W400,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    },
                    textStyle = TextStyle(
                        fontWeight = FontWeight.W400,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.secondary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(top = 10.dp, start = 25.dp),
                    maxLines = 5,
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = MaterialTheme.colorScheme.background
                        , focusedIndicatorColor = MaterialTheme.colorScheme.secondary,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.secondary,
                        disabledIndicatorColor = MaterialTheme.colorScheme.secondary,
                        cursorColor = Color(0xff1DA1F2)
                    ),
                )
                Spacer(modifier = Modifier.height(30.dp))
                Row (horizontalArrangement = Arrangement.Center , modifier = Modifier.fillMaxWidth()){
                    Button(
                        onClick = {
                                  mainViewModel.updateProfile(name = name.value ,  url = mediaUri, bio =  bio.value)
                            mainViewModel.getProfile()
                            navController.popBackStack()
                        }, modifier = Modifier
                            .width(230.dp)
                            .clip(
                                RoundedCornerShape(corner = CornerSize(0.dp))
                            ), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            text = "Save",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.W400,
                            color = Color(0xFFFFFFFF)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }


}