package com.example.petgrooming;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.petgrooming.databinding.ActivityLoginBinding;

//Seulah 03/13/2023 - Login
public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        setContentView(R.layout.activity_login);

        databaseHelper = new DatabaseHelper(this);

        binding.loginButton.setOnClickListener((View view) -> {
            String email = binding.loginEmail.getText().toString();
            String password = binding.loginPassword.getText().toString();

            if(email.equals("") || password.equals(""))
                Toast.makeText(this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
            else {
                Boolean checkCredentials = databaseHelper.checkEmailPassword(email, password);

                if(checkCredentials == true) {
                    Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, HomeScreenMainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.signupRedirectText.setOnClickListener((View view) -> {
            Intent intent = new Intent(this, SignupActivity.class);
            startActivity(intent);
        });
    }
}