package com.company.david.fts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.company.david.fts.Data.DatabaseTable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mButtonEnterData = findViewById(R.id.bt_enter_data);
        Button mButtonSearchData = findViewById(R.id.bt_search_data);
        Button mButtonViewData = findViewById(R.id.bt_view_data);

        mButtonEnterData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EnterDataActivity.class);
                startActivity(intent);
            }
        });

        mButtonSearchData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        mButtonViewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ViewDataActivity.class);
                startActivity(intent);
            }
        });
    }
}
