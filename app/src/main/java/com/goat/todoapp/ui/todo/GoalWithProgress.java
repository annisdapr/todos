package com.goat.todoapp.ui.todo;

import java.io.Serializable;

public class GoalWithProgress implements Serializable {

    public final Goal goal;
    public final long currentProgressMillis;
    public final int progressPercentage;
    public final int sessionCount;

    public GoalWithProgress(Goal goal, long currentProgressMillis, int progressPercentage, int sessionCount) { // PERBARUI CONSTRUCTOR
        this.goal = goal;
        this.currentProgressMillis = currentProgressMillis;
        this.progressPercentage = progressPercentage;
        this.sessionCount = sessionCount;
    }
}
