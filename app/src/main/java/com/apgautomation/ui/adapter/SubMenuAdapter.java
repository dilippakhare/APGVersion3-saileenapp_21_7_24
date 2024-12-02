package com.apgautomation.ui.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.apgautomation.R;
import com.apgautomation.model.SubmenuModel;

import java.util.ArrayList;

public class SubMenuAdapter extends ArrayAdapter {
    Context context;
    ArrayList<SubmenuModel> list;
    public SubMenuAdapter(@NonNull Context context, int resource, ArrayList<SubmenuModel> list) {
        super(context, resource, list);
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.item_submenu,null);
        TextView txtTitle=convertView.findViewById(R.id.txtTitle);
        ImageView imgLeft=convertView.findViewById(R.id.imgLeft);


        SubmenuModel model=list.get(position);
        txtTitle.setText(model.SubmenuName);

        return convertView;
    }
}
