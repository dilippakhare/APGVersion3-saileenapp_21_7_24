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

public class EmployeeRecentAttenacneAdapter extends ArrayAdapter {
   Context context;
   ArrayList<EmployeeAttendanceListModel> list;
    public EmployeeRecentAttenacneAdapter(@NonNull Context context, int resource,  ArrayList<EmployeeAttendanceListModel> list) {
        super(context, resource, list);
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.item_recent_attendance,null);
        TextView textStatus,textDept,textName,textCurrentStatus,txtTime;
        textName=convertView.findViewById(R.id.textName);
        textDept=convertView.findViewById(R.id.textDept);
        textStatus=convertView.findViewById(R.id.textStatus);
        textCurrentStatus=convertView.findViewById(R.id.textCurrentStatus);
        txtTime=convertView.findViewById(R.id.txtTime);
        EmployeeAttendanceListModel model=list.get(position);
        textName.setText(model.EmpName);
        textDept.setText(model.DeptName);
        textStatus.setText(model.StatusName);
        txtTime.setText(CommonShare.getDateTime(CommonShare.parseDate(  model.AttendanceDate)).split(" ")[1]
                          +" "+CommonShare.getDateTime(CommonShare.parseDate(  model.AttendanceDate)).split(" ")[2]
                           );
        try {
            if(txtTime.getText().toString().contains("1970"))
            {
                txtTime.setText("");
            }
        }catch (Exception ex){}
        if(model.CurrentStatus.equalsIgnoreCase("Started"))
        {
            textCurrentStatus.setText("Day Started");
        }
        else if(model.CurrentStatus.equalsIgnoreCase("Closed"))
        {
            textCurrentStatus.setText("Day Closed");
            textCurrentStatus.setTextColor(Color.parseColor("#009688"));
        }
        else if(model.CurrentStatus.equalsIgnoreCase("leave"))
        {
            textStatus.setText("On Leave");
           // textStatus.setText(" Leave");
            textStatus.setTextColor(Color.rgb(204,204,0));
        }
        else if(model.CurrentStatus.equalsIgnoreCase("Weekly off"))
        {
            textStatus.setText("Weekly off");
            // textStatus.setText(" Leave");
            textStatus.setTextColor(Color.rgb(204,204,0));
        }
        else if(model.StatusName==null || model.StatusName.equalsIgnoreCase(""))
        {
            textStatus.setText("Not Marked");
            textStatus.setTextColor(Color.rgb(255,0,0));
        }

        return convertView;
    }
}
