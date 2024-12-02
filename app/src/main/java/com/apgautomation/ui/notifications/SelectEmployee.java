package com.apgautomation.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.apgautomation.R;
import com.apgautomation.model.EmployeeModel;
import com.apgautomation.ui.adapter.EmployeeSelectionAdapter;
import com.apgautomation.utility.CommonShare;

import java.util.ArrayList;

public class SelectEmployee extends AppCompatActivity {

    ListView listview;
    ArrayList<EmployeeModel> mainList;
    Button button2;
    public  static   String ids="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_employee);
        ids="";
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Select Employee");
        listview=findViewById(R.id.listview);
        mainList=CommonShare.getEmployeeList(this);
        button2=findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (EmployeeModel m:mainList
                     )
                {
                    if(m.IsChecked)
                    {
                      ids=ids+m.EmpId+",";
                    }

                }
                ids=","+ids;

                setResult(RESULT_OK);
                setIntent(new Intent());
                finish();

            }
        });

        EmployeeSelectionAdapter adapter=new EmployeeSelectionAdapter(this,1,mainList);
        listview.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
