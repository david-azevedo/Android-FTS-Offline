package com.company.david.fts.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseTable {

    private static final String TAG = "AppointmentDatabase";

    private static DatabaseTable sDatabaseTable = null;
    // Columns
    private static final String COL_DOCTOR = "DOCTOR";
    private static final String COL_HOSPITAL = "HOSPITAL";
    private static final String COL_TRANSCRIPT = "TRANSCRIPT";

    private static final String DATABASE_NAME = "APPOINTMENT";
    private static final String FTS_VIRTUAL_TABLE = "FTS";
    private static final int DATABASE_VERSION = 1;

    public final DatabaseOpenHelper mDatabaseOpenHelper;

    // Wrapper method for add entry
    public long addNewEntry(String hospital, String doctor, String transcript) {
        Log.d("CONSULTATION","\n\n\nHospital: "
                + hospital + "\nDoctor: "
                + doctor + "\nTranscript:\n\""
                + transcript + "\"");
        return mDatabaseOpenHelper.addEntry(doctor,hospital,transcript);
    }

    private DatabaseTable(Context context) {
        mDatabaseOpenHelper = new DatabaseOpenHelper(context);
    }

    public static DatabaseTable getInstance(Context context) {
        if (sDatabaseTable == null) {
            sDatabaseTable = new DatabaseTable(context.getApplicationContext());
        }
        return sDatabaseTable;
    }

    private static class DatabaseOpenHelper extends SQLiteOpenHelper {

        private final Context mHelperContext;
        private SQLiteDatabase mDatabase;

        private static final String FTS_TABLE_CREATE =
                "CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE +
                        " USING fts3 (" +
                        COL_DOCTOR + ", " +
                        COL_HOSPITAL + ", " +
                        COL_TRANSCRIPT + ")";

        DatabaseOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            mHelperContext = context;
            mDatabase = getWritableDatabase();
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            mDatabase = sqLiteDatabase;
            mDatabase.execSQL(FTS_TABLE_CREATE);
            Log.w("DATABASE", "Database was created");

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            Log.w(TAG, "Upgrading database from version " + i + " to " +
                    i1 + ", which will destroy all old data");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE);
            onCreate(sqLiteDatabase);
        }

        // Function to add 1 entry to the appointment fts table
        public long addEntry(String doctor, String hospital, String transcript) {

            if (mDatabase == null) {
                Log.w("DATABASE", "Database is null!");
            }
            ContentValues cv = new ContentValues();
            cv.put(COL_DOCTOR, doctor);
            cv.put(COL_HOSPITAL, hospital);
            cv.put(COL_TRANSCRIPT, transcript);

            return mDatabase.insert(FTS_VIRTUAL_TABLE, null, cv);
        }
    }

    public void onDestroy() {
        mDatabaseOpenHelper.close();
    }
}
