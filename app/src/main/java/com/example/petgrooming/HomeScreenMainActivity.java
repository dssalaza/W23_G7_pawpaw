package com.example.petgrooming;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeScreenMainActivity extends AppCompatActivity {
    Button btnBookAppt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setLogo(R.mipmap.ic_launcher_paw_round);
        actionBar.setTitle("PawPaw");


        // Book Appointment Start - Sri
        btnBookAppt = findViewById(R.id.btnBookAppt);
        btnBookAppt.setOnClickListener((View v) -> {
            startActivity(new Intent(this, BookAppointmentActivity.class));
        });
        // Book Appointment End - Sri



    }
}