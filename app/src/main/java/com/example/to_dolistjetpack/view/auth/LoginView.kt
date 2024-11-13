package com.example.to_dolistjetpack.view.auth

import android.content.Context
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.to_dolistjetpack.ui.theme.LightBlue
import com.example.to_dolistjetpack.ui.theme.Tertiary
import com.example.to_dolistjetpack.util.validateEmail
import com.example.to_dolistjetpack.util.validatePassword
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginView(
    navController: NavController?,
    context: Context
) {
    val auth = Firebase.auth
    var isRedirected by remember { mutableStateOf(false) }
    auth.currentUser?.let {
        if (!isRedirected) {
            LaunchedEffect(Unit) {
                isRedirected = true
                navController?.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }
        return
    }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    val isButtonActive = validateEmail(email) && validatePassword(password)


    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Tertiary
                ),
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "Login",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.LightGray
                            )
                        )
                    }
                }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    start = 20.dp,
                    end = 20.dp
                )
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.list),
                contentDescription = "List Icon",
                modifier = Modifier
                    .size(if (isPortrait) 120.dp else 180.dp)
                    .padding(bottom = 40.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                text = "Welcome back to our To-do List!",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
            )
            Text(
                text = "Login to your account",
                style = TextStyle(
                    fontSize = 16.sp,
                    color = Color.Gray
                ),
                modifier = Modifier.padding(20.dp)
            )

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
            Spacer(modifier = Modifier.size(10.dp))

            TaskTextField(
                value = password,
                onValueChange = { passwordString ->
                    password = passwordString
                    passwordError = ""
                },
                modifier = Modifier.fillMaxWidth(),
                label = "Password",
                maxLines = 1,
                keyboardType = KeyboardType.Password,
                singleLine = true,
                isPassword = true,
                isValid = { validatePassword(password) },
                errorMessage = "The password must be at least 6 characters"
            )

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                Button(
                    onClick = { navController?.navigate("passwordRecovery") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier.height(35.dp)
                ) {
                    Text(
                        text = "Forgot password?",
                        style = TextStyle(
                            fontSize = 15.sp,
                            color = LightBlue
                        ),
                        textAlign = TextAlign.End
                    )
                }
            }

            TaskButton(
                onClick = {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                isRedirected = true
                                navController?.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            } else {
                                val errorMessage = task.exception?.localizedMessage
                                    ?: "Login failed"
                                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                            }
                        }
                },
                text = "Login",
                modifier = Modifier.fillMaxWidth(),
                enabled = isButtonActive
            )
            Spacer(modifier = Modifier.size(5.dp))

            Button(
                onClick = { navController?.navigate("register") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                )
            ) {
                Text(
                    text = "Not have an account? Sign up",
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                )
            }
        }
    }
}
