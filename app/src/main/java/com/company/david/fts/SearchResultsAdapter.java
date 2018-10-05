package com.company.david.fts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
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
import com.company.david.fts.Utils.TfIdfHelper;

import java.sql.Blob;
import java.util.Arrays;
import java.util.Calendar;

import javax.xml.transform.Result;


public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ResultViewHolder> {

    private Cursor mCursor;
    private Context mContext;
    private int[] offset = null;

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

        if(offset != null) {
            mCursor.moveToPosition(offset[position]);
        } else {
            mCursor.moveToPosition(position);
        }


        String doctor = mCursor.getString(mCursor.getColumnIndex(DatabaseTable.COL_DOCTOR));
        String hospital = mCursor.getString(mCursor.getColumnIndex(DatabaseTable.COL_HOSPITAL));
        String transcript = mCursor.getString(mCursor.getColumnIndex(DatabaseTable.COL_TRANSCRIPT));
        String date = mCursor.getString(mCursor.getColumnIndex(DatabaseTable.COL_DATE));

        holder.doctorName.setText(doctor);
        holder.hospitalName.setText(hospital);
        holder.displayTranscript.setText(transcript);
        holder.displayDate.setText(date);
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    void swapCursor(Cursor newCursor) {

        offset = null;

        mCursor = newCursor;

        if (mContext.getClass() == SearchActivity.class) {
            Log.d("SEARCHRESULTSADAPTER", "Calculating Tf x Idf");
            offset = TfIdfHelper.calcTfIdf(this.mContext, mCursor);
            Log.d("SEARCHRESULTSADAPTER", "Tf x Idf finished");
        }

        notifyDataSetChanged();
    }

    class ResultViewHolder extends RecyclerView.ViewHolder {

        TextView doctorName;
        TextView hospitalName;
        TextView displayTranscript;
        TextView displayDate;

        public ResultViewHolder(View itemView) {
            super(itemView);

            doctorName = itemView.findViewById(R.id.tv_display_doctor_name);
            hospitalName = itemView.findViewById(R.id.tv_display_hospital_name);
            displayTranscript = itemView.findViewById(R.id.tv_display_transcript);
            displayDate = itemView.findViewById(R.id.tv_display_date);
        }
    }
}
