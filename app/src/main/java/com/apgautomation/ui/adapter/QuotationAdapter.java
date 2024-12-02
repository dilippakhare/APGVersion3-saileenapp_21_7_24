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
import com.apgautomation.model.GsonQuotationRequestModel;
import com.apgautomation.utility.CommonShare;

import java.util.ArrayList;

public class QuotationAdapter extends ArrayAdapter {
    Context context;
    ArrayList<GsonQuotationRequestModel> list;
    public QuotationAdapter(@NonNull Context context, int resource, ArrayList<GsonQuotationRequestModel> list) {
        super(context, resource, list);
        this.context=context;
        this.list=list;
    }

    public boolean isCustomerNameVisible=false;
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.item_quotationasign,null);
       // convertView.findViewById(R.id.ln).setVisibility(View.VISIBLE);
        TextView text1;
        TextView text2;
        text1=convertView.findViewById(R.id.text1);
        text2=convertView.findViewById(R.id.text2);
        GsonQuotationRequestModel model=list.get(position);
        text1.setText(model.toString());
        text2.setText("Assign To :-"+model.getAssignedToName());

        if(model.getAssignToEmpId()==CommonShare.getEmpId(context))
        {
            convertView.setBackgroundColor(Color.LTGRAY);
            text1.setHighlightColor(Color.blue(250));
            text2.setHighlightColor(Color.blue(250));
            //convertView
        }

        return convertView;
    }
}
