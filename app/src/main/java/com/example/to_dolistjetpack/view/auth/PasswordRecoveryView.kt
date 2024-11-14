package com.example.to_dolistjetpack.view.auth

import android.content.Context
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.to_dolistjetpack.R
import com.example.to_dolistjetpack.components.TaskButton
import com.example.to_dolistjetpack.components.TaskTextField
import com.example.to_dolistjetpack.ui.theme.Tertiary
import com.example.to_dolistjetpack.util.validateEmail
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordRecoveryView(
    navController: NavController,
    context: Context
) {
    val auth = Firebase.auth
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    var emailError by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

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
                    value = email,
                    onValueChange = { emailString ->
                        email = emailString
                        emailError = ""
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = "Email",
                    maxLines = 1,
                    keyboardType = KeyboardType.Email,
                    singleLine = true,
                    isValid = { validateEmail(email) },
                    errorMessage = "Enter a valid email"
                )
                Spacer(modifier = Modifier.size(20.dp))

                TaskButton(
                    onClick = {
                        sendPasswordRecoveryEmail(context, email)
                        navController.popBackStack()
                    },
                    text = "Send", modifier = Modifier.fillMaxWidth(),
                    enabled = validateEmail(email),
                )

            }
        }
    }
}

fun sendPasswordRecoveryEmail(context: Context, email: String) {
    val auth = Firebase.auth

    auth.sendPasswordResetEmail(email)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Password recovery email sent", Toast.LENGTH_SHORT).show()
            } else {
                val errorMessage = task.exception?.message
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
}