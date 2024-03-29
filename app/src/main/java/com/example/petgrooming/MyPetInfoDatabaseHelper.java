package com.example.petgrooming;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyPetInfoDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "PetInformation.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "pet_info";
    private static final String COLUMN_firebasephotoid = "firebase_photo_id";
    private static final String COLUMN_ID = "_id";

    private static final String COLUMN_petname = "pet_name";
    private static final String COLUMN_petbreed = "pet_breed";
    private static final String COLUMN_petcondition = "pet_condition";
    private static final String COLUMN_petsize = "pet_size_in_feet";
    private static final String COLUMN_petage = "pet_age";
    private static final String COLUMN_pettype = "pet_type";

    private static final String APPT_TABLE = "appointment";
    private static final String APPT_ID = "_id";
    private static final String COLUMN_APPT_PET_NAME = "pet_name";
    private static final String COLUMN_APPT_DATE = "date";
    private static final String COLUMN_APPT_PKG = "package";
    private static final String COLUMN_APPT_TOTAL = "total_price";
    private static final String COLUMN_APPT_STATUS = "payment_status";

    MyPetInfoDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }
    public MyPetInfoDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_petname + " TEXT, " +
                COLUMN_pettype + " TEXT, " +
                COLUMN_petbreed + " TEXT, " +
                COLUMN_petage + " INTEGER, " +
                COLUMN_petsize + " REAL, " +
                COLUMN_petcondition + " TEXT, " +
                COLUMN_firebasephotoid + " TEXT)";
        db.execSQL(query);

        String queryCreateAPPT = "CREATE TABLE " + APPT_TABLE +
                " (" + APPT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_APPT_PET_NAME + " TEXT, " +
                COLUMN_APPT_DATE + " TEXT, " +
                COLUMN_APPT_PKG + " TEXT, " +
                COLUMN_APPT_TOTAL + " REAL, " +
                COLUMN_APPT_STATUS + " TEXT)";
        db.execSQL(queryCreateAPPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + APPT_TABLE);
        onCreate(db);

    }

    void addPet(String petName, String petType, String petBreed, int petAge, double petSize, String petCondition, String firebasePhotoId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_petname, petName);
        cv.put(COLUMN_pettype, petType);
        cv.put(COLUMN_petbreed, petBreed);
        cv.put(COLUMN_petage, petAge);
        cv.put(COLUMN_petsize, petSize);
        cv.put(COLUMN_petcondition, petCondition);
        cv.put(COLUMN_firebasephotoid, firebasePhotoId);
        long result = db.insert(TABLE_NAME,null, cv);
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Pet added successfully!", Toast.LENGTH_SHORT).show();
        }
    }
    Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }


    void updateData(String row_id, String petName, String petType, String petBreed,
                    String petAge, String petSize, String petCondition, String firebasePhotoId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_petname, petName);
        cv.put(COLUMN_pettype, petType);
        cv.put(COLUMN_petbreed, petBreed);
        cv.put(COLUMN_petage, petAge);
        cv.put(COLUMN_petsize, petSize);
        cv.put(COLUMN_petcondition, petCondition);
        cv.put(COLUMN_firebasephotoid, firebasePhotoId);

        long result = db.update(TABLE_NAME, cv, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }

    }




    void deleteOneRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }
    }

    void addAppointment(String petName, String apptDate, String apptPkg, double total, String paymentStatus){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_APPT_PET_NAME, petName);
        cv.put(COLUMN_APPT_DATE, apptDate);
        cv.put(COLUMN_APPT_PKG, apptPkg);
        cv.put(COLUMN_APPT_TOTAL, total);
        cv.put(COLUMN_APPT_STATUS, paymentStatus);

        long result = db.insert(APPT_TABLE,null, cv);
        if(result == -1){
            Toast.makeText(context, "We have problems creating your appointment. Please try again.", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Appointment scheduled successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor readAPPTData(){
        String query = "SELECT * FROM " + APPT_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }


}
