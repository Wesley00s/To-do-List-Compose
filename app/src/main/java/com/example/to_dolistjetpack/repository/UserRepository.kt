package com.example.to_dolistjetpack.repository

import android.content.Context
import android.widget.Toast
import androidx.navigation.NavController
import com.example.to_dolistjetpack.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserRepository(private val firestore: FirebaseFirestore) {

    fun createUser(
        context: Context,
        user: User
    ) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val userRef = firestore.collection("users").document(userId)

        userRef.set(user)
            .addOnSuccessListener {
                Toast.makeText(context, "Perfil criado com sucesso", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Erro ao criar perfil", Toast.LENGTH_SHORT).show()
            }
    }

    fun logout(navController: NavController, currentRoute: String) {
        FirebaseAuth.getInstance().signOut()
        navController.navigate("login") {
            popUpTo(currentRoute) { inclusive = true }
        }
    }

    fun deleteAccount(
        navController: NavController,
        context: Context,
        currentRoute: String,
    ) {
        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid ?: return

        firestore.collection("users").document(userId)
            .delete()
            .addOnSuccessListener {
                user.delete()
                    .addOnSuccessListener {
                        navController.navigate("login") {
                            popUpTo(currentRoute) { inclusive = true }
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "An error ocurred while deleting account", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(context, "An error ocurred while deleting data", Toast.LENGTH_SHORT).show()
            }
    }
}