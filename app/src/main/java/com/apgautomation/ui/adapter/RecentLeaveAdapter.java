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
import com.apgautomation.model.LeaveDetails;
import com.apgautomation.model.LeaveModel;
import com.apgautomation.utility.CommonShare;

import java.util.ArrayList;

public class RecentLeaveAdapter extends ArrayAdapter {
    Context context;
    ArrayList<LeaveModel> list;
    public RecentLeaveAdapter(@NonNull Context context, int resource, ArrayList<LeaveModel> list) {
        super(context, resource, list);
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.item_leaveapplication,null);
        TextView txtPlaanedType,txtResponsible,txtApplicationDate,txtEmpName,txtNumberOfLeave,txtSendToName,txtLeaveDates,txtApprovalStatus,txtApprovalRemarks;
        txtApplicationDate=convertView.findViewById(R.id.txtApplicationDate);
        txtEmpName=convertView.findViewById(R.id.txtEmpName);
        txtSendToName=convertView.findViewById(R.id.txtSendToName);
        txtNumberOfLeave=convertView.findViewById(R.id.txtNumberOfLeave);
        txtApprovalStatus=convertView.findViewById(R.id.txtApprovalStatus);
        txtApprovalRemarks=convertView.findViewById(R.id.txtApprovalRemarks);
        txtLeaveDates=convertView.findViewById(R.id.txtLeaveDates);
        txtResponsible=convertView.findViewById(R.id.txtResponsible);
        LeaveModel model=list.get(position);
        txtApplicationDate.setText(CommonShare.convertToDate( model.EnterTime));
        txtEmpName.setText(CommonShare.empMap.get(model.EmpId).EmpName);
        txtSendToName.setText(CommonShare.empMap.get(model.ToId).EmpName);
        try
        {
            txtResponsible.setText("");
            txtResponsible.setText(CommonShare.empMap.get(model.ResponsiblePersonId).EmpName);
        }
        catch (Exception ex){}
        txtNumberOfLeave.setText(model.TotalDays+"");
        if(model.ApproveStatus==null)
        {
            model.ApproveStatus="";
        }
        else
        {
            if(model.ApproveStatus.equalsIgnoreCase("Approved"))
            {
                txtApprovalStatus.setTextColor(Color.GREEN);
            }
            if(model.ApproveStatus.equalsIgnoreCase("Reject"))
            {
                txtApprovalStatus.setTextColor(Color.RED);
            }
        }

        if(model.ApproveRemark==null)
            model.ApproveRemark="";

        txtApprovalStatus.setText(model.ApproveStatus==null? "":model.ApproveStatus);
        txtApprovalRemarks.setText(model.ApproveRemark);
        try
        {
            String dates="";
            for (LeaveDetails detais:model.leaveDetails
                 )
            {
                if(dates.equalsIgnoreCase(""))
                    dates=CommonShare.convertToDate(detais.Ldate)+"("+detais.LeaveType+")";
                else
                    dates=dates+"\n"+CommonShare.convertToDate(detais.Ldate)+"("+detais.LeaveType+")";
            }

            txtLeaveDates.setText(dates);
        }
        catch (Exception ew){}
        txtPlaanedType=convertView.findViewById(R.id.txtPlaanedType);
        if(checkForunPlanned(model).equalsIgnoreCase("Unplanned Leave"))
        {
            txtPlaanedType.setText("Unplanned Leave");
            txtPlaanedType.setTextColor(Color.RED);
        }
        return convertView;
    }


    String  checkForunPlanned(LeaveModel model)
    {
        ArrayList<LeaveDetails> detais=model.leaveDetails;
        long closeDate=0l;
        for(int i=0;i<detais.size();i++)
        {
            if(closeDate==0)
            {
                closeDate=detais.get(i).Ldate;
            }
            else
            {
                long newDate=detais.get(i).Ldate;;
                if(newDate<closeDate)
                    closeDate=newDate;
            }
        }
        long schduleDate=  closeDate+(1000*60*60*10);
        long cureentMillisecond=model.EnterTime;
        long timeDiffrence=schduleDate-cureentMillisecond;

        if(timeDiffrence>  (1000*60*60*40))
        {
            return "Planned Leave";
        }
        return "Unplanned Leave";
    }
}
