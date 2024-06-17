package com.example.todoapp.utils

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ClearAll {
    // Initialize Firebase (replace with your project's configuration)
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference.child("profile_pictures")
    val firebaseAuth = FirebaseAuth.getInstance()

    // Function to delete all data in Realtime Database (careful!)
    fun deleteAllFromDatabase() {
        val databaseReference = firebaseDatabase.reference
        databaseReference.setValue(null) // This deletes everything under the reference
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Database", "Data deleted successfully")
                } else {
                    Log.w("Database", "Error deleting data", task.exception)
                }
            }
    }

    // Function to delete all files in Cloud Storage (careful!)
    fun deleteAllFromStorage() {
        storageRef.listAll().addOnSuccessListener { result ->
            for (item in result.items) {
                item.delete().addOnSuccessListener {
                    // File deleted successfully
                    Log.d("TAG", "File deleted successfully!")
                }.addOnFailureListener { exception ->
                    // Uh oh, an error occurred!
                    Log.w("TAG", "Error deleting file", exception)
                }
            }
        }.addOnFailureListener { exception ->
            // Uh oh, an error occurred!
            Log.w("TAG", "Error listing files", exception)
        }
    }

    // Function to delete all users in Authentication (careful!)
    fun deleteUser() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            currentUser.delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("Auth", "User deleted successfully")
                    } else {
                        Log.w("Auth", "Error deleting user", task.exception)
                    }
                }
        } else {
            Log.w("Auth", "No user signed in")
        }
    }
}