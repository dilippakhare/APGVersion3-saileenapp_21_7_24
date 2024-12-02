package com.apgautomation.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.apgautomation.R;
import com.apgautomation.model.ServerModel.NoticeServerModel;
import com.apgautomation.ui.adapter.NoticeAdapterAdapter;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.AsyncUtilities;
import com.apgautomation.utility.serverutility.DownloadUtility;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;

public class NotificationsFragment extends Fragment implements DownloadUtility {

    private NotificationsViewModel notificationsViewModel;

    ArrayList<NoticeServerModel> list=new ArrayList<>();
    ListView listview;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        listview=root.findViewById(R.id.listview);

        String url="";
        if(CommonShare.getEmpId(getActivity())>0)
           url=        CommonShare.url+ "Service1.svc/GetNotice?Empid="+CommonShare.getEmpId(getActivity());

        if(CommonShare.getCustomerId(getActivity())>0)
            url=   CommonShare.url+ "Service1.svc/GetCustomerNotice?UserId="+CommonShare.getUserId(getActivity());

        AsyncUtilities utilities=new AsyncUtilities(getActivity(),false,url,"",1,this);
        utilities.execute();
        return root;
    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode)
    {
         try
         {
             Gson gson=new Gson();
             JSONArray arr=new JSONArray(str);
             for(int i=0;i<arr.length();i++)
             {
                 NoticeServerModel model=gson.fromJson(arr.getJSONObject(i).toString(),NoticeServerModel.class);
                 list.add(model);
             }

             Collections.reverse(list);
             NoticeAdapterAdapter adapterAdapter=new NoticeAdapterAdapter(getActivity(),R.layout.item_notice,list);
             listview.setAdapter(adapterAdapter);
             listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                 @Override
                 public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                     NoticeDetails.model=list.get(i);
                     startActivity(new Intent(getActivity(),NoticeDetails.class));
                 }
             });
         }
         catch (Exception ex)
         {}
    }
    void setListview()
    {
;
    }
}