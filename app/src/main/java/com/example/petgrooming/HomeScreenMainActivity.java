package com.example.petgrooming;

import androidx.appcompat.app.ActionBar;
import androidx.cardview.widget.CardView;

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
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class HomeScreenMainActivity extends NavigationBar  {

    CardView cardViewBookAppt;

    TextView userName;
    Button logout;
    GoogleSignInClient gClient;
    GoogleSignInOptions gOptions;

    BottomNavigationView bottomNavigationView;

    final String TAG = "PawPaw";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Do not use setContentView(R.id.activity_main)
        // the super class (NavigationBar) will handle it if you return it in the getContentViewId() method;
        Log.d(TAG, "Starting pawpaw home page ...");

        bottomNavigationView = findViewById(R.id.bottonnav);
        bottomNavigationView.setOnItemSelectedListener(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setLogo(R.mipmap.ic_launcher_pawicon_round);
        actionBar.setTitle("PawPaw");

        cardViewBookAppt = findViewById(R.id.cardViewBook);
        cardViewBookAppt.setOnClickListener((View v) -> {
            startActivity(new Intent(this, BookAppointmentActivity.class));
        });

//        logout = findViewById(R.id.btnLogout);
//        userName = findViewById(R.id.txtViewUserName);
//
//        gOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
//        gClient = GoogleSignIn.getClient(this, gOptions);
//
//        GoogleSignInAccount gAccount = GoogleSignIn.getLastSignedInAccount(this);
//        if (gAccount != null) {
//            String gName = gAccount.getDisplayName();
//            userName.setText(gName);
//        }
//        logout.setOnClickListener((View view) -> {
//                gClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        finish();
//                        startActivity(new Intent(HomeScreenMainActivity.this, LoginActivity.class));
//                    }
//                });
//        });
    }

    @Override
    int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.bottonnav;
    }


}