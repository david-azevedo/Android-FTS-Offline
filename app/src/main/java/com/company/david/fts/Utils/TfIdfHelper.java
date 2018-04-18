package com.company.david.fts.Utils;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.company.david.fts.Data.DatabaseTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class TfIdfHelper {

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
    public static void calcTfIdf(Context context, Cursor cursor) {

        if (cursor == null)
            return;
        // Array to store the tfxidf value of each row from the result
        ArrayList<Integer> valuesArray = new ArrayList<>();

        // Total number of rows in the table
        int totalDocs = (int) DatabaseTable.getInstance(context).getRowCount();

        // TODO talvez pedir a contagem de linhas onde cada termo aparece (pedidos extra à DB)
        // TODO usar o max dos documentos onde aparece o termo (por coluna)
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
            int accumulator = 0;

            // Go through all the phrases and calculate each tfxidf value
            for(int i = 0; i < phrases; i++) {

                // Term Frequency
                int tf = shortened[(i * 3) + 2];
                // Docs with hits
                int aux = shortened[(i * 3) + 4];
                // Docs with hits caped to be lower than totalDocs
                int docsWithHits = (aux < totalDocs)? aux : totalDocs;
                // Inverted document frequency
                double idf = Math.log(totalDocs / docsWithHits);
                // Tf x Idf value for 1 phrase
                int result = (int) (tf * idf);
                // Add value to the total of the row
                accumulator += result;
            }

            // Add the row value to the result array
            valuesArray.add(accumulator);
        }

        // TODO print
        Log.d("TF IDF INDEXES", Arrays.toString(getOrderedIndexes(valuesArray)));


        Log.d("TF IDF VALUES",valuesArray.toString());
    }

    private static int[] getOrderedIndexes(ArrayList<Integer> valuesArray) {

        // TODO Verificar este código e passar para 1 função
        int[] orderIndexes = new int[valuesArray.size()];

        TreeMap<Integer, Integer> map = new TreeMap<>();

        for(int i = 0; i < valuesArray.size(); i++) {
            map.put(valuesArray.get(i) * valuesArray.size() + i, i);
        }

        int t = 0;

        for(Integer index: map.values()) {
            orderIndexes[t++] = index;
        }

        return orderIndexes;
    }
}
