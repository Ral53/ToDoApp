package com.example.todoapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.adapter.TaskAdapter
import com.example.todoapp.databinding.ActivityDashboardBinding
import com.example.todoapp.model.Task


class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

       popuplateTasks()

        binding.newTaskButton.setOnClickListener {
            startActivity(Intent(this, AddToDoActivity::class.java))
        }

        binding.profileButton.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    private fun popuplateTasks() {
        val taskList = findViewById<RecyclerView>(com.example.todoapp.R.id.task_list)
        taskList.layoutManager = LinearLayoutManager(this)


        // Prepare your data (replace with your actual data fetching logic)
        val tasks: MutableList<Task> = ArrayList<Task>()
        tasks.add(Task("Task 1", "This is task 1 description"))
        tasks.add(Task("Task 2", "This is task 2 description"))

        val adapter = TaskAdapter(tasks)
        taskList.adapter = adapter    }
}