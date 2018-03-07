package com.company.david.fts.Utils;

import java.util.ArrayList;

public class TfIdfHelper {

    /* Function to reduce the amount of information the initial parsed array
    from matchinfo returns in order to calculate the tf * idf for each row
    */
    // TODO complete this function
    public static int[] shortenInitialArray(int[] array) {

        // Number of phrases in the query
        int phrases = array[0];

        // Number of user defined columns in the database
        int cols = array[1];

        // Creating the result array with the reduced sized
        int[] result = new int[((array.length - 2) / cols) + 2];

        // Counter for the results array;
        int counter = 2;

        // Loop through the array
        for(int p = 0; p < phrases; p++) {
            int hits_this_row = 0;
            int hits_all_rows = 0;
            int docs_with_hits = 0;
            for(int c = 0; c < cols; c++) {
                hits_this_row += array[3 * (c + p * cols)];
                hits_all_rows += array[3 * (c + p * cols) + 1];
                docs_with_hits += array[3 * (c + p * cols) + 2];
            }

            // TODO confirmar valores
            result[counter] = hits_this_row;
            result[counter+ 1] = hits_all_rows;
            result[counter + 2] = docs_with_hits;
            counter += 3;
        }

        return null;
    }
}
