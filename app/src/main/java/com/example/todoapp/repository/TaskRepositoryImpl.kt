package com.example.todoapp.repository

import android.util.Log
import com.example.todoapp.model.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class TaskRepositoryImpl : TaskRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("Tasks")

    override suspend fun createTask(task: Task): Task {
        val userId = auth.currentUser?.uid ?: throw RuntimeException("User not authenticated")
        val taskId = database.child(userId).push().key ?: throw RuntimeException("Failed to push task")
        val newTask = task.copy(id = taskId)
        database.child(userId).child(taskId).setValue(newTask)
        Log.d("TaskRepositoryImpl", "Created task: $newTask")
        return newTask
    }

    override suspend fun getTaskById(taskId: String): Task? {
        val userId = auth.currentUser?.uid ?: throw RuntimeException("User not authenticated")
        return suspendCoroutine { continuation ->
            database.child(userId).child(taskId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val task = snapshot.getValue(Task::class.java)
                    Log.d("TaskRepositoryImpl", "Fetched task by ID: $task")
                    continuation.resume(task)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("TaskRepositoryImpl", "Error fetching task by ID: $error")
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
                    Log.d("TaskRepositoryImpl", "Fetched all tasks: $tasks")
                    continuation.resume(tasks)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("TaskRepositoryImpl", "Error fetching all tasks: $error")
                    continuation.resume(emptyList())
                }
            })
        }
    }

    override suspend fun deleteTask(taskId: String): Boolean {
        val userId = auth.currentUser?.uid ?: throw RuntimeException("User not authenticated")
        return try {
            database.child(userId).child(taskId).removeValue()
            Log.d("TaskRepositoryImpl", "Deleted task with ID: $taskId")
            true
        } catch (e: Exception) {
            Log.e("TaskRepositoryImpl", "Error deleting task with ID: $taskId", e)
            false
        }
    }

    override suspend fun getTasksByUserId(userId: String): List<Task> {
        return getAllTasks(userId)
    }
}
