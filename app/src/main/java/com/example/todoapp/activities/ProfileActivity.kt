package com.example.todoapp.activities

import UserViewModel
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.todoapp.R
import com.example.todoapp.databinding.ActivityProfileBinding
import com.example.todoapp.utils.CredentialManager
import com.squareup.picasso.Picasso

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val viewModel: UserViewModel by viewModels()
    private lateinit var credentialManager: CredentialManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        credentialManager = CredentialManager(this)

        val fullNameTextView = binding.fullNameTextView
        val emailTextView = binding.emailTextView

        viewModel.fullName.observe(this) { fullName ->
            fullNameTextView.text = fullName ?: "Full Name"
        }

        viewModel.email.observe(this) { email ->
            emailTextView.text = email ?: "Email"
        }

        viewModel.profilePictureUrl.observe(this) { url ->
            url?.let {
                Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show()
                Picasso.get().load(it).into(binding.imageView)
            }
        }

        binding.newTaskButton.setOnClickListener {
            viewModel.logOut()
            credentialManager.setLoggedIn(false)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this, "Logged out!", Toast.LENGTH_SHORT).show();
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
