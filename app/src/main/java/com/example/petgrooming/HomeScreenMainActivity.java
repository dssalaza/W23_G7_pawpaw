package com.example.petgrooming;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class HomeScreenMainActivity extends AppCompatActivity {

    Button btnBookAppt;

    //03/15/2023 logout - Seu
    TextView userName;
    Button logout;
    GoogleSignInClient gClient;
    GoogleSignInOptions gOptions;

    final String TAG = "PawPaw";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "Starting pawpaw home page ...");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setLogo(R.mipmap.ic_launcher_pawicon_round);
        actionBar.setTitle("PawPaw");

        // Book Appointment Start - Sri
        btnBookAppt = findViewById(R.id.btnBookAppt);
        btnBookAppt.setOnClickListener((View v) -> {
            startActivity(new Intent(this, BookAppointmentActivity.class));
        });
        // Book Appointment End - Sri

        //03/15/2023 logout - Seu
        logout = findViewById(R.id.btnLogout);
        userName = findViewById(R.id.userName);

        gOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gClient = GoogleSignIn.getClient(this, gOptions);

        GoogleSignInAccount gAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (gAccount != null) {
            String gName = gAccount.getDisplayName();
            userName.setText(gName);
        }
        logout.setOnClickListener((View view) -> {
                gClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        finish();
                        startActivity(new Intent(HomeScreenMainActivity.this, LoginActivity.class));
                    }
                });
        });
    }
}