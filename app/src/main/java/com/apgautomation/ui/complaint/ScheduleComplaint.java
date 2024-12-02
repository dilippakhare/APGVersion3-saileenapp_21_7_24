package com.apgautomation.ui.complaint;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.apgautomation.R;
import com.apgautomation.model.EmployeeModel;
import com.apgautomation.model.ServerModel.ServerComplaintModel;
import com.apgautomation.ui.visit.ScheduleVisit;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.AsyncUtilities;
import com.apgautomation.utility.serverutility.DownloadUtility;
import com.google.gson.Gson;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.apgautomation.ui.complaint.ViewComplaintDetails.model;

public class ScheduleComplaint extends AppCompatActivity implements DownloadUtility {

    Button btnEng,btnDate,btnTime,btnSave;
    RadioButton rdlow,rdMedium,rdHigh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_complaint);
        btnEng=findViewById(R.id.btnEng);
        btnDate=findViewById(R.id.btnDate);
        btnTime=findViewById(R.id.btnTime);
        btnSave=findViewById(R.id.btnSave);
        btnEng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<EmployeeModel> list= CommonShare.geSEmployeeList(ScheduleComplaint.this);
                ArrayAdapter adapter=new ArrayAdapter(ScheduleComplaint.this,android.R.layout.simple_list_item_1,list);
                AlertDialog.Builder alert=new AlertDialog.Builder(ScheduleComplaint.this);
                alert.setTitle("Select Engineer To Assign this complaint");
                alert.setAdapter(adapter, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        //schedule(list.get(i));
                        btnEng.setText(list.get(i).EmpName);
                        btnEng.setTag(list.get(i));
                    }
                });
                alert.show();
            }
        });
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment(btnDate);
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePick();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Schedule();
            }
        });
        rdlow=findViewById(R.id.rdlow);
        rdMedium=findViewById(R.id.rdMedium);
        rdHigh=findViewById(R.id.rdHigh);
        btnTime.setText("17:00");

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Schedule Complaints");
        Long days = CommonShare.getForwordDay(System.currentTimeMillis(),  1);
        String strDate = CommonShare.convertToDate(days);

        Date  dd=new Date();
        String ms= dd.getHours()+":"+dd.getMinutes();
        btnTime.setText(ms);
        btnDate.setText(strDate);
    }

    private int mYear, mMonth, mDay, mHour, mMinute;
    void timePick()
    {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        btnTime.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    void Schedule()
    {
        //Stream ScheduleComplaintAndPriority(int ComplaintId, int UserId, int EmpId, string EmpName, long CommitedTime, string Priority);

        if(btnEng.getTag()==null)
        {
            Toast.makeText(this, "Select Enginner", Toast.LENGTH_SHORT).show();
            return;
        }
        EmployeeModel emp= (EmployeeModel)btnEng.getTag();;
        String Priority="";
        if(rdlow.isChecked())
            Priority=rdlow.getText().toString();
        if(rdHigh.isChecked())
            Priority=rdHigh.getText().toString();
        if(rdMedium.isChecked())
            Priority=rdMedium.getText().toString();
        try
        {
            long cTime= CommonShare.ddMMYYDateToLong(btnDate.getText().toString().replace("/","-"));

            long tTime=  Long.parseLong (btnTime.getText().toString().split(":")[0])*   (3600*1000);
            long mTime=  Long.parseLong (btnTime.getText().toString().split(":")[1])*   (1000);
            String url = CommonShare.url + "Service1.svc/ScheduleComplaintAndPriority?EmpId=" + emp.EmpId +
                    "&ComplaintId=" + model.ComplaintId + "&UserId=" + CommonShare.getUserId(this)
                    +"&EmpName="+ URLEncoder.encode(emp.EmpName,"utf-8")+"&Priority="+Priority+"&CommitedTime="+(cTime+tTime+mTime);                   ;

            AsyncUtilities utilities=new AsyncUtilities(this,false,url,"",21,this);
            utilities.execute();
        }
        catch (Exception ex)
        {}
    }
    @Override
    public void onComplete(final String str, int requestCode, int responseCode)
    {
                 if(requestCode==21 && responseCode==200)
                 {
                     Intent intent=new Intent();
                     intent.setAction("RefreshComplaint");
                     sendBroadcast(intent);


                     AlertDialog.Builder alert=new AlertDialog.Builder(this);
                     alert.setTitle("Schedule Visit");
                     alert.setMessage("Complaint Schedule to engineer.Do you want to schedule visit for  this complaint?");
                     alert.setPositiveButton("Schedule Visit", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialogInterface, int i) {
                             finish();
                             try
                             {
                                 Gson gson = new Gson();
                                 ServerComplaintModel m =gson.fromJson(str,ServerComplaintModel.class);
                                 startActivity(
                                         new Intent(ScheduleComplaint.this, ScheduleVisit.class).putExtra("Complaint",str)
                                 );
                             }
                             catch (Exception ew){}
                         }
                     });
                     alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialogInterface, int i) {

                             finish();
                             startActivity(new Intent(ScheduleComplaint.this,ComplaintDashboard.class)
                                     .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                         }
                     });
                     alert.setCancelable(false);
                     alert.show();
                 }

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
