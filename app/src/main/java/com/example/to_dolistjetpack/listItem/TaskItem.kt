package com.example.to_dolistjetpack.listItem

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.to_dolistjetpack.R
import com.example.to_dolistjetpack.enumeration.PriorityLevel
import com.example.to_dolistjetpack.model.Task
import com.example.to_dolistjetpack.ui.theme.Tertiary
import com.example.to_dolistjetpack.ui.theme.White
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskItem(index: Int, tasks: MutableList<Task>) {
    val task = Task(
        name = tasks[index].name,
        description = tasks[index].description,
        priority = tasks[index].priority,
        updateAt = tasks[index].updateAt,
        isDone = false
    )

    val priorityLevel: String = when (task.priority) {
        PriorityLevel.LOW -> "Low"
        PriorityLevel.MEDIUM -> "Medium"
        PriorityLevel.HIGH -> "Hight"
        else -> "No priority"
    }

    val indicatorColor = when (priorityLevel) {
        "Low" -> Color.Green
        "Medium" -> Color.Yellow
        "Hight" -> Color.Red
        else -> Color.Gray
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
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

                Checkbox(checked = false, onCheckedChange = {

                })
                Column {

                    Text(
                        text = task.name.toString(),
                        modifier = Modifier.padding(vertical = 2.dp, horizontal = 15.dp),
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )

                    task.updateAt?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss"))?.let {
                        Text(
                            text = it,
                            style = TextStyle(
                                fontSize = 12.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Medium,
                                fontStyle = FontStyle.Italic
                            ),
                            modifier = Modifier.padding(start = 15.dp)
                        )
                    }

                    Text(
                        text = task.description.toString(),
                        style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(start = 15.dp),
                    )
                    Row(
                        modifier = Modifier.padding(start = 15.dp, top = 10.dp)
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

                            },
                            modifier = Modifier.size(25.dp).padding(bottom = 5.dp),
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