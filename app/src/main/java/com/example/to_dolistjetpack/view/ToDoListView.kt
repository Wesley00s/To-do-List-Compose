package com.example.to_dolistjetpack.view

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.to_dolistjetpack.R
import com.example.to_dolistjetpack.components.TaskItem
import com.example.to_dolistjetpack.model.Task
import com.example.to_dolistjetpack.repository.UserRepository
import com.example.to_dolistjetpack.ui.theme.Secondary
import com.example.to_dolistjetpack.ui.theme.Tertiary
import com.example.to_dolistjetpack.ui.theme.White
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoList(
    navController: NavController,
    context: Context
) {
    val userRef = FirebaseAuth.getInstance().currentUser
    val firestore = Firebase.firestore
    val userRepository = UserRepository(firestore)
    val taskList = remember { mutableStateListOf<Task>() }
    var expanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var isSearchVisible by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var isTextFieldFocused by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    val filteredTasks by remember {
        derivedStateOf {
            if (searchQuery.isNotEmpty()) {
                taskList.filter { it.name.contains(searchQuery, ignoreCase = true) }
            } else {
                taskList
            }
        }
    }

    LaunchedEffect(isSearchVisible) {
        if (isSearchVisible) {
            focusRequester.requestFocus()
        }
    }

    val bgColor = if (isSystemInDarkTheme()) Color.Black else Secondary

    LaunchedEffect(Unit) {
        getTasksFromFirebase(taskList, firestore, userRef) {
            isLoading = false
        }
    }

    Scaffold(
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures {
                    if (isSearchVisible) {
                        isSearchVisible = false
                        searchQuery = ""
                    }
                }
            },
        containerColor = bgColor,

        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Tertiary
                ),
                title = {
                    if (isSearchVisible) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            textStyle = TextStyle(
                                fontSize = 14.sp
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp)
                                .focusRequester(focusRequester)
                                .onFocusChanged { focusState ->
                                    isTextFieldFocused = focusState.isFocused
                                },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                focusedTextColor = White,
                                focusedLabelColor = Color.LightGray,
                                unfocusedLabelColor = Color.LightGray,
                                cursorColor = White,
                            ),
                            placeholder = {
                                Text(
                                    text = "Search tasks...",
                                    style = TextStyle(
                                        color = Color.LightGray,
                                        fontSize = 14.sp
                                    )
                                )
                            },
                            leadingIcon = {
                                Image(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_search),
                                    contentDescription = "Search"
                                )
                            },
                            singleLine = true,
                            trailingIcon = {
                                if (isTextFieldFocused) {
                                    IconButton(onClick = {
                                        searchQuery = ""
                                        isSearchVisible = false
                                        focusManager.clearFocus()
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Clear",
                                            tint = Color.White
                                        )
                                    }
                                }
                            }
                        )
                    } else {
                        Text(
                            text = "To-do List",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.LightGray
                            )
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { expanded = true }) {
                        Image(
                            ImageVector.vectorResource(id = R.drawable.ic_more_vert),
                            contentDescription = "Menu"
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.width(150.dp)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Search") },
                            onClick = {
                                expanded = false
                                isSearchVisible = !isSearchVisible
                            },
                            leadingIcon = {
                                Image(
                                    ImageVector.vectorResource(id = R.drawable.ic_search),
                                    contentDescription = "Search"
                                )
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Profile") },
                            onClick = {
                                navController.navigate("profile")
                                expanded = false
                            },
                            leadingIcon = {
                                Image(
                                    ImageVector.vectorResource(id = R.drawable.ic_profile),
                                    contentDescription = "Profile"
                                )
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Logout") },
                            onClick = {
                                userRepository.logout(navController, "home")
                                expanded = false
                            },
                            leadingIcon = {
                                Image(
                                    ImageVector.vectorResource(id = R.drawable.ic_logout),
                                    contentDescription = "Logout"
                                )
                            }
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("saveTask") },
                containerColor = Tertiary,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_add),
                    contentDescription = "Add"
                )
            }
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Tertiary,
                    modifier = Modifier.size(48.dp)
                )
            }
        } else {

            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyColumn {
                    itemsIndexed(filteredTasks) { _, task ->
                        TaskItem(
                            navController = navController,
                            taskList = taskList,
                            taskId = task.id.toString(),
                            context = context,
                            firestore = firestore,
                            userId = userRef?.uid.orEmpty()
                        )

                    }
                }
            }
        }
    }
}

fun getTasksFromFirebase(
    taskList: SnapshotStateList<Task>,
    firestore: FirebaseFirestore,
    userRef: FirebaseUser?,
    onComplete: () -> Unit
) {
    userRef?.let { user ->
        firestore.collection("users")
            .document(user.uid)
            .collection("tasks")
            .get()
            .addOnSuccessListener { querySnapshot ->
                taskList.clear()
                querySnapshot.documents.forEach { document ->
                    val task = document.toObject(Task::class.java)
                    task?.id = document.id
                    task?.let { taskList.add(it) }
                }
                onComplete()
            }
            .addOnFailureListener {
                onComplete()
            }
    } ?: onComplete()
}