package com.apgautomation.ui.taskmgmt.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.apgautomation.R;
import com.apgautomation.model.EmployeeModel;
import com.apgautomation.utility.CommonShare;

import java.util.ArrayList;
import java.util.Calendar;

public class Filter extends AppCompatActivity implements View.OnClickListener {

    ArrayList<EmployeeModel> emplList=new ArrayList<>();
    ArrayList<String> statusList=new ArrayList<>();
    Spinner SpinnerEmp,SpinnerStatus;
    Button btnFromDate,btnToDate,btnDone;
    int Option;

    public  static  int EmpId;
    public  static  String status="";
    public  static  long FromDate,ToDate;
    public  static  String prioirity="";

    RadioButton rdlow,rdmedium,rdhigh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        rdlow=findViewById(R.id.rdlow);
        rdmedium=findViewById(R.id.rdmedium);
        rdhigh=findViewById(R.id.rdhigh);

        SpinnerEmp=findViewById(R.id.SpinnerEmp);
        SpinnerStatus=findViewById(R.id.SpinnerStatus);
        btnFromDate=findViewById(R.id.btnFromDate);
        btnToDate=findViewById(R.id.btnToDate);
        btnDone=findViewById(R.id.btnDone);
        btnFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment(btnFromDate);
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
        btnToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment(btnToDate);
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
        EmployeeModel emp=new EmployeeModel();
        emp.EmpId=0;emp.EmpName="All";
        emplList.add(emp);
        emplList.addAll(  CommonShare.getEmployeeList(this));
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,emplList);
        SpinnerEmp.setAdapter(adapter);
        statusList.add("All");
        statusList.add("Open");
        statusList.add("Started");
        statusList.add("In Progress");

        statusList.add("Closed");
        statusList.add("Completed");

        ArrayAdapter adapter1=new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,statusList);
        SpinnerStatus.setAdapter(adapter1);

        try {
            Option = getIntent().getExtras().getInt("Option");
            if (Option == 0) {
                TextView nameLable=findViewById(R.id.nameLable);
                nameLable.setText("Created By");
            }
        }
        catch (Exception ex)
        {}

        btnDone.setOnClickListener(this);
        prioirity="";
    }

    @Override
    public void onClick(View view) {
        if(btnFromDate.getText().toString().equalsIgnoreCase("Select Date"))
        {
            FromDate=0;
        }
        else
        {
            FromDate= CommonShare.ddMMYYDateToLong(btnFromDate.getText().toString().replace("/","-"));
        }
        if(btnToDate.getText().toString().equalsIgnoreCase("Select Date"))
        {
            ToDate=215533938600000L;
        }
        else
        {
            ToDate= CommonShare.ddMMYYDateToLong(btnToDate.getText().toString().replace("/","-"));
        }
        EmpId=  ( (EmployeeModel)SpinnerEmp.getSelectedItem()).EmpId;
        status=SpinnerStatus.getSelectedItem().toString();

        if(rdhigh.isChecked())
            prioirity=rdhigh.getText().toString();
        if(rdlow.isChecked())
            prioirity=rdlow.getText().toString();
        if(rdmedium.isChecked())
            prioirity=rdmedium.getText().toString();

        setResult(RESULT_OK,new Intent());
        finish();
    }



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
