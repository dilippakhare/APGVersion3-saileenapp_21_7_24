package com.apgautomation.ui.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.apgautomation.R;
import com.apgautomation.model.GSONCustomerMasterBeanExtends;

import java.util.ArrayList;

public class MultipleCustomerSelectionAdapter extends ArrayAdapter {
    Context context;
    ArrayList<GSONCustomerMasterBeanExtends> list;
    public MultipleCustomerSelectionAdapter(@NonNull Context context, int resource, ArrayList<GSONCustomerMasterBeanExtends> list) {
        super(context, resource, list);
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.item_checkbox_single_item,null);
        CheckBox checkBox=convertView.findViewById(R.id.checkbox);
        checkBox.setText(list.get(position).getCustomerName()+"("+list.get(position).getGroupName()+")");
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                list.get(position).setSelect(b);
            }
        });

        checkBox.setChecked(list.get(position).isSelect());

        return convertView;
    }
}
