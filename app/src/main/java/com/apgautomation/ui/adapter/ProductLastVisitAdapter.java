package com.apgautomation.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.apgautomation.R;
import com.apgautomation.model.GsonCustomerProduct;
import com.apgautomation.ui.complaint.BookComplaint;
import com.apgautomation.utility.CommonShare;

import java.util.ArrayList;
import java.util.Date;

public class ProductLastVisitAdapter extends ArrayAdapter {
    Context context;
    ArrayList<GsonCustomerProduct> list;
    public ProductLastVisitAdapter(@NonNull Context context, int resource, ArrayList<GsonCustomerProduct> list) {
        super(context, resource, list);
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.item_productlastvisit,null);
        TextView txtName,txtPort,txtType,txtSerial,txtAmcStart,txtAmcEnd,txtStatus;
        Button btnBook;
        txtName=convertView.findViewById(R.id.txtName);
        txtPort=convertView.findViewById(R.id.txtPort);
        txtType=convertView.findViewById(R.id.txtType);

        txtSerial=convertView.findViewById(R.id.txtSerial);
        txtAmcStart=convertView.findViewById(R.id.txtAmcStart);
        txtAmcEnd=convertView.findViewById(R.id.txtAmcEnd);
        txtStatus=convertView.findViewById(R.id.txtStatus);
        TextView txtLastVisit=convertView.findViewById(R.id.txtLastVisit);

        GsonCustomerProduct model=list.get(position);
        txtAmcStart.setText(CommonShare.convertToDate(  CommonShare.parseDate( model.getAmcStartDate())));
        txtAmcEnd.setText(CommonShare.convertToDate(  CommonShare.parseDate( model.getAmcEndDate())));
        txtName.setText(model.getEquipmentName());
        txtType.setText(model.getEquipmentType());

        txtPort.setText(model.getPortNo());
        txtSerial.setText(model.getSerialNumber());
        txtStatus.setText(model.AMCStatus);
        if(model.AMCStatus!=null)
        {
            //No AMC Warranty
            if(model.AMCStatus.equalsIgnoreCase("Under Amc"))
            {
                txtStatus.setBackgroundColor(Color.GREEN);
            }
            if(model.AMCStatus.equalsIgnoreCase("No AMC Warranty"))
            {
                txtStatus.setBackgroundColor(Color.RED);
            }
            try
            {
                model.convertDates();
              if(model.AmcEndDateObj.before(new Date(System.currentTimeMillis()) )) {
                  txtStatus.setBackgroundColor(Color.RED);
                  txtStatus.setText("No AMC Warranty");
               }
            }
            catch (Exception ex)
            {}
        }
       // txtPort.setText(model.PortNo);
       // txtSerial.setText(model.SerialNumber);
        //Linkify.addLinks(txtDesc, Linkify.ALL);

        btnBook=convertView.findViewById(R.id.btnBook);
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookComplaint.model=list.get(position);
               context.startActivity(new Intent(context, BookComplaint.class));
            }
        });
        TextView  txtCustomer=convertView.findViewById(R.id.txtCustomer);
        txtCustomer.setText(model.getCustomerName()+"("+model.getGroupName()+")");

        try
        {
            txtLastVisit.setText(CommonShare.getDateTime(model.getLastVisitMilliSecond()));
            if(txtLastVisit.getText().toString().contains("1970"))
            {
                txtLastVisit.setText("Not Found");

            }
            if( System.currentTimeMillis()- model.getLastVisitMilliSecond()> 6460200000l)
            {
                txtLastVisit.setTextColor(Color.RED);
            }
        }
        catch (Exception ex)
        {}
        return convertView;
    }
}
