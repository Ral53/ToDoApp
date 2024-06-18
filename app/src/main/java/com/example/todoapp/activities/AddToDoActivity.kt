package com.example.todoapp.activities

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.todoapp.databinding.ActivityAddToDoBinding
import com.example.todoapp.model.Task
import TaskViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AddToDoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddToDoBinding
    private val viewModel: TaskViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddToDoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()

        binding.fab.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val message = binding.messageEditText.text.toString().trim()

            if (name.isEmpty() || message.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                createTask(name, message)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createTask(name: String, message: String) {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val createdDate = currentDate.format(formatter)

        val newTask = Task(name = name, message = message, createdDate = createdDate)
        viewModel.createTask(newTask)
    }

    private fun setupObservers() {
        viewModel.taskCreated.observe(this, Observer { success ->
            val message = if (success) {
                binding.nameEditText.text.clear()
                binding.messageEditText.text.clear()
                "Task created successfully!"
            } else {
                "Failed to create task!"
            }
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        })
    }
}
