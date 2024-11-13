package com.example.to_dolistjetpack.components

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.to_dolistjetpack.R
import com.example.to_dolistjetpack.enumeration.PriorityLevel
import com.example.to_dolistjetpack.model.Task
import com.example.to_dolistjetpack.ui.theme.HighPriorityColor
import com.example.to_dolistjetpack.ui.theme.LightBlue
import com.example.to_dolistjetpack.ui.theme.LowPriorityColor
import com.example.to_dolistjetpack.ui.theme.MediumPriorityColor
import com.example.to_dolistjetpack.ui.theme.NonePriorityColor
import com.example.to_dolistjetpack.ui.theme.White
import com.google.firebase.database.DatabaseReference
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskItem(
    navController: NavController,
    index: Int,
    taskList: SnapshotStateList<Task>,
    taskId: String,
    context: Context,
    datasource: DatabaseReference,
    userId: String
) {
    val task = taskList[index]
    var isChecked by remember { mutableStateOf(task.isDone) }

    val priorityLevel: String = when (task.priority) {
        PriorityLevel.LOW -> "Low"
        PriorityLevel.MEDIUM -> "Medium"
        PriorityLevel.HIGH -> "High"
        PriorityLevel.NONE -> "None"
        else -> "None"
    }

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
            .padding(8.dp)
            .clickable {
                navController.navigate("editTask/$taskId")
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = White
        ),
    ) {
        Column(
            modifier = Modifier
                .padding(top = 5.dp, bottom = 5.dp, end = 10.dp)
                .fillMaxWidth(),
        ) {
            Row {
                Checkbox(
                    checked = isChecked!!,
                    onCheckedChange = {
                        isChecked = it
                        task.isDone = it
                    },
                    modifier = Modifier
                        .scale(0.8f)
                        .size(20.dp)
                        .padding(start = 20.dp, end = 10.dp, top = 15.dp),
                    colors = CheckboxDefaults.colors(
                        checkedColor = LightBlue,
                        uncheckedColor = Color.Gray,
                        checkmarkColor = Color.White
                    )
                )
                completeTask(datasource, userId, taskId, task) { isEdited ->
                    if (isEdited) {
                        taskList[index] = task
                    }
                }
                Column {
                    Text(
                        text = task.name.toString(),
                        modifier = Modifier.padding(vertical = 5.dp, horizontal = 20.dp),
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isChecked as Boolean) Color.Gray else Color.Black,
                            textDecoration = if (isChecked as Boolean) TextDecoration.LineThrough else TextDecoration.None
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
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isChecked as Boolean) Color.Gray else Color.Black,
                            textDecoration = if (isChecked as Boolean) TextDecoration.LineThrough else TextDecoration.None
                        ),
                        modifier = Modifier.padding(start = 20.dp)
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
                                deleteTask(context, datasource, userId, taskId) { isDeleted ->
                                    if (isDeleted) {
                                        taskList.removeAt(index)
                                    }
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

fun deleteTask(
    context: Context,
    datasource: DatabaseReference,
    userId: String,
    taskId: String,
    onDeleted: (Boolean) -> Unit
) {
    val taskRef = datasource.child(userId).child("tasks").child(taskId)
    taskRef.removeValue().addOnCompleteListener { task ->
        if (task.isSuccessful) {
            Toast.makeText(context, "Task deleted", Toast.LENGTH_SHORT).show()
            onDeleted(true)
        } else {
            Toast.makeText(context, "Failed to delete task", Toast.LENGTH_SHORT).show()
            onDeleted(false)
        }
    }
}

fun completeTask(
    datasource: DatabaseReference,
    userId: String,
    taskId: String,
    task: Task,
    onEdited: (Boolean) -> Unit
) {
    val taskRef = datasource.child(userId).child("tasks").child(taskId)

    val updates = mapOf(
        "done" to task.isDone,
    )

    taskRef.updateChildren(updates).addOnCompleteListener {
        if (it.isSuccessful) {
            onEdited(true)
        } else {
            onEdited(false)
        }
    }
}