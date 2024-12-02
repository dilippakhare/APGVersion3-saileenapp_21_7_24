package com.apgautomation.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.apgautomation.R;
import com.apgautomation.ui.enquiry.EnquiryDTO;
import com.apgautomation.utility.CommonShare;

import java.util.ArrayList;

public class EnquiryAdapter extends ArrayAdapter {
    Context context;
    ArrayList<EnquiryDTO> list;
    public EnquiryAdapter(@NonNull Context context, int resource, ArrayList<EnquiryDTO> list) {
        super(context, resource, list);
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.item_enquiry,null);
        TextView txtTitle=convertView.findViewById(R.id.txtTitle);
        TextView txtEngName=convertView.findViewById(R.id.txtEngName);
        TextView txtScheduleDate=convertView.findViewById(R.id.txtScheduleDate);
        TextView txtStatus=convertView.findViewById(R.id.txtStatus);
        TextView txtCustomer=convertView.findViewById(R.id.txtCustomer);
       // ImageView imgLeft=convertView.findViewById(R.id.imgLeft);


        EnquiryDTO model=list.get(position);
        txtTitle.setText(model.getGroupName());
        txtScheduleDate.setText(CommonShare.convertToDate(CommonShare.parseDate(model.getEnterDate())));
        txtEngName.setText(model.getEmpName());
        txtStatus.setText(model.getEnquiryVerification());
        txtCustomer.setText(model.getCustomerName());

        if(txtStatus.getText().toString().equalsIgnoreCase("Started"))
        {
            txtStatus.setTextColor(Color.GREEN);
        }
        return convertView;
    }
}
