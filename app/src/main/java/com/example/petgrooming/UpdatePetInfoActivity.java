package com.example.petgrooming;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class UpdatePetInfoActivity extends AppCompatActivity {

    EditText pet_name_input, pet_animal_type_input, pet_breed_input, pet_size_input, pet_age_input, pet_condition_input, pet_firebase_photoid;
    Button update_button,delete_button, book_button;
    String id, breed, name, type, size, age, condition, firebasePhotoId,firebasePhotoIdFetchFromDB;
    ImageView imgViewUpdate;

    //Firebase
    Uri imageUri;
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pet_info);
        imgViewUpdate = findViewById(R.id.imgViewUpdate);
        pet_name_input = findViewById(R.id.pet_name_update);
        pet_animal_type_input = findViewById(R.id.pet_animal_type_update);
        pet_breed_input = findViewById(R.id.pet_breed_update);
        pet_size_input = findViewById(R.id.pet_size_update);
        pet_age_input = findViewById(R.id.pet_age_update);
        pet_condition_input = findViewById(R.id.pet_condition_update);
        update_button = findViewById(R.id.add_button_update);
        delete_button = findViewById(R.id.delete_button_update);
        book_button = findViewById(R.id.btnBookAppt1);
        pet_firebase_photoid = findViewById(R.id.editTextFireBaseIdinUpdate);

        //Firebase instrumentation
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        imgViewUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseProfilePicture();
            }
        });

        getAndSetIntentData();
        update_button.setOnClickListener((View v) -> {
            MyPetInfoDatabaseHelper myDB = new MyPetInfoDatabaseHelper(UpdatePetInfoActivity.this);
            name = pet_name_input.getText().toString();
            type = pet_animal_type_input.getText().toString();
            breed = pet_breed_input.getText().toString();
            size = pet_size_input.getText().toString();
            age = pet_age_input.getText().toString();
            condition = pet_condition_input.getText().toString();
            firebasePhotoId = pet_firebase_photoid.getText().toString();
            myDB.updateData(id, name, type, breed, age, size, condition,firebasePhotoId);
            Intent intent = new Intent(UpdatePetInfoActivity.this,
                    HomeScreenMainActivity.class);
            startActivity(intent);

        });

        delete_button.setOnClickListener((View v) -> {
            MyPetInfoDatabaseHelper myDB = new MyPetInfoDatabaseHelper(UpdatePetInfoActivity.this);
            myDB.deleteOneRow(id);
        });

        book_button.setOnClickListener((View v) -> {
            startActivity(new Intent(this, MapsActivityBooking.class));
        });

    }

    void getAndSetIntentData()
    {
        firebasePhotoIdFetchFromDB = getIntent().getStringExtra("pet_firebase_id_update");
        setPetPicture(firebasePhotoIdFetchFromDB);

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

    private void setPetPicture(String firebasePhotoIdFetchFromDB){
        Log.d("Firebase", "Firebase id: " + firebasePhotoIdFetchFromDB);

        if (firebasePhotoIdFetchFromDB!= null){

            try {
                StorageReference pathReference = storageReference.child("images/pets/" + firebasePhotoIdFetchFromDB);
                final long ONE_MEGABYTE = 1024 * 1024;
                pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Log.d("Firebase", "Pet profile photo downloaded");
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        imgViewUpdate.setImageBitmap(bmp);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d("Firebase", "There is an error downloading the pet photo");
                    }
                });

            } catch (Exception e){
                Log.d("Firebase","Error:" + e);
            }

        }
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
            imgViewUpdate.setImageURI(imageUri);
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
}

