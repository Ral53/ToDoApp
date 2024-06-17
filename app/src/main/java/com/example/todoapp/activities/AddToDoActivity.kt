package com.example.todoapp.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.example.todoapp.R
import com.example.todoapp.databinding.ActivityAddToDoBinding
import com.example.todoapp.databinding.ActivityLoginBinding
import com.example.todoapp.model.Task
import com.example.todoapp.utils.ClearAll
import com.example.todoapp.utils.CredentialManager
import com.example.todoapp.viewmodel.TaskViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddToDoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddToDoBinding
    private val viewModel: TaskViewModel by viewModels()
    private lateinit var credentialManager: CredentialManager // Declare lateinit


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddToDoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        credentialManager = CredentialManager(this)


        binding.fab.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val message = binding.messageEditText.text.toString()
            val dueDate = Triple(binding.datePicker.dayOfMonth, binding.datePicker.month + 1, binding.datePicker.year).toString()

            // Get the current date and time
            val currentDate = LocalDate.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val createdDate = currentDate.format(formatter)

            if (name.isEmpty() || message.isEmpty() || dueDate.isEmpty() || createdDate.isNullOrEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                val newTask = Task(name = name, message = message, dueDate = dueDate, createdDate = createdDate)
                Log.d("AddToDoActivity", "Add Task: $newTask")

                // Call createTask and observe the returned LiveData
                viewModel.createTask(newTask).observe(this) { success ->
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

//        binding.clearAll.setOnClickListener {
//            ClearAll().deleteAllFromDatabase()
//            ClearAll().deleteAllFromStorage()
//            ClearAll().deleteUser()
//            credentialManager.setLoggedIn(false)
//        }
    }
}

