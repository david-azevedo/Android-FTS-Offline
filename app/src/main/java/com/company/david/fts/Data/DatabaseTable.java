package com.company.david.fts.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public class DatabaseTable {

    private static final String TAG = "AppointmentDatabase";
    private static final String DATABASE_NAME = "APPOINTMENT";
    private static final String FTS_VIRTUAL_TABLE = "FTS";
    private static final int DATABASE_VERSION = 1;

    private static final int MY_256 = 256;

    private static DatabaseTable sDatabaseTable = null;
    // Columns
    public static final String COL_DOCTOR = "DOCTOR";
    public static final String COL_HOSPITAL = "HOSPITAL";
    public static final String COL_TRANSCRIPT = "TRANSCRIPT";
    public static final String COL_TOKENIZE = "tokenize=unicode61";
    public static final String COL_MATCHINFO = "MATCHINFO";
    public static final String COL_SNIPPET = "SNIPPET";
    public static final String COL_OFFSETS = "OFFSETS";

    public static final String ALL_COLUMNS = "*";
    public static final String MATCHINFO = "matchinfo(" + FTS_VIRTUAL_TABLE + ") as " + COL_MATCHINFO;
    public static final String SNIPPET = "snippet(" + FTS_VIRTUAL_TABLE + ") as " + COL_SNIPPET;
    public static final String OFFSETS = "offsets(" + FTS_VIRTUAL_TABLE + ") as " + COL_OFFSETS;

    public final DatabaseOpenHelper mDatabaseOpenHelper;

    // Wrapper method for add entry
    public long addNewEntry(String doctor, String hospital, String transcript) {
        Log.d("CONSULTATION","\n\n\nHospital: "
                + hospital + "\nDoctor: "
                + doctor + "\nTranscript:\n\""
                + transcript + "\"");
        return mDatabaseOpenHelper.addEntry(doctor,hospital,transcript);
    }

    public static int[] parseMatchInfoBlob(byte[] blob) {

        int length = blob.length;
        int[] result = new int[length/4];
        for(int i = 0; i < length; i += 4) {
            result[i/4] = blob[i] +
                        (blob[i+1] * MY_256) +
                        (blob[i + 2] * MY_256 * MY_256) +
                        (blob[i +3 ] * MY_256 * MY_256 * MY_256);
        }

        return result;
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
                        COL_TRANSCRIPT + ", " +
                        COL_TOKENIZE + ")";

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

    //Function to search given a query string
    public Cursor getWordMatches(String query, String[] columns,boolean orSwitch) {
        /*
        * By using the table name in the match clause we are searching all
        * columns of the virtual table.
        * */
        String[] selectionArgs = new String[1];

        if (orSwitch) {// Pesquisar com OR
            String[] terms = query.trim().split(" ");
            String args = "";
            for(int i = 0; i < terms.length; i++) {
                args += terms[i] + "*";

                if(i != terms.length - 1) {
                    args += " OR ";
                }
            }

            selectionArgs[0] = args;
        } else { // Pesquisar com AND
            selectionArgs[0] = query + "*";
        }

        String selection = FTS_VIRTUAL_TABLE + " MATCH ?";

        return query(selection, selectionArgs, columns);
    }

    //Function to get all data rows
    public Cursor getAllRows() {
        return mDatabaseOpenHelper.getReadableDatabase().rawQuery("SELECT * FROM " + FTS_VIRTUAL_TABLE, null);
    };

    //Function to get number of entries
    public long getRowCount() {

        long res = DatabaseUtils.queryNumEntries(mDatabaseOpenHelper.mDatabase,FTS_VIRTUAL_TABLE);

        Log.d("DATABASETABLE:", "Number of entries: " + res);

        return res;
    }

    //Helper function to query the database
    private Cursor query(String selection, String[] selectionArgs, String[] columns) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(FTS_VIRTUAL_TABLE);

        String[] mColumns = new String[] {MATCHINFO,"*"};

        Cursor cursor = builder.query(mDatabaseOpenHelper.getReadableDatabase(),
                mColumns, selection, selectionArgs, null, null, null);

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }

        Log.d("DATABASETABLE",cursor.getColumnNames().toString());

        return cursor;
    }

    public void onDestroy() {
        mDatabaseOpenHelper.close();
    }
}
