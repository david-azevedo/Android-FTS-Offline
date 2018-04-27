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
        DatabaseTable db = DatabaseTable.getInstance(getBaseContext());
        // DOCTOR | HOSPITAL | TRANSCRIPT

        /* PARTICIPANTE 1
        // A
        db.addNewEntry("Dr. João", "Hospital de São João", "Consulta de medicina geral devido a uma dor de garganta. Foi receitado brufen.");
        // B
        db.addNewEntry("Dra. Marta","Hospital de São João","Consulta de medicina geral por crise de alergia. Foi receitado brufen e anti-alérgico.");
        // C
        db.addNewEntry("Dr. João","Hospital da Maia","Consulta de medicina dentária devido a dor de dentes.");
        // D
        db.addNewEntry("Dra. Ana","Hospital da Maia","Consulta de pediatria.");
        // E
        db.addNewEntry("Dra. Marta","Hospital da Trofa","Consulta de medicina geral devido a constipação e febre. Foi receitado brufen e benuron.");
        // F
        db.addNewEntry("Dr. Joao","Hospital da Trofa","consulta de medicina dentária. Foi arrancado um dente.");
        // G
        db.addNewEntry("Dr. Pedro","Hospital de São João","consulta de dermatologia. Irritação na pela e vermelhidão.");
        // H
        db.addNewEntry("Dr. Neto","Hospital de São João","consulta de dermatologia.");
        // I
        db.addNewEntry("Dra. Sara","Hospital da Trofa","Consulta de ortopedia devido a dor no joelho. Aconselhada cirurgia.");
        // J
        db.addNewEntry("Dra. Sara","Hospital da Trofa","consulta de ortopedia. Receitado analgésico.");
        */

        /* PARTICIPANTE 2
        // A
        db.addNewEntry();
        // B
        db.addNewEntry();
        // C
        db.addNewEntry();
        // D
        db.addNewEntry();
        // E
        db.addNewEntry();
        // F
        db.addNewEntry();
        // G
        db.addNewEntry();
        // H
        db.addNewEntry();
        // I
        db.addNewEntry();
        // J
        db.addNewEntry();
        */

        /* Fake data */
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
