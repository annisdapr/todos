package com.goat.todoapp.ui.todo;

public class Todo {
    private String title;
    private String description;
    private String date;
    private String time;
    private String imageUri;
    private boolean isDone; // ✅ tambahan

    public Todo(String title, String description, String date, String time, String imageUri) {
        this(title, description, date, time, imageUri, false);
    }

    public Todo(String title, String description, String date, String time, String imageUri, boolean isDone) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.imageUri = imageUri;
        this.isDone = isDone;
    }

    public boolean isDone() { return isDone; }
    public void setDone(boolean done) { isDone = done; }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getImageUri() { return imageUri; }
}

