package com.example.to_dolistjetpack.view

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.to_dolistjetpack.R
import com.example.to_dolistjetpack.components.TaskButton
import com.example.to_dolistjetpack.components.TaskTextField
import com.example.to_dolistjetpack.enumeration.PriorityLevel
import com.example.to_dolistjetpack.model.Task
import com.example.to_dolistjetpack.repository.TaskRepository
import com.example.to_dolistjetpack.ui.theme.LightBlue
import com.example.to_dolistjetpack.ui.theme.MediumGreen
import com.example.to_dolistjetpack.ui.theme.MediumRed
import com.example.to_dolistjetpack.ui.theme.MediumYellow
import com.example.to_dolistjetpack.ui.theme.Tertiary
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveTask(
    navController: NavController,
    context: Context
) {
    val firestore = FirebaseFirestore.getInstance()

    val taskRepository = TaskRepository(firestore)
    var taskName by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf<String?>(null) }
    val userId = FirebaseAuth.getInstance().currentUser?.uid!!

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Tertiary
                ),
                title = {

                    Text(
                        text = "Create Task",
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
                            colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.LightGray)
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                TaskTextField(
                    value = taskName,
                    onValueChange = {
                        taskName = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = "Task Name",
                    maxLines = 1,
                    keyboardType = KeyboardType.Text,
                    singleLine = true,
                    focusedBorderColor = LightBlue,
                    leadingIcon = ImageVector.vectorResource(id = R.drawable.ic_name),
                    isValid = { taskName.isNotBlank() },
                    errorMessage = "Task name cannot be empty"
                )
                TaskTextField(
                    value = taskDescription,
                    onValueChange = {
                        taskDescription = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    label = "Task Description",
                    maxLines = 5,
                    keyboardType = KeyboardType.Text,
                    singleLine = false,
                    focusedBorderColor = LightBlue,
                    leadingIcon = ImageVector.vectorResource(id = R.drawable.ic_description)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    Text(text = "Priority level", modifier = Modifier.padding(horizontal = 20.dp))
                    RadioButton(
                        selected = selectedPriority == "low",
                        onClick = {
                            selectedPriority = if (selectedPriority == "low") null else "low"
                        },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color.Green,
                            unselectedColor = MediumGreen
                        )
                    )

                    RadioButton(
                        selected = selectedPriority == "medium",
                        onClick = {
                            selectedPriority = if (selectedPriority == "medium") null else "medium"
                        },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color.Yellow,
                            unselectedColor = MediumYellow
                        )
                    )

                    RadioButton(
                        selected = selectedPriority == "high",
                        onClick = {
                            selectedPriority = if (selectedPriority == "high") null else "high"
                        },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color.Red,
                            unselectedColor = MediumRed
                        )
                    )
                }
                TaskButton(
                    onClick = {
                        val newTask = Task(
                            name = taskName,
                            description = taskDescription,
                            priority = when (selectedPriority) {
                                "low" -> PriorityLevel.LOW
                                "medium" -> PriorityLevel.MEDIUM
                                "high" -> PriorityLevel.HIGH
                                else -> PriorityLevel.NONE
                            },
                            updateAt = LocalDateTime.now().toString(),
                            isDone = false
                        )
                        taskRepository.createTask(context, userId, newTask)
                        navController.popBackStack()
                    },
                    enabled = taskName.isNotBlank(),
                    text = "Save",
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}