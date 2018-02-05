package com.company.david.fts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SearchActivity extends AppCompatActivity {

    private EditText mSearchData;
    private TextView mSearchResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setTitle("Search data");

        mSearchData = findViewById(R.id.et_search_query);
        mSearchResults = findViewById(R.id.tv_display_results);

        // TODO add search funcionality
        // TODO implement TextWatcher for when text changes in the et_search_query

    }

    private void searchQuery(View v) {

    }
}
