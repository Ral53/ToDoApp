package com.example.todoapp.activities

import TaskViewModel
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.todoapp.databinding.ActivityAddToDoBinding
import com.example.todoapp.model.Task
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

        binding.fab.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val message = binding.messageEditText.text.toString().trim()

            if (name.isEmpty() || message.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val currentDate = LocalDate.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val createdDate = currentDate.format(formatter)

            val newTask = Task(name = name, message = message, createdDate = createdDate)

            // Call viewModel to create task
            viewModel.createTask(newTask)

            // Observe task creation result
            viewModel.taskCreated.observe(this) { success ->
                if (success) {
                    Toast.makeText(this, "Task created successfully!", Toast.LENGTH_LONG).show()
                    binding.nameEditText.text.clear()
                    binding.messageEditText.text.clear()
                } else {
                    Toast.makeText(this, "Failed to create task!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
