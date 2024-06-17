package com.example.todoapp.repository

import com.example.todoapp.model.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class TaskRepositoryImpl : TaskRepository {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val tasksReference: DatabaseReference = database.getReference("tasks")
    private val currentUser = FirebaseAuth.getInstance().currentUser

    override suspend fun createTask(task: Task): Task {
        val taskId = tasksReference.push().key ?: throw Exception("Failed to get task ID")
        val userId = currentUser?.uid ?: throw Exception("Failed to get user ID")
        task.id = taskId
        task.userId = userId
        tasksReference.child(taskId).setValue(task.toMap()).await()
        return task
    }

    override suspend fun getAllTasks(userId: String): List<Task> {
        val tasks = mutableListOf<Task>()
        tasksReference.orderByChild("userId").equalTo(userId)
            .get()
            .addOnSuccessListener { dataSnapshot ->
                for (snapshot in dataSnapshot.children) {
                    val task = snapshot.getValue(Task::class.java)
                    if (task != null) {
                        tasks.add(task)
                    }
                }
            }
            .await()
        return tasks
    }

    override suspend fun deleteTask(taskId: String): Boolean {
        return try {
            tasksReference.child(taskId).removeValue().await()
            true
        } catch (e: Exception) {
            false
        }
    }

    private suspend fun Task.toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "name" to name,
            "message" to message,
            "userId" to userId
        )
    }
}
