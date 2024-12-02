package com.apgautomation.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.apgautomation.R;
import com.apgautomation.model.VisitSummaryModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class VisitSummaryAdapter extends ArrayAdapter {
    Context context;
    public int isFsrShow=0;
    ArrayList<VisitSummaryModel> list;
    public VisitSummaryAdapter(@NonNull Context context, int resource, ArrayList<VisitSummaryModel> list) {
        super(context, resource, list);
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.item_visit_summary,null);
        // convertView.findViewById(R.id.ln).setVisibility(View.VISIBLE);

        TextView  txtTitile,scheduleCount,completeCount;
        txtTitile=convertView.findViewById(R.id.completeCount);
        scheduleCount=convertView.findViewById(R.id.scheduleCount);
        completeCount=convertView.findViewById(R.id.completeCount);
        txtTitile=convertView.findViewById(R.id.txtTitile);
       // completeCount=convertView.findViewById(R.id.completeCount);
        txtTitile.setText(list.get(position).EmpName);
        scheduleCount.setText(list.get(position).PlannedCnt+"");
        completeCount.setText(list.get(position).CompletedCnt+"");
        if(isFsrShow==1) {
          convertView.findViewById(R.id.linearFSRR).setVisibility(View.VISIBLE);
          TextView txtFSRCnt= convertView.findViewById(R.id.txtFSRCnt);
          txtFSRCnt.setText(list.get(position).FsrCnt+"");
        }
        return convertView;
    }
}
