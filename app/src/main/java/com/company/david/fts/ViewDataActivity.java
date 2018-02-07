package com.company.david.fts;

import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.company.david.fts.Data.DatabaseTable;

import java.util.Date;

public class ViewDataActivity extends AppCompatActivity {

    private RecyclerView mResults;
    private SearchResultsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);

        mResults = findViewById(R.id.rv_list_results);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mResults.setLayoutManager(linearLayoutManager);
        mResults.setHasFixedSize(true);
        mAdapter = new SearchResultsAdapter(this);
        mResults.setAdapter(mAdapter);

        new SearchTask().execute();
    }

    private class SearchTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... args) {
            return DatabaseTable.getInstance(getBaseContext()).getAllRows();
        }

        @Override
        protected void onPostExecute(Cursor cursor) {

            if (cursor == null) {
                Log.d("VIEW DATA", "Cursor is null");
                return;
            }

            Log.d("VIEW DATA", "Search ended with " + cursor.getCount() + " results");

            if(cursor.getCount() <= 0) {
                return;
            }
            mAdapter.swapCursor(cursor);
        }
    }
}
