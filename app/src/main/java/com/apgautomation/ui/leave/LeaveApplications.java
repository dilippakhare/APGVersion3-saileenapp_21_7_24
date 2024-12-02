package com.apgautomation.ui.leave;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.apgautomation.R;
import com.apgautomation.controller.LeaveController;
import com.apgautomation.model.EmployeeModel;
import com.apgautomation.model.LeaveDetails;
import com.apgautomation.model.LeaveModel;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.DownloadUtility;
import com.apgautomation.utility.syncutilities.LeaveSyncUtility;

import java.util.ArrayList;
import java.util.Calendar;

public class LeaveApplications extends AppCompatActivity implements AdapterView.OnItemSelectedListener , DownloadUtility {

    String[] arr = new String[]{"1", "0.5", "1.5", "2", "2.5", "3", "3.5", "4", "4.5", "5", "5.5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"};
    Spinner spinnerNoOfDays, spinnerResponsible, spinnerSendTo;
    ArrayList<EmployeeModel> sendToList = new ArrayList<>();
    ArrayList<EmployeeModel> empList = new ArrayList<>();

    LinearLayout linearDates;
    Button btnSubmit;
    EditText edReason;

    LeaveController controller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_applications);
        controller=new LeaveController(this);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Leave Applications");
        spinnerNoOfDays = findViewById(R.id.spinnerNoOfDays);
        spinnerResponsible = findViewById(R.id.spinnerResponsible);
        spinnerSendTo = findViewById(R.id.spinnerSendTo);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, arr);
        spinnerNoOfDays.setAdapter(adapter);

        sendToList.add(CommonShare.getManager(this));
        empList.add(new EmployeeModel(0, "Select"));
        ArrayList<EmployeeModel> eeList=CommonShare.getEmployeeList(this);
        for (EmployeeModel mm:eeList   ) {
            if(!mm.DeleteStatus)
                empList.add(mm);
        }

        ArrayAdapter adapter1 = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, sendToList);
        ArrayAdapter adapter2 = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, empList);
        spinnerSendTo.setAdapter(adapter1);
        spinnerResponsible.setAdapter(adapter2);

        linearDates = findViewById(R.id.linearDates);
        edReason = findViewById(R.id.edReason);

        spinnerNoOfDays.setOnItemSelectedListener(this);
        addDates(1f);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edReason.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(LeaveApplications.this, "Write a reason for leave", Toast.LENGTH_LONG).show();
                    return;
                }
                AlertDialog.Builder alert=new AlertDialog.Builder(LeaveApplications.this);
                alert.setTitle("Leave Application");
                alert.setMessage("Do you want to save this leave application?");
                if(checkForunPlanned().equalsIgnoreCase("Unplanned Leave"))
                {
                    alert.setMessage("This leave will be consider as unplanned leave.Try to apply before 40 hours.\n\nDo you want to save this leave application?");
                }
                alert.setCancelable(false);
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        saveLeave();
                    }
                });
                alert.setNegativeButton("No",null);
                alert.show();

            }
        });
    }
    String  checkForunPlanned()
    {
        long closeDate=0l;
        for(int i=0;i<linearDates.getChildCount();i++)
        {
            LinearLayout layout=(LinearLayout) linearDates.getChildAt(i);
            Button btnDate=layout.findViewById(R.id.btnDate);
            if(closeDate==0)
            {
                closeDate=CommonShare.ddMMYYDateToLong(btnDate.getText().toString().replace("/","-"));
            }
            else
            {
              long newDate=CommonShare.ddMMYYDateToLong(btnDate.getText().toString().replace("/","-"));
              if(newDate<closeDate)
                  closeDate=newDate;
            }
        }
        long schduleDate=  closeDate+(1000*60*60*10);
        long cureentMillisecond=System.currentTimeMillis();
        long timeDiffrence=schduleDate-cureentMillisecond;

        if(timeDiffrence>  (1000*60*60*40))
        {
            return "Planned Leave";
        }
        return "Unplanned Leave";
    }

    void saveLeave()
    {

        LeaveModel model=new LeaveModel();
        model.Token=CommonShare.generateToken(this);
        model.EmpId=CommonShare.getEmpId(this);
        model.ToId=sendToList.get(spinnerSendTo.getSelectedItemPosition()).EmpId;
        model.ResponsiblePersonId=empList.get(spinnerResponsible.getSelectedItemPosition()).EmpId;
        model.TotalDays=Float.parseFloat( arr[spinnerNoOfDays.getSelectedItemPosition()] );
        model.EnterTime=System.currentTimeMillis();
        model.Reason=edReason.getText().toString();
        model.DeleteStatus=false;


        controller.saveLocalModel(model);
        for(int i=0;i<linearDates.getChildCount();i++)
        {
            LeaveDetails mObj=new LeaveDetails();
            LinearLayout layout=(LinearLayout) linearDates.getChildAt(i);
            Button btnDate=layout.findViewById(R.id.btnDate);
            Spinner spleavetype = layout.findViewById(R.id.spleavetype);
            mObj.TokenId=model.Token;
            mObj.Ldate=CommonShare.ddMMYYDateToLong(btnDate.getText().toString().replace("/","-"));
            mObj.LeaveType=arr1[spleavetype.getSelectedItemPosition()];
            controller.insertLeaveDetailsRecord(mObj);

        }

        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setTitle("Leave Application");
        alert.setMessage("Leave Application Saved Successfully");
        alert.setCancelable(false);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
                startActivity(new Intent(LeaveApplications.this,LeaveApplicationsLiist.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
        alert.show();
        LeaveSyncUtility utility=new LeaveSyncUtility(this);
        utility.sync();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        float SelectedValus = Float.parseFloat(arr[i]);
        addDates(SelectedValus);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    String[] arr1 = new String[]{"Full", "First Half", "Second Half"};

    ArrayList<Button> buttons=new ArrayList<>();
    void addDates(float value)
    {
        linearDates.removeAllViews();
        LayoutInflater inflater = getLayoutInflater();

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, arr1);

        for (float i = 0; i < value; i++)
        {
            View v = inflater.inflate(R.layout.item_leavedate, null);
            linearDates.addView(v);
            Integer index = (int) i;
            Long days = CommonShare.getForwordDay(System.currentTimeMillis(), index + 1);
            String strDate = CommonShare.convertToDate(days);
            final Button btnDate = v.findViewById(R.id.btnDate);

            btnDate.setTag(btnDate);
            btnDate.setText(strDate);
            Spinner spleavetype = v.findViewById(R.id.spleavetype);
            spleavetype.setAdapter(adapter);


            btnDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogFragment newFragment = new DatePickerFragment(btnDate);
                    newFragment.show(getSupportFragmentManager(), "datePicker");
                }
            });


        }

    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode) {

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
        }

    }
}