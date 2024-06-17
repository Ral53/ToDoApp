package com.example.todoapp.activities

import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class ActivityDashboardBinding : ViewBinding {
    lateinit var taskList: RecyclerView
    lateinit var profileButton: Button
    lateinit var newTaskButton: Button
}