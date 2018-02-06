package com.company.david.fts;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.company.david.fts.Data.DatabaseTable;

import java.util.Date;

public class SearchActivity extends AppCompatActivity {

    private EditText mSearchData;
    private TextView mSearchResults;
    private Button mSearchButton;
    private Toast mToast = null;

    // TODO replace for a recycler view

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setTitle("Search data");

        mSearchData = findViewById(R.id.et_search_query);
        mSearchResults = findViewById(R.id.tv_display_results);
        mSearchButton = findViewById(R.id.bt_search_action);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = mSearchData.getText().toString().trim();
                new AddFakeDataTask().execute(query);
            }
        });

        // TODO implement TextWatcher for when text changes in the et_search_query

    }

    private class AddFakeDataTask extends AsyncTask<String, Void, Cursor> {

        private long startTime = 0;
        private Date mDate = new Date();

        @Override
        protected Cursor doInBackground(String... args) {
            startTime = mDate.getTime();
            return DatabaseTable.getInstance(getBaseContext()).getWordMatches(args[0],null);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {

            if(cursor ==  null) {
                showToast("No results");
                return;
            }

            long timeElapsed = mDate.getTime() - startTime;
            showToast( cursor.getCount() + " results in " + timeElapsed + " ms.");
            clearResults();
            if(!cursor.moveToFirst())
                return;

            do {
                int index = cursor.getColumnIndex(DatabaseTable.COL_DOCTOR);
                String doctorName = cursor.getString(index);
                mSearchResults.append("Doctor: " + doctorName + "\n\n");
            } while(cursor.moveToNext());
            cursor.close();
        }
    }

    private void showToast(String message) {
        if (mToast != null)
            mToast.cancel();

        mToast = Toast.makeText(this, message,Toast.LENGTH_LONG);

        mToast.show();
    }

    private void clearResults() {
        mSearchResults.setText("");
    }
}
