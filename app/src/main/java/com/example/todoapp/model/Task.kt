package com.example.todoapp.model

data class Task(
    var id: String = "",
    val name: String = "",
    val message: String = "",
    var userId: String = "", // id of user who created the task
    val createdDate: String = ""
)
