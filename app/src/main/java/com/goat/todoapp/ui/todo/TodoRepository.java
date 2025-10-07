package com.goat.todoapp.ui.todo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.ArrayList;
import java.util.List;

public class TodoRepository {
    private static final List<TaskItem> taskList = new ArrayList<>();
    private static final MutableLiveData<List<TaskItem>> tasksLiveData = new MutableLiveData<>();

    // Inisialisasi hanya satu LiveData
    static {
        tasksLiveData.setValue(new ArrayList<>(taskList));
    }

    // Method addTodo sekarang menambahkan ke list utama
    public static void addTodo(Todo todo) {
        taskList.add(todo);
        tasksLiveData.postValue(new ArrayList<>(taskList));
    }

    // Method addTimerTask sekarang menambahkan ke list utama
    public static void addTimerTask(TimerTask timerTask) {
        taskList.add(timerTask);
        tasksLiveData.postValue(new ArrayList<>(taskList));
    }
    public static void updateTimerTask(TimerTask updatedTask) {
        for (int i = 0; i < taskList.size(); i++) {
            TaskItem item = taskList.get(i);
            if (item instanceof TimerTask && ((TimerTask) item).getId() == updatedTask.getId()) {
                taskList.set(i, updatedTask);
                break;
            }
        }
        tasksLiveData.postValue(new ArrayList<>(taskList));
    }

    // Getter untuk LiveData gabungan
    public static LiveData<List<TaskItem>> getTasksLiveData() {
        return tasksLiveData;
    }
}