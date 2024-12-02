package com.apgautomation.ui.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.apgautomation.R;
import com.apgautomation.model.GsonCustomerProduct;
import com.apgautomation.model.InterfaceCustomerProduct;
import com.apgautomation.model.ServerModel.ServerCustomerProducts;
import com.apgautomation.ui.complaint.SelectProducts;
import com.apgautomation.utility.CommonShare;

import java.util.ArrayList;

public class ProductComplaintAdapter extends ArrayAdapter {
    Context context;
    ArrayList<InterfaceCustomerProduct> list;
    public ProductComplaintAdapter(@NonNull Context context, int resource, ArrayList<InterfaceCustomerProduct> list) {
        super(context, resource, list);
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.item_product,null);
        convertView.findViewById(R.id.ln).setVisibility(View.VISIBLE);
        TextView txtName,txtPort,txtType,txtSerial,txtAmcStart,txtAmcEnd;
        Button btnBook;

        txtName=convertView.findViewById(R.id.txtName);
        txtPort=convertView.findViewById(R.id.txtPort);
        txtType=convertView.findViewById(R.id.txtType);

        txtSerial=convertView.findViewById(R.id.txtSerial);
        txtAmcStart=convertView.findViewById(R.id.txtAmcStart);
        txtAmcEnd=convertView.findViewById(R.id.txtAmcEnd);

        try {
            ServerCustomerProducts model = null;
            if (list.get(position) instanceof ServerCustomerProducts)
                model = (ServerCustomerProducts) list.get(position);

            txtAmcStart.setText(CommonShare.convertToDate(CommonShare.parseDate(model.AmcStartDate)));
            txtAmcEnd.setText(CommonShare.convertToDate(CommonShare.parseDate(model.AmcEndDate)));
            txtName.setText(model.EquipmentName);
            txtType.setText(model.EquipmentType);

            txtPort.setText(model.PortNo);
            txtSerial.setText(model.SerialNumber);
            TextView txtCustomer = convertView.findViewById(R.id.txtCustomer);
            txtCustomer.setText(model.CustomerName+"("+model.GroupName+")");

            // txtPort.setText(model.PortNo);
            // txtSerial.setText(model.SerialNumber);
            //Linkify.addLinks(txtDesc, Linkify.ALL);

            btnBook = convertView.findViewById(R.id.btnBook);
            btnBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        SelectProducts s = (SelectProducts) context;
                        if (list.get(position) instanceof ServerCustomerProducts)
                            s.setData((ServerCustomerProducts) list.get(position));
                    } catch (Exception ex) {
                    }
                }
            });
        }
        catch (Exception ex)
        {}

        try {
            GsonCustomerProduct model = null;
            if (list.get(position) instanceof GsonCustomerProduct)
                model = (GsonCustomerProduct) list.get(position);

            txtAmcStart.setText(CommonShare.convertToDate(CommonShare.parseDate(model.getAmcStartDate())));
            txtAmcEnd.setText(CommonShare.convertToDate(CommonShare.parseDate(model.getAmcEndDate())));
            txtName.setText(model.getEquipmentName());//  EquipmentName);
            txtType.setText(model.getEquipmentType()    ); //EquipmentType);

            txtPort.setText(model. getPortNo()    );//PortNo);
            txtSerial.setText(model.getSerialNumber()     );//SerialNumber);
            TextView txtCustomer = convertView.findViewById(R.id.txtCustomer);
            txtCustomer.setText(model.getCustomerName());

            // txtPort.setText(model.PortNo);
            // txtSerial.setText(model.SerialNumber);
            //Linkify.addLinks(txtDesc, Linkify.ALL);

            btnBook = convertView.findViewById(R.id.btnBook);
            btnBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        SelectProducts s = (SelectProducts) context;
                        if (list.get(position) instanceof GsonCustomerProduct)
                        {
                            s.setData((GsonCustomerProduct) list.get(position));
                        }
                    } catch (Exception ex)
                    {
                        CommonShare.alert(context,ex.toString());
                    }
                }
            });

            try
            {
                TextView txtStatus=convertView.findViewById(R.id.txtStatus);
                txtStatus=convertView.findViewById(R.id.txtStatus);
                txtStatus.setText(model.getAMCStatus());
                long amcEnd=CommonShare.parseDate(model.getAmcEndDate());
                if(amcEnd==0)
                {
                    txtStatus.setTextColor(Color.RED);
                    txtAmcEnd.setTextColor(Color.RED);
                }
                else if(amcEnd<System.currentTimeMillis())
                {
                    txtStatus.setTextColor(Color.RED);
                    txtAmcEnd.setTextColor(Color.RED);
                }
                else
                {
                    txtStatus.setTextColor(Color.GREEN);
                }
            }
            catch (Exception ex)
            {}
        }
        catch (Exception ex)
        {}
        return convertView;
    }
}
