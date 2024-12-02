package com.apgautomation.ui.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.apgautomation.R;
import com.apgautomation.model.ServerModel.ServerCustomerProducts;
import com.apgautomation.ui.complaint.BookComplaint;
import com.apgautomation.utility.CommonShare;

import java.util.ArrayList;

public class ProductAdapter extends ArrayAdapter {
    Context context;
    ArrayList<ServerCustomerProducts> list;
    public ProductAdapter(@NonNull Context context, int resource, ArrayList<ServerCustomerProducts> list) {
        super(context, resource, list);
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.item_product,null);
        TextView txtName,txtPort,txtType,txtSerial,txtAmcStart,txtAmcEnd;
        Button btnBook;
        txtName=convertView.findViewById(R.id.txtName);
        txtPort=convertView.findViewById(R.id.txtPort);
        txtType=convertView.findViewById(R.id.txtType);

        txtSerial=convertView.findViewById(R.id.txtSerial);
        txtAmcStart=convertView.findViewById(R.id.txtAmcStart);
        txtAmcEnd=convertView.findViewById(R.id.txtAmcEnd);

        ServerCustomerProducts model=list.get(position);
        txtAmcStart.setText(CommonShare.convertToDate(  CommonShare.parseDate( model.InstallationDate)));
        txtAmcEnd.setText(CommonShare.convertToDate(  CommonShare.parseDate( model.AmcEndDate)));
        txtName.setText(model.EquipmentName);
        txtType.setText(model.EquipmentType);

        txtPort.setText(model.PortNo);
        txtSerial.setText(model.SerialNumber);

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
        txtCustomer.setText(model.CustomerName);
        return convertView;
    }
}
