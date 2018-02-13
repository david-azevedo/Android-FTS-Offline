package com.company.david.fts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.company.david.fts.Data.DatabaseTable;

import java.sql.Blob;

import javax.xml.transform.Result;


public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ResultViewHolder> {

    private Cursor mCursor;
    private Context mContext;

    public SearchResultsAdapter(@NonNull Context context) {
        mContext = context;
    }

    @Override
    public ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutID = R.layout.search_result_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutID, parent, false);
        ResultViewHolder result = new ResultViewHolder(view);

        return result;
    }


    @Override
    public void onBindViewHolder(ResultViewHolder holder, int position) {
        mCursor.moveToPosition(position);


        // FIXME Apagar isto
        byte[] blob = mCursor.getBlob(mCursor.getColumnIndex("MATCHINFO"));
        for(int i = 0; i < blob.length; i++) {
            Log.d("ADAPTER", Byte.toString(blob[i]));
        }

        String doctor = mCursor.getString(mCursor.getColumnIndex(DatabaseTable.COL_DOCTOR));
        String hospital = mCursor.getString(mCursor.getColumnIndex(DatabaseTable.COL_HOSPITAL));
        String transcript = mCursor.getString(mCursor.getColumnIndex(DatabaseTable.COL_TRANSCRIPT));

        holder.doctorName.setText(doctor);
        holder.hospitalName.setText(hospital);
        holder.displayTranscript.setText(transcript);
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    class ResultViewHolder extends RecyclerView.ViewHolder {

        TextView doctorName;
        TextView hospitalName;
        TextView displayTranscript;

        public ResultViewHolder(View itemView) {
            super(itemView);

            doctorName = itemView.findViewById(R.id.tv_display_doctor_name);
            hospitalName = itemView.findViewById(R.id.tv_display_hospital_name);
            displayTranscript = itemView.findViewById(R.id.tv_display_transcript);
        }
    }
}
