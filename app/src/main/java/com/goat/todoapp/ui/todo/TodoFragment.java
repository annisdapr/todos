package com.goat.todoapp.ui.todo;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.goat.todoapp.R;
import java.util.ArrayList;
import java.util.List;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.Observer;

public class TodoFragment extends Fragment {

    private RecyclerView recyclerView;
    private TodoAdapter adapter;
    private List<Todo> dummyTodos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo, container, false);
        recyclerView = view.findViewById(R.id.recyclerTodo);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new TodoAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // === Tambahkan ViewModel ===
        TodoViewModel viewModel = new ViewModelProvider(requireActivity()).get(TodoViewModel.class);

        // Observe LiveData supaya list otomatis update
        viewModel.getTodosLiveData().observe(getViewLifecycleOwner(), new Observer<List<Todo>>() {
            @Override
            public void onChanged(List<Todo> todos) {
                adapter.setTodos(todos);
            }
        });

        // Dummy data bisa kamu hapus nanti
        List<Todo> dummyTodos = new ArrayList<>();
        dummyTodos.add(new Todo("Finish UI Design", "UI layout for dashboard", "07 Oct 2025", "14:00", null, false));
        dummyTodos.add(new Todo("Write daily summary", "Daily reflection", "07 Oct 2025", "19:00", null, true));
        adapter.setTodos(dummyTodos);

        FloatingActionButton fabAdd = view.findViewById(R.id.fabAddTodo);
        fabAdd.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_nav_todo_fragment_to_addTodoFragment);
        });

        return view;
    }

}
