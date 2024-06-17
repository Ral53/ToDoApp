package com.example.todoapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.model.Task
import com.example.todoapp.repository.TaskRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TaskViewModel() : ViewModel() {

    private val taskRepository = TaskRepositoryImpl()

    fun createTask(task: Task): LiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                taskRepository.createTask(task)
                liveData.postValue(true) // Indicate success
            } catch (e: Exception) {
                // Handle exception (log, notify user)
                liveData.postValue(false) // Indicate failure (optional with error message)
            }
        }
        return liveData
    }

    // Function to fetch a task by its ID
    fun fetchTaskById(taskId: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val task = taskRepository.getTaskById(taskId)
            // Optionally, handle the retrieved task (e.g., update UI)
        }
    }

    // Function to update an existing task
    fun updateTask(task: Task) {
        GlobalScope.launch(Dispatchers.IO) {
            taskRepository.updateTask(task)
            // Optionally, perform any additional operations after task update
        }
    }

    // Function to delete a task by its ID
    fun deleteTask(taskId: String) {
        GlobalScope.launch(Dispatchers.IO) {
            taskRepository.deleteTask(taskId)
            // Optionally, perform any additional operations after task deletion
        }
    }

    // Function to fetch all tasks for a specific user
    fun fetchTasksByUserId(userId: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val tasks = taskRepository.getTasksByUserId(userId)
            // Optionally, handle the retrieved list of tasks (e.g., update UI)
        }
    }
}
