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
import com.apgautomation.model.ServerModel.NoticeServerModel;
import com.apgautomation.utility.CommonShare;

import java.util.ArrayList;

public class NoticeAdapterAdapter extends ArrayAdapter {
    Context context;
    ArrayList<NoticeServerModel> list;
    public NoticeAdapterAdapter(@NonNull Context context, int resource, ArrayList<NoticeServerModel> list) {
        super(context, resource, list);
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.item_notice,null);
        TextView txtTopic,txtApplicationDate,txtDesc;
        txtApplicationDate=convertView.findViewById(R.id.txtApplicationDate);
        txtTopic=convertView.findViewById(R.id.txtTopic);
        txtDesc=convertView.findViewById(R.id.txtDesc);

        NoticeServerModel model=list.get(position);
        txtApplicationDate.setText(CommonShare.convertToDate(  CommonShare.parseDate( model.EnterDate)));
        txtTopic.setText(model.Topic);
        txtDesc.setText(model.Details);

       // Linkify.addLinks(txtDesc, Linkify.ALL);

        return convertView;
    }
}
