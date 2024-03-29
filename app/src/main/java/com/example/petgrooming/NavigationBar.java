package com.example.petgrooming;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public abstract class NavigationBar extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener{

    protected BottomNavigationView navigationView;
    TextView userNameNav;
    TextView emailNav;

    //Third Party Auth
    GoogleSignInClient gClient;
    GoogleSignInOptions gOptions;

    final String TAG = "PawPaw";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        navigationView = findViewById(R.id.bottonnav);
        navigationView.setOnItemSelectedListener(this);

        gOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();
        gClient = GoogleSignIn.getClient(this, gOptions);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //updateNavigationBarState();
    }

    // Remove inter-activity transition to avoid screen tossing on tapping bottom navigation items
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //Bottom nav bar options
            case R.id.btnPets:
                startActivity(new Intent(this, PetListActivity.class));
                break;
            case R.id.btnHome:
                startActivity(new Intent(this, HomeScreenMainActivity.class));
                break;
            case R.id.btnBook:
                startActivity(new Intent(this, PetListActivity.class));
                break;
            //Left drawer options
            case R.id.nav_home:
                startActivity(new Intent(this, HomeScreenMainActivity.class));
                break;
            case R.id.nav_myProfile:
                startActivity(new Intent (this, UpdateUserProfileActivity.class));
                break;
            case R.id.nav_mypet:
                startActivity(new Intent (this, PetListActivity.class));
                break;
            case R.id.nav_logout:
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                gClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        finish();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    }
                });
                break;
        }
        return true;
    }

    abstract int getContentViewId();

    abstract int getNavigationMenuItemId();

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
