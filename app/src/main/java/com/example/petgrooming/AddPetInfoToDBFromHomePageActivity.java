package com.example.petgrooming;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class AddPetInfoToDBFromHomePageActivity extends NavigationBar {
    EditText petName, petBreed, petAge, petSize, petCondition,pet_firebase_photoid;
    Button add_button;
    //ImageView imgViewUpdate;
    String firebasePhotoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent1 = getIntent();
        String animalType = intent1.getStringExtra("petAnimalTypeStringExtra");

        petName = findViewById(R.id.pet_name_hm);
        petBreed = findViewById(R.id.pet_breed_hm);
        petAge = findViewById(R.id.pet_age_hm);
        petSize = findViewById(R.id.pet_size_hm);
        pet_firebase_photoid = findViewById(R.id.editTextFireBaseIdinAddPetInfoFromDB);
        petCondition = findViewById(R.id.pet_condition_hm);
        add_button = findViewById(R.id.add_button_hm);
        add_button.setOnClickListener((View view) -> {
            MyPetInfoDatabaseHelper myDB = new MyPetInfoDatabaseHelper(AddPetInfoToDBFromHomePageActivity.this);
            firebasePhotoId = pet_firebase_photoid.getText().toString();
            myDB.addPet(petName.getText().toString().trim(),
                    animalType,
                    petBreed.getText().toString().trim(),
                    Integer.valueOf(petAge.getText().toString().trim()),
                    Double.valueOf(petSize.getText().toString().trim()),
                    petCondition.getText().toString().trim(), firebasePhotoId
            );
            Intent intent = new Intent(AddPetInfoToDBFromHomePageActivity.this,
                    HomeScreenMainActivity.class);
            startActivity(intent);

        });
    }

    @Override
    int getContentViewId() {
        return R.layout.activity_add_pet_info_to_dbfrom_home_page;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.bottonnav;
    }
}