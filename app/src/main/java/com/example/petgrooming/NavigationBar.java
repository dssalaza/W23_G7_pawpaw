package com.example.petgrooming;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public abstract class NavigationBar extends AppCompatActivity implements  NavigationBarView.OnItemSelectedListener{

    protected BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        navigationView = findViewById(R.id.bottonnav);
        navigationView.setOnItemSelectedListener(this);
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
            case R.id.btnBookAppt:
                startActivity(new Intent(this, BookAppointmentActivity.class));
                break;
            case R.id.btnCheckHistory:
                startActivity(new Intent(this, HomeScreenMainActivity.class));
                break;
            case R.id.btnDownloadPdf:
                startActivity(new Intent(this, PetProfileActivity.class));
                break;
        }
        return true;    }

//    private void updateNavigationBarState(){
//        int actionId = getNavigationMenuItemId();
//        selectBottomNavigationBarItem(actionId);
//    }
//
//    void selectBottomNavigationBarItem(int itemId) {
//        MenuItem item = navigationView.getMenu().findItem(itemId);
//        item.setChecked(true);
//    }

    abstract int getContentViewId();

    abstract int getNavigationMenuItemId();

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
