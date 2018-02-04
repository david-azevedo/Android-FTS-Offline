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

        mSearchData = (EditText) findViewById(R.id.et_search_query);
        mSearchResults = (TextView) findViewById(R.id.tv_display_results);
        setTitle("Search data");
    }

    private void searchQuery(View v) {

    }
}
