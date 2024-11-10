package com.example.to_dolistjetpack.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.to_dolistjetpack.components.TaskButton
import com.example.to_dolistjetpack.components.TaskTextField
import com.example.to_dolistjetpack.ui.theme.MediumGreen
import com.example.to_dolistjetpack.ui.theme.MediumRed
import com.example.to_dolistjetpack.ui.theme.MediumYellow
import com.example.to_dolistjetpack.ui.theme.PurpleGrey40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveTask(
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = PurpleGrey40
                ),
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Save Task",
                            style = TextStyle(
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.LightGray
                            )
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        var taskName by remember { mutableStateOf("") }
        var taskDescription by remember { mutableStateOf("") }
        var noPriority by remember { mutableStateOf(false) }
        var selectedPriority by remember { mutableStateOf<String?>(null) }

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
        ) {
            Column (
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ){
                TaskTextField(
                    value = taskName,
                    onValueChange = {
                        taskName = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = "Task Name",
                    maxLines = 1,
                    keyboardType = KeyboardType.Text
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
                    keyboardType = KeyboardType.Text
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

                    },
                    enabled = taskName.isNotBlank(),
                    text = "Salve",
                    modifier = Modifier.fillMaxWidth())
            }
        }
    }
}