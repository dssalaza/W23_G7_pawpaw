package com.example.petgrooming;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;


public class BookAppointmentActivity extends NavigationBar {
    CalendarView clndrViewBookAppt;
    TextView txtTitleBookAppt;
    Button btnBookApptToCheckOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Do not use setContentView(R.id.activity_book_appointment)
        // the super class (NavigationBar) will handle it if you return it in the getContentViewId() method;
        clndrViewBookAppt = findViewById(R.id.calendarViewBookAppt);
        txtTitleBookAppt = findViewById(R.id.txtViewTitleBookAppt);
        btnBookApptToCheckOut = findViewById(R.id.btnBookApptToCheckOutActivity);
        //btnTestingMap = findViewById(R.id.btnTestingMap);
        clndrViewBookAppt.setMinDate(System.currentTimeMillis());

        clndrViewBookAppt.setOnDateChangeListener((@NonNull CalendarView view, int year, int month, int dayOfMonth) -> {

        });

        btnBookApptToCheckOut.setOnClickListener((View v) -> {
            startActivity(new Intent(this, CheckOutActivity.class));
        });



    }

    @Override
    int getContentViewId() {
        return R.layout.activity_book_appointment;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.bottonnav;
    }
}