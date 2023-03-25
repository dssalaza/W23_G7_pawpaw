package com.example.petgrooming;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;


public class HomeScreenMainActivity extends NavigationBar implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    TextView userName;

    GoogleSignInClient gClient;
    GoogleSignInOptions gOptions;

    CardView cardViewBookAppt;
    CardView cardViewAddDog;
    CardView cardViewAddCat;

    BottomNavigationView bottomNavigationView;

    final String TAG = "PawPaw";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Do not use setContentView(R.id.activity_main)
        // the super class (NavigationBar) will handle it if you return it in the getContentViewId() method;
        Log.d(TAG, "Starting pawpaw home page ...");

        //Hooks------------------------------------------
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        //ToolBar----------------------------------------
        setSupportActionBar(toolbar);

        //Navigation Drawer Menu-------------------------
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        //login Information to be displayed--------------------------------
        userName = findViewById(R.id.txtViewUserName);
        //Bundle bundle = getIntent().getExtras();
        //String trEmail = bundle.getString("EMAIL", "-");
        //userName.setText(trEmail);

        gOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gClient = GoogleSignIn.getClient(this, gOptions);

        GoogleSignInAccount gAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (gAccount != null) {
            String gName = gAccount.getDisplayName();
            userName.setText(gName);
        }


        bottomNavigationView = findViewById(R.id.bottonnav);
        bottomNavigationView.setOnItemSelectedListener(this);

        cardViewBookAppt = findViewById(R.id.cardViewBook);
        cardViewBookAppt.setOnClickListener((View v) -> {
            startActivity(new Intent(this, BookAppointmentActivity.class));
        });

        cardViewAddDog = findViewById(R.id.cardViewDog);
        cardViewAddDog.setOnClickListener((View v) -> {
            Intent intent = new Intent(this, AddPetInfoToDBFromHomePageActivity.class);
            intent.putExtra("petAnimalTypeStringExtra", "Dog");
            startActivity(intent);
        });

        cardViewAddCat = findViewById(R.id.cardViewCat);
        cardViewAddCat.setOnClickListener((View v) -> {
            Intent intent = new Intent(this, AddPetInfoToDBFromHomePageActivity.class);
            intent.putExtra("petAnimalTypeStringExtra", "Cat");
            startActivity(intent);
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                break;
            case R.id.nav_mypet:
                Intent intent = new Intent (HomeScreenMainActivity.this, PetListActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_settings:
                break;
            case R.id.nav_logout:
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                gClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        finish();
                        startActivity(new Intent(HomeScreenMainActivity.this, LoginActivity.class));
                    }
                });
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START); //as one item is clicked, drawer closes
        return true;
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