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
import com.apgautomation.model.EmployeeModel;
import com.apgautomation.model.EmployeewithattendanceModel;
import com.apgautomation.model.ServerModel.EmployeeAttendanceListModel;

import java.util.ArrayList;

public class EmployeeRecentAttenacneAdapter1 extends ArrayAdapter {
   Context context;
   ArrayList<EmployeewithattendanceModel> list;
    public EmployeeRecentAttenacneAdapter1(@NonNull Context context, int resource, ArrayList<EmployeewithattendanceModel> list) {
        super(context, resource, list);
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.item_recent_attendance,null);
        TextView textStatus,textDept,textName,textCurrentStatus;
        textName=convertView.findViewById(R.id.textName);
        textDept=convertView.findViewById(R.id.textDept);
        textStatus=convertView.findViewById(R.id.textStatus);
        textCurrentStatus=convertView.findViewById(R.id.textCurrentStatus);
        EmployeeAttendanceListModel model=list.get(position).attendanceListModel;
        EmployeeModel model1=list.get(position).empModel;
        textName.setText(model1.EmpName);
        //textDept.setText(model1.DeptName);
        try
        {
        textStatus.setText(model.StatusName);
        if(model.CurrentStatus.equalsIgnoreCase("Started"))
        {
            textCurrentStatus.setText("Day Started");
        }
        if(model.CurrentStatus.equalsIgnoreCase("Closed"))
        {
            textCurrentStatus.setText("Day Closed");
            textCurrentStatus.setTextColor(Color.parseColor("#009688"));
        }
        }
        catch (Exception we)
        {}
        return convertView;
    }
}
