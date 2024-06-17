package com.example.todoapp.repository

import com.example.todoapp.model.Task

interface TaskRepository {
    suspend fun createTask(task: Task): Task
    suspend fun getTaskById(taskId: String): Task?
    suspend fun getAllTasks(userId: String): List<Task>
    suspend fun updateTask(task: Task): Boolean
    suspend fun deleteTask(taskId: String): Boolean
    suspend fun getTasksByUserId(userId: String): List<Task>
}