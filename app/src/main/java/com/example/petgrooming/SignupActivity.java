package com.example.petgrooming;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.petgrooming.databinding.ActivitySignupBinding;

//Seulah 03/13/2023 - Signup
public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        setContentView(R.layout.activity_signup);

        databaseHelper = new DatabaseHelper(this);

        binding.signupButton.setOnClickListener((View view) -> {
            String email = binding.signupEmail.getText().toString();
            String password = binding.signupPassword.getText().toString();
            String confirmPassword = binding.signupConfirm.getText().toString();

            if(email.equals("") || password.equals("") || confirmPassword.equals(""))
                Toast.makeText(SignupActivity.this, "All fields are mandatory" , Toast.LENGTH_SHORT).show();
            else {
                if(password.equals(confirmPassword)) {
                    Boolean checkUserEmail = databaseHelper.checkEmail(email);

                    if (checkUserEmail == false) {
                        Boolean insert = databaseHelper.insertData(email, password);

                        if(insert == true) {
                            Toast.makeText(SignupActivity.this, "Signup Success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(SignupActivity.this, "Signup Failed", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SignupActivity.this, "User Already exists, Please login", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignupActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.loginRedirectText.setOnClickListener((View view) -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });

    }
}