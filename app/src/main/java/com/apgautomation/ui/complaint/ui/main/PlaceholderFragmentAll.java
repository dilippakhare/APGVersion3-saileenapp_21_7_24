package com.apgautomation.ui.complaint.ui.main;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.apgautomation.R;
import com.apgautomation.model.ServerModel.ServerComplaintModel;
import com.apgautomation.ui.adapter.ComplaintGridAdapter;
import com.apgautomation.ui.complaint.ViewComplaintDetails;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;


/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragmentAll extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    public static PlaceholderFragmentAll newInstance(int index) {
        PlaceholderFragmentAll fragment = new PlaceholderFragmentAll();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
     ListView listview;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
            View root = inflater.inflate(R.layout.fragment_main, container, false);
            listview=root.findViewById(R.id.listview);
            return root;
    }


    ArrayList<ServerComplaintModel> list=new ArrayList<>();

    public void onComplete(String str, int requestCode, int responseCode) {

        list.clear();
        try
        {
            Gson g=new Gson();
            JSONArray array=new JSONArray(str);
            for(int i=0;i<array.length();i++)
            {
                ServerComplaintModel model=    g.fromJson(array.getJSONObject(i).toString(), ServerComplaintModel.class);
                list.add(model);
            }
            Collections.reverse(list);
            // ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,list);
            //listview.setAdapter(adapter);
            ComplaintGridAdapter adapter1=new ComplaintGridAdapter(getActivity(),R.layout.item_complaint_grid,list);
            adapter1.isCustomerNameVisible=true;
            listview.setAdapter(adapter1);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    ViewComplaintDetails.model=list.get(i);
                    startActivity(new Intent(getActivity(),ViewComplaintDetails.class));

                }
            });
        }
        catch (Exception ex)
        {}
    }
}