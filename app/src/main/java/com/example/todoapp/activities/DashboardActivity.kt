package com.example.todoapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.adapter.TaskAdapter
import com.example.todoapp.databinding.ActivityDashboardBinding
import com.example.todoapp.viewmodel.TaskViewModel

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private val taskViewModel: TaskViewModel by viewModels()
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        fetchTasks()

        binding.newTaskButton.setOnClickListener {
            startActivity(Intent(this, AddToDoActivity::class.java))
        }

        binding.profileButton.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter(emptyList(), taskViewModel)
        binding.taskList.layoutManager = LinearLayoutManager(this)
        binding.taskList.adapter = taskAdapter
    }

    private fun fetchTasks() {
        val userId = getUserID()
        taskViewModel.fetchTasksByUserId(userId)
        taskViewModel.tasks.observe(this, Observer { tasks ->
            Log.d("DashboardActivity", "Observed tasks: $tasks")
            taskAdapter.updateTasks(tasks)
        })
    }

    private fun getUserID(): String {
        // Retrieve and return the current user's ID
        return "user-id" // Replace this with actual user ID retrieval logic
    }
}
