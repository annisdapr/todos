package com.goat.todoapp;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        }

        LinearLayout btnHome = findViewById(R.id.btn_nav_home);
        LinearLayout btnStats = findViewById(R.id.btn_nav_stats);
        LinearLayout bottomNav = findViewById(R.id.bottom_navigation);

        // Tambahkan ke list biar gampang reset
        List<LinearLayout> buttons = Arrays.asList(btnHome, btnStats);

        // Fungsi bantu reset selected state
        View.OnClickListener navClickListener = v -> {
            for (LinearLayout btn : buttons) {
                btn.setSelected(false);
            }
            v.setSelected(true);

            if (v.getId() == R.id.btn_nav_home && navController != null) {
                navController.navigate(R.id.homeFragment);
            } else if (v.getId() == R.id.btn_nav_stats && navController != null) {
                navController.navigate(R.id.statsFragment);
            }
        };

        btnHome.setOnClickListener(navClickListener);
        btnStats.setOnClickListener(navClickListener);

        // Default: Home aktif
        btnHome.setSelected(true);

        // Sembunyikan nav bar di halaman tertentu
        if (navController != null) {
            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                if (destination.getId() == R.id.homeFragment || destination.getId() == R.id.statsFragment) {
                    bottomNav.setVisibility(View.VISIBLE);
                } else {
                    bottomNav.setVisibility(View.GONE);
                }
            });
        }
    }
}
