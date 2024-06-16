package com.example.todoapp.activities

import UserViewModel
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.todoapp.R
import com.example.todoapp.databinding.ActivityLoginBinding
import com.example.todoapp.utils.CredentialManager

class LoginActivity : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding
    private lateinit var credentialManager: CredentialManager // Declare lateinit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        credentialManager = CredentialManager(this)
        setupObservers()

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                userViewModel.logInUser(email, password)
                credentialManager.setLoggedIn(true)
            } else {
                Toast.makeText(
                    this@LoginActivity,
                    "Please enter email and password",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.signupTextView.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignupActivity::class.java))
            finish()
        }

        binding.passwordToggle.setOnClickListener {
            togglePasswordVisibility()
        }
    }

    private fun setupObservers() {
        userViewModel.userMessage.observe(this) { success ->
            if (success) {
                Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
                finish()
            } else {
                Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT).show()
            }
        }
//
//        userViewModel.user.observe(this) { user ->
//            user?.let {
//                startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
//                finish()
//            } ?: run {
//                Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT).show()
//            }
//        }
    }

    private fun togglePasswordVisibility() {
        val currentInputType = binding.passwordEditText.inputType
        binding.passwordEditText.inputType =
            if (currentInputType == android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
            } else {
                android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            }

        binding.passwordToggle.setImageResource(
            if (currentInputType == android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                R.drawable.ic_eye_off
            } else {
                R.drawable.ic_eye
            }
        )

        binding.passwordEditText.setSelection(binding.passwordEditText.text.length)
    }
}
