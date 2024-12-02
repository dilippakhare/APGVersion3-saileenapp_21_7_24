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
import com.apgautomation.model.GsonVisitMasterNnRealm;
import com.apgautomation.utility.CommonShare;

import java.util.ArrayList;
import java.util.Date;

public class AllVisitScheduledapter extends ArrayAdapter {
    Context context;
    ArrayList<GsonVisitMasterNnRealm> list;
    public AllVisitScheduledapter(@NonNull Context context, int resource, ArrayList<GsonVisitMasterNnRealm> list) {
        super(context, resource, list);
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.item_visitschedule,null);

        try
        {
            TextView txtTitle = convertView.findViewById(R.id.txtTitle);
            TextView txtEngName = convertView.findViewById(R.id.txtEngName);
            TextView txtScheduleDate = convertView.findViewById(R.id.txtScheduleDate);
            TextView txtStatus = convertView.findViewById(R.id.txtStatus);
            // ImageView imgLeft=convertView.findViewById(R.id.imgLeft);


            GsonVisitMasterNnRealm model = list.get(position);
            txtTitle.setText(model.getGroupName());
            txtScheduleDate.setText(CommonShare.convertToDate(CommonShare.parseDate(model.getScheduleDate())));
            txtEngName.setText(model.getEmpName());
            txtStatus.setText(model.getVisitStatus());

            if (txtStatus.getText().toString().equalsIgnoreCase("Started")) {
                txtStatus.setTextColor(Color.GREEN);
            }

            try {
                // GsonVisitMaster employeeModelObj=  model;
                if (model.getBookByEmpId() == model.getAssigntoEmpId()) {
                    Date date = new Date(CommonShare.parseDate(model.getEnterDate()));
                    Date today = new Date(CommonShare.parseDate(model.getScheduleDate()));

                    //long difference=  (date.getTime()-today.getTime()) / (24 * 60 * 60 * 1000); //date.compareTo(today);
                    if (date.getDay() == today.getDay() && date.getMonth() == today.getMonth() && date.getYear() == today.getYear())
                        if (date.getHours() > 9) {
                            //  msg=msg+"\nThis visit will be marked as unplanned.Try to schedule visit one day before.";
                            txtStatus.setText(txtStatus.getText().toString() + "(Unplanned Visit)");
                        }


                }

            } catch (Exception ex) {
            }
            try
            {
                TextView txtvisitDate =convertView.findViewById(R.id.txtvisitDate);
                if( model.getStartTime().length()>0)
                    txtvisitDate.setText(CommonShare.convertToDate(  CommonShare.parseDate(  model.getStartTime())));
            }
            catch (Exception ex)
            {}
        }catch (Exception ex)
        {
          //  CommonShare.alert(context,ex.toString());
           // CommonShare.alert(context,list.size()+"");
        }
        return convertView;
    }
}
