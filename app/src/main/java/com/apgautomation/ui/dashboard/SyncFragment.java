package com.apgautomation.ui.dashboard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.apgautomation.R;
import com.apgautomation.controller.AttendanceController;
import com.apgautomation.controller.SyncCustomerControlller;
import com.apgautomation.controller.SyncCustomerProductControlller;
import com.apgautomation.controller.SyncGroupAndEquipment;
import com.apgautomation.controller.VisitModuleController;
import com.apgautomation.model.AttendanceModel;
import com.apgautomation.model.GSONCustomerMasterBean;
import com.apgautomation.model.GsonCustomerProduct;
import com.apgautomation.model.GsonGroup;
import com.apgautomation.model.GsonVisitMaster;
import com.apgautomation.model.ModelRecord;
import com.apgautomation.model.ServerModel.GsonSalesVisit;
import com.apgautomation.ui.adapter.SyncAdapter;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.AsyncUtilities;
import com.apgautomation.utility.serverutility.DownloadUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SyncFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SyncFragment extends Fragment implements DownloadUtility ,OnItemButtonClick {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SyncFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SyncFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SyncFragment newInstance(String param1, String param2) {
        SyncFragment fragment = new SyncFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sync, container, false);

    }

    ListView listview;
    @Override
    public void onResume() {
        super.onResume();
        listview= getView().findViewById(R.id.listview);
        String url= CommonShare.url + "Service1.svc/GetRecordCount";;

        AsyncUtilities utilities =new AsyncUtilities(getActivity(),false, url,null,1,this);
        utilities.execute();

    }

    int attendancePending=0 , serviceVisitPending =0 , SalesVistSchedulePending=0 , salesVisitModfied =0;
    int Scustomer, Sgroup, Sproducts;
    int customer, group, products;
    void calucation()
    {
        // ---  Attendance
        AttendanceController attendanceController=new AttendanceController(getActivity());
        ArrayList<AttendanceModel> list1= attendanceController.getModifiedRecord();
        attendancePending= list1.size();
        //-----   Visit
        VisitModuleController controller=new VisitModuleController(getActivity());
        ArrayList<GsonVisitMaster> modifiedList = controller.getModifiedRecord();
        serviceVisitPending =modifiedList.size();
        //-----  Sales Visit
        Realm realm=Realm.getDefaultInstance();
        RealmResults<GsonSalesVisit> cp= realm.where(GsonSalesVisit.class).equalTo("SalesVisitId",0).findAll();
        ArrayList<GsonSalesVisit> list=new ArrayList<>(cp );
        SalesVistSchedulePending =list.size();
        RealmResults<GsonSalesVisit> cp1= realm.where(GsonSalesVisit.class)
                .equalTo("IsModified",true).and().notEqualTo("SalesVisitId",0).findAll();
        salesVisitModfied =cp1.size();
        //-------



        customer=  realm.where(GSONCustomerMasterBean.class).findAll().size();
        products=  realm.where(GsonCustomerProduct.class).findAll().size();
        group=  realm.where(GsonGroup.class).findAll().size();

    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode) {
        calucation();
        if(requestCode==1 &&responseCode == 200) {

            // CommonShare.alert(getActivity() , str +"customer="+customer+", group="+group+",prducts-"+products);
            JSONObject j = null;
            try {
                j = new JSONObject(str);
                Scustomer = j.getInt("Customer");
                Sgroup = j.getInt("Groups");
                Sproducts = j.getInt("Products");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            list.clear();
            ModelRecord m = new ModelRecord();
            m.topic = "Attendance  :- ";
            m.subtitle1 = "Pending :-" + attendancePending;
            list.add(m);

            m = new ModelRecord();
            m.topic = "Sales Visit Schedule :- ";
            m.subtitle1 = "Pending :-" + SalesVistSchedulePending;
            list.add(m);

            m = new ModelRecord();
            m.topic = "Sales Visit  :- ";
            m.subtitle1 = "Pending :-" + salesVisitModfied;
            list.add(m);

            m = new ModelRecord();
            m.topic = "Service Visit  :- ";
            m.subtitle1 = "Pending :-" + serviceVisitPending;
            list.add(m);

            m = new ModelRecord();
            m.topic = "Customer :- ";
            m.subtitle1 = "Server Count :-" + Scustomer;
            m.subtitle2 = "App Count :-" + customer;
            if (Scustomer != customer)
                m.showSync = true;
            list.add(m);

            m = new ModelRecord();
            m.topic = "Group :- ";
            m.subtitle1 = "Server Count :-" + Sgroup;
            m.subtitle2 = "App Count :-" + group;
            if (Sgroup != group)
                m.showSync = true;
            list.add(m);

            m = new ModelRecord();
            m.topic = "Products :- ";
            m.subtitle1 = "Server Count :-" + Sproducts;
            m.subtitle2 = "App Count :-" + products;
            if (Sproducts != products)
                m.showSync = true;
            list.add(m);


            SyncAdapter adapter = new SyncAdapter(getActivity(), R.layout.item_sync, list);
            adapter.itemButtonClick = this;
            listview.setAdapter(adapter);
        }
        else if(requestCode==2)
        {
            onResume();
        }

    }
    ArrayList<ModelRecord>  list =new ArrayList<>();

    @Override
    public void clickItem(int position) {
        if(position==6) {
            SyncCustomerProductControlller c= new SyncCustomerProductControlller(getActivity());
            c.syncInsiviual();
        }
        if(position==5) {
            SyncGroupAndEquipment c= new SyncGroupAndEquipment(getActivity());
            c.syncIndiviual();
        }
        if(position==4) {
            SyncCustomerControlller c= new SyncCustomerControlller(getActivity());
            c.syncIndiual();
        }
    }
}