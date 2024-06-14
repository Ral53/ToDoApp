package com.example.todoapp.repository

import com.example.todoapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UserRepositoryImpl : UserRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun signUp(email: String, password: String, user: User, callback: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                user.id = auth.currentUser?.uid ?: ""
                addUser(user)
                callback(true, null)
            } else {
                callback(false, task.exception?.message)
            }
        }
    }

    override fun login(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(true, null)
            } else {
                callback(false, task.exception?.message)
            }
        }
    }

    override fun addUser(user: User) {
        database.child("users").child(user.id).setValue(user)
    }

    override fun getUser(userId: String, callback: (User?) -> Unit) {
        database.child("users").child(userId).get().addOnSuccessListener {
            callback(it.getValue(User::class.java))
        }.addOnFailureListener {
            callback(null)
        }
    }

    override fun updateUser(user: User) {
        database.child("users").child(user.id).setValue(user)
    }
}