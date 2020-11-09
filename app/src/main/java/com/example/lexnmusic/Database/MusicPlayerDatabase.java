package com.example.lexnmusic.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.lexnmusic.CustomClass.DbParams;
import com.example.lexnmusic.CustomClass.Songs;

import java.util.ArrayList;

public class MusicPlayerDatabase extends SQLiteOpenHelper {

    public static SQLiteDatabase db;
    public static ArrayList<Songs> getSongs = new ArrayList<Songs>();

    public MusicPlayerDatabase(@Nullable Context context) {
        super(context, DbParams.DB_NAME, null, DbParams.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // String create= "CREATE TABLE  "+ DbParams.TABLE_NAME + "( " + DbParams.COLUMN_ID + " INTEGER," +DbParams.COLUMN_SONG_TITLE + " STRING," +
        // DbParams.COLUMN_SONG_ARTIST+ " STRING," + DbParams.COLUMN_SONG_PATH + " STRING" + ")";
        //db.execSQL(create);
        db.execSQL("CREATE TABLE " + DbParams.TABLE_NAME + "( " + DbParams.COLUMN_ID + " INTEGER," +
                DbParams.COLUMN_SONG_TITLE + " STRING," + DbParams.COLUMN_SONG_ARTIST + " STRING," + DbParams.COLUMN_SONG_PATH + " STRING);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void storeAsFavorite(Long id, String title, String artist, String path) { //adding of favorite songs to the database
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbParams.COLUMN_ID, id);
        contentValues.put(DbParams.COLUMN_SONG_TITLE, title);
        contentValues.put(DbParams.COLUMN_SONG_ARTIST, artist);
        contentValues.put(DbParams.COLUMN_SONG_PATH, path);
        db.insert(DbParams.TABLE_NAME, null, contentValues);
        db.close();
    }

    public ArrayList<Songs> queryDBList() { //reading of favorite songs from the database
        try {
            db = this.getReadableDatabase();
            String query = "SELECT * FROM " + DbParams.TABLE_NAME;
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(DbParams.COLUMN_ID));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(DbParams.COLUMN_SONG_TITLE));
                    String artist = cursor.getString(cursor.getColumnIndexOrThrow(DbParams.COLUMN_SONG_ARTIST));
                    String path = cursor.getString(cursor.getColumnIndexOrThrow(DbParams.COLUMN_SONG_PATH));
                    getSongs.add(new Songs(Long.valueOf(id), title, artist, path, null));
                } while (cursor.moveToNext());
            } else return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getSongs;
    }

    public boolean checkIfIdExists(int id) { //Checks if the given Song ID exists
        int storeId = 0;
        db = this.getReadableDatabase();
        String query = "SELECT " + DbParams.COLUMN_ID + " FROM " + DbParams.TABLE_NAME + " WHERE " + DbParams.COLUMN_ID + " = " + id;
        //String query = "SELECT * FROM " + DbParams.TABLE_NAME + " WHERE SongID = '$id'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                storeId = cursor.getInt(cursor.getColumnIndexOrThrow(DbParams.COLUMN_ID));
            } while (cursor.moveToNext());
        } else return false;
        return storeId != 0;
    }

    public void deleteFavorites(int id) { //Deletes song from favorites
        db = this.getWritableDatabase();
        db.delete(DbParams.TABLE_NAME, DbParams.COLUMN_ID + " = " + id, null);
        db.close();
    }

    public int checkSize() {
        int c = 0;
        db = this.getReadableDatabase();
        String query = "SELECT * FROM " + DbParams.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                c = c + 1;
            } while (cursor.moveToNext());
        } else return 0;
        return c;
    }
}
