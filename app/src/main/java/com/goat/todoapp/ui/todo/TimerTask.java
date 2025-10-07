package com.goat.todoapp.ui.todo;

import java.io.Serializable;

public class TimerTask extends TaskItem implements Serializable { // Implement Serializable
    // Status Constants
    public static final int STATUS_PAUSED = 0;
    public static final int STATUS_RUNNING = 1;
    public static final int STATUS_FINISHED = 2;

    private long id;
    private String title;
    private long totalDurationMillis; // Durasi total
    private long timeLeftMillis;      // Sisa waktu
    private int status;

    // Constructor untuk task baru
    public TimerTask(String title, long totalDurationMillis) {
        this.id = System.currentTimeMillis(); // ID unik sederhana
        this.title = title;
        this.totalDurationMillis = totalDurationMillis;
        this.timeLeftMillis = totalDurationMillis;
        this.status = STATUS_PAUSED; // Status awal adalah pause
    }

    // --- Getter dan Setter ---
    public long getId() { return id; }
    public String getTitle() { return title; }
    public long getTotalDurationMillis() { return totalDurationMillis; }
    public long getTimeLeftMillis() { return timeLeftMillis; }
    public int getStatus() { return status; }

    public void setTimeLeftMillis(long timeLeftMillis) { this.timeLeftMillis = timeLeftMillis; }
    public void setStatus(int status) { this.status = status; }

    @Override
    public int getViewType() {
        return TaskItem.TYPE_TIMER;
    }
}