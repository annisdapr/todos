package com.goat.todoapp.ui.todo;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goat.todoapp.R;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private final List<Todo> todoList;

    public TodoAdapter(List<Todo> todoList) {
        this.todoList = todoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_todo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Todo todo = todoList.get(position);

        holder.tvTodoText.setText(todo.getTitle());
        holder.checkboxTodo.setChecked(todo.isDone());

        // Tambahkan efek coret jika todo sudah selesai
        if (todo.isDone()) {
            holder.tvTodoText.setPaintFlags(holder.tvTodoText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.tvTodoText.setPaintFlags(holder.tvTodoText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        // Listener ketika checkbox diubah
        holder.checkboxTodo.setOnCheckedChangeListener((buttonView, isChecked) -> {
            todo.setDone(isChecked);

            // Update tampilan teks biar real-time coret/tidak coret
            if (isChecked) {
                holder.tvTodoText.setPaintFlags(holder.tvTodoText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder.tvTodoText.setPaintFlags(holder.tvTodoText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
        });
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkboxTodo;
        TextView tvTodoText;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkboxTodo = itemView.findViewById(R.id.checkboxTodo);
            tvTodoText = itemView.findViewById(R.id.tvTodoText);
        }
    }

    public void setTodos(List<Todo> newTodos) {
        todoList.clear();
        todoList.addAll(newTodos);
        notifyDataSetChanged();
    }
}
