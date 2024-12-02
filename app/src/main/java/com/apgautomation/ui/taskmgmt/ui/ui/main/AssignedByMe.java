package com.apgautomation.ui.taskmgmt.ui.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.apgautomation.R;
import com.apgautomation.model.GsonTodo;
import com.apgautomation.ui.adapter.TodoAdapter;
import com.apgautomation.ui.taskmgmt.ui.Filter;
import com.apgautomation.ui.taskmgmt.ui.TaskDetails;
import com.apgautomation.utility.CommonShare;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AssignedByMe.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AssignedByMe#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssignedByMe extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ListView listview;

    public AssignedByMe() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment AssignedByMe.
     */
    // TODO: Rename and change types and number of parameters
    public static AssignedByMe newInstance() {
        AssignedByMe fragment = new AssignedByMe();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=    inflater.inflate(R.layout.fragment_assigned_by_me, container, false);
        listview=v.findViewById(R.id.listview);

        setAdapter();
        return v;
    }
    RealmResults<GsonTodo> cp;
    void setAdapter()
    {
        //Toast.makeText(getActivity(), CommonShare.getEmpId(getActivity()), Toast.LENGTH_SHORT).show();

        Realm realm= Realm.getDefaultInstance();
         cp =realm.where(GsonTodo.class).
                equalTo("EnterById", CommonShare.getEmpId(getActivity()))
                   .and().notEqualTo("Status","Closed").
                 and().equalTo("DeleteStatus",false).
                 findAll();
        /*
        ArrayList<GsonTodo> newlist=new ArrayList<>();
        for (GsonTodo item:cp)
        {
            GsonTodo newItem=realm.copyFromRealm(item);
            newlist.add(newItem);
        }*/
        TodoAdapter adapter=new TodoAdapter(getActivity(),android.R.layout.simple_list_item_1,cp);
        adapter.flag=1;
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getActivity(), TaskDetails.class).putExtra("Token",cp.get(i).getToken()));
            }
        });

    }




    @Override
    public void onDetach() {
        super.onDetach();

    }

    public void filter() {

        if(Filter.status.equalsIgnoreCase("All"))
        {
            if(Filter.EmpId==0)
            {

                Realm realm= Realm.getDefaultInstance();
                cp =realm.where(GsonTodo.class).
                        equalTo("EnterById", CommonShare.getEmpId(getActivity()))
                        .and().greaterThanOrEqualTo("EnterDateMillisecond",Filter.FromDate).
                                and().lessThanOrEqualTo("EnterDateMillisecond",Filter.ToDate).
                                and().equalTo("DeleteStatus",false).
                                findAll();

            }
            else
            {
                Realm realm= Realm.getDefaultInstance();
                cp =realm.where(GsonTodo.class).
                        equalTo("EnterById", CommonShare.getEmpId(getActivity())).
                        and().greaterThanOrEqualTo("EnterDateMillisecond",Filter.FromDate).
                        and().lessThanOrEqualTo("EnterDateMillisecond",Filter.ToDate).
                        and().contains("empids", ","+Filter.EmpId+",").
                        and().equalTo("DeleteStatus",false).
                        findAll();
            }
        }
        else
        {
            if(Filter.EmpId==0)
            {

                Realm realm= Realm.getDefaultInstance();
                cp =realm.where(GsonTodo.class).
                        equalTo("EnterById", CommonShare.getEmpId(getActivity())).
                        and().greaterThanOrEqualTo("EnterDateMillisecond",Filter.FromDate).
                        and().lessThanOrEqualTo("EnterDateMillisecond",Filter.ToDate).
                        and().equalTo("Status",Filter.status).
                        and().equalTo("DeleteStatus",false)
                        .findAll();

            }
            else
            {
                Realm realm= Realm.getDefaultInstance();
                cp =realm.where(GsonTodo.class).
                        equalTo("EnterById", CommonShare.getEmpId(getActivity())).
                        and().greaterThanOrEqualTo("EnterDateMillisecond",Filter.FromDate).
                        and().lessThanOrEqualTo("EnterDateMillisecond",Filter.ToDate).
                        and().contains("empids", ","+Filter.EmpId+",").
                        and().equalTo("DeleteStatus",false).

                        findAll();
            }
        }

        if(Filter.prioirity!=null)
        {
            if(!Filter.prioirity.equalsIgnoreCase(""))
            {
                cp=cp.where().equalTo("Priority",Filter.prioirity).findAll();
            }
        }
        TodoAdapter adapter=new TodoAdapter(getActivity(),android.R.layout.simple_list_item_1,cp);
        adapter.flag=1;
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getActivity(), TaskDetails.class).putExtra("Token",cp.get(i).getToken()));
            }
        });
    }

    public void setDefault() {
        setAdapter();
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
