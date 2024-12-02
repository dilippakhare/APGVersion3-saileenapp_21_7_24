package com.apgautomation.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.apgautomation.R;
import com.apgautomation.model.GSONCustomerMasterBean;
import com.apgautomation.model.GsonGroup;
import com.apgautomation.model.InstallationModel;
import com.apgautomation.utility.CommonShare;

import java.util.ArrayList;

import io.realm.Realm;

public class InstallationAdapter extends ArrayAdapter {
    Context context;
    ArrayList<InstallationModel> list;
    Realm realm;
    public InstallationAdapter(@NonNull Context context, int resource, ArrayList<InstallationModel> list) {
        super(context, resource, list);
        this.context=context;
        this.list=list;
        realm=Realm.getDefaultInstance();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.item_intsallation,null);
        TextView txtGroupName,txtPoDate,txtCustomer;
        txtPoDate=convertView.findViewById(R.id.txtPoDate);
        txtGroupName=convertView.findViewById(R.id.txtGroupName);
        txtCustomer=convertView.findViewById(R.id.txtDesc);

        InstallationModel model=list.get(position);
        txtPoDate.setText(CommonShare.convertToDate(  CommonShare.parseDate( model.getEnterDate())));
        GsonGroup g = realm.where(GsonGroup.class).equalTo("GroupId",(int)model.getGroupId()).findFirst();
        txtGroupName.setText(g.getGroupName());

        GSONCustomerMasterBean c = realm.where(GSONCustomerMasterBean.class).equalTo("CustomerId",(int)model.getCustomerId()).findFirst();
        txtCustomer.setText(c.getCustomerName());

        // Linkify.addLinks(txtDesc, Linkify.ALL);
        TextView  txtStatus=convertView.findViewById(R.id.txtStatus);
        txtStatus.setText(model.getStatus());
        try {
            if (model.getStatus().equalsIgnoreCase("Initiated")) {
                txtStatus.setTextColor(Color.rgb(255, 0, 0));
            }
            else
            if (model.getStatus().equalsIgnoreCase("In-Process")) {
                txtStatus.setTextColor(Color.rgb(0, 255, 0));
            }
            else
            if (model.getStatus().equalsIgnoreCase("Complete")) {
                txtStatus.setTextColor(Color.BLUE);
                txtStatus.setTypeface(null, Typeface.BOLD);
            }
        }
        catch (Exception ex){}

        return convertView;
    }
}
