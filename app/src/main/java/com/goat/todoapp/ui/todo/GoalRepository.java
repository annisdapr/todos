package com.goat.todoapp.ui.todo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.ArrayList;
import java.util.List;

public class GoalRepository {

    private static final List<Goal> goalList = new ArrayList<>();
    private static final MutableLiveData<List<Goal>> goalsLiveData = new MutableLiveData<>();

    private static final List<TimerTask> timerTaskList = new ArrayList<>();
    private static final MutableLiveData<List<TimerTask>> timerTasksLiveData = new MutableLiveData<>();

    static {

        goalsLiveData.setValue(new ArrayList<>());
        timerTasksLiveData.setValue(new ArrayList<>());
    }


    public static void addGoal(Goal goal) {
        goalList.add(goal);
        goalsLiveData.postValue(new ArrayList<>(goalList));
    }

    public static LiveData<List<Goal>> getGoalsLiveData() {
        return goalsLiveData;
    }

    public static void updateGoal(Goal updatedGoal) {
        for (int i = 0; i < goalList.size(); i++) {
            if (goalList.get(i).getId() == updatedGoal.getId()) {
                goalList.set(i, updatedGoal);
                break;
            }
        }
        goalsLiveData.postValue(new ArrayList<>(goalList));
    }

    public static void addTimerTask(TimerTask timerTask) {
        timerTaskList.add(timerTask);
        timerTasksLiveData.postValue(new ArrayList<>(timerTaskList));
    }

    public static void updateTimerTask(TimerTask updatedTask) {
        for (int i = 0; i < timerTaskList.size(); i++) {
            if (timerTaskList.get(i).getId() == updatedTask.getId()) {
                timerTaskList.set(i, updatedTask);
                break;
            }
        }
        timerTasksLiveData.postValue(new ArrayList<>(timerTaskList));
    }

    public static LiveData<List<TimerTask>> getTimerTasksLiveData() {
        return timerTasksLiveData;
    }
}