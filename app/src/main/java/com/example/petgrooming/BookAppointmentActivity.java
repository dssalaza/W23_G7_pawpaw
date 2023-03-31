package com.example.petgrooming;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


public class BookAppointmentActivity extends NavigationBar {
    CalendarView clndrViewBookAppt;
    TextView txtTitleBookAppt;
    Button btnBookApptToCheckOut;
    HashMap<String, String> sendInfoToCheckout;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        putInfoInHashmap();
        // Do not use setContentView(R.id.activity_book_appointment)
        // the super class (NavigationBar) will handle it if you return it in the getContentViewId() method;


        clndrViewBookAppt = findViewById(R.id.calendarViewBookAppt);
        txtTitleBookAppt = findViewById(R.id.txtViewTitleBookAppt);
        radioGroup = findViewById(R.id.radioGroupBookAppt);
        btnBookApptToCheckOut = findViewById(R.id.btnBookApptToCheckOutActivity);
        //btnTestingMap = findViewById(R.id.btnTestingMap);
        clndrViewBookAppt.setMinDate(System.currentTimeMillis());
        btnBookApptToCheckOut.setOnClickListener((View v) -> {

            if(radioGroup.getCheckedRadioButtonId() == -1)
            {
                Toast.makeText(this, "Please select package type", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Intent intent = new Intent(this,
                        CheckOutActivity.class);
                intent.putExtra("sendInfoToCheckout", sendInfoToCheckout);
                startActivity(intent);
            }

        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.basicPackage)
                {
                    sendInfoToCheckout.put("PackageInfo", "Basic Package");
                }
                else if (checkedId == R.id.premiumPackage)
                {
                    sendInfoToCheckout.put("PackageInfo", "Premium Package");
                }
            }
        });

        clndrViewBookAppt.setOnDateChangeListener((@NonNull CalendarView view, int year, int month, int dayOfMonth) -> {

            String date = (month + 1) + "/" + dayOfMonth + "/" + year;
            sendInfoToCheckout.put("selectedDate", date);
        });
    }

    void putInfoInHashmap()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        String currDate = formatter.format(date);
        sendInfoToCheckout = (HashMap<String, String>) getIntent().getSerializableExtra("sendInfoToBookAppt");
        sendInfoToCheckout.put("selectedDate", currDate);
        sendInfoToCheckout.put("LatitudeFromMap", getIntent().getStringExtra("LatitudeFromMap").toString());
        sendInfoToCheckout.put("LongitudeFromMap", getIntent().getStringExtra("LongitudeFromMap").toString());

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