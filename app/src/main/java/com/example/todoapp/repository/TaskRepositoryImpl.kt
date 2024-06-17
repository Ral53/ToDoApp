// TaskRepositoryImpl.kt
package com.example.todoapp.repository

import android.util.Log
import com.example.todoapp.model.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class TaskRepositoryImpl : TaskRepository {

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("Tasks")

    override suspend fun createTask(task: Task): Task {
        val userId = auth.currentUser?.uid ?: throw RuntimeException("User not authenticated")
//        val userId = "2345678345"
        val taskId = database.child(userId).push().key ?: throw RuntimeException("Failed to push task")
        val newTask = task.copy(id = taskId)
        database.child(userId).child(taskId).setValue(newTask)
        Log.d("TaskRepositoryImpl", "Repository Impl: $newTask")
        return newTask
    }

    override suspend fun getTaskById(taskId: String): Task? {
        return suspendCoroutine { continuation ->
            database.child(taskId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    continuation.resume(snapshot.getValue(Task::class.java))
                }

                override fun onCancelled(error: DatabaseError) {
                    continuation.resume(null)
                }
            })
        }
    }

    override suspend fun getAllTasks(userId: String): List<Task> {
        return suspendCoroutine { continuation ->
            database.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val tasks = mutableListOf<Task>()
                    for (child in snapshot.children) {
                        val task = child.getValue(Task::class.java)
                        if (task != null) {
                            tasks.add(task)
                        }
                    }
                    continuation.resume(tasks)
                }

                override fun onCancelled(error: DatabaseError) {
                    continuation.resume(emptyList())
                }
            })
        }
    }

    override suspend fun updateTask(task: Task): Boolean {
        val userId = task.userId
        val taskId = task.id
        return try {
            database.child(userId).child(taskId).setValue(task)
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun deleteTask(taskId: String): Boolean {
        return try {
            database.child(taskId).removeValue()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getTasksByUserId(userId: String): List<Task> {
        return getAllTasks(userId)
    }
}
