package com.apgautomation.ui.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.apgautomation.R;
import com.apgautomation.model.ServerModel.ServerComplaintModel;
import com.apgautomation.utility.CommonShare;

import java.util.ArrayList;

public class ComplaintAdapter extends ArrayAdapter {
    Context context;
    ArrayList<ServerComplaintModel> list;
    public ComplaintAdapter(@NonNull Context context, int resource, ArrayList<ServerComplaintModel> list) {
        super(context, resource, list);
        this.context=context;
        this.list=list;
    }

    public boolean isCustomerNameVisible=false;
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.item_complaint,null);
       // convertView.findViewById(R.id.ln).setVisibility(View.VISIBLE);
        TextView txtDate,txtEquipment,txtEquipmentType,txtSerailNumber,txtComplaint,txtStatus,txtNO;
        TextView txtCustomer;
        txtCustomer=convertView.findViewById(R.id.txtCustomer);




        Button btnBook;
        txtDate=convertView.findViewById(R.id.txtDate);
        txtEquipment=convertView.findViewById(R.id.txtEquipment);
        txtEquipmentType=convertView.findViewById(R.id.txtEquipmentType);

        txtSerailNumber=convertView.findViewById(R.id.txtSerailNumber);
        txtComplaint=convertView.findViewById(R.id.txtComplaint);
        txtStatus=convertView.findViewById(R.id.txtStatus);

        ServerComplaintModel model=list.get(position);
       // txtAmcStart.setText(CommonShare.convertToDate(  CommonShare.parseDate( model.AmcStartDate)));
        txtDate.setText(CommonShare.convertToDate(  CommonShare.parseDate( model.EnterDate)));
        txtEquipment.setText(model.EquipmentName);
        txtEquipmentType.setText(model.SelectedEquipment);


        txtComplaint.setText(model.ComplaintDetails);
        txtSerailNumber.setText(model.SerialNumber);
        //txtStatus=convertView.findViewById(R.id.txtStatus);
        txtStatus.setText(model.ComplaintStatus);
        txtNO=convertView.findViewById(R.id.txtNO);
        txtNO.setText("Complaint No :-"+model.ComplaintId+"");

       // txtPort.setText(model.PortNo);
       // txtSerial.setText(model.SerialNumber);
        //Linkify.addLinks(txtDesc, Linkify.ALL);
        if(isCustomerNameVisible)
        {
            txtCustomer.setVisibility(View.VISIBLE);
            txtCustomer.setText(model.CustomerName+"("+model.GroupName+","+model.Area+")");
        }

        return convertView;
    }
}
