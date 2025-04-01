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
import com.example.to_dolistjetpack.view.EditTask
import com.example.to_dolistjetpack.view.ProfileView
import com.example.to_dolistjetpack.view.SaveTask
import com.example.to_dolistjetpack.view.SplashScreen
import com.example.to_dolistjetpack.view.ToDoList
import com.example.to_dolistjetpack.view.auth.LoginView
import com.example.to_dolistjetpack.view.auth.PasswordRecoveryView
import com.example.to_dolistjetpack.view.auth.RegisterView

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
                        startDestination = "splash",
                    ) {
                        composable("splash") {
                            SplashScreen {
                                navController.navigate("login") {
                                    popUpTo("splash") { inclusive = true }
                                }
                            }
                        }
                        composable("home") {
                            ToDoList(navController, this@MainActivity)
                        }
                        composable("saveTask") {
                            SaveTask(navController, this@MainActivity)
                        }
                        composable("profile") {
                            ProfileView(navController, this@MainActivity)
                        }
                        composable("login") {
                            LoginView(navController, this@MainActivity)
                        }
                        composable("register") {
                            RegisterView(navController, this@MainActivity)
                        }
                        composable("passwordRecovery") {
                            PasswordRecoveryView(navController, this@MainActivity)
                        }
                        composable("editTask/{taskId}") { backStackEntry ->
                            val taskId = backStackEntry.arguments?.getString("taskId")
                            if (taskId != null) {
                                EditTask(navController, taskId)
                            }
                        }

                    }
                }
            }
        }
    }
}
