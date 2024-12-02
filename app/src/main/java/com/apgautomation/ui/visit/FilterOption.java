package com.apgautomation.ui.visit;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;

import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.apgautomation.R;
import com.apgautomation.model.EmployeeModel;
import com.apgautomation.model.GsonGroup;
import com.apgautomation.ui.customer.SelectGroup;
import com.apgautomation.utility.CommonShare;

import java.util.ArrayList;
import java.util.Calendar;

public class FilterOption extends AppCompatActivity {

    Button btnShow,btnSelectGroup,btnFromDate,btnToDate;
    Spinner spAssignTO;
    ArrayList<EmployeeModel> teamList=new ArrayList<>();
    public static long fromDateMiilisecond=0,toDateMillisecond=0;
    public static  int EmpId=0,GroupId;
    GsonGroup group;
    boolean IsSales;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_option);
        try
        {
            IsSales=getIntent().getExtras().getBoolean("IsSales");
        }
        catch (Exception exz)
        {}
        spAssignTO=findViewById(R.id.spAssignTO);
        btnShow=findViewById(R.id.btnShow);
        btnSelectGroup=findViewById(R.id.btnSelectGroup);
        btnFromDate=findViewById(R.id.btnFromDate);
        btnToDate=findViewById(R.id.btnToDate);
        fromDateMiilisecond=0;
        toDateMillisecond=0;
        EmpId=0;
        GroupId=0;
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Filter");

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

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show();
            }
        });
        btnSelectGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EmployeeModel m=  CommonShare.getMyEmployeeModel(FilterOption.this);
                if(m.DeptId==2) {
                    startActivityForResult(new Intent(FilterOption.this, SelectGroup.class).putExtra("IsSales",true), 1);
                }
                else
                {
                    startActivityForResult(new Intent(FilterOption.this, SelectGroup.class).putExtra("IsSales",false), 1);

                }
            }
        });
        EmployeeModel m=new EmployeeModel();
        m.EmpId=0;
        m.EmpName="Select";
        teamList.add(m);

       // if(CommonShare.getEmpId(this)==17 ||CommonShare.getEmpId(this)==19 || CommonShare.getEmpId(this)==52|| CommonShare.getEmpId(this)==65  ||CommonShare.getRole(this).equalsIgnoreCase("admin"))
        if((CommonShare.getRole(this).equalsIgnoreCase("Assistance Manager")
                && CommonShare.getMyEmployeeModel(this).DeptId==3  )  ||
                CommonShare.getRole(this).equalsIgnoreCase("Admin")
        )

        {
            if(!IsSales)
               teamList.addAll( CommonShare.geSEmployeeList(this));
            else
                teamList.addAll( CommonShare.geSalesEmployeeList(this));

        }
        else
        {
            teamList.addAll( CommonShare.getTeamListPlusOwn(this));

        }
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,teamList);
        spAssignTO.setAdapter(adapter);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    void  show()
    {
        fromDateMiilisecond = CommonShare.ddMMYYDateToLong(btnFromDate.getText().toString().replace("/", "-"));
        toDateMillisecond = CommonShare.ddMMYYDateToLong(btnToDate.getText().toString().replace("/", "-"));
        EmpId = ((EmployeeModel) spAssignTO.getSelectedItem()).EmpId;
        try {
            GroupId = group.getGroupId();

        }
        catch (Exception ex)
        {}
        setResult(RESULT_OK,new Intent());
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK)
        {
            group=SelectGroup.selectedGroup;
            btnSelectGroup.setText(group.getGroupName());
        }

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
