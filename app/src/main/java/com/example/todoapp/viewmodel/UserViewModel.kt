package com.example.todoapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todoapp.model.User
import com.example.todoapp.repository.UserRepository
import com.example.todoapp.repository.UserRepositoryImpl

class UserViewModel : ViewModel() {
    private val repository: UserRepository = UserRepositoryImpl()
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user
    private val _authResult = MutableLiveData<Pair<Boolean, String?>>()
    val authResult: LiveData<Pair<Boolean, String?>> get() = _authResult

    fun signUp(email: String, password: String, user: User) {
        repository.signUp(email, password, user) { success, message ->
            _authResult.value = Pair(success, message)
        }
    }

    fun login(email: String, password: String) {
        repository.login(email, password) { success, message ->
            _authResult.value = Pair(success, message)
        }
    }

    fun loadUser(userId: String) {
        repository.getUser(userId) {
            _user.value = it
        }
    }

    fun updateUser(user: User) {
        repository.updateUser(user)
    }

    fun addUser(user: User) {
        repository.addUser(user)
    }
}
