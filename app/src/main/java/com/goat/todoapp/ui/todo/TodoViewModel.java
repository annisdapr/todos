package com.goat.todoapp.ui.todo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;

public class TodoViewModel extends ViewModel {
    // Hanya ada satu LiveData untuk semua jenis tugas
    private final LiveData<List<TaskItem>> tasksLiveData;

    public TodoViewModel() {
        // Mengambil satu LiveData gabungan dari repository
        tasksLiveData = TodoRepository.getTasksLiveData();
    }

    // Getter untuk LiveData gabungan
    public LiveData<List<TaskItem>> getTasksLiveData() {
        return tasksLiveData;
    }

    // Method untuk menambah To-Do
    public void addTodo(Todo todo) {
        TodoRepository.addTodo(todo);
    }
    public void updateTimerTask(TimerTask timerTask) {
        TodoRepository.updateTimerTask(timerTask);
    }
    // Method untuk menambah Timer Task
    public void addTimerTask(TimerTask timerTask) {
        TodoRepository.addTimerTask(timerTask);
    }
}