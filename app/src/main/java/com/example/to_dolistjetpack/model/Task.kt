package com.example.to_dolistjetpack.model

import com.example.to_dolistjetpack.enumeration.PriorityLevel

data class Task(
    var id: String? = null,
    val name: String? = null,
    val description: String? = null,
    val priority: PriorityLevel? = null,
    val updateAt: String = "",
    var isDone: Boolean? = null
)