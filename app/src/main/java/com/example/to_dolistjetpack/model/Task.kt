package com.example.to_dolistjetpack.model

import com.example.to_dolistjetpack.enumeration.PriorityLevel

data class Task(
    var id: String = "",
    val name: String = "",
    val description: String = "",
    val priority: PriorityLevel? = null,
    val updateAt: String = "",
    var isDone: Boolean = false
)