package com.example.petgrooming;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class AddPetInfoToDBFromHomePageActivity extends NavigationBar {
    EditText petName, petBreed, petAge, petSize, petCondition,pet_firebase_photoid;
    Button add_button;
    //ImageView imgViewUpdate;
    String firebasePhotoId;
    ImageView petPicture;
    Uri imageUri;
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        petPicture = findViewById(R.id.imgViewAddPetInfoFromDB);

        petPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseProfilePicture();
            }
        });

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


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
            petPicture.setImageURI(imageUri);
            uploadPicture();
        }
    }

    private void uploadPicture(){

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading Image...");
        pd.show();

        final String uniqueID = UUID.randomUUID().toString();
        pet_firebase_photoid.setText(uniqueID);

        // Create a reference to the image uniqueID created
        StorageReference imageRef = storageReference.child("images/pets/" + uniqueID);

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


    @Override
    int getContentViewId() {
        return R.layout.activity_add_pet_info_to_dbfrom_home_page;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.bottonnav;
    }
}