package com.goat.todoapp.ui.todo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class TodoViewModel extends ViewModel {

    private final LiveData<List<Todo>> todosLiveData;

    public TodoViewModel() {
        // ambil data dari repository
        todosLiveData = TodoRepository.getTodosLiveData();
    }

    public LiveData<List<Todo>> getTodosLiveData() {
        return todosLiveData;
    }

    public void addTodo(Todo todo) {
        TodoRepository.addTodo(todo);
    }

    public List<Todo> getTodos() {
        return TodoRepository.getTodos();
    }
}
