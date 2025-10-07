package com.goat.todoapp.ui.todo;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.goat.todoapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

public class TodoFragment extends Fragment {

    private RecyclerView recyclerView;
    private TodoAdapter adapter;
    private TodoViewModel viewModel;
    // Variabel dummyTodos sudah dihapus

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo, container, false);

        // 1. Setup RecyclerView dan Adapter (dengan constructor kosong)
        recyclerView = view.findViewById(R.id.recyclerTodo);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TodoAdapter(); // DIPERBAIKI: Panggil constructor tanpa argumen
        recyclerView.setAdapter(adapter);

        // 2. Inisialisasi ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(TodoViewModel.class);

        // 3. Observe LiveData dari ViewModel
        viewModel.getTasksLiveData().observe(getViewLifecycleOwner(), new Observer<List<TaskItem>>() {
            @Override
            public void onChanged(List<TaskItem> taskItems) {
                adapter.setTasks(taskItems);
            }
        });

        // 4. Setup Floating Action Buttons
        FloatingActionButton fabMain = view.findViewById(R.id.fabAddTodo);
        FloatingActionButton fabAddTodo = view.findViewById(R.id.fabAddTodoOption);
        FloatingActionButton fabAddTimer = view.findViewById(R.id.fabAddTimer);

        final boolean[] isFabOpen = {false};

        fabMain.setOnClickListener(v -> {
            // Kita akan tampilkan Toast di sini
            if (isFabOpen[0]) {
                Toast.makeText(getContext(), "Menutup Opsi...", Toast.LENGTH_SHORT).show();

                // Sembunyikan tombol
                fabAddTodo.setVisibility(View.GONE);
                fabAddTimer.setVisibility(View.GONE);
                isFabOpen[0] = false;
            } else {
                Toast.makeText(getContext(), "Membuka Opsi...", Toast.LENGTH_SHORT).show();

                // Munculkan tombol
                fabAddTodo.setVisibility(View.VISIBLE);
                fabAddTimer.setVisibility(View.VISIBLE);
                isFabOpen[0] = true;
            }
        });


        fabAddTodo.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_nav_todo_fragment_to_addTodoFragment);
        });

        fabAddTimer.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_nav_todo_fragment_to_addTimerFragment);
        });

        return view;
    }
}