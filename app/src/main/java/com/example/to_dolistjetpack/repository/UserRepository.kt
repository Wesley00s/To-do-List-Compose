package com.example.to_dolistjetpack.repository

import android.content.Context
import android.widget.Toast
import androidx.navigation.NavController
import com.example.to_dolistjetpack.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class UserRepository(private val database: FirebaseDatabase) {
    fun createUser(
        path: String,
        context: Context,
        user: User,
    ) {
        val userId = Firebase.auth.currentUser?.uid!!
        val userMap = mapOf(
            "firstName" to user.firstName,
            "lastName" to user.lastName,
            "email" to user.email,
        )
        database
            .reference
            .child(path)
            .child(userId)
            .setValue(userMap)
            .addOnSuccessListener {
                Toast.makeText(context, "User added successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to add user", Toast.LENGTH_SHORT).show()
            }
    }

    fun logout(navController: NavController, currentRoute: String) {
        Firebase.auth.signOut()
        navController.navigate("login") {
            popUpTo(currentRoute) { inclusive = true }
        }
    }

    fun deleteAccount(
        navController: NavController,
        currentRoute: String,
    ) {
        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid
        val databaseReference = database.reference.child("users").child(userId ?: "")

        databaseReference.removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                user?.delete()?.addOnCompleteListener { deleteTask ->
                    if (deleteTask.isSuccessful) {
                        navController.navigate("login") {
                            popUpTo(currentRoute) { inclusive = true }
                        }
                    }
                }
            }
        }
    }
}