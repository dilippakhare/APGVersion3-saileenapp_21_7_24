package com.apgautomation.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.apgautomation.R;
import com.apgautomation.model.ModelRecord;
import com.apgautomation.model.ServerModel.ServerComplaintModel;
import com.apgautomation.ui.dashboard.OnItemButtonClick;
import com.apgautomation.utility.CommonShare;

import java.util.ArrayList;

public class SyncAdapter  extends ArrayAdapter {
    Context context;
    ArrayList<ModelRecord> list;
    public SyncAdapter(@NonNull Context context, int resource, ArrayList<ModelRecord> list) {
        super(context, resource, list);
        this.context=context;
        this.list=list;
    }
   public   OnItemButtonClick  itemButtonClick;

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.item_sync,null);
        // convertView.findViewById(R.id.ln).setVisibility(View.VISIBLE);
        TextView txtTopic,txtLocal,txtServer;

        txtTopic=convertView.findViewById(R.id.txtTopic);
        txtLocal=convertView.findViewById(R.id.txtLocal);
        txtServer=convertView.findViewById(R.id.txtServer);

        txtTopic.setText(list.get(position).topic);
        txtLocal.setText(list.get(position).subtitle1);
        txtServer.setText(list.get(position).subtitle2);
        Button  syncRec=convertView.findViewById(R.id.syncRec);
        if(position>=4)
            syncRec.setVisibility(View.VISIBLE);
        syncRec.setTag(position+"");
        syncRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemButtonClick.clickItem(Integer.parseInt(v.getTag().toString()));
            }
        });
        return convertView;
    }
}
