package com.goat.todoapp.ui.todo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class GoalViewModel extends ViewModel {

    private final LiveData<List<Goal>> goalsSource;
    private final LiveData<List<TimerTask>> sessionsSource;
    private final MediatorLiveData<List<GoalWithProgress>> goalsWithProgressLiveData = new MediatorLiveData<>();

    public GoalViewModel() {
        goalsSource = GoalRepository.getGoalsLiveData();
        sessionsSource = GoalRepository.getTimerTasksLiveData();

        goalsWithProgressLiveData.addSource(goalsSource, goals -> {
            calculateProgress(goals, sessionsSource.getValue());
        });
        goalsWithProgressLiveData.addSource(sessionsSource, sessions -> {
            calculateProgress(goalsSource.getValue(), sessions);
        });
    }

    private void calculateProgress(List<Goal> goals, List<TimerTask> sessions) {
        if (goals == null || sessions == null) {
            goalsWithProgressLiveData.setValue(new ArrayList<>());
            return;
        }

        List<GoalWithProgress> resultList = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        int today = cal.get(Calendar.DAY_OF_YEAR);

        for (Goal goal : goals) {
            long totalProgressToday = 0;
            int sessionCount = 0; // Inisialisasi penghitung sesi

            for (TimerTask session : sessions) {
                if (Objects.equals(session.getTitle(), goal.getTitle())) {
                    cal.setTimeInMillis(session.getId());
                    if (cal.get(Calendar.DAY_OF_YEAR) == today) {
                        totalProgressToday += session.getTotalDurationMillis();
                        sessionCount++; // Tambahkan hitungan setiap sesi yang cocok
                    }
                }
            }

            int percentage = 0;
            if (goal.getDailyTargetMillis() > 0) {
                percentage = (int) ((totalProgressToday * 100) / goal.getDailyTargetMillis());
            }

            resultList.add(new GoalWithProgress(goal, totalProgressToday, percentage, sessionCount));
        }
        goalsWithProgressLiveData.setValue(resultList);
    }

    public LiveData<List<GoalWithProgress>> getGoalsWithProgressLiveData() {
        return goalsWithProgressLiveData;
    }

    public LiveData<List<TimerTask>> getTimerTasksLiveData() {
        return sessionsSource;
    }

    public void addGoal(Goal goal) {
        GoalRepository.addGoal(goal);
    }

    public void addTimerTask(TimerTask timerTask) {
        GoalRepository.addTimerTask(timerTask);
    }

    public void updateTimerTask(TimerTask timerTask) {
        GoalRepository.updateTimerTask(timerTask);
    }
}