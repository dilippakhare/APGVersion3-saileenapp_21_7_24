package com.apgautomation.ui.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.apgautomation.model.EmployeeModel;

import java.util.ArrayList;

public class EmployeeSelectionAdapter extends ArrayAdapter {
    Context context;
    ArrayList<EmployeeModel> list;
    public EmployeeSelectionAdapter(@NonNull Context context, int resource, ArrayList<EmployeeModel> list) {
        super(context, resource, list);
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        CheckBox ch=new CheckBox(context);
        ch.setText(list.get(position).EmpName);
        ch.setChecked(list.get(position).IsChecked);
        ch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                list.get(position).IsChecked=b;
            }
        });

        return ch;
    }
}
