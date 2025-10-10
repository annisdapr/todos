package com.goat.todoapp.ui.todo; // Atau ui.home jika kamu sudah membuat package baru

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.goat.todoapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private GoalAdapter adapter;
    private GoalViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo, container, false);

        recyclerView = view.findViewById(R.id.recycler_goals); // Menggunakan ID RecyclerView BARU
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new GoalAdapter();
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(requireActivity()).get(GoalViewModel.class);

        viewModel.getGoalsWithProgressLiveData().observe(getViewLifecycleOwner(), new Observer<List<GoalWithProgress>>() {
            @Override
            public void onChanged(List<GoalWithProgress> goalsWithProgress) {
                adapter.setGoals(goalsWithProgress);
            }
        });

        FloatingActionButton fabAddGoal = view.findViewById(R.id.fab_add_goal);
        fabAddGoal.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_homeFragment_to_addGoalFragment);
        });

        return view;
    }
}