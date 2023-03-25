package com.example.petgrooming;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UpdatePetInfoActivity extends AppCompatActivity {

    EditText pet_name_input, pet_animal_type_input, pet_breed_input, pet_size_input, pet_age_input, pet_condition_input;
    Button update_button;
    String id, breed, name, type, size, age, condition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pet_info);

        pet_name_input = findViewById(R.id.pet_name_update);
        pet_animal_type_input = findViewById(R.id.pet_animal_type_update);
        pet_breed_input = findViewById(R.id.pet_breed_update);
        pet_size_input = findViewById(R.id.pet_size_update);
        pet_age_input = findViewById(R.id.pet_age_update);
        pet_condition_input = findViewById(R.id.pet_condition_update);
        update_button = findViewById(R.id.add_button_update);
        getAndSetIntentData();
        update_button.setOnClickListener((View v) -> {
            MyPetInfoDatabaseHelper myDB = new MyPetInfoDatabaseHelper(UpdatePetInfoActivity.this);
            myDB.updateData(id, name, type, breed, age, size, condition);
            Intent intent = new Intent(UpdatePetInfoActivity.this,
                    HomeScreenMainActivity.class);
            startActivity(intent);

        });




    }
    void getAndSetIntentData()
    {
        id = getIntent().getStringExtra("pet_id_update");
        name = getIntent().getStringExtra("pet_name_update");
        type = getIntent().getStringExtra("pet_animal_type_update");
        size = getIntent().getStringExtra("pet_size_update");
        age = getIntent().getStringExtra("pet_age_update");
        breed = getIntent().getStringExtra("pet_breed_update");
        condition = getIntent().getStringExtra("pet_condition_update_new");
        pet_name_input.setText(name);
        pet_animal_type_input.setText(type);
        pet_age_input.setText(age);
        pet_size_input.setText(size);
        pet_breed_input.setText(breed);
        pet_condition_input.setText(condition);

    }

}