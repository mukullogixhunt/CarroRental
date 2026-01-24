package com.carro.carrorental.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.carro.carrorental.R;
import com.carro.carrorental.databinding.ActivityEnableLocationBinding;

public class EnableLocationActivity extends AppCompatActivity {

    ActivityEnableLocationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEnableLocationBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initiateLocation();
    }

    private void initiateLocation() {

        binding.ivBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.btnAllow.setOnClickListener(v -> {
            Intent intent = new Intent(EnableLocationActivity.this, HomeActivity.class);
            startActivity(intent);

        });
        binding.btnLater.setOnClickListener(v -> {
            Intent intent = new Intent(EnableLocationActivity.this, MainActivity.class);
            startActivity(intent);

        });


    }
}