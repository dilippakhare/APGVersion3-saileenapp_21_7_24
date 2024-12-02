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
import com.apgautomation.model.GsonTodo;
import com.apgautomation.utility.CommonShare;

import io.realm.RealmResults;

public class TodoAdapter extends ArrayAdapter {
    Context context;
    RealmResults<GsonTodo> list;
    public int flag=0;
    public TodoAdapter(@NonNull Context context, int resource, RealmResults<GsonTodo> list) {
        super(context, resource, list);
        this.context=context;
        this.list=list;
    }

    public boolean isCustomerNameVisible=false;
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.item_todotask,null);
       // convertView.findViewById(R.id.ln).setVisibility(View.VISIBLE);
        TextView txtTitle;
        TextView txtCreatedDate,txtDueDate,txtCreatedBy,txtStatus;
        txtTitle=convertView.findViewById(R.id.txtTitle);
        txtStatus=convertView.findViewById(R.id.txtStatus);
        txtCreatedDate=convertView.findViewById(R.id.txtCreatedDate);
        txtDueDate=convertView.findViewById(R.id.txtDueDate);
        txtCreatedBy=convertView.findViewById(R.id.txtCreatedBy);

        GsonTodo model=list.get(position);
        txtTitle.setText(model.getTitle());
        txtStatus.setText(model.getStatus());
        txtCreatedDate.setText(model.getEnterDate().split(" ")[0]);
        txtDueDate.setText(model.getDueDate());
        txtCreatedBy.setText("Created By:-"+model.getEnterByName());

        if((model.getDueDateMillisecond()+86400000)<System.currentTimeMillis())
        {
            //convertView.setBackgroundColor(Color.rgb(15,0,0));
            convertView.setBackgroundColor(Color.rgb(200,0,0));
        }

        if(CommonShare.convertToDate(model.getDueDateMillisecond()).equalsIgnoreCase(CommonShare.convertToDate(System.currentTimeMillis())))
        {
            convertView.setBackgroundColor(Color.rgb(255,255,0));
        }

         if(flag==1)
         {
             String str="";
             try {

                 for(int i=1;i<model.getEmpids().split(",").length;i++)
                 {
                     if(str.equalsIgnoreCase(""))
                     {
                         str=CommonShare.empMap.get( Integer.parseInt(model.getEmpids().split(",")[i])).EmpName;
                     }
                     else
                     {
                         str=str+","+CommonShare.empMap.get( Integer.parseInt(model.getEmpids().split(",")[i])).EmpName;

                     }
                 }

             }
             catch (Exception ex)
             {}
             txtCreatedBy.setText("Assign To:-"+str);
         }

        return convertView;
    }
}
