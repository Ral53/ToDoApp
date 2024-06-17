package com.example.todoapp.model

data class Task(
    val id: String = "",
    val userId: String = "",  // ID of the user who created the task, this is task name
    val name: String = "",
    val message: String = "",
    val isCompleted: Boolean = false,
    val createdDate: String = "",
    val dueDate: String = ""
)
