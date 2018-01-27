package com.company.david.fts;

import android.icu.lang.UCharacterEnums;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

public class EnterDataActivity extends AppCompatActivity {

    private EditText mHospitalName;
    private EditText mDoctorName;
    private EditText mTranscript;
    private Button mAddButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_data);

        setTitle("Enter Data");
        mHospitalName = findViewById(R.id.et_hospital_name);
        mDoctorName = findViewById(R.id.et_doctor_name);
        mTranscript = findViewById(R.id.et_transcript);
        mAddButton = findViewById(R.id.bt_submit_data);
    }
}
