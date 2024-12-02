package com.apgautomation.ui.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.apgautomation.R;
import com.apgautomation.model.ServerModel.ServerComplaintModel;
import com.apgautomation.utility.CommonShare;

import java.util.ArrayList;

public class ComplaintGridAdapter extends ArrayAdapter {
    Context context;
    ArrayList<ServerComplaintModel> list;
    public ComplaintGridAdapter(@NonNull Context context, int resource, ArrayList<ServerComplaintModel> list) {
        super(context, resource, list);
        this.context=context;
        this.list=list;
    }

    public boolean isCustomerNameVisible=false;
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.item_complaint_grid,null);
       // convertView.findViewById(R.id.ln).setVisibility(View.VISIBLE);
        TextView txtDate,txtEquipment;
        TextView txtCustomer;

        txtCustomer=convertView.findViewById(R.id.txtCustomer);
        txtDate=convertView.findViewById(R.id.txtDate);
        txtEquipment=convertView.findViewById(R.id.txtEquipment);


        ServerComplaintModel model=list.get(position);
       // txtAmcStart.setText(CommonShare.convertToDate(  CommonShare.parseDate( model.AmcStartDate)));

        txtDate.setText("C.No.:-"+model.ComplaintId+"\n"+ CommonShare.getDateTime(CommonShare.parseDate( model.EnterDate)).split(" ")[0]);
        txtEquipment.setText(model.EquipmentName);
        txtCustomer.setText(model.CustomerName);



        return convertView;
    }
}
