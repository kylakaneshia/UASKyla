package com.uaskyla.sensorkyla.sensorkyla;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "sensor_data.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "sensor_data";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_X = "x";
    private static final String COLUMN_Y = "y";
    private static final String COLUMN_Z = "z";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_X + " REAL, "
                + COLUMN_Y + " REAL, "
                + COLUMN_Z + " REAL)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTableQuery = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(dropTableQuery);
        onCreate(db);
    }
}
