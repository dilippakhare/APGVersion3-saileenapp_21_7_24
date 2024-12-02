package com.apgautomation.ui.attendance;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;

import com.apgautomation.R;
import com.apgautomation.model.ServerModel.EmployeeAttendanceListModel;
import com.apgautomation.ui.adapter.EmployeeRecentAttenacneAdapter;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.AsyncUtilities;
import com.apgautomation.utility.serverutility.DownloadUtility;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

public class TodaysEmployeeAttendanceList extends AppCompatActivity implements DownloadUtility
, DatePickerDialog.OnDateSetListener{

    ListView listview;
    Button btnDate;
   Calendar myCalendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_attendance_list);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Employee Attenance");

        listview=findViewById(R.id.listview);
        btnDate=findViewById(R.id.btnDate);

        Date date=new Date(System.currentTimeMillis());
        btnDate.setText(date.getDate()+"/"+(date.getMonth()+1)+"/"+(1900+date.getYear()));



        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new DatePickerDialog(TodaysEmployeeAttendanceList.this, TodaysEmployeeAttendanceList.this, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();


            }
        });
        callRequest();

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ViewAttenadnceDetails.model=list.get(i);
                startActivity(new Intent(TodaysEmployeeAttendanceList.this, ViewAttenadnceDetails.class));
            }
        });
    }
    void callRequest()
    {
        int Mode=0;
        int ManagerId=0;
        int EmpId=0;
        String roleName="";

        try
        {
            ManagerId= CommonShare.getEmpId(this);
            EmpId=CommonShare.getEmpId(this);

            roleName=CommonShare.getRole(this);
            if(roleName.equalsIgnoreCase("admin"))
            {
                Mode=1;
            }
            else
            {
                Mode=0;
            }
        }
        catch (Exception ex)
        {}

        long selectedDate=0l;
        try
        {
            selectedDate= CommonShare.ddMMYYDateToLong(btnDate.getText().toString().replace("/","-"));
        }
        catch (Exception ex){}
        String url= CommonShare.url+"Service1.svc/GetAttendanceRecordsByDate?ManagerId="+ManagerId+"&EmpId="+EmpId+"&Mode="+Mode+"&SelectedDate="+selectedDate;
        AsyncUtilities utilities=new AsyncUtilities(this,false,url,"",1,this);
        utilities.execute();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    ArrayList<EmployeeAttendanceListModel> list=new ArrayList<>();
    @Override
    public void onComplete(String str, int requestCode, int responseCode) {

    // CommonShare.alert(this,str);
        try
        {
            list.clear();
            Gson gson=new Gson();
            JSONArray arr=new JSONArray(str);
            for(int i=0;i<arr.length();i++)
            {
                EmployeeAttendanceListModel model=(EmployeeAttendanceListModel)  gson.fromJson(arr.getJSONObject(i).toString(),EmployeeAttendanceListModel.class);
               // if(!model.DeleteStatus)
                  list.add(model);
            }
            EmployeeRecentAttenacneAdapter adapter=new EmployeeRecentAttenacneAdapter(this,R.layout.item_recent_attendance,list);
            listview.setAdapter(adapter);
          //  CommonShare.alert(this,list.size()+"");
        }
        catch (Exception ex)
        {}
    }
    void setListview()
    {

    }
    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth)
    {
        //CommonShare.alert(this,year+"-"+monthOfYear+"-"+dayOfMonth);
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        btnDate.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
        callRequest();
    }
}
