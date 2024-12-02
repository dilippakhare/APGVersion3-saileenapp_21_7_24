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
import com.apgautomation.ui.customer.SelectSearchCustomerProduct;
import com.apgautomation.utility.CommonShare;

import java.util.ArrayList;

public class ProductSelectAdapter extends ArrayAdapter {
    Context context;
    ArrayList<GsonCustomerProduct> list;
    public ProductSelectAdapter(@NonNull Context context, int resource, ArrayList<GsonCustomerProduct> list) {
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
        TextView txtName,txtPort,txtType,txtSerial,txtAmcStart,txtAmcEnd,txtStatus;
        Button btnBook;

        txtName=convertView.findViewById(R.id.txtName);
        txtPort=convertView.findViewById(R.id.txtPort);
        txtType=convertView.findViewById(R.id.txtType);

        txtSerial=convertView.findViewById(R.id.txtSerial);
        txtAmcStart=convertView.findViewById(R.id.txtAmcStart);
        txtAmcEnd=convertView.findViewById(R.id.txtAmcEnd);

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
            txtCustomer.setText(model.getCustomerName()+"("+model.getGroupName()+")");

            // txtPort.setText(model.PortNo);
            // txtSerial.setText(model.SerialNumber);
            //Linkify.addLinks(txtDesc, Linkify.ALL);

            btnBook = convertView.findViewById(R.id.btnBook);
            btnBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        SelectSearchCustomerProduct s = (SelectSearchCustomerProduct) context;
                        if (list.get(position) instanceof GsonCustomerProduct)
                        {
                            s.selectProduct=  list.get(position);
                            s.callBack();
                        }
                    } catch (Exception ex)
                    {
                        CommonShare.alert(context,ex.toString());
                    }
                }
            });

            try
            {

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
