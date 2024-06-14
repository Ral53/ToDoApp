package com.example.todoapp.model

data class Task(
    val id: String = "",
    val userId: String = "",  // ID of the user who created the task
    val title: String = "",
    val description: String = "",
    val isCompleted: Boolean = false,
    val dueDate: Long = 0L  // Optional: Unix timestamp for due date
)
