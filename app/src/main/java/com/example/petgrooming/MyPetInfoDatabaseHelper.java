package com.example.petgrooming;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyPetInfoDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "PetInformation.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "pet_info";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_petname = "pet_name";
    private static final String COLUMN_petbreed = "pet_breed";
    private static final String COLUMN_petcondition = "pet_condition";
    private static final String COLUMN_petsize = "pet_size_in_feet";
    private static final String COLUMN_petage = "pet_age";
    private static final String COLUMN_pettype = "pet_type";

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
                COLUMN_petcondition + " TEXT);";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    void addPet(String petName, String petType, String petBreed, int petAge, double petSize, String petCondition){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_petname, petName);
        cv.put(COLUMN_pettype, petType);
        cv.put(COLUMN_petbreed, petBreed);
        cv.put(COLUMN_petage, petAge);
        cv.put(COLUMN_petsize, petSize);
        cv.put(COLUMN_petcondition, petCondition);
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



}
