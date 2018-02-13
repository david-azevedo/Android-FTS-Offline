package com.company.david.fts;

import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.company.david.fts.Data.DatabaseTable;
import com.company.david.fts.Utils.AppointmentUtils;

public class EnterDataActivity extends AppCompatActivity {

    private EditText mHospitalName;
    private EditText mDoctorName;
    private EditText mTranscript;
    private Button mAddButton;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_data);

        DatabaseTable.getInstance(this);

        setTitle("Enter Data");
        mHospitalName = findViewById(R.id.et_hospital_name);
        mDoctorName = findViewById(R.id.et_doctor_name);
        mTranscript = findViewById(R.id.et_transcript);
        mAddButton = findViewById(R.id.bt_submit_data);

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddDataTask().execute();
            }
        });

        mAddButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AddFakeDataTask().execute();
                return true;
            }
        });

    }

    private class AddFakeDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            addInfo();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            addToast("Entries added");
            clearInputs();
        }
    }

    private class AddDataTask extends AsyncTask<Void, Void, Long> {
        protected Long doInBackground(Void... voids) {
            return storeInfo();
        }

        protected void onPostExecute(Long result) {
            String idString = String.valueOf(result);
            addToast("New row with id: " + idString);
            clearInputs();
        }
    }

    private void addInfo () {

        int i = 0;
        while (i <= 10) {
            String[] temp = AppointmentUtils.getFakeData();
            DatabaseTable.getInstance(getBaseContext()).addNewEntry(temp[0], temp[1], temp[2]);
            i++;
        }

    }

    // Function to store data and show a Toast (run in the different thread)
    private long storeInfo() {

        String hospital = mHospitalName.getText().toString();
        String doctor = mDoctorName.getText().toString();
        String transcript = mTranscript.getText().toString();

        if (hospital.equals("") || doctor.equals("") || transcript.equals(""))
            return -1;

        long result = DatabaseTable.getInstance(getBaseContext()).addNewEntry(doctor,hospital,transcript);

        return result;

    }

    // Function to clear text inputs
    private void clearInputs() {
        mHospitalName.getText().clear();
        mDoctorName.getText().clear();
        mTranscript.getText().clear();
        mTranscript.clearFocus();
    }

    // Function to display a new toast when data is inserted
    private void addToast(String message) {
        if (mToast != null) {
            mToast.cancel();
        }

        mToast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
        mToast.show();
    }
}
