package com.example.petgrooming;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddPetInfoToDBActivity extends AppCompatActivity {


    EditText petName, petAnimalType, petBreed, petAge, petSize, petCondition;
    Button add_button;

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
        add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener((View view) -> {
                MyPetInfoDatabaseHelper myDB = new MyPetInfoDatabaseHelper(AddPetInfoToDBActivity.this);
                myDB.addPet(petName.getText().toString().trim(),
                        petAnimalType.getText().toString().trim(),
                        petBreed.getText().toString().trim(),
                        Integer.valueOf(petAge.getText().toString().trim()),
                        Double.valueOf(petSize.getText().toString().trim()),
                        petCondition.getText().toString().trim()
                );
            Intent intent = new Intent(AddPetInfoToDBActivity.this, HomeScreenMainActivity.class);
            startActivity(intent);

        });

    }
}