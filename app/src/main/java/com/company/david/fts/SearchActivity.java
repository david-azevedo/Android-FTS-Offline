package com.company.david.fts;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.company.david.fts.Data.DatabaseTable;
import com.company.david.fts.Utils.PerformanceTime;

import java.util.Calendar;

public class SearchActivity extends AppCompatActivity {

    private AutoCompleteTextView mSearchData;
    private Button mSearchButton;
    private RecyclerView mResults;
    private SwitchCompat m4GramSwitch;
    private SwitchCompat mDateSwitch;
    private SwitchCompat mSynonymSwitch;
    private SearchResultsAdapter mAdapter;
    private SearchTask mAsyncTask;
    private String mQuery = "";
    private boolean mLast4gramState = false;
    private boolean mLastDateState = false;
    private boolean mLastSynonymState = false;
    private Toast mToast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setTitle("Search data");

        mSearchData = findViewById(R.id.et_search_query);
        mSearchButton = findViewById(R.id.bt_search_action);
        mResults = findViewById(R.id.rv_show_results);
        m4GramSwitch = findViewById(R.id.sw_4gram);
        mDateSwitch = findViewById(R.id.sw_date);
        mSynonymSwitch = findViewById(R.id.sw_synonym);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {

                String query = mSearchData.getText().toString().trim();

                if (query.equals("") ||
                        (query.equals(mQuery) &&
                        mLast4gramState == m4GramSwitch.isChecked() &&
                        mLastDateState == mDateSwitch.isChecked() &&
                        mLastSynonymState == mSynonymSwitch.isChecked()))
                    return;

                query = query.replaceAll("[.,]","");
                mQuery = query;
                mLast4gramState = m4GramSwitch.isChecked();
                mLastDateState = mDateSwitch.isChecked();
                mLastSynonymState = mSynonymSwitch.isChecked();
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
            return DatabaseTable.getInstance(getBaseContext()).getWordMatches(args[0],null,
                    m4GramSwitch.isChecked(), mDateSwitch.isChecked(), mSynonymSwitch.isChecked());
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

            PerformanceTime.setT5(Calendar.getInstance().getTimeInMillis());
            mAdapter.swapCursor(cursor);
            PerformanceTime.setT6(Calendar.getInstance().getTimeInMillis());
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
