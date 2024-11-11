package com.example.to_dolistjetpack

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.to_dolistjetpack.ui.theme.TodoListJetPackTheme
import com.example.to_dolistjetpack.view.auth.LoginView
import com.example.to_dolistjetpack.view.auth.RegisterView
import com.example.to_dolistjetpack.view.SaveTask
import com.example.to_dolistjetpack.view.ToDoList
import com.example.to_dolistjetpack.view.auth.PasswordRecoveryView

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoListJetPackTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "login",
                    ) {
                        composable("home") {
                            ToDoList(navController)
                        }
                        composable("saveTask") {
                            SaveTask(navController)
                        }
                        composable("login") {
                            LoginView(navController)
                        }
                        composable("register") {
                            RegisterView(navController,this@MainActivity)
                        }
                        composable("passwordRecovery") {
                            PasswordRecoveryView(navController)
                        }
                    }
                }
            }
        }
    }
}
