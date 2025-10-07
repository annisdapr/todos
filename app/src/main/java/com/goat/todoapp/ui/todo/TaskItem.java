package com.goat.todoapp.ui.todo;

public abstract class TaskItem {
    public static final int TYPE_TODO = 0;
    public static final int TYPE_TIMER = 1;

    abstract public int getViewType();
}
