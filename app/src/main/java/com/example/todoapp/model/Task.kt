package com.example.todoapp.model

import java.util.Date

data class Task(
    val id: String = "",
    val name: String = "",
    val message: String = "",
    val isCompleted: Boolean = false,
    val createdDate: String = "",
    val dueDate: Date? = null
)
