package com.apgautomation.ui.leave;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apgautomation.HomePage;
import com.apgautomation.R;
import com.apgautomation.model.LeaveDetails;
import com.apgautomation.model.LeaveModel;
import com.apgautomation.model.ServerModel.ServerLeaveDetails;
import com.apgautomation.model.ServerModel.ServerLeaveModel;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.AsyncUtilities;
import com.apgautomation.utility.serverutility.DownloadUtility;
import com.apgautomation.utility.serverutility.netrequest.ConnectionDetector;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LeaveApplicationDetails extends AppCompatActivity  implements DownloadUtility {

    TextView txtReason;
    TextView txtResponsible,txtApplicationDate,txtEmpName,txtNumberOfLeave,txtSendToName,txtLeaveDates,txtApprovalStatus,txtApprovalRemarks;
    public static  LeaveModel model;
    LeaveApplicationDetails convertView;
    Button btnReject,btnApprove;
    LinearLayout lBottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_application_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Leave Applications");
        convertView=this;
        lBottom=findViewById(R.id.lBottom);

        try
        {
            Gson g = new Gson();
            String str= getIntent().getExtras().getString("msg");
            JSONObject jsonObject=new JSONObject(str);
            ServerLeaveModel model1 = g.fromJson(jsonObject.getJSONObject("leaveApplication").toString(), ServerLeaveModel.class);

            try
            {
                model=model1.toLeaveModel();
                model.leaveDetails=new ArrayList<>();
                JSONArray arr=jsonObject.getJSONArray("leaveDetails");
                for(int j=0;j<arr.length();j++)
                {
                    ServerLeaveDetails mOb = g.fromJson(arr.getJSONObject(j).toString(), ServerLeaveDetails.class);
                    model1.leaveDetails.add(mOb);
                    model.leaveDetails.add(mOb.toLeaveDetils());
                }


            }
            catch (Exception ex)
            {}

        }
        catch (Exception ex)
        {}
        init();
        if(CommonShare.getEmpId(this)!=model.ToId)
        {
            lBottom.setVisibility(View.INVISIBLE);
        }
        try
        {
           if(model.ApproveStatus.length()>4)
           {
               lBottom.setVisibility(View.INVISIBLE);
           }
        }
        catch (Exception ex)
        {}
      //  Toast.makeText(convertView, model.DeleteStatus+"", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(model.EmpId==CommonShare.getEmpId(this))
        if( model.ApproveStatus ==null ||  model.ApproveStatus.equalsIgnoreCase("null")|| model.ApproveStatus.equalsIgnoreCase("")  )
        {
           getMenuInflater().inflate(R.menu.menudelete,menu);
        }
        if(CommonShare.getRole(this).equalsIgnoreCase("Admin"))
        {
            getMenuInflater().inflate(R.menu.menudelete,menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        if(item.getItemId()==R.id.action_delete)
        {
            AlertDialog.Builder alert=new AlertDialog.Builder(this);
            alert.setTitle("Delete");
            alert.setMessage("Do you want to delete leave application ?");
            alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    callDelete();
                }
            });
            alert.setNegativeButton("No",null);
            alert.show();

        }
        return super.onOptionsItemSelected(item);
    }
    void callDelete()
    {

        String url=CommonShare.url+"Service1.svc/deleteLeave?LeaveId="+model.LeaveId;

        AsyncUtilities utilities=new AsyncUtilities(this,false,url,null,2,this);
        utilities.execute();

    }
    void init()
    {
        txtApplicationDate=convertView.findViewById(R.id.txtApplicationDate);
        txtEmpName=convertView.findViewById(R.id.txtEmpName);
        txtSendToName=convertView.findViewById(R.id.txtSendToName);
        txtNumberOfLeave=convertView.findViewById(R.id.txtNumberOfLeave);
        txtApprovalStatus=convertView.findViewById(R.id.txtApprovalStatus);
        txtApprovalRemarks=convertView.findViewById(R.id.txtApprovalRemarks);
        txtLeaveDates=convertView.findViewById(R.id.txtLeaveDates);
        txtResponsible=convertView.findViewById(R.id.txtResponsible);
        txtReason=findViewById(R.id.txtReason);
        btnReject=findViewById(R.id.btnReject);
        btnApprove=findViewById(R.id.btnApprove);
        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                AlertDialog.Builder alBuilder=new AlertDialog.Builder(convertView);
                alBuilder.setTitle("Approve Leave");
                alBuilder.setMessage("Do you want to approve this leave?");
                alBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                   approve();
                    }
                });
                alBuilder.setNegativeButton("No",null);
                alBuilder.show();

            }
        });
        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                LayoutInflater inflater = getLayoutInflater();
                View v1=inflater.inflate(R.layout.item_alertedittext,null);
                final EditText ed=v1.findViewById(R.id.rejectReason);
                AlertDialog.Builder alBuilder=new AlertDialog.Builder(convertView);
                alBuilder.setTitle("Reject Leave");
                alBuilder.setView(v1);
                alBuilder.setCancelable(true);
                alBuilder.setMessage("Do you want to reject this leave?");
                alBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        reject(ed.getText().toString());
                    }
                });
                alBuilder.setNegativeButton("No",null);
                alBuilder.show();
            }
        });

        txtReason.setText(model.Reason);
        txtApplicationDate.setText(CommonShare.convertToDate( model.EnterTime));
        txtEmpName.setText(CommonShare.empMap.get(model.EmpId).EmpName);
        txtSendToName.setText(CommonShare.empMap.get(model.ToId).EmpName);
        try
        {
            txtResponsible.setText("");
            txtResponsible.setText(CommonShare.empMap.get(model.ResponsiblePersonId).EmpName);
        }
        catch (Exception ex){}
        txtNumberOfLeave.setText(model.TotalDays+"");
        if(model.ApproveStatus==null)
            model.ApproveStatus="";
        if(model.ApproveRemark==null)
            model.ApproveRemark="";

        txtApprovalStatus.setText(model.ApproveStatus==null? "":model.ApproveStatus);
        txtApprovalRemarks.setText(model.ApproveRemark);
        try
        {
            String dates="";
            for (LeaveDetails detais:model.leaveDetails
            )
            {
                if(dates.equalsIgnoreCase(""))
                    dates=CommonShare.convertToDate(detais.Ldate)+"("+detais.LeaveType+")";
                else
                    dates=dates+"\n"+CommonShare.convertToDate(detais.Ldate)+"("+detais.LeaveType+")";
            }

            txtLeaveDates.setText(dates);
        }
        catch (Exception ew){}
    }
    void approve()
    {
        if(!new ConnectionDetector(this).isConnectingToInternet())
        {


            Toast.makeText(convertView, "No Internet Connection Found", Toast.LENGTH_SHORT).show();
            return;
        }
         model. ApproveStatus="Approved";
         model.ApproveRemark="";
        try {
            Gson gson=new Gson();
            String payload=gson.toJson(model);
            String url = CommonShare.url + "Service1.svc/approveLeave";
            AsyncUtilities utilities = new AsyncUtilities(this, true, url, payload, 1, this);
            utilities.execute();
        }
        catch (Exception ex){}
    }
    void reject(String rejectReason)
    {
        if(!new ConnectionDetector(this).isConnectingToInternet())
        {
            Toast.makeText(convertView, "No Internet Connection Found", Toast.LENGTH_SHORT).show();
            return;
        }
        model. ApproveStatus="Reject";
        model.ApproveRemark=rejectReason;
        try {
            Gson gson=new Gson();
            String payload=gson.toJson(model);
            String url = CommonShare.url + "Service1.svc/approveLeave";
            AsyncUtilities utilities = new AsyncUtilities(this, true, url, payload, 1, this);
            utilities.execute();
        }
        catch (Exception ex){}
    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode) {
        if(responseCode==200)
        {
            if(requestCode==1) {
                try {
                    Gson gson = new Gson();
                    JSONObject jsonObject = new JSONObject(str);

                    ServerLeaveModel sModel = gson.fromJson(jsonObject.getJSONObject("leaveApplication").toString(), ServerLeaveModel.class);
                    // if(sModel.ApproveStatus.equalsIgnoreCase("App"))
                    AlertDialog.Builder alerBuilder = new AlertDialog.Builder(this);
                    alerBuilder.setTitle(sModel.ApproveStatus);
                    alerBuilder.setMessage("Leave application is " + sModel.ApproveStatus);
                    alerBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                            startActivity(new Intent(LeaveApplicationDetails.this, LeaveApplicationsLiistForManager.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        }
                    });
                    alerBuilder.setCancelable(false);
                    alerBuilder.show();
                } catch (Exception ex) {
                }
            }
            if(requestCode==2)
            {
               startActivity(new Intent(this, HomePage.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        }
        else
        {
            Toast.makeText(this,"Unable to perform operation at this time",Toast.LENGTH_LONG).show();
        }

    }
}
