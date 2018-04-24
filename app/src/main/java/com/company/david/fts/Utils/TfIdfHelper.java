package com.company.david.fts.Utils;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.util.Log;

import com.company.david.fts.Data.DatabaseTable;
import com.company.david.fts.ViewDataActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class TfIdfHelper {

    private static String[] searchTerms;

    public static void setSearchTerms(String[] terms) {
        searchTerms = terms;
    }

    /* Function to reduce the amount of information the initial parsed array
    from matchinfo returns in order to calculate the tf * idf for each row
    */
    public static int[] shortenInitialArray(int[] array) {

        // Number of phrases in the query
        int phrases = array[0];

        // Number of user defined columns in the database
        int cols = array[1];

        // Creating the result array with the reduced sized
        int[] result = new int[((array.length - 2) / cols) + 2];

        // setting the appropriate values
        result[0] = phrases;
        result[1] = cols;

        // Counter for the results array;
        int counter = 2;

        // Loop through the array
        for(int p = 0; p < phrases; p++) {

            int hits_this_row = 0;
            int hits_all_rows = 0;
            int docs_with_hits = 0;

            for(int c = 0; c < cols; c++) {
                hits_this_row += array[3 * (c + p * cols) + 2];
                hits_all_rows += array[3 * (c + p * cols) + 3];
                docs_with_hits += array[3 * (c + p * cols) + 4];
            }

            result[counter] = hits_this_row;
            result[counter + 1] = hits_all_rows;
            result[counter + 2] = docs_with_hits;

            counter += 3;
        }

        return result;
    }

    /*
    * This function takes a cursor with a search result
    * and performs the TFxIDF ranking algorithm using
    * the information provided by the matchinfo auxiliary
    * function from the module FTS3 from SQLite.
    */
    public static int[] calcTfIdf(Context context, Cursor cursor) {

        // TODO usar função e testar

        if (cursor == null)
            return null;
        // Array to store the tfxidf value of each row from the result
        ArrayList<Double> valuesArray = new ArrayList<>();

        // Total number of rows in the table
        int totalDocs = (int) DatabaseTable.getInstance(context).getRowCount();

        // Document Frequency for each searched term
        long[] documentFrequency = new long[searchTerms.length];

        // Getting the document frequency for each terms from the database
        for (int i = 0; i < searchTerms.length; i++) {
            // TODO Remove Log
            Log.d("TFXIDF","Getting DF value for: " + searchTerms[i]);

            documentFrequency[i] = DatabaseTable.getInstance(context).getDocumentFrequency(searchTerms[i]);

            Log.d("TFXIDF", "Value is: " + documentFrequency[i]);
        }

        // Iterating over each result (row);
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            // Index of the matchinfo column in the cursor
            int colIndex = cursor.getColumnIndex(DatabaseTable.COL_MATCHINFO);
            // Retrieving information
            byte[] blob = cursor.getBlob(colIndex);
            // Parsing the byte blob to an int array
            int[] parsed = DatabaseTable.parseMatchInfoBlob(blob);
            // Collapsing information from all columns to a single row
            int[] shortened = TfIdfHelper.shortenInitialArray(parsed);
            // Number of phrases in the query
            int phrases = shortened[0];
            // Variable to accumulate all tfxIdf values for a given row
            double accumulator = 0;

            // Go through all the phrases and calculate each tfxidf value
            for(int i = 0; i < phrases; i++) {

                // Term Frequency
                int tf = shortened[(i * 3) + 2];
                // Inverted document frequency
                double idf = Math.log(totalDocs/documentFrequency[i]);
                // Tf x Idf value for 1 phrase
                double result = tf * idf;
                // Add value to the total of the row
                accumulator += result;
            }

            // Add the row value to the result array
            valuesArray.add(accumulator);
        }

        // TODO Remove print
        int[] result = getOrderedIndexes(valuesArray);
        Log.d("TF IDF INDEXES", Arrays.toString(result));

        Log.d("TF IDF VALUES",valuesArray.toString());

        return result;
    }

    private static int[] getOrderedIndexes(ArrayList<Double> valuesArray) {

        // TODO Verificar este código e passar para 1 função
        int[] orderIndexes = new int[valuesArray.size()];

        TreeMap<Double, Integer> map = new TreeMap<>();

        for(int i = 0; i < valuesArray.size(); i++) {
            map.put(valuesArray.get(i) * valuesArray.size() + i, i);
        }

        int t = valuesArray.size();

        for(Integer index: map.values()) {
            orderIndexes[--t] = index;
        }

        return orderIndexes;
    }
}
