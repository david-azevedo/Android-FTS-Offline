package com.company.david.fts.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.company.david.fts.Utils.Date;
import com.company.david.fts.Utils.DateMatch;
import com.company.david.fts.Utils.PerformanceTime;
import com.company.david.fts.Utils.TfIdfHelper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DatabaseTable {

    private static final String TAG = "AppointmentDatabase";
    private static final String DATABASE_NAME = "APPOINTMENT";
    private static final String FTS_VIRTUAL_TABLE = "FTS";
    private static final String SINOM_TABLE = "SINONIMOS";
    private static final String SIGLAS_TABLE = "SIGLAS";
    private static final int DATABASE_VERSION = 1;

    private static final int MY_256 = 256;

    private static DatabaseTable sDatabaseTable = null;
    // Columns for table FTS
    public static final String COL_DOCTOR = "DOCTOR";
    public static final String COL_HOSPITAL = "HOSPITAL";
    public static final String COL_TRANSCRIPT = "TRANSCRIPT";
    public static final String COL_DATE = "DATE";
    public static final String COL_TOKENIZE = "tokenize=unicode61";
    public static final String COL_PREFIX = "prefix=\"4\"";
    public static final String COL_MATCHINFO = "MATCHINFO";
    public static final String COL_SNIPPET = "SNIPPET";
    public static final String COL_OFFSETS = "OFFSETS";

    // Columns for table SINONIMOS
    public static final String COL_SINOM1 = "SINOM1";
    public static final String COL_SINOM2 = "SINOM2";

    // Columns for table SIGLAS
    public static final String COL_SIGLA = "SIGLA";
    public static final String COL_SIGNIFICADO = "SIGNIFICADO";

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

    // Wrapper method for add entry with date
    public long addNewEntryDate(String doctor, String hospital, String transcript,Calendar calendar) {
        Log.d("CONSULTATION","\n\n\nHospital: "
                + hospital + "\nDoctor: "
                + doctor + "\nTranscript:\n\""
                + transcript + "\n\""
                + calendar.toString() + "\"");
        return mDatabaseOpenHelper.addEntryWithDate(doctor,hospital,transcript,calendar);
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
        private static final SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());

        private static final String FTS_TABLE_CREATE =
                "CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE +
                        " USING fts4 (" +
                        COL_DOCTOR + ", " +
                        COL_HOSPITAL + ", " +
                        COL_DATE + ", " +
                        COL_TRANSCRIPT + ", " +
                        COL_TOKENIZE + ", " +
                        COL_PREFIX + ")";
        private static final String SINOMIMOS_TABLE_CREATE =
                "CREATE TABLE " + SINOM_TABLE + " (" +
                        COL_SINOM1 + " TEXT PRIMARY KEY," +
                        COL_SINOM2 + " TEXT NOT NULL)";

        private static final String SIGLAS_TABLE_CREATE =
                "CREATE TABLE " + SIGLAS_TABLE + " (" +
                        COL_SIGLA + " TEXT PRIMARY KEY," +
                        COL_SIGNIFICADO + " TEXT NOT NULL)";

        DatabaseOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            mHelperContext = context;
            mDatabase = getWritableDatabase();
        }

        private void addDefaultEntries() {
            Context context = mHelperContext.getApplicationContext();
            try {
                InputStream ins = context.getResources().openRawResource(
                        context.getResources().getIdentifier("base_dados",
                                "raw", context.getPackageName()));

                BufferedReader reader= null;

                reader = new BufferedReader(new InputStreamReader(ins, "UTF-8"));

                while(reader.ready())
                {
                    String line = reader.readLine();
                    String[] entries = line.split(";");
                    java.util.Date d = mDateFormat.parse(entries[0]);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(d);

                    addEntryWithDate(entries[1],entries[2],entries[3],cal);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void addDefaultSinons() {

            Context context = mHelperContext.getApplicationContext();
            try {
                InputStream ins = context.getResources().openRawResource(
                        context.getResources().getIdentifier("sinom_entries",
                                "raw", context.getPackageName()));

                BufferedReader reader= null;

                reader = new BufferedReader(new InputStreamReader(ins, "UTF-8"));

                while(reader.ready())
                {
                    String line = reader.readLine();
                    Log.d("EXEC SQL", line);
                    mDatabase.execSQL(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            mDatabase = sqLiteDatabase;
            mDatabase.execSQL(FTS_TABLE_CREATE);
            mDatabase.execSQL(SINOMIMOS_TABLE_CREATE);
            mDatabase.execSQL(SIGLAS_TABLE_CREATE);

            addDefaultEntries();
            addDefaultSinons();

            Log.w("DATABASE", "Database was created");

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            Log.w(TAG, "Upgrading database from version " + i + " to " +
                    i1 + ", which will destroy all old data");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SINOM_TABLE);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SIGLAS_TABLE);
            onCreate(sqLiteDatabase);
        }

        public long addEntryWithDate(String doctor, String hospital, String transcript, Calendar c) {

            if (mDatabase == null) {
                Log.w("DATABASE", "Database is null!");
            }

            int day = c.get(Calendar.DAY_OF_MONTH);
            String month = c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
            int year = c.get(Calendar.YEAR);
            String day_of_week = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());

            // String representing the current day, month, year and week day
            String date = "d" + String.format("%02d", day) + " m" + month + " y" + year + " w" + day_of_week;

            Log.d("ENTRY DATE", date);

            ContentValues cv = new ContentValues();
            cv.put(COL_DOCTOR, doctor);
            cv.put(COL_HOSPITAL, hospital);
            cv.put(COL_DATE, date);
            cv.put(COL_TRANSCRIPT, transcript);

            return mDatabase.insert(FTS_VIRTUAL_TABLE, null, cv);
        }

        // Function to add 1 entry to the appointment fts table
        public long addEntry(String doctor, String hospital, String transcript) {

            if (mDatabase == null) {
                Log.w("DATABASE", "Database is null!");
            }

            // Getting the current data to add in a new column
            Calendar c = Calendar.getInstance();
            int day = c.get(Calendar.DAY_OF_MONTH);
            String month = c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
            int year = c.get(Calendar.YEAR);
            String day_of_week = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());

            // String representing the current day, month, year and week day
            String date = "d" + String.format("%02d", day) + " m" + month + " y" + year + " w" + day_of_week;

            Log.d("CURRENT DATE", date);

            ContentValues cv = new ContentValues();
            cv.put(COL_DOCTOR, doctor);
            cv.put(COL_HOSPITAL, hospital);
            cv.put(COL_DATE, date);
            cv.put(COL_TRANSCRIPT, transcript);

            return mDatabase.insert(FTS_VIRTUAL_TABLE, null, cv);
        }
    }

    //Function to search given a query string
    public Cursor getWordMatches(String query, String[] columns,
                                 boolean use4gram, boolean useDate, boolean useSynonym) {
        /*
        * By using the table name in the match clause we are searching all
        * columns of the virtual table.
        * */

        if( useDate ) {
            Log.d("SEARCH", "Using dates!");
            // Detecting dates
            DateMatch dMatch = Date.detectDates(query);
            Calendar c = Calendar.getInstance();

            if (dMatch.day != null) {
                PerformanceTime.setFoundDate();
                query += " d" + dMatch.day;
            }
            if (dMatch.month != -1) {
                PerformanceTime.setFoundDate();
                c.set(Calendar.MONTH, dMatch.month);
                query += " m" + c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
            }
            if (dMatch.year != null) {
                PerformanceTime.setFoundDate();
                query += " y" + dMatch.year;
            }
            if (dMatch.day_of_week != -1) {
                PerformanceTime.setFoundDate();
                c.set(Calendar.DAY_OF_WEEK, dMatch.day_of_week);
                query += " w" + c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
            }
        }

        // End date detection
        Log.d("DATE MATCHER:", query);

        PerformanceTime.setT2(Calendar.getInstance().getTimeInMillis());

        String[] selectionArgs = new String[1];
        String[] terms = query.trim().split("[- +]");

        if ( useSynonym ) {

            int array_size = terms.length;
            ArrayList<String> terms_list = new ArrayList<String>(Arrays.asList(terms));

            Log.d("SEARCH TERMS B SYNONYMS", Arrays.toString(terms));

            for (String term : terms) {
                String sinom = getSinom(term);
                if (sinom != null) {
                    PerformanceTime.setFoundSinom();

                    String[] temp = sinom.split(" ");
                    array_size += temp.length;
                    terms_list.addAll(Arrays.asList(temp));
                }
            }

            terms = terms_list.toArray(new String[array_size]);

            Log.d("SYNONYM", "Ended search for synonyms");

            Log.d("SEARCH TERMS A SYNONYMS", Arrays.toString(terms));

        }

        PerformanceTime.setT3(Calendar.getInstance().getTimeInMillis());

        if (use4gram) {
            Log.d("SEARCH", "Using 4gram!");
            // Start 4gram
            String[] search_terms = query.trim().split("[- +]");

            for (String term : search_terms) {

                if (term.length() > 4) {
                    String gram = term.substring(0, 4);
                    query += " " + gram + "*";
                }
            }
            // End 4gram
        }

        PerformanceTime.setT4(Calendar.getInstance().getTimeInMillis());

        // Setting the new search terms in the tfidf helper
        TfIdfHelper.setSearchTerms(terms);


        String args = "";
        for(int i = 0; i < terms.length; i++) {

            args += terms[i];

            if(i != terms.length - 1) {
                args += " OR ";
            }
        }

        selectionArgs[0] = args;

        Log.d("SELECTION ARGS", Arrays.toString(selectionArgs));
        String selection = FTS_VIRTUAL_TABLE + " MATCH ?";

        return query(selection, selectionArgs, columns);
    }

    public String getSinom(String expression) {

        Cursor result = mDatabaseOpenHelper.getReadableDatabase().rawQuery("SELECT " + COL_SINOM2 + " FROM " +
                SINOM_TABLE + " WHERE " + COL_SINOM1 + " = ? COLLATE NOCASE", new String[] {expression});

        String sinom = null;
        if (result != null) {
            if (result.moveToFirst()) {
                sinom = result.getString(0);
                result.close();
            }
        }
        return sinom;
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

    public long getDocumentFrequency(String word) {

        long res = DatabaseUtils.queryNumEntries(mDatabaseOpenHelper.mDatabase, FTS_VIRTUAL_TABLE, FTS_VIRTUAL_TABLE + " match '" + word + "'");

        Log.d("DATABASETABLE:", "Number of columns with " + word + " : " + res);

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
