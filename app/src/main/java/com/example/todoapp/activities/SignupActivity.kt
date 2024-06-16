package com.example.todoapp.activities

import UserViewModel
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.todoapp.R
import com.example.todoapp.databinding.ActivitySignupBinding
import com.example.todoapp.model.User
import com.squareup.picasso.Picasso

class SignupActivity : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModels()
    private lateinit var binding: ActivitySignupBinding

    private var profileImageUri: Uri? = null
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.selectImageButton.setOnClickListener {
            Log.d("SignUp Activity", "Opening Gallery")
            Toast.makeText(this, "Opening Gallery", Toast.LENGTH_SHORT).show()
            openGallery()
        }

        binding.signInTextView.setOnClickListener {
            Log.d("SignUp Activity", "Starting Login Activity")
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val user = User(
                uid = "", // ID will be set by Firebase Auth
                name = name,
                email = email,
                profilePictureUrl = profileImageUri.toString()
            )

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                Log.d("SignUp Activity", "Fields are empty")
            }
            else if (email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Invalid email format or Email is empty", Toast.LENGTH_SHORT)
                    .show()
                Log.d("SignUp Activity", "Email Empty or Invalid")
            } else if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT)
                    .show()
                Log.d("SignUp Activity", "Password too short")
            } else if (profileImageUri == null) {
                Toast.makeText(this, "Please select a profile picture", Toast.LENGTH_SHORT).show()
                Log.d("SignUp Activity", "Image not selected")
            } else {
                userViewModel.signUpUser(email, password, user)
//                addUserProfilePicture(user.uid, profileImageUri)
                Toast.makeText(this, "SignUp Successful", Toast.LENGTH_SHORT).show()
                Log.d("SignUp Activity", "Signed User $user")
            }
        }

        binding.passwordToggle.setOnClickListener {
            togglePasswordVisibility()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryLauncher.launch(intent)
    }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                profileImageUri = data?.data
                profileImageUri?.let {
                    Picasso.get().load(it).into(binding.profileImageView)
                }
            }
        }

    private fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            binding.passwordEditText.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            binding.passwordToggle.setImageResource(R.drawable.ic_eye_off)
        } else {
            binding.passwordEditText.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            binding.passwordToggle.setImageResource(R.drawable.ic_eye)
        }
        binding.passwordEditText.setSelection(binding.passwordEditText.text.length) // Move cursor to the end
        isPasswordVisible = !isPasswordVisible
    }
}
