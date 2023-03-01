package com.example.petgrooming;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import java.util.Timer;
import java.util.TimerTask;

public class Splash_Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //1. Declare TimerTask
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(Splash_Activity.this,
                        HomeScreenMainActivity.class));
                        finish();
            }
        };
        //2. Create Timer object
        Timer timer = new Timer();
        timer.schedule(timerTask, 2000);


    }
}