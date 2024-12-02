package com.apgautomation.ui.salesvisit;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.apgautomation.R;
import com.apgautomation.controller.SaleVisitController;
import com.apgautomation.controller.SyncCustomerControlller;
import com.apgautomation.model.EmployeeModel;
import com.apgautomation.model.GSONCustomerMasterBeanExtends;
import com.apgautomation.model.GsonGroup;
import com.apgautomation.model.GsonSalesVisitType;
import com.apgautomation.model.ServerModel.GsonSalesVisit;
import com.apgautomation.ui.customer.SelectGroup;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.netrequest.ConnectionDetector;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SalesScheduleVisit extends AppCompatActivity {

    Button btnSelectGroup,btnScheduleDate,btnSelectCustomer;
    Spinner spVisitType;
    Spinner spAssignTO;

    Button btnSchedule;

    ArrayList<EmployeeModel> teamList;
    ArrayList<GsonSalesVisitType> visitTypes;
    GsonGroup group;
    CheckBox chkIsNewGroup,chkIsNewCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_schedule_visit);
        btnSelectGroup=findViewById(R.id.btnSelectGroup);
        btnSelectCustomer=findViewById(R.id.btnSelectCustomer);
        btnScheduleDate=findViewById(R.id.btnScheduleDate);
        spVisitType=findViewById(R.id.spVisitType);
        spAssignTO=findViewById(R.id.spAssignTO);

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

                startActivityForResult(new Intent(SalesScheduleVisit.this, SelectGroup.class).putExtra("IsSales",true),1);
            }
        });
        btnSelectCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showCulstomerListAlert();
                //startActivityForResult(new Intent(SalesScheduleVisit.this, SelectGroup.class),1);
            }
        });

        btnScheduleDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmVisit();
            }
        });

         ArrayList<EmployeeModel> tList= CommonShare.getTeamListPlusOwn(this);
        teamList=new ArrayList<>();
        for (EmployeeModel t:tList )
        {
            if(!t.DeleteStatus)
            {
                teamList.add(t);
            }
        }
        //teamList=
              //  CommonShare.getTeamListPlusOwn(this);
        visitTypes= CommonShare.getSalesVisitTypes(this);
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,teamList);
        spAssignTO.setAdapter(adapter);

        ArrayAdapter adapter1=new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,visitTypes);
        spVisitType.setAdapter(adapter1);


        btnScheduleDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new SalesScheduleVisit.DatePickerFragment(btnScheduleDate);
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sales Visit Scheduling");
        chkIsNewGroup=findViewById(R.id.chkIsNewGroup);
        chkIsNewCustomer=findViewById(R.id.chkIsNewCustomer);
        chkIsNewGroup.setChecked(false);
        chkIsNewCustomer.setChecked(false);


        chkIsNewGroup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                if(b)
                {
                    chkIsNewCustomer.setEnabled(false);
                    startActivityForResult(new Intent(SalesScheduleVisit.this,CreateNewCustomer.class),2);
                    btnSelectGroup.setEnabled(false);
                    btnSelectCustomer.setEnabled(false);
                }
                else
                {
                    btnSelectGroup.setEnabled(true);
                    chkIsNewCustomer.setEnabled(true);
                }
                //chkIsNewCustomer.setChecked(b);
            }
        });
        chkIsNewCustomer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {

                    if(!chkIsNewGroup.isChecked()) {
                        startActivityForResult(new Intent(SalesScheduleVisit.this, CreateNewCustomer.class).putExtra("GroupName", btnSelectGroup.getText().toString()), 3);
                        btnSelectCustomer.setEnabled(false);
                    }
                }
                else
                {
                    btnSelectCustomer.setEnabled(true);

                }
            }
        });
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
            if(CommonShare.getEmpId(SalesScheduleVisit.this)==employeeModelObj.EmpId)
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
                try {
                    scheduleVisit();
                }
                catch (Exception ex){
                    CommonShare.alert(SalesScheduleVisit.this,ex.toString());
                }
            }
        });
        alert.setNegativeButton("Cancel",null);
        alert.show();
    }

    void showCulstomerListAlert()
    {
        SyncCustomerControlller cnt=new SyncCustomerControlller(this);
        final ArrayList<GSONCustomerMasterBeanExtends>  mList=  cnt.getCustomerListByGroupIdNonRelam(group.getGroupId());
        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setTitle("Select Customer");
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,mList);
        alert.setSingleChoiceItems(adapter, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                    btnSelectCustomer.setText(mList.get(i).CustomerName);
                    btnSelectCustomer.setTag(mList.get(i));
                    dialogInterface.dismiss();
            }
        });
        alert.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1) {
            if (resultCode == RESULT_OK) {
                group = SelectGroup.selectedGroup;
                btnSelectGroup.setText(group.getGroupName());
            }
        }
        if(requestCode==2) {
            if (resultCode == RESULT_OK)
            {
                group=null;
               // group = SelectGroup.selectedGroup;
                btnSelectGroup.setText(CreateNewCustomer.GroupName);
                btnSelectCustomer.setText(CreateNewCustomer.CustomerName);
            }
        }
        if(requestCode==3)
        {
            if (resultCode == RESULT_OK)
            {
                /*
                if(!chkIsNewGroup.isChecked()) {
                    group = SelectGroup.selectedGroup;
                    btnSelectGroup.setText(group.getGroupName());
                }
                else  if(chkIsNewGroup.isChecked())
                {
                    btnSelectCustomer.setText(CreateNewCustomer.CustomerName);
                }
                */
                 btnSelectCustomer.setText(CreateNewCustomer.CustomerName);
            }
        }


    }

    void scheduleVisit()
    {
        GsonSalesVisit model=new GsonSalesVisit();
        model.VisitTokenId=CommonShare.generateToken(this);
        model.AssignToId=((EmployeeModel)spAssignTO.getSelectedItem()).EmpId;
       try
       {
         model.AssignToName=   ((EmployeeModel)spAssignTO.getSelectedItem()).EmpName;
       }
       catch (Exception ex)
       {}
        model.EnterById=CommonShare.getEmpId(this);
       try
       {
           model.EnterByName = CommonShare.getMyEmployeeModel(this).EmpName;
       }
       catch (Exception exx){}
        model.EnteredDateMillisecond=System.currentTimeMillis();
        model.ScheduleDateMillisecond=CommonShare.ddMMYYDateToLong(btnScheduleDate.getText().toString().replace("/","-"));

        model.VisitStatus="Schedule";
        model.VisitType=spVisitType.getSelectedItem().toString();
        model.IsModified=true;


        if(!chkIsNewGroup.isChecked())
        {
            model.IsExistingGroup=true;
            model.GroupId = group.getGroupId();
            try
            {
                model.VGroupName=group.getGroupName();
            }
            catch (Exception ex){}
        }
        else
        {
            model.setVGroupToken(CommonShare.generateToken(this));
            model.setVGroupName(CreateNewCustomer.GroupName);
            model.IsExistingGroup=false;

            model.setVCustomerToken(CommonShare.generateToken(this));
            model.setVCustomerName(CreateNewCustomer.CustomerName);
            model.IsExistingCustomer=false;
            model.VCustomerAreaId=CreateNewCustomer.AreaId;
        }
        if(model.IsExistingGroup)
        {
            if (!chkIsNewCustomer.isChecked()) {
                model.IsExistingCustomer = true;
                model.CustomerId = ((GSONCustomerMasterBeanExtends) btnSelectCustomer.getTag()).CustomerId;
                try
                {
                    model.VCustomerName=((GSONCustomerMasterBeanExtends) btnSelectCustomer.getTag()).getCustomerName();
                }
                catch (Exception ex){}

            } else {
                model.setVCustomerToken(CommonShare.generateToken(this));
                model.setVCustomerName(CreateNewCustomer.CustomerName);
                model.IsExistingCustomer = false;
                model.VCustomerAreaId = CreateNewCustomer.AreaId;
            }
        }
        try
        {
            model.AssignToName=CommonShare.empMap.get(model.getAssignToId()).EmpName;
            model.EnterByName=CommonShare.empMap.get(CommonShare.getEmpId(this)).EmpName;
        }
        catch (Exception ex){}
        SaleVisitController controller=new SaleVisitController(this);
        model.IsModified=true;
        controller.saveVisitSchedule(model);
       // controller.saveVisitSchedule(model);
       // VisitSyncUtility utility=new VisitSyncUtility(this);
      //  utility.sync();
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
