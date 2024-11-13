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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.to_dolistjetpack.R
import com.example.to_dolistjetpack.components.TaskButton
import com.example.to_dolistjetpack.components.TaskTextField
import com.example.to_dolistjetpack.ui.theme.LightBlue
import com.example.to_dolistjetpack.ui.theme.Tertiary
import com.example.to_dolistjetpack.util.validateEmail
import com.example.to_dolistjetpack.util.validatePassword
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterView(
    navController: NavController,
    context: Context
) {
    val auth = Firebase.auth
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val isButtonActive = validateEmail(email)
            && validatePassword(password)
            && firstName.length >= 3
            && lastName.length >= 3

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
                            text = "Register",
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
                    text = "We are happy to you have join our \n To-do List \uD83D\uDE00",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.padding(bottom = 20.dp),

                    )
                TaskTextField(
                    value = firstName,
                    onValueChange = { firstNameString ->
                        firstName = firstNameString
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = "First Name",
                    maxLines = 1,
                    keyboardType = KeyboardType.Text,
                    singleLine = true,
                    focusedBorderColor = LightBlue,
                    leadingIcon = ImageVector.vectorResource(id = R.drawable.ic_name),
                    isValid = { firstName.length >= 3 },
                    errorMessage = "Enter a valid name"
                )
                Spacer(modifier = Modifier.size(10.dp))
                TaskTextField(
                    value = lastName,
                    onValueChange = { latNameString ->
                        lastName = latNameString
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = "Last Name",
                    maxLines = 1,
                    keyboardType = KeyboardType.Text,
                    singleLine = true,
                    focusedBorderColor = LightBlue,
                    leadingIcon = ImageVector.vectorResource(id = R.drawable.ic_name),
                    isValid = { lastName.length >= 3 },
                    errorMessage = "Enter a valid name"

                )
                Spacer(modifier = Modifier.size(10.dp))
                TaskTextField(
                    value = email,
                    onValueChange = { emailString ->
                        email = emailString
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = "Email",
                    maxLines = 1,
                    keyboardType = KeyboardType.Email,
                    singleLine = true,
                    focusedBorderColor = LightBlue,
                    isValid = { validateEmail(email) },
                    errorMessage = "Enter a valid email"

                )
                Spacer(modifier = Modifier.size(10.dp))
                TaskTextField(
                    value = password,
                    onValueChange = { passwordString ->
                        password = passwordString
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = "Password",
                    isPassword = true,
                    maxLines = 1,
                    keyboardType = KeyboardType.Password,
                    singleLine = true,
                    focusedBorderColor = LightBlue,
                    isValid = { validatePassword(password) },
                    errorMessage = "The password must be at least 6 characters"
                )

                Spacer(modifier = Modifier.size(25.dp))
                TaskButton(
                    onClick = {
                        auth.createUserWithEmailAndPassword(
                            email,
                            password
                        )
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    navController.navigate("home")
                                    addUser(
                                        "users",
                                        context,
                                        firstName,
                                        lastName,
                                        email
                                    )
                                } else {
                                    val errorMessage = task.exception?.message
                                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                                }
                            }
                    },
                    text = "Register", modifier = Modifier.fillMaxWidth(),
                    enabled = isButtonActive
                )
                Spacer(modifier = Modifier.size(5.dp))
                Button(
                    onClick = {
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    )

                ) {
                    Text(
                        text = "Already have an account? Login",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Gray
                        )
                    )
                }
            }
        }
    }
}

fun addUser(
    path: String,
    context: Context,
    firstName: String,
    lastName: String,
    email: String,
) {
    val userId = Firebase.auth.currentUser?.uid!!
    val user = mapOf(
        "firstName" to firstName,
        "lastName" to lastName,
        "email" to email,
    )
    Firebase.database
        .reference
        .child(path)
        .child(userId)
        .setValue(user)
        .addOnSuccessListener {
            Toast.makeText(context, "User added successfully", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener {
            Toast.makeText(context, "Failed to add user", Toast.LENGTH_SHORT).show()
        }
}

@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
    RegisterView(navController = rememberNavController(), context = LocalContext.current)
}