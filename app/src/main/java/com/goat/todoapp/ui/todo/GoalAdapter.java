package com.goat.todoapp.ui.todo;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.goat.todoapp.R;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.ViewHolder> {

    private List<GoalWithProgress> goalList = new ArrayList<>();

    public void setGoals(List<GoalWithProgress> goals) {
        this.goalList = goals;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Kirim seluruh objek "paket" ke method bind
        GoalWithProgress goalWithProgress = goalList.get(position);
        holder.bind(goalWithProgress);
    }

    @Override
    public int getItemCount() {
        return goalList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View viewColorIndicator;
        TextView tvGoalTitle, tvGoalTarget, tvGoalProgressText;
        ProgressBar progressBarGoal;
        MaterialButton btnStartFocus;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            viewColorIndicator = itemView.findViewById(R.id.view_color_indicator);
            tvGoalTitle = itemView.findViewById(R.id.tv_goal_title);
            tvGoalTarget = itemView.findViewById(R.id.tv_goal_target);
            tvGoalProgressText = itemView.findViewById(R.id.tv_goal_progress_text);
            progressBarGoal = itemView.findViewById(R.id.progress_bar_goal);
            btnStartFocus = itemView.findViewById(R.id.btn_start_focus);
        }

        void bind(GoalWithProgress goalWithProgress) {
            Goal goal = goalWithProgress.goal;
            long currentProgressMillis = goalWithProgress.currentProgressMillis;
            int progressPercentage = goalWithProgress.progressPercentage;

            tvGoalTitle.setText(goal.getTitle());

            if (goal.getColorHex() != null && !goal.getColorHex().isEmpty()) {
                viewColorIndicator.setBackgroundColor(Color.parseColor(goal.getColorHex()));
            }

            long totalMinutes = TimeUnit.MILLISECONDS.toMinutes(goal.getDailyTargetMillis());
            tvGoalTarget.setText(String.format("Target: %d menit", totalMinutes));

            long currentProgressMinutes = TimeUnit.MILLISECONDS.toMinutes(currentProgressMillis);

            tvGoalProgressText.setText(String.format("%dmin / %dmin", currentProgressMinutes, totalMinutes));
            progressBarGoal.setProgress(progressPercentage);

            btnStartFocus.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putSerializable("goal", goal);
                Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_addTimerFragment, bundle);
            });
        }
    }
}