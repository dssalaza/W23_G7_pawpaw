package com.example.petgrooming;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.petgrooming.databinding.ActivitySignupBinding;
import com.example.petgrooming.databinding.ActivityUpdateUserProfileBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UpdateUserProfileActivity extends AppCompatActivity {
    ActivityUpdateUserProfileBinding binding;
    DatabaseHelper db;
    Cursor cursor;

    final String TAG = "PawPaw";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_update_user_profile);
        binding = ActivityUpdateUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Log.d(TAG, "Starting pawpaw User Profile Update page ...");

        //Setup database
        db = new DatabaseHelper(this);

        //Display UserAccount
        cursor = db.readUserData();
        if (cursor.moveToFirst()) {
            do {
                String fname = cursor.getString(cursor.getColumnIndexOrThrow("fname"));
                String lname = cursor.getString(cursor.getColumnIndexOrThrow("lname"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));

                binding.editTextFname.setText(fname);
                binding.editTextLname.setText(lname);
                binding.txtViewEmail.setText(email);
            } while (cursor.moveToNext());
        }
        cursor.close();

        //Update UserAccount
        binding.btnUpdateUser.setOnClickListener((View view) -> {
            String ffname = binding.editTextFname.getText().toString();
            String llname = binding.editTextLname.getText().toString();
            String eemail = binding.txtViewEmail.getText().toString();;
            db.updateUserData(eemail, ffname, llname);
            Intent intent = new Intent(this, HomeScreenMainActivity.class);
            startActivity(intent);
        });

        //Delete UserAccount
        binding.btnDeleteUser.setOnClickListener((View view) -> {
            String eemail = binding.txtViewEmail.getText().toString();;
            db.deleteOneRow(eemail);
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
    }

}