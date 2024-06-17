package com.example.todoapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.model.Task
import com.example.todoapp.repository.TaskRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel : ViewModel() {

    private val taskRepository = TaskRepositoryImpl()
    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> get() = _tasks

    fun createTask(task: Task): LiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                taskRepository.createTask(task)
                liveData.postValue(true)
            } catch (e: Exception) {
                liveData.postValue(false)
            }
        }
        return liveData
    }

    fun fetchTaskById(taskId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val task = taskRepository.getTaskById(taskId)
            Log.d("TaskViewModel", "Fetched task by ID: $task")
        }
    }

    fun deleteTask(taskId: String): LiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()
        viewModelScope.launch(Dispatchers.IO) {
            val success = taskRepository.deleteTask(taskId)
            liveData.postValue(success)
            Log.d("TaskViewModel", "Deleted task with ID: $taskId, success: $success")
        }
        return liveData
    }

    fun fetchTasksByUserId(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val tasks = taskRepository.getTasksByUserId(userId)
            _tasks.postValue(tasks)
            Log.d("TaskViewModel", "Fetched tasks by user ID: $userId, tasks: $tasks")
        }
    }
}
