package com.example.petgrooming;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.petgrooming.databinding.ActivityMapsBookingBinding;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.HashMap;

public class MapsActivityBooking extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private ActivityMapsBookingBinding binding;
    Button checkOutFromMaps;
    double wayLatitude = -34;
    double wayLongitude = 151;

    private static final int LOCATION_PERMISSION_CODE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

        checkOutFromMaps = findViewById(R.id.checkOutFromMaps);
        checkOutFromMaps.setOnClickListener((View v) -> {
            HashMap<String, String> sendInfoToBookAppt;
            sendInfoToBookAppt = (HashMap<String, String>) getIntent().getSerializableExtra("sendInfoToMapsActivityHM");
                Intent intent = new Intent(this,
                        BookAppointmentActivity.class);
                intent.putExtra("LatitudeFromMap", String.valueOf(wayLatitude));
                intent.putExtra("LongitudeFromMap", String.valueOf(wayLongitude));
                intent.putExtra("sendInfoToBookAppt", sendInfoToBookAppt);
                startActivity(intent);
        });

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            FusedLocationProviderClient fusedLocationProviderClient =
                    new FusedLocationProviderClient(this);
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener((Location location) -> {
               if(location!=null)
               {
                   wayLatitude = location.getLatitude();
                   wayLongitude = location.getLongitude();
               }

            });

        } else {
            Toast.makeText(this, "Please allow location access to get current location", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    LOCATION_PERMISSION_CODE);

        }


    }



    private boolean isLocationPermissionGranted()
    {
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_CODE);
    }
    // Need to work on this code -
    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions();

        // Setting the position for the marker
        markerOptions.position(latLng);

        // Setting the title for the marker.
        // This will be displayed on taping the marker
        markerOptions.title(latLng.latitude + " : " + latLng.longitude);

        // Clears the previously touched position
        mMap.clear();

        // Animating to the touched position
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

        // Placing a marker on the touched position
        mMap.addMarker(markerOptions).setDraggable(true);
        Marker myMarker = mMap.addMarker(markerOptions);
        LatLng position = myMarker.getPosition(); //
        Toast.makeText(
                MapsActivityBooking.this,
                "Lat " + position.latitude + " " + "Long " + position.longitude,
                Toast.LENGTH_LONG).show();
    }
}