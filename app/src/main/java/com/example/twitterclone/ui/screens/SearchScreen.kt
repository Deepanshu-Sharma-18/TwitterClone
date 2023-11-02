package com.example.twitterclone.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.twitterclone.R
import com.example.twitterclone.ui.components.UserCard
import com.example.twitterclone.provider.viewModels.appViewModel.MainViewModel
import com.example.twitterclone.Navigation.Screens
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition", "UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(mainViewModel: MainViewModel, navController: NavHostController) {


    val visible = remember {
        mutableStateOf(false)
    }

    val search = remember {
        mutableStateOf("")
    }
    Scaffold(modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomAppBar(
                contentPadding = PaddingValues(0.dp),
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = Color(0xffAAB8C2),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {

                Column(verticalArrangement = Arrangement.Top) {
                    Divider(
                        thickness = 0.4.dp,
                        color =  MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f),
                        modifier = Modifier.padding(horizontal = 1.dp)
                    )
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp, vertical = 13.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.home),
                            contentDescription = "home",
                            modifier = Modifier
                                .size(23.dp)
                                .clickable {
                                    navController.navigate(Screens.HomeScreen.name)
                                },
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.search),
                            contentDescription = "search",
                            modifier = Modifier
                                .size(23.dp)
                                .clickable {
                                    navController.navigate(Screens.SearchScreen.name)
                                },
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.alert),
                            contentDescription = "notification",
                            modifier = Modifier.size(23.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.messagehome),
                            contentDescription = "Mail",
                            modifier = Modifier.size(23.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }


                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)

        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 30.dp, horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                AsyncImage(
                    model = mainViewModel.data.value!!["profilePic"],
                    contentDescription = "profile",
                    modifier = Modifier
                        .size(45.dp)
                        .clip(
                            shape = RoundedCornerShape(
                                corner = CornerSize(
                                    50
                                )
                            )
                        ),
                    contentScale = ContentScale.FillWidth
                )
                Spacer(modifier = Modifier.width(10.dp))
                OutlinedTextField(
                    value = search.value,
                    onValueChange = {
                        search.value = it
                    },
                    placeholder = {
                        Text(
                            text = "UserId",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.W400,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    },
                    textStyle = TextStyle(
                        fontWeight = FontWeight.W400,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.secondary
                    ),
                    modifier = Modifier
                        .width(280.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(corner = CornerSize(15.dp)),
                    maxLines = 1,
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
                Spacer(modifier = Modifier.width(7.dp))
                IconButton(onClick = {
                    visible.value = true
                }) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "search",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(50.dp))
            if (visible.value) {
                var exist = remember {
                    mutableStateOf<String?>(null)
                }
                GlobalScope.launch {
                    exist.value = mainViewModel.searchUserExist(search.value)
                }
                if (exist.value != null) {

                    val result by mainViewModel.getUser(exist.value!!)
                        .collectAsState(initial = null)

                    if (result == null) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .background(color = MaterialTheme.colorScheme.background),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(60.dp),
                                color =  MaterialTheme.colorScheme.primary
                            )
                        }
                    } else {

                        if (result == mutableMapOf<String, Any>("error" to "No User Found"))
                        else {
                            Card(modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp)
                                .clickable {
                                    navController.navigate(Screens.SearchProfileScreen.name + "/${exist.value}")
                                }) {
                                UserCard(result!!)
                            }
                        }
                    }
                }
            }else{
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {

                    Text(text = "Search Twitter" , style = TextStyle(
                        fontSize = 25.sp,
                        fontWeight = FontWeight.W500
                    ))
                }

            }
        }
    }
}