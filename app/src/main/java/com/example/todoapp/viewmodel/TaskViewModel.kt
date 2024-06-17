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

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> get() = _tasks

    fun fetchTasksByUserId(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val taskList = taskRepository.getTasksByUserId(userId)
            _tasks.postValue(taskList)
        }
    }

    fun deleteTask(taskId: String): LiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()
        viewModelScope.launch(Dispatchers.IO) {
            val result = taskRepository.deleteTask(taskId)
            liveData.postValue(result)
        }
        return liveData
    }
}

//// Function to update an existing task
//fun updateTask(task: Task) {
//    GlobalScope.launch(Dispatchers.IO) {
//        taskRepository.updateTask(task)
//        // Optionally, perform any additional operations after task update
//    }
//}
