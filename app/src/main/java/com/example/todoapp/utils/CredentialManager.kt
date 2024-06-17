package com.example.todoapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.todoapp.model.User
import com.google.gson.Gson

class CredentialManager(context: Context) {

    private val credentialsPref: SharedPreferences = context.getSharedPreferences(
        "CREDENTIALS",
        Context.MODE_PRIVATE
    )

    fun checkIfLoggedIn(): Boolean {
        return credentialsPref.getBoolean("isLoggedIn", false)
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        credentialsPref.edit().putBoolean("isLoggedIn", isLoggedIn).apply()
    }
}




//    private val userPrefs: SharedPreferences = context.getSharedPreferences(
//        "USER_PREFS",
//        Context.MODE_PRIVATE
//    )
//    private val gson = Gson()
//
//    fun saveUserData(user: User) {
//        val userJson = gson.toJson(user)
//        userPrefs.edit().putString("userData", userJson).apply()
//    }
//
//    fun getSavedUserData(): User? {
//        val userJson = userPrefs.getString("userData", null)
//        return userJson?.let { gson.fromJson(it, User::class.java) }
//    }
//
//    fun clearUserData() {
//        userPrefs.edit().remove("userData").apply()
//    }
