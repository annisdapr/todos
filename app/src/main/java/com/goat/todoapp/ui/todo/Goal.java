package com.goat.todoapp.ui.todo; // Atau package lain yang sesuai

import java.io.Serializable;
import java.util.Set;

public class Goal implements Serializable {

    private long id;
    private String title;
    private String colorHex;
    private long dailyTargetMillis;

    private long startDate;
    private long endDate;
    private boolean isOngoing;

    private Set<Integer> activeDays;

    private String group;

    public Goal(String title, String colorHex, long dailyTargetMillis, long startDate, long endDate, boolean isOngoing, Set<Integer> activeDays, String group) {
        this.id = System.currentTimeMillis();
        this.title = title;
        this.colorHex = colorHex;
        this.dailyTargetMillis = dailyTargetMillis;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isOngoing = isOngoing;
        this.activeDays = activeDays;
        this.group = group;
    }

    // --- Getter dan Setter ---

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    // Title
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    // Color
    public String getColorHex() { return colorHex; }
    public void setColorHex(String colorHex) { this.colorHex = colorHex; }

    // Daily Target
    public long getDailyTargetMillis() { return dailyTargetMillis; }
    public void setDailyTargetMillis(long dailyTargetMillis) { this.dailyTargetMillis = dailyTargetMillis; }

    // Start Date
    public long getStartDate() { return startDate; }
    public void setStartDate(long startDate) { this.startDate = startDate; }

    // End Date
    public long getEndDate() { return endDate; }
    public void setEndDate(long endDate) { this.endDate = endDate; }

    // Ongoing Status
    public boolean isOngoing() { return isOngoing; }
    public void setOngoing(boolean ongoing) { isOngoing = ongoing; }

    // Active Days
    public Set<Integer> getActiveDays() { return activeDays; }
    public void setActiveDays(Set<Integer> activeDays) { this.activeDays = activeDays; }

    // Group
    public String getGroup() { return group; }
    public void setGroup(String group) { this.group = group; }
}