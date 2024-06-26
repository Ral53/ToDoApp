package com.example.todoapp.repository

import com.example.todoapp.model.Task

interface TaskRepository {
    suspend fun createTask(task: Task): Task
    suspend fun getAllTasks(userId: String): List<Task>
    suspend fun deleteTask(taskId: String): Boolean
}
