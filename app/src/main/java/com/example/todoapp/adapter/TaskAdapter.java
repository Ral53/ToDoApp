package com.example.todoapp.adapter;

import android.util.Log;
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
import java.text.SimpleDateFormat;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> tasks;
    private TaskViewModel taskViewModel;

    public TaskAdapter(List<Task> tasks, TaskViewModel taskViewModel) {
        this.tasks = tasks;
        this.taskViewModel = taskViewModel;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Task task = tasks.get(holder.getAdapterPosition());
        Log.d("TaskAdapter", "Binding task: " + task);
        holder.titleTextView.setText(task.getName());
        holder.descriptionTextView.setText(task.getMessage());

        // Format due date if it exists and is a Date object
        if (task.getDueDate() != null) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                String formattedDueDate = dateFormat.format(task.getDueDate());
                holder.dueDateTextView.setText("Due Date: " + formattedDueDate);
            } catch (Exception e) {
                holder.dueDateTextView.setText("Due Date: Invalid date");
            }
        } else {
            holder.dueDateTextView.setText("Due Date: Not set");
        }

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    taskViewModel.deleteTask(task.getId()).observe((LifecycleOwner) holder.itemView.getContext(), success -> {
                        if (success) {
                            tasks.remove(adapterPosition);
                            notifyItemRemoved(adapterPosition);
                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void updateTasks(List<Task> newTasks) {
        this.tasks = newTasks;
        notifyDataSetChanged();
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
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}
