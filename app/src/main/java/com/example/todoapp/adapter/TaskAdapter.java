package com.example.todoapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.R;
import com.example.todoapp.model.Task;
import com.example.todoapp.viewmodel.TaskViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> tasks;
    private TaskViewModel taskViewModel;
    private SimpleDateFormat dateFormat;

    public TaskAdapter(List<Task> tasks, TaskViewModel taskViewModel) {
        this.tasks = tasks;
        this.taskViewModel = taskViewModel;
        this.dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.titleTextView.setText(task.getName());
        holder.descriptionTextView.setText(task.getMessage());

        Date dueDate = null;
        try {
            dueDate = task.getDueDate(); // Ensure this returns a Date object
        } catch (ClassCastException e) {
            // If dueDate is not a Date object, handle the error or convert appropriately
            // Example: If getDueDate() returns a String, parse it to Date
            try {
                dueDate = dateFormat.parse(task.getDueDate().toString());
            } catch (ParseException pe) {
                pe.printStackTrace();
            }
        }

        if (dueDate != null) {
            String formattedDueDate = dateFormat.format(dueDate);
            holder.dueDateTextView.setText("Due Date: " + formattedDueDate);
        } else {
            holder.dueDateTextView.setText("Due Date: N/A");
        }

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskViewModel.deleteTask(task.getId()).observe((LifecycleOwner) holder.itemView.getContext(), success -> {
                    if (success) {
                        tasks.remove(position);
                        notifyItemRemoved(position);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;
        TextView dueDateTextView;
        ImageButton deleteButton;

        public TaskViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.task_title);
            descriptionTextView = itemView.findViewById(R.id.task_Desc);
            dueDateTextView = itemView.findViewById(R.id.due_date);
            deleteButton = itemView.findViewById(R.id.my_image_button);
        }
    }
}
