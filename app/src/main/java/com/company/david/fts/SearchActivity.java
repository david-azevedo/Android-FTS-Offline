package com.company.david.fts;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.company.david.fts.Data.DatabaseTable;
import com.company.david.fts.Utils.PerformanceTime;

import java.util.Calendar;
import java.util.Date;

public class SearchActivity extends AppCompatActivity {

    private AutoCompleteTextView mSearchData;
    private Button mSearchButton;
    private RecyclerView mResults;
    private SearchResultsAdapter mAdapter;
    private SearchTask mAsyncTask;
    private String mQuery = "";
    private Toast mToast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setTitle("Search data");

        mSearchData = findViewById(R.id.et_search_query);
        mSearchButton = findViewById(R.id.bt_search_action);
        mResults = findViewById(R.id.rv_show_results);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {

                String query = mSearchData.getText().toString().trim();

                if (query.equals("") || query.equals(mQuery))
                    return;

                query = query.replaceAll("[.,]","");
                mQuery = query;
                if(mAsyncTask != null) {
                    mAsyncTask.cancel(true);
                }
                mAsyncTask = new SearchTask();
                mAsyncTask.execute(query);

            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mResults.setLayoutManager(linearLayoutManager);
        mResults.setHasFixedSize(true);
        mAdapter = new SearchResultsAdapter(this);
        mResults.setAdapter(mAdapter);

    }

    private class SearchTask extends AsyncTask<String, Void, Cursor> {

        @Override
        protected Cursor doInBackground(String... args) {
            PerformanceTime.setT1(Calendar.getInstance().getTimeInMillis());
            if(isCancelled())
                return null;
            return DatabaseTable.getInstance(getBaseContext()).getWordMatches(args[0],null);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {

            if(cursor ==  null || isCancelled()) {
                showToast("No results");
                return;
            }

            clearResults();
            if(cursor.getCount() <= 0)
                return;

            if(isCancelled())
                return;

            PerformanceTime.setT4(Calendar.getInstance().getTimeInMillis());
            mAdapter.swapCursor(cursor);
            PerformanceTime.setT5(Calendar.getInstance().getTimeInMillis());
            showToast( "Total results: " + cursor.getCount() + "\n" + PerformanceTime.getToastMessage());
        }
    }

    private void showToast(String message) {
        if (mToast != null)
            mToast.cancel();

        mToast = Toast.makeText(this, message,Toast.LENGTH_LONG);

        mToast.show();
    }

    private void clearResults() {
        mAdapter.swapCursor(null);
    }
}
