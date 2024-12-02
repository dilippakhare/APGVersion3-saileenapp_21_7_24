package com.apgautomation.ui.taskmgmt.ui.ui.main;

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
import com.apgautomation.model.GsonTodo;
import com.apgautomation.ui.adapter.TodoAdapter;
import com.apgautomation.ui.taskmgmt.ui.Filter;
import com.apgautomation.ui.taskmgmt.ui.TaskDetails;
import com.apgautomation.utility.CommonShare;

import io.realm.Realm;
import io.realm.RealmResults;


/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    ListView listview;
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        listview=root.findViewById(R.id.listview);

        setAdapter();
        return root;
    }
    RealmResults<GsonTodo> cp;
    void setAdapter()
    {
        //Toast.makeText(getActivity(), CommonShare.getEmpId(getActivity()), Toast.LENGTH_SHORT).show();

        Realm realm= Realm.getDefaultInstance();
        cp =realm.where(GsonTodo.class).
                contains("empids", ","+CommonShare.getEmpId(getActivity())+",").
                and().equalTo("DeleteStatus",false)
                .and().notEqualTo("Status","Closed").
                findAll();
        /*
        ArrayList<GsonTodo> newlist=new ArrayList<>();
        for (GsonTodo item:cp)
        {
            GsonTodo newItem=realm.copyFromRealm(item);
            newlist.add(newItem);
        }*/
        TodoAdapter adapter=new TodoAdapter(getActivity(),android.R.layout.simple_list_item_1,cp);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getActivity(), TaskDetails.class).putExtra("Token",cp.get(i).getToken()));
            }
        });
    }

    public void filter()
    {
        if(Filter.status.equalsIgnoreCase("All"))
        {
            if(Filter.EmpId==0)
            {

                Realm realm= Realm.getDefaultInstance();
                cp =realm.where(GsonTodo.class).
                        contains("empids", ","+CommonShare.getEmpId(getActivity())+",")
                        .and().greaterThanOrEqualTo("EnterDateMillisecond",Filter.FromDate).
                                and().lessThanOrEqualTo("EnterDateMillisecond",Filter.ToDate).
                                and().equalTo("DeleteStatus",false).
                                findAll();

            }
            else
            {
                Realm realm= Realm.getDefaultInstance();
                cp =realm.where(GsonTodo.class).contains("empids", ","+CommonShare.getEmpId(getActivity())+",").
                        and().greaterThanOrEqualTo("EnterDateMillisecond",Filter.FromDate).
                        and().lessThanOrEqualTo("EnterDateMillisecond",Filter.ToDate).
                        and().equalTo("EnterById",Filter.EmpId).
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
                        contains("empids", ","+CommonShare.getEmpId(getActivity())+",").
                        and().greaterThanOrEqualTo("EnterDateMillisecond",Filter.FromDate).
                        and().lessThanOrEqualTo("EnterDateMillisecond",Filter.ToDate).
                        and().equalTo("Status",Filter.status).
                        and().equalTo("DeleteStatus",false)
                        .findAll();

            }
            else
            {
                Realm realm= Realm.getDefaultInstance();
                cp =realm.where(GsonTodo.class).contains("empids", ","+CommonShare.getEmpId(getActivity())+",").
                        and().greaterThanOrEqualTo("EnterDateMillisecond",Filter.FromDate).
                        and().lessThanOrEqualTo("EnterDateMillisecond",Filter.ToDate).
                        and().equalTo("EnterById",Filter.EmpId).
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
}