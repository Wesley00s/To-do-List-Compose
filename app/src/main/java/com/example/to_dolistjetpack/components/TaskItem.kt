package com.example.to_dolistjetpack.components

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.to_dolistjetpack.R
import com.example.to_dolistjetpack.enumeration.PriorityLevel
import com.example.to_dolistjetpack.model.Task
import com.example.to_dolistjetpack.repository.TaskRepository
import com.example.to_dolistjetpack.ui.theme.DarkCardColor
import com.example.to_dolistjetpack.ui.theme.HighPriorityColor
import com.example.to_dolistjetpack.ui.theme.LightBlue
import com.example.to_dolistjetpack.ui.theme.LowPriorityColor
import com.example.to_dolistjetpack.ui.theme.MediumPriorityColor
import com.example.to_dolistjetpack.ui.theme.NonePriorityColor
import com.example.to_dolistjetpack.util.deleteTaskAlertDialog
import com.example.to_dolistjetpack.util.vibrateOnClick
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskItem(
    navController: NavController?,
    taskList: SnapshotStateList<Task>,
    taskId: String,
    context: Context,
    firestore: FirebaseFirestore,
    userId: String
) {
    val task = taskList.find { it.id == taskId } ?: Task(
        id = taskId,
        name = "Tarefa Exemplo",
        description = "Descrição da tarefa exemplo.",
        priority = PriorityLevel.MEDIUM,
        isDone = false,
        updateAt = "2023-11-10T15:30:00"
    )
    var isChecked by remember { mutableStateOf(task.isDone) }
    val taskRepository = remember { TaskRepository(firestore) }
    val cardColor = if (isSystemInDarkTheme()) DarkCardColor else Color.White
    val taskNameColor = if (isSystemInDarkTheme()) Color.LightGray else Color.Black
    val taskdescColor = if (isSystemInDarkTheme()) Color.LightGray else Color.Black
    val priorityLevel: String = when (task.priority) {
        PriorityLevel.LOW -> "Low"
        PriorityLevel.MEDIUM -> "Medium"
        PriorityLevel.HIGH -> "High"
        PriorityLevel.NONE -> "None"
        else -> "None"
    }

    val cardScale = if (isChecked) 0.92f else 1.0f

    val indicatorColor = when (priorityLevel) {
        "Low" -> LowPriorityColor
        "Medium" -> MediumPriorityColor
        "High" -> HighPriorityColor
        "None" -> NonePriorityColor
        else -> NonePriorityColor
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(cardScale)
            .padding(8.dp)
            .clickable {
                navController?.navigate("editTask/$taskId")
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
    ) {
        Column(
            modifier = Modifier
                .padding(top = 5.dp, bottom = 5.dp, end = 10.dp)
                .fillMaxWidth(),
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp)
            ){
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = {
                        isChecked = it
                        task.isDone = it
                        taskRepository.completeTask(userId, taskId, it)
                        vibrateOnClick(context)
                    },
                    modifier = Modifier
                        .scale(1.2f).align(Alignment.CenterVertically)
                    ,colors = CheckboxDefaults.colors(
                        checkedColor = LightBlue,
                        uncheckedColor = Color.Gray,
                        checkmarkColor = Color.White
                    )
                )
                Column {
                    Text(
                        text = task.name.toString(),
                        modifier = Modifier.padding(top = 10.dp, start = 20.dp, end = 20.dp),
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isChecked) Color.Gray else taskNameColor,
                            textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None,
                            fontStyle = if (isChecked) FontStyle.Italic else FontStyle.Normal
                        )
                    )

                    task.updateAt.let { updateAtStr ->
                        val formatter = DateTimeFormatter.ISO_DATE_TIME
                        val localDateTime = LocalDateTime.parse(updateAtStr, formatter)

                        Text(
                            text = localDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss")),
                            style = TextStyle(
                                fontSize = 12.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Medium,
                                fontStyle = FontStyle.Italic
                            ),
                            modifier = Modifier.padding(start = 20.dp)
                        )
                    }

                    Text(
                        text = task.description.toString(),
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isChecked) Color.Gray else taskdescColor,
                            textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None,
                            fontStyle = if (isChecked) FontStyle.Italic else FontStyle.Normal
                        ),
                        modifier = Modifier.padding(start = 20.dp, end = 10.dp, top = 5.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Row(
                        modifier = Modifier.padding(start = 20.dp, top = 10.dp)
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = indicatorColor
                            ),
                            modifier = Modifier.size(15.dp)
                        ) {}
                        Text(
                            text = priorityLevel,
                            style = TextStyle(
                                fontSize = 12.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Medium,
                            ),
                            modifier = Modifier.padding(horizontal = 15.dp)
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        IconButton(
                            onClick = {
                                if (navController != null) {
                                    deleteTaskAlertDialog(
                                        context,
                                        userId,
                                        taskId,
                                        taskRepository,
                                        taskList
                                    )
                                }
                            },
                            modifier = Modifier
                                .size(25.dp)
                                .padding(bottom = 5.dp),
                        ) {
                            Image(
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_delete),
                                contentDescription = "Delete task"
                            )
                        }
                    }
                }
            }
        }
    }
}