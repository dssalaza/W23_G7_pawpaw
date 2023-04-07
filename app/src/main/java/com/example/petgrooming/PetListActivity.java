package com.example.petgrooming;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class PetListActivity extends NavigationBar {
    RecyclerView recyclerView;
    FloatingActionButton add_button;
    TextView txtViewDefaultMsg;


    MyPetInfoDatabaseHelper myDB;
    ArrayList<String> pet_id, pet_name, pet_type, pet_breed, pet_condition, pet_age, pet_size, pet_firebaseid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myDB = new MyPetInfoDatabaseHelper(PetListActivity.this);
        pet_id = new ArrayList<>();
        pet_name = new ArrayList<>();
        pet_type = new ArrayList<>();
        pet_breed = new ArrayList<>();
        pet_condition = new ArrayList<>();
        pet_age = new ArrayList<>();
        pet_size = new ArrayList<>();
        pet_firebaseid = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerViewPetInfo);
        add_button = findViewById(R.id.floatingActionButtonAddPet);
        txtViewDefaultMsg = findViewById(R.id.txtViewDefaultMsg);

        add_button.setOnClickListener((View v) -> {

            Intent intent = new Intent(PetListActivity.this, AddPetInfoToDBActivity.class);
            startActivity(intent);
        });

        storeDataInArrays();
        CustomAdapterForRecyclerView customAdapterForRecyclerView= new CustomAdapterForRecyclerView(PetListActivity.this,
                pet_name, pet_type, pet_breed, pet_size, pet_age, pet_condition, pet_id,pet_firebaseid);
        recyclerView.setAdapter(customAdapterForRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(PetListActivity.this));

    }

    @Override
    int getContentViewId() {
        return R.layout.activity_pet_list;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.bottonnav;
    }

    void storeDataInArrays(){
        Cursor cursor = myDB.readAllData();
        if(cursor.getCount() == 0){
            txtViewDefaultMsg.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            //empty_imageview.setVisibility(View.VISIBLE);
            //no_data.setVisibility(View.VISIBLE);
        }else{
            while (cursor.moveToNext()){
                txtViewDefaultMsg.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                pet_id.add(cursor.getString(0));
                pet_name.add(cursor.getString(1));
                pet_type.add(cursor.getString(2));
                pet_breed.add(cursor.getString(3));
                pet_age.add(cursor.getString(4));
                pet_size.add(cursor.getString(5));
                pet_condition.add(cursor.getString(6));
                pet_firebaseid.add(cursor.getString(7));
            }

        }
    }


}