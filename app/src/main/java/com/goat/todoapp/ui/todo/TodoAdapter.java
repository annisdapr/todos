package com.goat.todoapp.ui.todo;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.goat.todoapp.R;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TodoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TaskItem> taskList = new ArrayList<>();

    // ... (getItemViewType, onCreateViewHolder, onBindViewHolder, getItemCount, setTasks tetap sama) ...
    @Override
    public int getItemViewType(int position) {
        return taskList.get(position).getViewType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TaskItem.TYPE_TODO) {
            View view = inflater.inflate(R.layout.item_todo, parent, false);
            return new TodoViewHolder(view); // Listener akan dibuat di dalam constructor ini
        } else {
            View view = inflater.inflate(R.layout.item_timer_task, parent, false);
            return new TimerTaskViewHolder(view); // Listener akan dibuat di dalam constructor ini
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TaskItem.TYPE_TODO) {
            ((TodoViewHolder) holder).bind((Todo) taskList.get(position));
        } else {
            ((TimerTaskViewHolder) holder).bind((TimerTask) taskList.get(position));
        }
    }

    @Override
    public int getItemCount() { return taskList.size(); }

    public void setTasks(List<TaskItem> tasks) {
        this.taskList = tasks;
        notifyDataSetChanged();
    }


    // ViewHolder untuk To-Do
    class TodoViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkboxTodo;
        TextView tvTodoText;

        TodoViewHolder(@NonNull View itemView) {
            super(itemView);
            checkboxTodo = itemView.findViewById(R.id.checkboxTodo);
            tvTodoText = itemView.findViewById(R.id.tvTodoText);

            // === LISTENER DIPINDAH KE SINI ===
            checkboxTodo.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Todo todo = (Todo) taskList.get(position);
                    todo.setDone(isChecked);
                    updateStrikeThrough(isChecked);
                }
            });

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Todo todo = (Todo) taskList.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("todoItem", todo);
                    Navigation.findNavController(v).navigate(R.id.action_nav_todo_fragment_to_addTodoFragment, bundle);
                }
            });
        }

        void bind(final Todo todo) {
            tvTodoText.setText(todo.getTitle());
            checkboxTodo.setChecked(todo.isDone());
            updateStrikeThrough(todo.isDone());
        }

        private void updateStrikeThrough(boolean isDone) {
            if (isDone) {
                tvTodoText.setPaintFlags(tvTodoText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                tvTodoText.setPaintFlags(tvTodoText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
        }
    }

    // ViewHolder untuk Timer Task
    class TimerTaskViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvDuration;

        TimerTaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTaskTitle);
            tvDuration = itemView.findViewById(R.id.tvTaskDuration);

            // === LISTENER DIPINDAH KE SINI ===
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    TimerTask task = (TimerTask) taskList.get(position);
                    if (task.getStatus() != TimerTask.STATUS_FINISHED) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("timerTask", task);
                        Navigation.findNavController(v).navigate(R.id.action_nav_todo_fragment_to_addTimerFragment, bundle);
                    } else {
                        Toast.makeText(v.getContext(), "Sesi fokus ini sudah selesai.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        void bind(final TimerTask task) {
            tvTitle.setText(task.getTitle());
            String statusText;
            if (task.getStatus() == TimerTask.STATUS_FINISHED) {
                long totalMinutes = TimeUnit.MILLISECONDS.toMinutes(task.getTotalDurationMillis());
                statusText = String.format("Selesai (%d menit)", totalMinutes);
            } else {
                long remainingMinutes = TimeUnit.MILLISECONDS.toMinutes(task.getTimeLeftMillis());
                statusText = String.format("Sisa: %d menit", remainingMinutes);
            }
            tvDuration.setText(statusText);
        }
    }
}