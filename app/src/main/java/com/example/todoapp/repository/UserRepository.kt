package com.example.todoapp.repository

import androidx.lifecycle.LiveData
import com.example.todoapp.model.User

interface UserRepository {
    suspend fun signUpUser(email: String, password: String, user: User): Result<Unit>
    suspend fun logInUser(email: String, password: String): Result<Unit>
    fun addUserInRealtimeDatabase(userId: String?, user: User)
    fun getLoggedInUser(userId: String): LiveData<User?>
    suspend fun resetPassword(email: String): Result<Unit>


    fun logOut()


}
