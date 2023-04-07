package com.example.petgrooming;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AddPetInfoToDBActivity extends AppCompatActivity {


    EditText petName, petAnimalType, petBreed, petAge, petSize, petCondition,pet_firebase_photoid;
    Button add_button;
    //ImageView imgViewUpdate;
    String firebasePhotoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet_info);
        
        petName = findViewById(R.id.pet_name);
        petAnimalType = findViewById(R.id.pet_animal_type);
        petBreed = findViewById(R.id.pet_breed);
        petAge = findViewById(R.id.pet_age);
        petSize = findViewById(R.id.pet_size);
        petCondition = findViewById(R.id.pet_condition);
        pet_firebase_photoid = findViewById(R.id.editTextFireBaseIdinAddPetInfoToDB);
        add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener((View view) -> {

            if (petName.getText().length() > 0 &&
            petAnimalType.getText().length() > 0 &&
                    petBreed.getText().length() > 0 &&
                    petAge.getText().length() > 0 &&
                    petSize.getText().length() > 0 &&
                    petCondition.getText().length() > 0)
            {
                MyPetInfoDatabaseHelper myDB = new MyPetInfoDatabaseHelper(AddPetInfoToDBActivity.this);
                firebasePhotoId = pet_firebase_photoid.getText().toString();
                myDB.addPet(petName.getText().toString().trim(),
                        petAnimalType.getText().toString().trim(),
                        petBreed.getText().toString().trim(),
                        Integer.valueOf(petAge.getText().toString().trim()),
                        Double.valueOf(petSize.getText().toString().trim()),
                        petCondition.getText().toString().trim(),firebasePhotoId
                );
                Intent intent = new Intent(AddPetInfoToDBActivity.this, HomeScreenMainActivity.class);
                startActivity(intent);

            }
            else
            {
                Toast.makeText(this,"Please enter all fields before proceeding further",Toast.LENGTH_SHORT).show();
            }



        });

    }
}