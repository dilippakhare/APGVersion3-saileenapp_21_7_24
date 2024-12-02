package com.apgautomation.ui.visit;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.apgautomation.R;
import com.apgautomation.controller.SyncGroupAndEquipment;
import com.apgautomation.controller.VisitModuleController;
import com.apgautomation.model.EmployeeModel;
import com.apgautomation.model.GSONCustomerMasterBean;
import com.apgautomation.model.GsonGroup;
import com.apgautomation.model.GsonVisitMaster;
import com.apgautomation.model.GsonVisitType;
import com.apgautomation.model.ServerModel.ServerComplaintModel;
import com.apgautomation.ui.customer.SelectGroup;
import com.apgautomation.ui.customer.SelectSearchCustomerProduct;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.netrequest.ConnectionDetector;
import com.apgautomation.utility.syncutilities.VisitSyncUtility;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;


public class ScheduleVisit extends AppCompatActivity
{

    Button btnSelectGroup,btnScheduleDate;
    Spinner spVisitType;
    Spinner spAssignTO;
    EditText edReason;
    Button btnSchedule;

    ArrayList<EmployeeModel> teamList=new ArrayList<>();
    ArrayList<GsonVisitType> visitTypes;
    GsonGroup group;
    VisitModuleController controller;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
    CheckBox chkEquipment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_visit);
        chkEquipment=findViewById(R.id.chkEquipment);
        btnSelectGroup=findViewById(R.id.btnSelectCustomer);
        btnScheduleDate=findViewById(R.id.btnScheduleDate);
        spVisitType=findViewById(R.id.spVisitType);
        spAssignTO=findViewById(R.id.spAssignTO);
        edReason=findViewById(R.id.edReason);
        btnSchedule=findViewById(R.id.btnSchedule);
        btnSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmVisit();
            }
        });

        btnSelectGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!chkEquipment.isChecked())
                {
                    startActivityForResult(new Intent(ScheduleVisit.this, SelectGroup.class),1);
                }
                else
                {
                    startActivityForResult(new Intent(ScheduleVisit.this, SelectSearchCustomerProduct.class),2);

                }
            }
        });

        btnScheduleDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmVisit();
            }
        });

      //  teamList= CommonShare.getTeamListPlusOwn(this);
      //  if(CommonShare.getEmpId(this)==17 ||CommonShare.getEmpId(this)==19 || CommonShare.getEmpId(this)==52 || CommonShare.getEmpId(this)==65)
        if((CommonShare.getRole(this).equalsIgnoreCase("Assistance Manager")
                && CommonShare.getMyEmployeeModel(this).DeptId==3  )  ||
                CommonShare.getRole(this).equalsIgnoreCase("Admin")
        )
        {
           ArrayList<EmployeeModel> employeeModelArrayList=  CommonShare.geSEmployeeList(this);
            for (EmployeeModel m:employeeModelArrayList
                 )
            {
                if(!m.DeleteStatus)
                   teamList.add(m );
            }

        }
        else
        {
            ArrayList<EmployeeModel> employeeModelArrayList=CommonShare.getTeamListPlusOwn(this);
            for (EmployeeModel m:employeeModelArrayList
            )
            {
                if(!m.DeleteStatus)
                    teamList.add(m );
            }
        }
        visitTypes= CommonShare.getVisitTypes(this);
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,teamList);
        spAssignTO.setAdapter(adapter);

        ArrayAdapter adapter1=new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,visitTypes);
        spVisitType.setAdapter(adapter1);


        btnScheduleDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment(btnScheduleDate);
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Visit Scheduling");
        controller=new VisitModuleController(this);


        try
        {
            //Complaint
            String str=   getIntent().getExtras().getString("Complaint");
            Gson gson=new Gson();
            ServerComplaintModel m=   gson.fromJson(str, ServerComplaintModel.class);
            if(m.ComplaintId>0)
            {
                for (int i=0;i<teamList.size();i++)
                {
                    if(teamList.get(i).EmpId==m.AssignToId)
                    {
                        spAssignTO.setSelection(i);
                    }
                }
            }
            Realm realm=Realm.getDefaultInstance();
            GSONCustomerMasterBean gsp=   realm.where(GSONCustomerMasterBean.class).equalTo("CustomerId",m.CustomerId).findFirst();
            int gid=(int)gsp.getGroupId();
            GsonGroup gmaster=  realm.where(GsonGroup.class).equalTo("GroupId",gid).findFirst();
            group=gmaster;
            btnSelectGroup.setText(group.getGroupName());


            for (int i=0;i<visitTypes.size();i++)
            {
                if(visitTypes.get(i).VisitType1.equalsIgnoreCase("Complaint Booking"))
                {
                    spVisitType.setSelection(i);
                }
            }
            edReason.setText("Complaint No. "+m.ComplaintId+"\nComplaint Details :-"+m.ComplaintDetails);

        }
        catch (Exception ex)
        {
            Log.d("e",ex.toString());
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
    private void confirmVisit()
    {
        if(!new ConnectionDetector(this).isConnectingToInternet())
        {
            Toast.makeText(this, "Internet Not Available", Toast.LENGTH_SHORT).show();
            return;
        }
        if(btnSelectGroup.getText().toString().equalsIgnoreCase("Select Group"))
        {
            Toast.makeText(this, "Select Group", Toast.LENGTH_SHORT).show();
            return;
        }
        if( btnScheduleDate.getText().toString().equalsIgnoreCase("Select Date"))
        {
            Toast.makeText(this, "Select Scheduling Date", Toast.LENGTH_SHORT).show();
            return;
        }
        if(spVisitType.getSelectedItemPosition()==0)
        {
            Toast.makeText(this, "Select Visit Type", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setTitle("Confirm visit scheduling");
        String msg="Enginner Name : "+spAssignTO.getSelectedItem().toString()+"\nCustomer Group :"+btnSelectGroup.getText().toString()+"\nSchedule Date :"+btnScheduleDate.getText().toString();
        try
        {
          EmployeeModel employeeModelObj=  (EmployeeModel)spAssignTO.getSelectedItem();
          if(CommonShare.getEmpId(ScheduleVisit.this)==employeeModelObj.EmpId)
          {
              Date date = new Date(    CommonShare.ddMMYYDateToLong(btnScheduleDate.getText().toString().replace("/","-")));
              Date today=new Date(System.currentTimeMillis());

             // long difference=  (date.getTime()-today.getTime()) / (24 * 60 * 60 * 1000); //date.compareTo(today);

              if(date.getDay()==today.getDay()  &&  date.getMonth()==today.getMonth() && date.getYear()==today.getYear())
              if(today.getHours()>9)
              {
                  msg=msg+"\nThis visit will be marked as unplanned.Try to schedule visit one day before.";
              }


          }

        }
        catch (Exception ex)
        {}
        alert.setMessage(msg);
        alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                scheduleVisit();
            }
        });
        alert.setNegativeButton("Cancel",null);
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 &&  resultCode==RESULT_OK)
        {
            group=SelectGroup.selectedGroup;
            btnSelectGroup.setText(group.getGroupName());
        }
        if(requestCode==2 &&  resultCode==RESULT_OK)
        {
            SyncGroupAndEquipment groupAndEquipment=new SyncGroupAndEquipment(this);
            group=groupAndEquipment.getGroupById((int)(SelectSearchCustomerProduct.selectProduct).getGroupId());
            btnSelectGroup.setText(group.getGroupName());
        }
    }

    void scheduleVisit()
    {
        GsonVisitMaster model=new GsonVisitMaster();
        model.VisitToken=CommonShare.generateToken(this);
        model.AssigntoEmpId=((EmployeeModel)spAssignTO.getSelectedItem()).EmpId;
        model.GroupId=group.getGroupId();
        model.BookByEmpId=CommonShare.getEmpId(this);
        model.BookByName=CommonShare.getEmpName(this);
        model.EnterDateMillisecond=System.currentTimeMillis();
        model.ScheduleDateMillisecond=CommonShare.ddMMYYDateToLong(btnScheduleDate.getText().toString().replace("/","-"));
        model.ReasonForVisit=edReason.getText().toString();
        model.VisitStatus="Schedule";
        model.VisitType=spVisitType.getSelectedItem().toString();
        model.isModified=true;
        controller.saveVisitSchedule(model);
        VisitSyncUtility utility=new VisitSyncUtility(this);
        utility.sync();
        Toast.makeText(this,"Visit Schedule",Toast.LENGTH_LONG).show();
        finish();
    }

    //***************************DatePicker*****************************************************

    @SuppressLint("ValidFragment")
    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        Button  res;

        DatePickerFragment(Button res) {
            this.res = res;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }


        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            Button ed = res;//(Button) getActivity().findViewById(res);
            String dayStr=day+"";
            month=month+1;
            String monthStr=month+"";
            if(dayStr.length()==0)
                dayStr=0+dayStr;
            if(monthStr.length()==0)
                monthStr=0+monthStr;
            ed.setText(dayStr + "/" + monthStr + "/" + year);

            long ScheduleDateLong=CommonShare.ddMMYYDateToLong(ed.getText().toString().replace("/","-"));

            final Calendar c = Calendar.getInstance();
            int tyear = c.get(Calendar.YEAR);
            int tmonth = c.get(Calendar.MONTH);
            int tday = c.get(Calendar.DAY_OF_MONTH);
            long TodayDateLong=CommonShare.ddMMYYDateToLong(
                    tday+"-"+(tmonth+1)+"-"+tyear);

            // CommonShare.alert(getContext(),  ScheduleDateLong+":"+TodayDateLong+"::"+(ScheduleDateLong-TodayDateLong)+"");
            if(ScheduleDateLong-TodayDateLong<0)
            {
                CommonShare.alert(getContext(),("Invalid Date "));//+(ScheduleDateLong-TodayDateLong))+"");
                ed.setText("Select Date");
            }
        }

    }
}

