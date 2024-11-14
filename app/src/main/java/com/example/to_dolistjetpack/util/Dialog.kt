package com.example.to_dolistjetpack.util

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation.NavController
import com.example.to_dolistjetpack.model.Task
import com.example.to_dolistjetpack.repository.TaskRepository
import com.example.to_dolistjetpack.repository.UserRepository

fun deleteTaskAlertDialog(
    context: Context,
    userId: String,
    taskId: String,
    taskRepository: TaskRepository,
    taskList: SnapshotStateList<Task>,
    backPopStack: () -> Boolean? = { null }
) {
    val alertDialog = AlertDialog.Builder(context)
    alertDialog.setTitle("Delete Task")
        .setMessage("Are you sure you want to delete this task?")
        .setPositiveButton("Yes") { _, _ ->
            taskRepository.deleteTask(context, userId, taskId) { isDeleted ->
                if (isDeleted) {
                    val taskIndex = taskList.indexOfFirst { it.id.toString() == taskId }
                    if (taskIndex != -1) {
                        taskList.removeAt(taskIndex)
                        backPopStack()
                    }
                }
            }
        }
        .setNegativeButton("No") { _, _ ->
            Toast.makeText(context, "Task not deleted", Toast.LENGTH_SHORT).show()
        }
        .show()
}


fun deleteAccountAlertDialog(
    context: Context,
    userRepository: UserRepository,
    navController: NavController,
    currentRoute: String,
) {
    val alertDialog = AlertDialog.Builder(context)
    alertDialog.setTitle("Delete Account")
        .setMessage("Are you sure you want to delete your account?")
        .setPositiveButton("Yes") { _, _ ->
            userRepository.deleteAccount(
                navController,
                currentRoute,
            )
        }
        .setNegativeButton("No") { _, _ ->
            Toast.makeText(context, "Account not deleted", Toast.LENGTH_SHORT).show()
        }
        .show()
}