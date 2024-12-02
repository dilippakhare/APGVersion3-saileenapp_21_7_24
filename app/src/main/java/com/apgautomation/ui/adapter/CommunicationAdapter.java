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
import com.apgautomation.model.GsonAskQuery;
import com.apgautomation.utility.CommonShare;

import io.realm.RealmResults;

public class CommunicationAdapter extends ArrayAdapter {
    Context context;
    RealmResults<GsonAskQuery> list;
    int EmpId=0;
    public int flag=0;
    public CommunicationAdapter(@NonNull Context context, int resource, RealmResults<GsonAskQuery> list) {
        super(context, resource, list);
        this.context=context;
        this.list=list;
       EmpId= CommonShare.getEmpId(context);
    }

    public boolean isCustomerNameVisible=false;
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.item_communication,null);
       // convertView.findViewById(R.id.ln).setVisibility(View.VISIBLE);
        TextView txtName;
        TextView txtTime;
        txtName=convertView.findViewById(R.id.txtName);
        txtTime=convertView.findViewById(R.id.txtTime);


        GsonAskQuery model=list.get(position);
        txtName.setText(model.getEnterByName());
        txtTime.setText(model.getEnterDate());
        if(!model.isReed())
             txtTime.setTextColor(Color.GREEN);
        if(model.getEnterById()==EmpId)
        {
            txtName.setText(model.getToName());
        }

        return convertView;
    }
}
