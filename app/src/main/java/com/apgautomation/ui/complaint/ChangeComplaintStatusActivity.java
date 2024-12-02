package com.apgautomation.ui.complaint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.apgautomation.R;
import com.apgautomation.model.EmployeeModel;
import com.apgautomation.model.GsonComplaintStatus;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.AsyncUtilities;
import com.apgautomation.utility.serverutility.DownloadUtility;

import org.json.JSONObject;

import java.util.ArrayList;

public class ChangeComplaintStatusActivity extends AppCompatActivity implements DownloadUtility {



    Spinner spStatus,spAssignTO;
    ArrayList<EmployeeModel> teamList=new ArrayList<>();
    ArrayList<GsonComplaintStatus> statussList=new ArrayList<>();
    EditText edReason;

    Button  btnChangeStatus;
    int selectionId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_complaint_status);
        spAssignTO=findViewById(R.id.spAssignTO);
        spStatus=findViewById(R.id.spStatus);

        teamList=   CommonShare.getEmployeeList(this);
        statussList=CommonShare.getComplaintStatusList(this);
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,teamList);
        spAssignTO.setAdapter(adapter);

        ArrayAdapter adapter1=new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,statussList);
        spStatus.setAdapter(adapter1);

        edReason=findViewById(R.id.edReason);

        btnChangeStatus=findViewById(R.id.btnChangeStatus);
        btnChangeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  validate();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        try
        {
                for(int i=0;i<teamList.size();i++)
                {
                    if(ViewComplaintDetails.model.AssignToId==teamList.get(i).EmpId)
                    {
                        this.selectionId=i;
                          new Thread(new Runnable() {
                              @Override
                              public void run() {
                                  try {
                                      Thread.sleep(1500);
                                  } catch (InterruptedException e) {
                                      e.printStackTrace();
                                  }
                                  runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                  spAssignTO.setSelection(  selectionId);
                                            }
                                            catch (Exception ex){}
                                        }
                                    });
                              }
                          }).start();
                    }
                }
        }
        catch (Exception ex)
        {}
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();

        return super.onOptionsItemSelected(item);
    }

    private void validate()
    {
          if(spStatus.getSelectedItemPosition()==0)
          {
              Toast.makeText(this, "Select New Status", Toast.LENGTH_SHORT).show();
              return;
          }
          try
          {
              JSONObject j=new JSONObject();
              j.put("ComplaintToken",ViewComplaintDetails.model.Token);
              j.put("ComplaintStatus",statussList.get(spStatus.getSelectedItemPosition()).toString());
              j.put("Description",edReason.getText().toString());
              j.put("ResponsibleEmpId",teamList.get(spAssignTO.getSelectedItemPosition()).EmpId);
              j.put("ResponsibleName",teamList.get(spAssignTO.getSelectedItemPosition()).EmpName);
              j.put("EnterBy",CommonShare.getMyEmployeeModel(this).EmpName);


              String url=CommonShare.url+"Service1.svc/ChangeLogStatus";
              try {
                  AsyncUtilities utilities = new AsyncUtilities(this, true, url, j.toString(), 702, this);
                  utilities.execute();
              }
              catch (Exception ex){}

          }
          catch (Exception ex)
          {}
    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode) {

         if(responseCode==200)
         {
             startActivity(new Intent(this,ComplaintDashboard.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
         }
        //Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}