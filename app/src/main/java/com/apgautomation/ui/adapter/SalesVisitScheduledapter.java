package com.apgautomation.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.apgautomation.R;
import com.apgautomation.model.ServerModel.GsonSalesVisit;
import com.apgautomation.utility.CommonShare;

import java.util.ArrayList;
import java.util.Date;

public class SalesVisitScheduledapter extends ArrayAdapter {
    Context context;
    ArrayList<GsonSalesVisit> list;
    public SalesVisitScheduledapter(@NonNull Context context, int resource, ArrayList<GsonSalesVisit> list) {
        super(context, resource, list);
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.item_visitsales,null);
        TextView txtTitle=convertView.findViewById(R.id.txtTitle);
        TextView txtEngName=convertView.findViewById(R.id.txtEngName);
        TextView txtScheduleDate=convertView.findViewById(R.id.txtScheduleDate);
        TextView txtStatus=convertView.findViewById(R.id.txtStatus);
        TextView txtCustomer=convertView.findViewById(R.id.txtCustomer);
       // ImageView imgLeft=convertView.findViewById(R.id.imgLeft);


        GsonSalesVisit model=list.get(position);
        txtTitle.setText(model.getVGroupName());
        txtScheduleDate.setText(CommonShare.convertToDate(model.getScheduleDateMillisecond()));
        txtEngName.setText(model.getAssignToName());
        txtStatus.setText(model.getVisitStatus());
        txtCustomer.setText(model.getVCustomerName());

        if(txtStatus.getText().toString().equalsIgnoreCase("Started"))
        {
            txtStatus.setTextColor(Color.GREEN);
        }
        try
        {
            GsonSalesVisit employeeModelObj=  model;
            if(employeeModelObj.getAssignToId()==employeeModelObj.getEnterById() )
            {
                Date date = new Date(   (model.getEnteredDateMillisecond()));
                Date today=new Date( CommonShare.parseDate( model.getScheduleDate()));

                long difference=  (date.getTime()-today.getTime()) / (24 * 60 * 60 * 1000); //date.compareTo(today);
                if(date.getDay()==today.getDay()  &&  date.getMonth()==today.getMonth() && date.getYear()==today.getYear())
                    if(date.getHours()>9)
                {
                  //  msg=msg+"\nThis visit will be marked as unplanned.Try to schedule visit one day before.";
                    txtStatus.setText(txtStatus.getText().toString()+"(Unplanned Visit)");
                }


            }

        }
        catch (Exception ex)
        {
            Log.d("com.apgautomation",ex.toString());
        }
        return convertView;
    }
}
