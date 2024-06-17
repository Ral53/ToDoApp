package com.example.todoapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.adapter.TaskAdapter
import com.example.todoapp.databinding.ActivityDashboardBinding
import com.example.todoapp.viewmodel.TaskViewModel
import com.google.firebase.auth.FirebaseAuth

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private val taskViewModel: TaskViewModel by viewModels()
    private lateinit var taskAdapter: TaskAdapter
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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
        val userId = getUserID()  // Implement this method to get the current user's ID
        taskViewModel.fetchTasksByUserId(userId)
        taskViewModel.tasks.observe(this) { tasks ->
            taskAdapter = TaskAdapter(tasks, taskViewModel)
            binding.taskList.adapter = taskAdapter
        }
    }

    private fun getUserID(): String {
        var user = auth.currentUser
        return (user?.uid).toString() // Replace this with actual user ID retrieval logic
    }
}
