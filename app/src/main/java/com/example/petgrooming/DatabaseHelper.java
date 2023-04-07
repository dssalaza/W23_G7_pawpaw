package com.example.petgrooming;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    public static final String databaseName = "Signup.db";

    final String TAG = "PawPaw";

    public DatabaseHelper(@Nullable Context context) {

        super(context, "Signup.db", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase MyDatabase) {
        MyDatabase.execSQL("CREATE TABLE allusers" +
                "(email TEXT primary key, " +
                "password TEXT, " +
                "fname TEXT, " +
                "lname TEXT, " +
                "firebasePhotoId TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS allusers");
    }

    public Boolean insertData
            (String email, String password, String fname, String lname) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("email", email);
        cv.put("password", password);
        cv.put("fname", fname);
        cv.put("lname ", lname);
        long result = db.insert("allusers", null, cv);
        if(result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean checkEmail(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(
                "Select * from allusers where email = ?", new String[]{email});
        if(cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean checkEmailPassword(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(
                "Select * from allusers where email = ? and password = ?",
                new String[]{email, password});
        if(cursor.getCount() > 0 ) {
            return true;
        } else {
            return false;
        }
    }

   public Cursor readUserData() {
       SQLiteDatabase db = this.getReadableDatabase();
       String query = "SELECT * FROM allusers";
       Cursor cursor = db.rawQuery(query, null);
       //To Test if data is populating-----------------
       if(cursor.moveToFirst()) {
           do {
               String fname = cursor.getString(cursor.getColumnIndexOrThrow("fname"));
               String lname = cursor.getString(cursor.getColumnIndexOrThrow("lname"));
               String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));

               Log.d(TAG, "First Name: " + fname);
               Log.d(TAG, "Last Name: " + lname);
               Log.d(TAG, "Email: " + email);
           } while (cursor.moveToNext());
       }
       //-----------------------------------------------
       return cursor;
   }

   public void updateUserData(String email, String fname, String lname) {
       SQLiteDatabase db = this.getWritableDatabase();
       ContentValues cv = new ContentValues();
       cv.put("fname", fname);
       cv.put("lname ", lname);
       long result = db.update("allusers", cv, "email = ?", new String[]{email});
       if(result == -1){
           Toast.makeText(context, "Failed to Update", Toast.LENGTH_SHORT).show();
       }else {
           Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
       }
   }

   public void deleteOneRow(String email) {
       SQLiteDatabase MyDatabase = this.getWritableDatabase();
       long result = MyDatabase.delete("allusers", "email = ?", new String[]{email});
       if(result == -1) {
           Toast.makeText(context, "Failed to Delete", Toast.LENGTH_SHORT).show();
       } else {
           Toast.makeText(context, "Successfully Delete!", Toast.LENGTH_SHORT).show();
       }
   }
}
