package com.goat.todoapp.ui.stats;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.goat.todoapp.R;
import com.goat.todoapp.ui.todo.Goal;
import com.goat.todoapp.ui.todo.GoalWithProgress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class StatsGoalPagerAdapter extends RecyclerView.Adapter<StatsGoalPagerAdapter.ViewHolder> {

    private List<GoalWithProgress> goals = new ArrayList<>();

    public void submitList(List<GoalWithProgress> newGoals) {
        this.goals = newGoals;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stats_goal_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GoalWithProgress goalWithProgress = goals.get(position);
        holder.bind(goalWithProgress);
    }

    @Override
    public int getItemCount() {
        return goals.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvGoalTime, tvSessions, tvAchievement;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_goal_card_title);
            tvGoalTime = itemView.findViewById(R.id.tv_goal_card_time);
            tvSessions = itemView.findViewById(R.id.tv_goal_card_sessions);
            tvAchievement = itemView.findViewById(R.id.tv_goal_card_achievement);
        }

        void bind(GoalWithProgress goalWithProgress) {
            Goal goal = goalWithProgress.goal;
            long hours = TimeUnit.MILLISECONDS.toHours(goal.getDailyTargetMillis());
            long minutes = TimeUnit.MILLISECONDS.toMinutes(goal.getDailyTargetMillis()) % 60;

            tvTitle.setText(goal.getTitle());
            tvGoalTime.setText(String.format("%02dh %02dm", hours, minutes));
            tvAchievement.setText(goalWithProgress.progressPercentage + "%");
            tvSessions.setText(String.valueOf(goalWithProgress.sessionCount));
        }
    }
}