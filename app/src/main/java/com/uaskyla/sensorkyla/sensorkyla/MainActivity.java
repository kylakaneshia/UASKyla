package com.uaskyla.sensorkyla.sensorkyla;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor gyroscope;
    private SensorEventListener gyroscopeEventListener;
    private TextView value;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        value = findViewById(R.id.text);
        databaseHelper = new DatabaseHelper(this);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        if (gyroscope == null) {
            Toast.makeText(this, "Fitur Gyroscope tidak tersedia di device ini..", Toast.LENGTH_LONG).show();
            finish();
        }

        gyroscopeEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                value.setText("Sensor Gyroscope Kyla: \nX: " + sensorEvent.values[0] + ";\nY: " + sensorEvent.values[1] + ";\nZ: " + sensorEvent.values[2] + ";");

                if (sensorEvent.values[2] > 0.5f) {
                    getWindow().getDecorView().setBackgroundColor(Color.BLUE);
                } else if (sensorEvent.values[2] < -0.5f) {
                    getWindow().getDecorView().setBackgroundColor(Color.DKGRAY);
                }

                // Insert data into the database
                SQLiteDatabase db = databaseHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.COLUMN_X, sensorEvent.values[0]);
                values.put(DatabaseHelper.COLUMN_Y, sensorEvent.values[1]);
                values.put(DatabaseHelper.COLUMN_Z, sensorEvent.values[2]);
                db.insert(DatabaseHelper.TABLE_NAME, null, values);
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
                // Not needed for this example
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(gyroscopeEventListener, gyroscope, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(gyroscopeEventListener);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

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
                    + COLUMN_Z + " REAL"
                    + ")";
            db.execSQL(createTableQuery);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}
