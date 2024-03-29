package com.example.petgrooming;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;


public class HomeScreenMainActivity extends NavigationBar implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView navProfileImgView; //Image view inside the drawerLayout
    Toolbar toolbar;
//    DatabaseHelper databaseHelper;

    TextView userName;

    //Firebase
    Uri imageUri;
    FirebaseStorage storage;
    StorageReference storageReference;

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
        navigationView.setNavigationItemSelectedListener(this); //onNavigationItemSelected
        navigationView.setCheckedItem(R.id.nav_home);

        //Firebase instrumentation
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        View headerLayout = navigationView.getHeaderView(0);
        navProfileImgView = headerLayout.findViewById(R.id.navProfileImgView);

        //Layout
        cardViewBookAppt = findViewById(R.id.cardViewBook);
        cardViewBookAppt.setOnClickListener((View v) -> {
            //startActivity(new Intent(this, MapsActivityBooking.class));
            //Need to update this activity when go live
            startActivity(new Intent(this, PetListActivity.class));
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

        navProfileImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseProfilePicture();
            }
        });
    }

    @Override
    int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.bottonnav;
    }

    private void chooseProfilePicture(){
        try {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent,1);

        }catch (Exception e){
            Toast.makeText(this, "Something went wrong while selecting the picture", Toast.LENGTH_SHORT).show();
            Log.d("UPLOAD_PHOTO","Error:" + e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode==RESULT_OK && data.getData()!=null){
            imageUri = data.getData();
            navProfileImgView.setImageURI(imageUri);
            uploadPicture();
        }
    }

    private void uploadPicture(){
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading Image...");
        pd.show();

        final String uniqueID = UUID.randomUUID().toString();

        // Create a reference to the image uniqueID created
        StorageReference imageRef = storageReference.child("images/" + uniqueID);

        imageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(),"Photo uploaded", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Photo not uploaded", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                pd.setMessage("Percentage: " + (int) progressPercent + "%");
            }
        });
    }
}