package com.example.todoapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.model.Task

class TaskAdapter(private val taskList: List<Task>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    // ViewHolder for each item
    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textTaskName: TextView = itemView.findViewById(R.id.task_title)
        val textDescription: TextView = itemView.findViewById(R.id.task_Desc)
        val textCreatedAt: TextView = itemView.findViewById(R.id.created_date)
        val buttonDelete: Button = itemView.findViewById(R.id.delete_button)
    }

    // Create ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(itemView)
    }

    // Bind data to ViewHolder
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentItem = taskList[position]

        holder.textTaskName.text = currentItem.name
        holder.textDescription.text = currentItem.message
        holder.textCreatedAt.text = currentItem.createdDate

        holder.buttonDelete.setOnClickListener {
            // Handle delete button click here
            // You can implement deletion logic or notify listener
        }
    }

    // Return the size of your dataset
    override fun getItemCount() = taskList.size
}
