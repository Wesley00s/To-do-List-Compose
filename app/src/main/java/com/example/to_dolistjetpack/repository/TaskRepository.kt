package com.example.to_dolistjetpack.repository

import android.content.Context
import android.widget.Toast
import com.example.to_dolistjetpack.model.Task
import com.google.firebase.database.DatabaseReference

class TaskRepository(private val datasource: DatabaseReference) {
    fun createTask(userId: String, task: Task) {
        val userTasksRef = datasource.child(userId).child("tasks").push()
        task.id = userTasksRef.key
        userTasksRef.setValue(task)
    }

    fun deleteTask(
        context: Context,
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

    fun editTask(
        userId: String,
        taskId: String,
        task: Task,
        onEdited: (Boolean) -> Unit
    ) {
        val taskRef = datasource.child(userId).child("tasks").child(taskId)

        val updates = mapOf(
            "name" to task.name,
            "description" to task.description,
            "priority" to task.priority,
            "isDone" to task.isDone,
            "updateAt" to task.updateAt
        )

        taskRef.updateChildren(updates).addOnCompleteListener {
            if (it.isSuccessful) {
                onEdited(true)
            } else {
                onEdited(false)
            }
        }
    }

    fun completeTask(
        userId: String,
        taskId: String,
        task: Task
    ) {
        val taskRef = datasource.child(userId).child("tasks").child(taskId)

        val updates = mapOf(
            "done" to task.isDone,
        )

        taskRef.updateChildren(updates)

    }
}