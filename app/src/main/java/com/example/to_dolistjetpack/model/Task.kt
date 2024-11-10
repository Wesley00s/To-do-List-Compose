package com.example.to_dolistjetpack.model

import com.example.to_dolistjetpack.enumeration.PriorityLevel
import java.time.LocalDateTime

data class Task(
    val name: String? = null,
    val description: String? = null,
    val priority: PriorityLevel? = null,
    val updateAt: LocalDateTime? = null,
    var isDone: Boolean? = null
)