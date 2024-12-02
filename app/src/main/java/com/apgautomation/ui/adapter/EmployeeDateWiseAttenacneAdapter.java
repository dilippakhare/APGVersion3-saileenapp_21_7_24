package com.apgautomation.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.apgautomation.R;
import com.apgautomation.model.ServerModel.EmployeeAttendanceListModel;
import com.apgautomation.utility.CommonShare;

import java.util.ArrayList;

public class EmployeeDateWiseAttenacneAdapter extends ArrayAdapter {
   Context context;
   ArrayList<EmployeeAttendanceListModel> list;
    public EmployeeDateWiseAttenacneAdapter(@NonNull Context context, int resource, ArrayList<EmployeeAttendanceListModel> list) {
        super(context, resource, list);
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.item_daywiseattendance,null);
        TextView textStatus,txtDate,textCurrentStatus;
        txtDate=convertView.findViewById(R.id.txtDate);
        textStatus=convertView.findViewById(R.id.textStatus);
        EmployeeAttendanceListModel model=list.get(position);
        textStatus.setText(model.StatusName);
        textCurrentStatus=convertView.findViewById(R.id.textCurrentStatus);
        txtDate.setText( CommonShare.getDateTime(CommonShare.parseDate(model.AttendanceDate)).split(" ")[0] );
        if(model.CurrentStatus.equalsIgnoreCase("Started"))
        {
            textCurrentStatus.setText("Day Started");
            textCurrentStatus.setTextColor(Color.parseColor("#2196F3"));

        }
        if(model.CurrentStatus.equalsIgnoreCase("Closed"))
        {
            textCurrentStatus.setText("Day Closed");
            textCurrentStatus.setTextColor(Color.parseColor("#009688"));
        }

        return convertView;
    }
}
