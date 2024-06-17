package com.example.todoapp.repository

import androidx.lifecycle.LiveData
import com.example.todoapp.model.User

interface UserRepository {
    suspend fun signUpUser(email: String, password: String, user: User): Result<Unit>
    suspend fun logInUser(email: String, password: String): Boolean
    fun addUserInRealtimeDatabase(userId: String?, user: User)
    fun getLoggedInUser(): LiveData<User?>
    fun logOut()
}
