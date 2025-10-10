package com.goat.todoapp.ui.todo;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.goat.todoapp.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class AddGoalFragment extends Fragment {

    // --- Deklarasi Semua View ---
    private TextInputEditText etGoalTitle, etHours, etMinutes;
    private MaterialButton btnStartDate, btnEndDate, btnSaveGoal;
    private CheckBox checkboxNoLimit, checkboxSelectAllDays;
    private ChipGroup chipGroupDays;
    private Spinner spinnerGroup;
    private ImageButton btnAddGroup;
    private LinearLayout colorSelectorContainer;

    private GoalViewModel viewModel;
    private final Calendar calendar = Calendar.getInstance();

    // --- Variabel untuk menampung data ---
    private long selectedStartDate = -1;
    private long selectedEndDate = -1;
    private String selectedColorHex = "#8B7FD9"; // Warna default
    private View lastSelectedColorView = null;
    private ArrayAdapter<String> groupAdapter;
    private List<String> groupList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_goal, container, false);

        initViews(view);
        viewModel = new ViewModelProvider(requireActivity()).get(GoalViewModel.class);
        setupGroupSpinner();
        setupListeners();

        return view;
    }

    private void initViews(View view) {
        etGoalTitle = view.findViewById(R.id.et_goal_title);
        etHours = view.findViewById(R.id.et_hours);
        etMinutes = view.findViewById(R.id.et_minutes);
        btnStartDate = view.findViewById(R.id.btn_start_date);
        btnEndDate = view.findViewById(R.id.btn_end_date);
        btnSaveGoal = view.findViewById(R.id.btn_save_goal);
        checkboxNoLimit = view.findViewById(R.id.checkbox_no_limit);
        checkboxSelectAllDays = view.findViewById(R.id.checkbox_select_all_days);
        chipGroupDays = view.findViewById(R.id.chip_group_days);
        spinnerGroup = view.findViewById(R.id.spinner_group);
        btnAddGroup = view.findViewById(R.id.btn_add_group);
        colorSelectorContainer = view.findViewById(R.id.color_selector_container);
    }

    private void setupGroupSpinner() {
        groupList = new ArrayList<>(Arrays.asList("Personal", "Work", "Health")); // Data awal
        groupAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, groupList);
        groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGroup.setAdapter(groupAdapter);
    }

    private void setupListeners() {
        btnStartDate.setOnClickListener(v -> showDatePickerDialog(true));
        btnEndDate.setOnClickListener(v -> showDatePickerDialog(false));

        checkboxNoLimit.setOnCheckedChangeListener((buttonView, isChecked) -> {
            btnEndDate.setEnabled(!isChecked);
            if (isChecked) {
                btnEndDate.setText("End Date");
                selectedEndDate = -1;
            }
        });

        checkboxSelectAllDays.setOnCheckedChangeListener((buttonView, isChecked) -> {
            for (int i = 0; i < chipGroupDays.getChildCount(); i++) {
                ((Chip) chipGroupDays.getChildAt(i)).setChecked(isChecked);
            }
        });

        for (int i = 0; i < colorSelectorContainer.getChildCount(); i++) {
            final View colorView = colorSelectorContainer.getChildAt(i);
            colorView.setOnClickListener(v -> {
                // Hapus border dari yang lama
                if (lastSelectedColorView != null) {
                    lastSelectedColorView.setForeground(null);
                }
                v.setForeground(getResources().getDrawable(R.drawable.color_swatch_selected_border));

                selectedColorHex = String.format("#%06X", (0xFFFFFF & ((MaterialCardView)v).getCardBackgroundColor().getDefaultColor()));
                lastSelectedColorView = v;
            });
        }

        btnAddGroup.setOnClickListener(v -> showAddGroupDialog());
        btnSaveGoal.setOnClickListener(v -> saveGoal());
    }

    private void showDatePickerDialog(final boolean isStartDate) {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            String formattedDate = sdf.format(calendar.getTime());

            if (isStartDate) {
                selectedStartDate = calendar.getTimeInMillis();
                btnStartDate.setText(formattedDate);
            } else {
                selectedEndDate = calendar.getTimeInMillis();
                btnEndDate.setText(formattedDate);
            }
        };

        new DatePickerDialog(requireContext(), dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void saveGoal() {
        String title = etGoalTitle.getText().toString().trim();
        if (title.isEmpty()) {
            Toast.makeText(getContext(), "Goal title tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        long hours = etHours.getText().toString().isEmpty() ? 0 : Long.parseLong(etHours.getText().toString());
        long minutes = etMinutes.getText().toString().isEmpty() ? 0 : Long.parseLong(etMinutes.getText().toString());
        long dailyTargetMillis = TimeUnit.HOURS.toMillis(hours) + TimeUnit.MINUTES.toMillis(minutes);

        Set<Integer> activeDays = new HashSet<>();
        for (int i = 0; i < chipGroupDays.getChildCount(); i++) {
            Chip chip = (Chip) chipGroupDays.getChildAt(i);
            if (chip.isChecked()) {
                activeDays.add(dayChipToCalendarInt(chip.getText().toString()));
            }
        }

        String selectedGroup = (spinnerGroup.getSelectedItem() != null) ? spinnerGroup.getSelectedItem().toString() : "";

        Goal newGoal = new Goal(title, selectedColorHex, dailyTargetMillis, selectedStartDate, selectedEndDate, checkboxNoLimit.isChecked(), activeDays, selectedGroup);

        viewModel.addGoal(newGoal);
        Toast.makeText(getContext(), "Goal '" + title + "' berhasil disimpan!", Toast.LENGTH_SHORT).show();
        Navigation.findNavController(getView()).popBackStack();
    }

    private int dayChipToCalendarInt(String day) {
        switch (day.toLowerCase()) {
            case "mon": return Calendar.MONDAY;
            case "tue": return Calendar.TUESDAY;
            case "wed": return Calendar.WEDNESDAY;
            case "thu": return Calendar.THURSDAY;
            case "fri": return Calendar.FRIDAY;
            case "sat": return Calendar.SATURDAY;
            case "sun": return Calendar.SUNDAY;
            default: return -1;
        }
    }
    private void showAddGroupDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add New Group");

        final EditText input = new EditText(requireContext());
        input.setHint("Group Name");
        builder.setView(input);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String newGroup = input.getText().toString().trim();
            if (!newGroup.isEmpty() && !groupList.contains(newGroup)) {
                groupList.add(newGroup);
                groupAdapter.notifyDataSetChanged();
                spinnerGroup.setSelection(groupAdapter.getPosition(newGroup));
                Toast.makeText(getContext(), "Group '" + newGroup + "' added", Toast.LENGTH_SHORT).show();
            } else if (newGroup.isEmpty()) {
                Toast.makeText(getContext(), "Group name cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Group already exists", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }
}