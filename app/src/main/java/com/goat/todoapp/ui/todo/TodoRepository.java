package com.goat.todoapp.ui.todo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class TodoRepository {
    private static final List<Todo> todoList = new ArrayList<>();
    private static final MutableLiveData<List<Todo>> todosLiveData = new MutableLiveData<>();

    static {
        // inisialisasi LiveData dengan list kosong
        todosLiveData.setValue(new ArrayList<>(todoList));
    }

    public static void addTodo(Todo todo) {
        todoList.add(todo);
        // update LiveData biar observer dapet data terbaru
        todosLiveData.postValue(new ArrayList<>(todoList));
    }

    public static List<Todo> getTodos() {
        return new ArrayList<>(todoList);
    }

    public static LiveData<List<Todo>> getTodosLiveData() {
        return todosLiveData;
    }
}
