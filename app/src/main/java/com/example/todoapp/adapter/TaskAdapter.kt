package com.example.todoapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.model.Task

class TaskAdapter(private val taskList: List<Task>, private val onDeleteClickListener: (Task) -> Unit) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentItem = taskList[position]

        holder.textTaskName.text = currentItem.name
        holder.textDescription.text = currentItem.message
        holder.textCreatedAt.text = currentItem.createdDate

        holder.buttonDelete.setOnClickListener {
            onDeleteClickListener.invoke(currentItem)
        }
    }

    override fun getItemCount() = taskList.size

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textTaskName: TextView = itemView.findViewById(R.id.task_title)
        val textDescription: TextView = itemView.findViewById(R.id.task_Desc)
        val textCreatedAt: TextView = itemView.findViewById(R.id.created_date)
        val buttonDelete: AppCompatImageButton = itemView.findViewById(R.id.delete_button)
    }
}
