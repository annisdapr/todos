package com.goat.todoapp.ui.todo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide; // PASTIKAN IMPORT INI ADA
import com.goat.todoapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTodoFragment extends Fragment {

    private EditText etTitle, etDescription;
    private Button btnPickDate, btnPickTime, btnSave, btnPickImage;
    private ImageView imgPreview;

    private String selectedDate = "";
    private String selectedTime = "";
    private Uri imageUri = null;

    private final Calendar calendar = Calendar.getInstance();
    private TodoViewModel viewModel;
    private Todo currentTodo; // Untuk menyimpan data Todo yang dikirim

    private final ActivityResultLauncher<String> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    imageUri = uri;
                    imgPreview.setImageURI(uri);
                    imgPreview.setVisibility(View.VISIBLE);
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_todo, container, false);

        // --- Inisialisasi semua view ---
        etTitle = view.findViewById(R.id.etTitle);
        etDescription = view.findViewById(R.id.etDescription);
        btnPickDate = view.findViewById(R.id.btnPickDate);
        btnPickTime = view.findViewById(R.id.btnPickTime);
        btnPickImage = view.findViewById(R.id.btnPickImage);
        btnSave = view.findViewById(R.id.btnSave);
        imgPreview = view.findViewById(R.id.imgPreview);

        viewModel = new ViewModelProvider(requireActivity()).get(TodoViewModel.class);

        // --- Logika untuk membedakan mode "Tambah" atau "Detail" ---
        if (getArguments() != null && getArguments().containsKey("todoItem")) {
            currentTodo = (Todo) getArguments().getSerializable("todoItem");
            setupDetailMode();
        } else {
            currentTodo = null;
            setupAddMode();
        }

        return view;
    }

    // Method ini berjalan jika fragment dibuka untuk MELIHAT DETAIL
    private void setupDetailMode() {
        etTitle.setText(currentTodo.getTitle());
        etDescription.setText(currentTodo.getDescription());

        // Tampilkan tanggal dan waktu jika ada
        if (currentTodo.getDate() != null && !currentTodo.getDate().isEmpty()) {
            btnPickDate.setText(currentTodo.getDate());
        }
        if (currentTodo.getTime() != null && !currentTodo.getTime().isEmpty()) {
            btnPickTime.setText(currentTodo.getTime());
        }

        // Tampilkan gambar jika ada
        if (currentTodo.getImageUri() != null && !currentTodo.getImageUri().isEmpty()) {
            imgPreview.setVisibility(View.VISIBLE);
            Glide.with(this).load(Uri.parse(currentTodo.getImageUri())).into(imgPreview);
        } else {
            imgPreview.setVisibility(View.GONE);
        }

        // Matikan interaksi (mode read-only)
        etTitle.setEnabled(false);
        etDescription.setEnabled(false);
        btnPickDate.setEnabled(false);
        btnPickTime.setEnabled(false);
        btnPickImage.setVisibility(View.GONE);
        btnSave.setText("Edit"); // Ganti teks tombol Save menjadi Edit (opsional)

        // Nanti kita bisa tambahkan logika untuk mode edit di sini
        btnSave.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Fitur edit belum diimplementasikan", Toast.LENGTH_SHORT).show();
        });
    }

    // Method ini berjalan jika fragment dibuka untuk MENAMBAH TUGAS BARU
    private void setupAddMode() {
        btnPickDate.setOnClickListener(v -> {
            new DatePickerDialog(
                    requireContext(),
                    (view1, year, month, dayOfMonth) -> {
                        calendar.set(year, month, dayOfMonth);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                        selectedDate = sdf.format(calendar.getTime());
                        btnPickDate.setText(selectedDate);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        btnPickTime.setOnClickListener(v -> {
            new TimePickerDialog(
                    requireContext(),
                    (view12, hour, minute) -> {
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);
                        selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
                        btnPickTime.setText(selectedTime);
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
            ).show();
        });

        btnPickImage.setOnClickListener(v -> pickImageLauncher.launch("image/*"));

        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String desc = etDescription.getText().toString().trim();

            if (title.isEmpty()) {
                Toast.makeText(requireContext(), "Judul tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }

            Todo newTodo = new Todo(title, desc, selectedDate, selectedTime, imageUri != null ? imageUri.toString() : null, false);
            viewModel.addTodo(newTodo);

            Toast.makeText(requireContext(), "Tugas disimpan!", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(v).popBackStack(); // Kembali ke halaman sebelumnya
        });
    }
}