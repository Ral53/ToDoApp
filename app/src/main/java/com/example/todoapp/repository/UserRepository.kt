package com.example.todoapp.repository

import com.example.todoapp.model.User

interface UserRepository {
    fun signUp(email: String, password: String, user: User, callback: (Boolean, String?) -> Unit)
    fun login(email: String, password: String, callback: (Boolean, String?) -> Unit)
    fun addUser(user: User)
    fun getUser(userId: String, callback: (User?) -> Unit)
    fun updateUser(user: User)
}