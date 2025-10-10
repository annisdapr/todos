package com.goat.todoapp.ui.todo;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Spannable;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.goat.todoapp.R;

import java.util.concurrent.TimeUnit;

public class AddTimerFragment extends Fragment {

    private TextView tvGoalTitle, tvTimerDisplay, tvSessionCount, tvFocusTime, tvSuccessRate;
    private ProgressBar progressBarTimer;
    private ImageButton btnPlayPause, btnReset, btnBold, btnItalic, btnFinishSession;
    private EditText etNotes;

    private GoalViewModel viewModel;
    private Goal currentGoal;

    private Handler timerHandler = new Handler(Looper.getMainLooper());
    private boolean isTimerRunning = false;
    private long startTime = 0L;
    private long timeInMillis = 0L;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_timer, container, false);

        initViews(view);

        viewModel = new ViewModelProvider(requireActivity()).get(GoalViewModel.class);

        if (getArguments() != null && getArguments().containsKey("goal")) {
            currentGoal = (Goal) getArguments().getSerializable("goal");
            tvGoalTitle.setText(currentGoal.getTitle());

            viewModel.getTimerTasksLiveData().observe(getViewLifecycleOwner(), sessions -> {
                if (sessions == null) return;

                long totalFocusMillis = 0;
                int sessionCount = 0;

                for(TimerTask session : sessions){
                    if(session.getTitle().equals(currentGoal.getTitle())){
                        totalFocusMillis += session.getTotalDurationMillis();
                        sessionCount++;
                    }
                }

                long hours = TimeUnit.MILLISECONDS.toHours(totalFocusMillis);
                long minutes = TimeUnit.MILLISECONDS.toMinutes(totalFocusMillis) % 60;

                tvSessionCount.setText(String.valueOf(sessionCount));
                tvFocusTime.setText(String.format("%dh %dm", hours, minutes));

                if(currentGoal.getDailyTargetMillis() > 0){
                    int successRate = (int) ((totalFocusMillis * 100) / currentGoal.getDailyTargetMillis());
                    tvSuccessRate.setText(successRate + "%");
                } else {
                    tvSuccessRate.setText("N/A");
                }
            });

        } else {
            tvGoalTitle.setText("General Focus");
        }

        setupListeners();

        return view;
    }

    private void initViews(View view) {
        tvGoalTitle = view.findViewById(R.id.tv_goal_title_timer);
        tvTimerDisplay = view.findViewById(R.id.tv_timer_display);
        progressBarTimer = view.findViewById(R.id.progress_bar_timer);
        btnPlayPause = view.findViewById(R.id.btn_play_pause);
        btnReset = view.findViewById(R.id.btn_reset);
        btnFinishSession = view.findViewById(R.id.btn_finish_session);
        etNotes = view.findViewById(R.id.et_notes);
        btnBold = view.findViewById(R.id.btn_bold);
        btnItalic = view.findViewById(R.id.btn_italic);

        tvSessionCount = view.findViewById(R.id.tv_session_count);
        tvFocusTime = view.findViewById(R.id.tv_focus_time);
        tvSuccessRate = view.findViewById(R.id.tv_success_rate);
    }

    private void setupListeners() {
        btnPlayPause.setOnClickListener(v -> {
            if (isTimerRunning) {
                pauseTimer();
            } else {
                startTimer();
            }
        });

        btnFinishSession.setOnClickListener(v -> finishSession());

        btnBold.setOnClickListener(v -> applyStyle(Typeface.BOLD));
        btnItalic.setOnClickListener(v -> applyStyle(Typeface.ITALIC));
    }

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            timeInMillis = System.currentTimeMillis() - startTime;
            updateTimerDisplay();
            timerHandler.postDelayed(this, 1000);
        }
    };

    private void startTimer() {
        startTime = System.currentTimeMillis() - timeInMillis;
        timerHandler.postDelayed(timerRunnable, 0);
        isTimerRunning = true;
        btnPlayPause.setImageResource(R.drawable.ic_pause);
        btnFinishSession.setVisibility(View.VISIBLE);
    }

    private void pauseTimer() {
        timerHandler.removeCallbacks(timerRunnable);
        isTimerRunning = false;
        btnPlayPause.setImageResource(R.drawable.ic_play);
    }

    private void finishSession() {
        pauseTimer();
        String title = (currentGoal != null) ? currentGoal.getTitle() : "General Focus";

        TimerTask completedSession = new TimerTask(title, timeInMillis);
        completedSession.setStatus(TimerTask.STATUS_FINISHED);

        viewModel.addTimerTask(completedSession);

        Toast.makeText(getContext(), "Sesi '" + title + "' disimpan!", Toast.LENGTH_LONG).show();
        Navigation.findNavController(getView()).popBackStack();
    }

    private void updateTimerDisplay() {
        int seconds = (int) (timeInMillis / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        int hours = minutes / 60;
        minutes = minutes % 60;

        tvTimerDisplay.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));

        if (currentGoal != null) {
            int progress = (int) (timeInMillis * 100 / currentGoal.getDailyTargetMillis());
            progressBarTimer.setProgress(Math.min(progress, 100));
        }
    }

    private void applyStyle(int style) {
        int start = etNotes.getSelectionStart();
        int end = etNotes.getSelectionEnd();
        Spannable spannable = etNotes.getText();
        spannable.setSpan(new StyleSpan(style), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isTimerRunning) {
            pauseTimer();
        }
    }
}