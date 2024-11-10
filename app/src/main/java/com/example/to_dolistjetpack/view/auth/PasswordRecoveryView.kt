package com.example.to_dolistjetpack.view.auth

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.to_dolistjetpack.R
import com.example.to_dolistjetpack.components.TaskButton
import com.example.to_dolistjetpack.components.TaskTextField
import com.example.to_dolistjetpack.ui.theme.Tertiary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordRecoveryView(
    navController: NavController
) {
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Tertiary
                ),
                title = {
                    Text(
                        text = "Password Recovery",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.LightGray
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Image(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            colorFilter = ColorFilter.tint(Color.LightGray)
                        )
                    }
                },
            )
        },
    ) {
        val topPadding =
            if (isPortrait) it.calculateTopPadding() - 50.dp else it.calculateTopPadding()
        val imageTopPadding = if (isPortrait) 0.dp else 50.dp
        val imageSize = if (isPortrait) 120.dp else 180.dp
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = topPadding,
                    start = 20.dp,
                    end = 20.dp
                )
                .verticalScroll(rememberScrollState()),

            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Image(
                    painter = painterResource(id = R.drawable.list),
                    contentDescription = "List Icon",
                    modifier = Modifier
                        .size(imageSize)
                        .padding(top = imageTopPadding, bottom = 40.dp),
                    contentScale = ContentScale.Fit
                )

                Text(
                    text = "You will receive an email with a link to reset your password",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                )
                Spacer(modifier = Modifier.size(20.dp))
                TaskTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    label = "Email",
                    maxLines = 1,
                    keyboardType = KeyboardType.Email,
                    singleLine = true
                )
                Spacer(modifier = Modifier.size(20.dp))

                TaskButton(
                    onClick = {
                        navController.navigate("home")

                    },
                    text = "Send", modifier = Modifier.fillMaxWidth()
                )

            }
        }
    }
}

@Preview
@Composable
fun ForgotPasswordViewPreview() {
    PasswordRecoveryView(navController = NavController(LocalContext.current))
}