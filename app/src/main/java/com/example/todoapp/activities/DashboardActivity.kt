package com.example.todoapp.activities

import TaskViewModel
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.adapter.TaskAdapter
import com.example.todoapp.databinding.ActivityDashboardBinding
import com.example.todoapp.model.Task
import com.google.firebase.auth.FirebaseAuth

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private val viewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeViewModel()
        setupListeners()

        val currentUid = FirebaseAuth.getInstance().currentUser?.uid
        currentUid?.let { userId ->
            viewModel.fetchTasks(userId)
        }
    }

    private fun setupRecyclerView() {
        binding.taskList.layoutManager = LinearLayoutManager(this)
    }

    private fun observeViewModel() {
        viewModel.tasks.observe(this, Observer { tasks ->
            // Update RecyclerView with the latest tasks
            val adapter = TaskAdapter(tasks) { task ->
                deleteTask(task)
            }
            binding.taskList.adapter = adapter
        })

        viewModel.taskDeleted.observe(this, Observer { isDeleted ->
            if (isDeleted) {
                Toast.makeText(this, "Task deleted successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to delete task!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupListeners() {
        binding.newTaskButton.setOnClickListener {
            startActivity(Intent(this, AddToDoActivity::class.java))
        }

        binding.profileButton.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    private fun deleteTask(task: Task) {
        viewModel.deleteTask(task.id)
    }
}
