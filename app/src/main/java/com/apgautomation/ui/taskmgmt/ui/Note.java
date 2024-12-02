package com.apgautomation.ui.taskmgmt.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.apgautomation.R;
import com.apgautomation.model.GsonToDoDetails;
import com.apgautomation.model.GsonTodo;
import com.apgautomation.model.GsonTodoWithDetail;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.AsyncUtilities;
import com.apgautomation.utility.serverutility.DownloadUtility;
import com.apgautomation.utility.serverutility.netrequest.ConnectionDetector;
import com.apgautomation.utility.syncutilities.VisitSyncUtility;
import com.google.gson.Gson;

import java.util.Calendar;

import io.realm.Realm;

public class Note extends AppCompatActivity implements DownloadUtility {
  static GsonTodo model;

  String masterToken;
  EditText note;
  CheckBox chk,chkStarted,chkProgress,chkChangeDueDate;
  Button btnSave;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_note);
    masterToken=  getIntent().getExtras().getString("Token");
    note=findViewById(R.id.note);
    chk=findViewById(R.id.chk);
    chkStarted=findViewById(R.id.chkStarted);
    chkProgress=findViewById(R.id.chkProgress);
    btnSave=findViewById(R.id.btnSave);
    chkChangeDueDate=findViewById(R.id.chkChangeDueDate);
    btnSave.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if(checkValidation())
        {
          saveData();
        }
      }
    });

    getSupportActionBar().setHomeButtonEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle("Add Note");
    Realm realm;
    realm=Realm.getDefaultInstance();
    model= realm.where(GsonTodo.class).equalTo("Token",masterToken).findFirst();
    if(model.getStatus().equalsIgnoreCase("Closed"))
    {
        chk.setVisibility(View.GONE);
        chkProgress.setVisibility(View.GONE);
        chkStarted.setVisibility(View.GONE);
    }

      if(model.getStatus().equalsIgnoreCase("Completed"))
      {
          chk.setVisibility(View.GONE);
          chkProgress.setVisibility(View.GONE);
          chkStarted.setVisibility(View.GONE);
      }
      if(model.getStatus().equalsIgnoreCase("In Progress"))
      {
          chkStarted.setVisibility(View.GONE);
      }
      chkStarted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
              if(b)
              {
                  chkProgress.setChecked(false);
                  chk.setChecked(false);
              }
          }
      });
      chkProgress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
              if(b)
              {
                  chk.setChecked(false);
                  chkStarted.setChecked(false);
              }
          }
      });
      chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    chkProgress.setChecked(false);
                    chkStarted.setChecked(false);
                }
          }
      });
      if(model.getEnterById()==CommonShare.getEmpId(this))
      {
          chkChangeDueDate.setVisibility(View.VISIBLE);
            //chkChangeDueDate.
          chkChangeDueDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
              @Override
              public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b)
                    {
                        note.setEnabled(false);
                        DialogFragment newFragment = new DatePickerFragment(note);
                        newFragment.show(getSupportFragmentManager(), "datePicker");
                    }
                    else
                    {
                        note.setEnabled(true);
                        note.setText("");
                    }
              }
          });
      }
  }

  private void saveData() {
    GsonToDoDetails details=new GsonToDoDetails();
    details.setDetailToken(CommonShare.generateToken(this));
    details.setToken(masterToken);
    details.setTaskDetails(note.getText().toString());
    details.setEnterBy(CommonShare.getMyEmployeeModel(this).EmpName);
    details.setEnterById(CommonShare.getEmpId(this));
    details.setComplete(chk.isChecked());
    details.setEnterDate(CommonShare.getDateTime(System.currentTimeMillis()));
    details.setEnterDateMillisecond(System.currentTimeMillis());
    details.setModified(true);
      Realm realm=Realm.getDefaultInstance();
    if(!chkChangeDueDate.isChecked()) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(details);
        realm.commitTransaction();
    }

    if(chk.isChecked())
    {
      GsonTodo t= realm.where(GsonTodo.class).equalTo("Token",details.getToken()).findFirst();

      realm.beginTransaction();
      t.setStatus("Completed");
      t.setModified(true);
      realm.copyToRealmOrUpdate(t);
      realm.commitTransaction();

      startActivity(new Intent(Note.this,TaskMgmt.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

    }
      if(chkStarted.isChecked())
      {
          GsonTodo t= realm.where(GsonTodo.class).equalTo("Token",details.getToken()).findFirst();

          realm.beginTransaction();
          t.setStatus("Started");
          t.setModified(true);
          realm.copyToRealmOrUpdate(t);
          realm.commitTransaction();

          startActivity(new Intent(Note.this,TaskMgmt.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

      }
      if(chkProgress.isChecked())
      {
          GsonTodo t= realm.where(GsonTodo.class).equalTo("Token",details.getToken()).findFirst();

          realm.beginTransaction();
          t.setStatus("In Progress");
          t.setModified(true);
          realm.copyToRealmOrUpdate(t);
          realm.commitTransaction();

          startActivity(new Intent(Note.this,TaskMgmt.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
      }
      if(chkChangeDueDate.isChecked())
      {
       try {
           Gson gson=new Gson();
           GsonTodo t= realm.where(GsonTodo.class).equalTo("Token",details.getToken()).findFirst();

           GsonTodo n= realm.copyFromRealm(t);
           long ml= CommonShare.ddMMYYDateToLong(cDATE);
           n.setDueDateMillisecond(ml);
           n.setDueDate(cDATE);

           GsonToDoDetails dd=   details; // realm.copyFromRealm(details);

           GsonTodoWithDetail tdd=new GsonTodoWithDetail();
           tdd.MasterModel=n;
           tdd.Details=dd;
           String payload=gson.toJson(tdd);

           String url=CommonShare.url+"Service1.svc/ChangeDueDate";
           AsyncUtilities utilities = new AsyncUtilities(this, true, url, payload, 1, this);
           utilities.execute();
       }
       catch (Exception exc)
       {
           Log.d("Apg",exc.toString());
       }
      }
      if(!chkChangeDueDate.isChecked())
      {
          VisitSyncUtility utility = new VisitSyncUtility(this);
          utility.sync();
          finish();
      }

  }

  boolean checkValidation()
  {
    if(note.getText().toString().equalsIgnoreCase(""))
    {
      Toast.makeText(this, "Note Required", Toast.LENGTH_SHORT).show();
      return false;
    }
    if(!new ConnectionDetector(this).isConnectingToInternet())
    {
      Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
      return false;
    }
    return true;
  }
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home)
      onBackPressed();
    return super.onOptionsItemSelected(item);
  }

    @Override
    public void onComplete(String str, int requestCode, int responseCode) {
        if(requestCode==1  && responseCode==200)
        {
            if(str.contains("Updated"))
              startActivity(new Intent(Note.this,TaskMgmt.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            else
                Toast.makeText(this, "Not Saved", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this, "Not Saved", Toast.LENGTH_SHORT).show();
    }


    //***************************DatePicker*****************************************************
   static String cDATE="";
    @SuppressLint("ValidFragment")
    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        EditText  res;

        DatePickerFragment(EditText res) {
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
            //res.setText("");
            String dayStr=day+"";
            month=month+1;
            String monthStr=month+"";
            if(dayStr.length()==0)
                dayStr=0+dayStr;
            if(monthStr.length()==0)
                monthStr=0+monthStr;
            cDATE=dayStr + "/" + monthStr + "/" + year;
            res.setText("Due Date Changed \nNew Due Date:-" +dayStr + "/" + monthStr + "/" + year+"\nOld Due Date:-"+model.getReDueDate());
        }

    }

}
