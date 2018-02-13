package com.company.david.fts.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

public class DatabaseTable {

    private static final String TAG = "AppointmentDatabase";
    private static final String DATABASE_NAME = "APPOINTMENT";
    private static final String FTS_VIRTUAL_TABLE = "FTS";
    private static final int DATABASE_VERSION = 1;

    private static DatabaseTable sDatabaseTable = null;
    // Columns
    public static final String COL_DOCTOR = "DOCTOR";
    public static final String COL_HOSPITAL = "HOSPITAL";
    public static final String COL_TRANSCRIPT = "TRANSCRIPT";
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

    // TODO complete the function
    public static int[] parseMatchInfoBlob(byte[] blob) {
        return null;
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

    //Function to search given a query string
    public Cursor getWordMatches(String query, String[] columns) {
            /*
            * By using the table name in the match clause we are searching all
            * columns of the virtual table.
            * */
        String selection = FTS_VIRTUAL_TABLE + " MATCH ?";
        // TODO add trick for *half word* here
        String[] selectionArgs = new String[] {query+"*"};

        return query(selection, selectionArgs, columns);
    }

    //Function to get all data rows
    public Cursor getAllRows() {
        return mDatabaseOpenHelper.getReadableDatabase().rawQuery("SELECT * FROM " + FTS_VIRTUAL_TABLE, null);
    };

    //Function to get number of entries
    public long getRowCount() {
        // TODO SELECT COUNT(*) FROM FTS
        return 0;
    }

    //Helper function to query the database
    private Cursor query(String selection, String[] selectionArgs, String[] columns) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(FTS_VIRTUAL_TABLE);

        // FIXME trying to call matchinfo function
        columns = new String[] {"matchinfo(" + FTS_VIRTUAL_TABLE+ ") as MATCHINFO","*"};

        Cursor cursor = builder.query(mDatabaseOpenHelper.getReadableDatabase(),
                columns, selection, selectionArgs, null, null, null);

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }

    public void onDestroy() {
        mDatabaseOpenHelper.close();
    }
}
