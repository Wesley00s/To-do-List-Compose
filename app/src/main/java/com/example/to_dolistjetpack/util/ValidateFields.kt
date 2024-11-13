package com.example.to_dolistjetpack.util

fun validateEmail(email: String): Boolean {
    if (email.isBlank()) {
        return false
    }
    if (!email.contains("@")
        || !email.contains(".")
    ) {
        return false
    }
    return true
}

fun validatePassword(password: String): Boolean {
    if (password.isBlank()) {
        return false
    }
    if (password.length < 6) {
        return false
    }
    return true
}