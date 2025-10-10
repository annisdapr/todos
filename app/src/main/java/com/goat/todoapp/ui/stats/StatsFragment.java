package com.goat.todoapp.ui.stats;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.goat.todoapp.R;
import com.goat.todoapp.ui.todo.TimerTask;
import com.goat.todoapp.ui.todo.GoalViewModel;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class StatsFragment extends Fragment {

    private GoalViewModel viewModel;
    private MaterialButtonToggleGroup toggleGroup;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private BarChart barChart;
    private TextView tvAverageTime, tvMaximumTime;
    private StatsGoalPagerAdapter pagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setupChart();

        viewModel = new ViewModelProvider(requireActivity()).get(GoalViewModel.class);

        pagerAdapter = new StatsGoalPagerAdapter();
        viewPager.setAdapter(pagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {}).attach();

        // Observer 1: Untuk Pager/Carousel (sudah benar)
        viewModel.getGoalsWithProgressLiveData().observe(getViewLifecycleOwner(), goalsWithProgress -> {
            if (goalsWithProgress != null) {
                pagerAdapter.submitList(goalsWithProgress);
            }
        });

        // Observer 2: Untuk Chart dan Rangkuman Bawah (sudah benar)
        viewModel.getTimerTasksLiveData().observe(getViewLifecycleOwner(), sessions -> {
            if (sessions != null) {
                updateChartAndSummaries(sessions);
            }
        });

        toggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked && viewModel.getTimerTasksLiveData().getValue() != null) {
                updateChartAndSummaries(viewModel.getTimerTasksLiveData().getValue());
            }
        });
    }


    private void updateChartAndSummaries(List<TimerTask> sessions) {
        int checkedId = toggleGroup.getCheckedButtonId();
        Calendar cal = Calendar.getInstance();

        List<TimerTask> filteredSessions = new ArrayList<>();
        if (checkedId == R.id.btn_daily) {
            int today = cal.get(Calendar.DAY_OF_YEAR);
            for (TimerTask session : sessions) {
                cal.setTimeInMillis(session.getId());
                if (cal.get(Calendar.DAY_OF_YEAR) == today) {
                    filteredSessions.add(session);
                }
            }
            updateChartForDaily(filteredSessions);
        } else if (checkedId == R.id.btn_weekly) {
            updateChartForWeekly(sessions);
        } else {
            // TODO: Logika filter untuk Monthly
        }

        long totalMillis = 0;
        long maxMillis = 0;
        for (TimerTask session : filteredSessions) {
            totalMillis += session.getTotalDurationMillis();
            if (session.getTotalDurationMillis() > maxMillis) {
                maxMillis = session.getTotalDurationMillis();
            }
        }
        long avgMillis = filteredSessions.isEmpty() ? 0 : totalMillis / filteredSessions.size();
        tvAverageTime.setText(formatDuration(avgMillis));
        tvMaximumTime.setText(formatDuration(maxMillis));
    }


    private void updateChartForDaily(List<TimerTask> dailySessions) {
        Map<Integer, Long> hourlyData = new HashMap<>();
        for (int i = 0; i < 24; i++) hourlyData.put(i, 0L);

        Calendar cal = Calendar.getInstance();
        for (TimerTask session : dailySessions) {
            cal.setTimeInMillis(session.getId());
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            long currentDuration = hourlyData.getOrDefault(hour, 0L);
            hourlyData.put(hour, currentDuration + session.getTotalDurationMillis());
        }

        ArrayList<BarEntry> entries = new ArrayList<>();
        String[] labels = new String[24];
        for (int i = 0; i < 24; i++) {
            long minutes = TimeUnit.MILLISECONDS.toMinutes(hourlyData.get(i));
            entries.add(new BarEntry(i, minutes));
            labels[i] = String.valueOf(i);
        }

        setupAndShowChart(entries, labels);
    }

    private void updateChartForWeekly(List<TimerTask> allSessions) {
        // Logika sederhana untuk weekly
        Map<Integer, Long> weeklyData = new HashMap<>();
        for (int i = 1; i <= 7; i++) weeklyData.put(i, 0L); // 1=Sunday, 7=Saturday

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        long startOfWeek = cal.getTimeInMillis();

        for(TimerTask session : allSessions){
            if(session.getId() >= startOfWeek){
                cal.setTimeInMillis(session.getId());
                int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
                long currentDuration = weeklyData.getOrDefault(dayOfWeek, 0L);
                weeklyData.put(dayOfWeek, currentDuration + session.getTotalDurationMillis());
            }
        }

        ArrayList<BarEntry> entries = new ArrayList<>();
        String[] labels = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for(int i=0; i < 7; i++){
            long minutes = TimeUnit.MILLISECONDS.toMinutes(weeklyData.getOrDefault(i+1, 0L));
            entries.add(new BarEntry(i, minutes));
        }

        setupAndShowChart(entries, labels);
    }
    private void setupChart() {
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.setDrawValueAboveBar(true);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisLeft().setAxisMinimum(0f);
        barChart.getAxisRight().setEnabled(false);
    }

    private void setupAndShowChart(ArrayList<BarEntry> entries, String[] labels) {
        BarDataSet dataSet = new BarDataSet(entries, "Focus Time");
        dataSet.setColor(Color.parseColor("#8B7FD9"));
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(10f);

        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        barChart.invalidate(); // Refresh chart
    }

    private String formatDuration(long millis) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void initViews(View view) {
        toggleGroup = view.findViewById(R.id.toggle_button_group_period);
        viewPager = view.findViewById(R.id.view_pager_goals);
        tabLayout = view.findViewById(R.id.tab_layout_indicator);
        barChart = view.findViewById(R.id.bar_chart_stats);
        tvAverageTime = view.findViewById(R.id.tv_average_time);
        tvMaximumTime = view.findViewById(R.id.tv_maximum_time);
        toggleGroup.check(R.id.btn_daily); // Set default ke Daily
    }
}
