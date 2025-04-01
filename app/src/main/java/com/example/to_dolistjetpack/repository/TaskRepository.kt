package com.example.to_dolistjetpack.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.to_dolistjetpack.model.Task
import com.google.firebase.firestore.FirebaseFirestore

class TaskRepository(private val db: FirebaseFirestore) {

    fun createTask(
        context: Context,
        userId: String, task: Task
    ) {
        val taskRef = db.collection("users").document(userId)
            .collection("tasks").document()

        task.id = taskRef.id
        taskRef.set(task)
            .addOnFailureListener {
                Toast.makeText(context, "Failed to create task", Toast.LENGTH_SHORT).show()
                Log.d("TaskRepository", "createTask: ${it.message}")

            }
    }

    fun deleteTask(
        context: Context,
        userId: String,
        taskId: String,
        onDeleted: (Boolean) -> Unit
    ) {
        db.collection("users").document(userId)
            .collection("tasks").document(taskId)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(context, "Task deleted", Toast.LENGTH_SHORT).show()
                onDeleted(true)
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to delete task", Toast.LENGTH_SHORT).show()
                onDeleted(false)
            }
    }

    fun editTask(
        userId: String,
        taskId: String,
        task: Task,
        onEdited: (Boolean) -> Unit
    ) {
        val updates = mapOf(
            "name" to task.name,
            "description" to task.description,
            "priority" to task.priority,
            "done" to task.isDone,
            "updateAt" to task.updateAt
        )

        db.collection("users").document(userId)
            .collection("tasks").document(taskId)
            .update(updates)
            .addOnSuccessListener { onEdited(true) }
            .addOnFailureListener { onEdited(false) }
    }

    fun completeTask(
        userId: String,
        taskId: String,
        isDone: Boolean
    ) {
        db.collection("users").document(userId)
            .collection("tasks").document(taskId)
            .update("done", isDone)
    }
}