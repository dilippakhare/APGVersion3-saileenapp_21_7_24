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
import com.apgautomation.model.GsonGroup;
import com.apgautomation.model.ModelMuterialRequestList;
import com.apgautomation.utility.CommonShare;

import java.util.ArrayList;

import io.realm.Realm;

public class MuterialAdapter extends ArrayAdapter {
    Context context;
    ArrayList<ModelMuterialRequestList> list;
    Realm realm;
    public MuterialAdapter(@NonNull Context context, int resource, ArrayList<ModelMuterialRequestList> list) {
        super(context, resource, list);
        this.context=context;
        this.list=list;
        realm=Realm.getDefaultInstance();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.item_muterial,null);
        TextView txtTopic,txtApplicationDate,txtDesc;
        txtApplicationDate=convertView.findViewById(R.id.txtApplicationDate);
        txtTopic=convertView.findViewById(R.id.txtTopic);
        txtDesc=convertView.findViewById(R.id.txtDesc);

        ModelMuterialRequestList model=list.get(position);
        txtApplicationDate.setText(CommonShare.convertToDate(  CommonShare.parseDate( model.getEnterDate())));
        GsonGroup g = realm.where(GsonGroup.class).equalTo("GroupId",(int)model.getGroupId()).findFirst();
        txtTopic.setText(g.getGroupName());


       // Linkify.addLinks(txtDesc, Linkify.ALL);
        TextView  txtStatus=convertView.findViewById(R.id.txtStatus);
        txtStatus.setText(model.getFinalStatus());
        try {
            if (model.getFinalStatus().equalsIgnoreCase("Rejected")) {
                txtStatus.setTextColor(Color.rgb(255, 0, 0));
            }
            else
            if (model.getFinalStatus().equalsIgnoreCase("Approved")) {
                txtStatus.setTextColor(Color.rgb(0, 255, 0));
            }
            else
            if (model.getFinalStatus().equalsIgnoreCase("Request")) {
                txtStatus.setTextColor(Color.BLUE);
                txtStatus.setTypeface(null, Typeface.BOLD);
            }
        }
        catch (Exception ex){}
        try {
            String emp= CommonShare.empMap.get((int)model.getEmpId()).EmpName;
            txtDesc.setText(model.getReason()+"\n\t"+emp);
        }
        catch (Exception ex){}
        return convertView;
    }
}
