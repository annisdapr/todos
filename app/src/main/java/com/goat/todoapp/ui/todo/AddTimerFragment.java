package com.goat.todoapp.ui.todo;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.goat.todoapp.R;

public class AddTimerFragment extends Fragment {

    private EditText etMinutes, etTaskTitle;
    private Button btnStart, btnPauseSave;
    private TextView tvTimer;
    private ProgressBar progressBar;

    private CountDownTimer countDownTimer;
    private TodoViewModel viewModel;

    private TimerTask currentTask; // Untuk menyimpan task yang sedang aktif

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_timer, container, false);

        // Inisialisasi View
        etTaskTitle = view.findViewById(R.id.etTaskTitle);
        etMinutes = view.findViewById(R.id.etMinutes);
        btnStart = view.findViewById(R.id.btnStart);
        btnPauseSave = view.findViewById(R.id.btnPauseSave);
        tvTimer = view.findViewById(R.id.tvTimer);
        progressBar = view.findViewById(R.id.progressBar);

        // Inisialisasi ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(TodoViewModel.class);

        // Cek apakah ini mode 'lanjutkan' atau 'baru'
        if (getArguments() != null && getArguments().containsKey("timerTask")) {
            currentTask = (TimerTask) getArguments().getSerializable("timerTask");
            etTaskTitle.setText(currentTask.getTitle());
            etMinutes.setText(String.valueOf(currentTask.getTotalDurationMillis() / 60000));
            etTaskTitle.setEnabled(false);
            etMinutes.setEnabled(false);
        } else {
            currentTask = null; // Mode baru
        }

        updateUI();

        btnStart.setOnClickListener(v -> startOrResumeTimer());
        btnPauseSave.setOnClickListener(v -> pauseAndSaveTimer());

        return view;
    }

    private void startOrResumeTimer() {
        if (currentTask != null && currentTask.getStatus() == TimerTask.STATUS_RUNNING) return;

        long durationToRun;
        if (currentTask == null) { // Membuat timer baru
            String title = etTaskTitle.getText().toString().trim();
            String minutesStr = etMinutes.getText().toString().trim();
            if (title.isEmpty() || minutesStr.isEmpty()) {
                Toast.makeText(getContext(), "Judul dan menit harus diisi", Toast.LENGTH_SHORT).show();
                return;
            }
            long totalMillis = Long.parseLong(minutesStr) * 60000;
            currentTask = new TimerTask(title, totalMillis);
            viewModel.addTimerTask(currentTask); // Langsung simpan ke repository
            durationToRun = totalMillis;
        } else { // Melanjutkan timer yang ada
            durationToRun = currentTask.getTimeLeftMillis();
        }

        currentTask.setStatus(TimerTask.STATUS_RUNNING);
        viewModel.updateTimerTask(currentTask);
        updateUI();

        countDownTimer = new CountDownTimer(durationToRun, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                currentTask.setTimeLeftMillis(millisUntilFinished);
                updateTimerText();
            }

            @Override
            public void onFinish() {
                currentTask.setTimeLeftMillis(0);
                currentTask.setStatus(TimerTask.STATUS_FINISHED);
                viewModel.updateTimerTask(currentTask);
                updateUI();
                Toast.makeText(getContext(), "Sesi '" + currentTask.getTitle() + "' selesai!", Toast.LENGTH_LONG).show();
                Navigation.findNavController(getView()).popBackStack();
            }
        }.start();
    }

    private void pauseAndSaveTimer() {
        if (currentTask == null) { // Jika belum ada timer yg dimulai, anggap sbg save & exit
            Navigation.findNavController(getView()).popBackStack();
            return;
        }

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        currentTask.setStatus(TimerTask.STATUS_PAUSED);
        viewModel.updateTimerTask(currentTask);
        Toast.makeText(getContext(), "Timer disimpan", Toast.LENGTH_SHORT).show();
        Navigation.findNavController(getView()).popBackStack();
    }

    private void updateUI() {
        if (currentTask == null) { // Tampilan untuk timer baru
            btnPauseSave.setText("Pause&Save");
            return;
        }

        etTaskTitle.setText(currentTask.getTitle());
        etMinutes.setText(String.valueOf(currentTask.getTotalDurationMillis() / 60000));
        updateTimerText();

        boolean isRunning = currentTask.getStatus() == TimerTask.STATUS_RUNNING;
        btnStart.setEnabled(!isRunning);
        etTaskTitle.setEnabled(currentTask.getStatus() != TimerTask.STATUS_FINISHED);
        etMinutes.setEnabled(currentTask.getStatus() != TimerTask.STATUS_FINISHED);
    }

    private void updateTimerText() {
        long millis = (currentTask != null) ? currentTask.getTimeLeftMillis() : 0;
        long totalMillis = (currentTask != null) ? currentTask.getTotalDurationMillis() : 1;

        int minutes = (int) (millis / 1000) / 60;
        int seconds = (int) (millis / 1000) % 60;

        tvTimer.setText(String.format("%02d:%02d", minutes, seconds));
        progressBar.setMax((int) (totalMillis / 1000));
        progressBar.setProgress((int) (millis / 1000));
    }

    @Override
    public void onStop() {
        super.onStop();
        // Pastikan timer berhenti jika fragment ditutup paksa
        if (countDownTimer != null) {
            countDownTimer.cancel();
            if(currentTask != null && currentTask.getStatus() == TimerTask.STATUS_RUNNING){
                currentTask.setStatus(TimerTask.STATUS_PAUSED);
                viewModel.updateTimerTask(currentTask);
            }
        }
    }
}