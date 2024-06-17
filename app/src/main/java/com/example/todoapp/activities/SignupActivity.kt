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
import com.example.todoapp.utils.CredentialManager
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class SignupActivity : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModels()
    private lateinit var binding: ActivitySignupBinding
    private lateinit var credentialManager: CredentialManager // Declare lateinit

    private var profileImageUri: Uri? = null
    private var isPasswordVisible = false

    private val firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
    private val storageReference: StorageReference = firebaseStorage.reference.child("profile_pictures")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        credentialManager = CredentialManager(this)

        binding.selectImageButton.setOnClickListener {
            Log.d("SignUp Activity", "Opening Gallery")
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

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
            } else if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            } else if (profileImageUri == null) {
                Toast.makeText(this, "Please select a profile picture", Toast.LENGTH_SHORT).show()
            } else {
                uploadProfileImageAndSignUp(name, email, password)
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                credentialManager.setLoggedIn(false)
                Toast.makeText(this, "Please Login!!", Toast.LENGTH_LONG).show()
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

    private fun uploadProfileImageAndSignUp(name: String, email: String, password: String) {
        profileImageUri?.let { uri ->
            val fileName = "${System.currentTimeMillis()}.jpg"
            val fileRef = storageReference.child(fileName)
            fileRef.putFile(uri)
                .addOnSuccessListener {
                    fileRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                        val user = User(
                            uid = "",
                            name = name,
                            email = email,
                            profilePictureUrl = downloadUrl.toString()
                        )
                        userViewModel.signUpUser(email, password, user)
                        Toast.makeText(this, "SignUp Successful", Toast.LENGTH_SHORT).show()
                        Log.d("SignUp Activity", "Signed User $user")
                    }.addOnFailureListener {
                        Log.d("SignUp Activity", "Failed to get download URL")
                    }
                }
                .addOnFailureListener {
                    Log.d("SignUp Activity", "Failed to upload image")
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
