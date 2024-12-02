package com.apgautomation.ui.complaint;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apgautomation.R;
import com.apgautomation.model.ComplaintTypeModel;
import com.apgautomation.model.EmployeeModel;
import com.apgautomation.model.GsonComplaintWork;
import com.apgautomation.model.GsonCustomerProduct;
import com.apgautomation.model.ServerModel.ServerComplaintModel;
import com.apgautomation.ui.ViewImage;
import com.apgautomation.ui.visit.ScheduleVisit;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.AsyncUtilities;
import com.apgautomation.utility.serverutility.DownloadUtility;
import com.apgautomation.utility.serverutility.netrequest.ConnectionDetector;
import com.apgautomation.utility.syncutilities.VisitSyncUtility;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

import io.realm.Realm;

public class ViewComplaintDetails extends AppCompatActivity implements DownloadUtility {


    TextView txtDate,txtEquipment,txtEquipmentType,txtSerailNumber,txtComplaint,txtStatus,txtNO;
    TextView txtCustomer,txtAssignTo;
    TextView txtPerson,txtContact;
    TextView txtBookBy,txtPriority,txtCommitedDate;
    ImageView img;
    Button btnOtp,btnWork;
    public static ServerComplaintModel model;
    public Button btnSchedule;

    boolean canUserChangeStatus;
    TextView txtAmcStatus,txtAllocatedEngineer;
    //8380039585	rajaram
// New Version Is Found here
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_complaint_details);
        isFeedbackGiven=false;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("View Complaints");

        init();
        btnSchedule=findViewById(R.id.btnSchedule);

       // if( CommonShare.getRole(this).equalsIgnoreCase("Assistance Manager")  ) // CommonShare.getEmpId(this)==65 ||  CommonShare.getEmpId(this)==17 ||CommonShare.getEmpId(this)==19||CommonShare.getEmpId(this)==52 ||CommonShare.getEmpId(this)==1064)// ||   true )//CommonShare.getUserId(this)==54)
        if((CommonShare.getRole(this).equalsIgnoreCase("Assistance Manager")
                && CommonShare.getMyEmployeeModel(this).DeptId==3  )  ||
                CommonShare.getRole(this).equalsIgnoreCase("Admin")
        )

        {

            if(!model.ComplaintStatus.equalsIgnoreCase("Closed"))
            {
                canUserChangeStatus=true;
                btnSchedule.setVisibility(View.VISIBLE);
                btnSchedule.setOnClickListener(new View.OnClickListener()
                {
                    //Stream ScheduleComplaintAndPriority(int ComplaintId, int UserId, int EmpId, string EmpName, long CommitedTime, string Priority);
                    @Override
                    public void onClick(View view)
                    {
                        /*
                         final ArrayList<EmployeeModel> list=CommonShare.geSEmployeeList(ViewComplaintDetails.this);
                         ArrayAdapter adapter=new ArrayAdapter(ViewComplaintDetails.this,android.R.layout.simple_list_item_1,list);
                         AlertDialog.Builder alert=new AlertDialog.Builder(ViewComplaintDetails.this);
                         alert.setTitle("Select Engineer To Assign this complaint");
                         alert.setAdapter(adapter, new DialogInterface.OnClickListener()
                         {
                             @Override
                             public void onClick(DialogInterface dialogInterface, int i)
                             {
                                 schedule(list.get(i));
                             }
                         });
                         alert.show();*/
                        startActivity(new Intent(ViewComplaintDetails.this,ScheduleComplaint.class));

                    }
                });
            }
        }

        if(CommonShare.getEmpId(this)>0)
        {
            String url = CommonShare.url + "Service1.svc/GetComplaintWorkAndLog?complainttoken=" + model.Token;
            AsyncUtilities utilities = new AsyncUtilities(this, false, url, null, 22, this);
            utilities.execute();
        }
    }
    ViewComplaintDetails convertView;
    LinearLayout linearWork;
    void init()
    {
        convertView=this;
        txtCustomer=convertView.findViewById(R.id.txtCustomer);
        txtDate=convertView.findViewById(R.id.txtDate);
        txtEquipment=convertView.findViewById(R.id.txtEquipment);
        txtEquipmentType=convertView.findViewById(R.id.txtEquipmentType);
        txtSerailNumber=convertView.findViewById(R.id.txtSerailNumber);
        txtComplaint=convertView.findViewById(R.id.txtComplaint);
        txtStatus=convertView.findViewById(R.id.txtStatus);
        txtPerson=convertView.findViewById(R.id.txtPerson);
        txtContact=convertView.findViewById(R.id.txtContact);
        btnOtp=convertView.findViewById(R.id.btnOtp);
        txtAssignTo=convertView.findViewById(R.id.txtAssignTo);
        txtBookBy=convertView.findViewById(R.id.txtBookBy);
        txtCommitedDate=convertView.findViewById(R.id.txtCommitedDate);
        txtPriority=convertView.findViewById(R.id.txtPriority);
        try
        {
            if(model.ReCommitedDate.length()>5)
            {
                txtCommitedDate.setText(  CommonShare.getDateTime(CommonShare.parseDate(model.ReCommitedDate)));
            }
        }
        catch (Exception ewx)
        {}
        try
        {
            if(model.Priority!=null  && !model.Priority.equalsIgnoreCase("null"))
               txtPriority.setText(model.Priority);
        }
        catch (Exception ex){}
        //=list.get(position);
        // txtAmcStart.setText(CommonShare.convertToDate(  CommonShare.parseDate( model.AmcStartDate)));
        txtDate.setText(CommonShare.getDateTime(  CommonShare.parseDate( model.EnterDate)));
        txtEquipment.setText(model.EquipmentName);
        txtEquipmentType.setText(model.SelectedEquipment);


        txtComplaint.setText(model.ComplaintDetails);
        txtSerailNumber.setText(model.SerialNumber);
        //txtStatus=convertView.findViewById(R.id.txtStatus);
        txtStatus.setText(model.ComplaintStatus);
        txtNO=convertView.findViewById(R.id.txtNO);
        txtNO.setText("Complaint No :-"+model.ComplaintId+"");
        txtContact.setText(model.ContactNumber+"");
        txtPerson.setText(model.ContactPerson+"");
        txtAssignTo.setText(model.AssignTo+"");
        try
        {
            if(  txtAssignTo.getText().toString().equalsIgnoreCase("null"))
            {
                txtAssignTo.setText("");
            }
        }
        catch (Exception ex){}

        // txtPort.setText(model.PortNo);
        // txtSerial.setText(model.SerialNumber);
        //Linkify.addLinks(txtDesc, Linkify.ALL);

        //  txtCustomer.setVisibility(View.GONE);
        txtCustomer.setText(model.CustomerName+"("+model.GroupName+","+model.Area+")");
        img=findViewById(R.id.imgCamera);
        try
        {
            final String url=model.Attachment1.replace("~",CommonShare.url1);
            if(url.length()>5)
            {
                Picasso.get().load(  url).placeholder(R.drawable.loading).into(img);
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ViewImage.url=url;
                        startActivity(new Intent(ViewComplaintDetails.this, ViewImage.class));
                    }
                });
            }
        }
        catch (Exception ex){}

        btnOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert=new AlertDialog.Builder(ViewComplaintDetails.this);
                alert.setTitle("View Otp");
                alert.setMessage("Otp for this complaint is '"+model.OTP+"' . Share this otp with our engineer if you are are satisfied with our service." );
                alert.setPositiveButton("Ok",null);
                alert.show();

            }
        });

        if(CommonShare.getRole(this).equalsIgnoreCase("Customer"))
        {
            btnOtp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    otpAlert();
                }
            });
        }
        else
        {
            btnOtp.setText("Close Complaint");
            btnOtp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(new ConnectionDetector(ViewComplaintDetails.this).isConnectingToInternet())
                    {
                        startActivity(new Intent(ViewComplaintDetails.this, ClosedComplaintActivity.class));
                    }
                    else
                    {
                        Toast.makeText(convertView, "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
                    }
                    /*
                    View vh=getLayoutInflater().inflate(R.layout.item_otp,null);
                    final EditText edOtp=vh.findViewById(R.id.edOtp);
                    AlertDialog.Builder alert=new AlertDialog.Builder(ViewComplaintDetails.this);
                    alert.setTitle("Close complaint");
                    alert.setView(vh);
                    // alert.setMessage("Enter Otp given by customer to close th complaint" );
                    alert.setPositiveButton("Close Complaint", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(edOtp.getText().toString().trim().equalsIgnoreCase(""))
                            {
                                Toast.makeText(convertView, "Please Enter Otp", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                if(model.OTP.equalsIgnoreCase(edOtp.getText().toString().trim()))
                                {
                                    String url=CommonShare.url+"Service1.svc/CloseComplaint?ComplaintId="+model.ComplaintId+"&UserId="+CommonShare.getUserId(ViewComplaintDetails.this)+"&solution=";
                                    AsyncUtilities utilities=new AsyncUtilities(ViewComplaintDetails.this,false,url,null,1,ViewComplaintDetails.this);
                                    utilities.execute();
                                }
                                else
                                {
                                    Toast.makeText(convertView, "Otp Not Matched", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                    alert.setNegativeButton("Cancel",null);
                    alert.show();
                    */
                }
            });

        }

        linearWork=findViewById(R.id.linearWork);
        if(CommonShare.getCustomerId(this)>0)
        {
            linearWork.setVisibility(View.GONE);
        }

        btnWork=findViewById(R.id.btnWork);
        /*
        if(model.AssignToId!=CommonShare.getEmpId(this)    )
        {
            btnWork.setVisibility(View.GONE);
        }
        if(CommonShare.getEmpId(this)==17 ||CommonShare.getEmpId(this)==19 || CommonShare.getEmpId(this)==52  || CommonShare.getEmpId(this)== 1064)
        {
            btnWork.setVisibility(View.VISIBLE);
        }  */
        btnWork.setVisibility(View.VISIBLE);

        btnWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View vh=getLayoutInflater().inflate(R.layout.item_add_work,null);
                final EditText edOtp=vh.findViewById(R.id.edittext);
                AlertDialog.Builder alert=new AlertDialog.Builder(ViewComplaintDetails.this);
                alert.setTitle("Add your work regarding complaint");
                alert.setView(vh);
                // alert.setMessage("Enter Otp given by customer to close th complaint" );
                alert.setPositiveButton("Add Work", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(edOtp.getText().toString().trim().equalsIgnoreCase(""))
                        {
                            Toast.makeText(convertView, "Add Work", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {

                            GsonComplaintWork w=new GsonComplaintWork();
                            w.ComplaintTokenToken=model.Token;

                            w.EmpId=CommonShare.getEmpId(ViewComplaintDetails.this);
                            w.EmpName=CommonShare.getEmpName(ViewComplaintDetails.this);
                            w.EnterMillisecond=System.currentTimeMillis();
                            w.EnterDate=CommonShare.getDateTime(System.currentTimeMillis());
                            w.Work=edOtp.getText().toString();
                            w.WorkToken=CommonShare.generateToken(ViewComplaintDetails.this);
                            w.IsModified=true;
                            Realm realm=Realm.getDefaultInstance();
                            realm.beginTransaction();
                            realm.copyToRealmOrUpdate(w);
                            realm.commitTransaction();

                            VisitSyncUtility utility=new VisitSyncUtility(ViewComplaintDetails.this);
                            utility.sync();
                            startActivity(new Intent(ViewComplaintDetails.this,ComplaintDashboard.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        }
                    }
                });
                alert.setNegativeButton("Cancel",null);
                alert.show();

            }
        });
        try
        {
          if(  model.IsBookedByClient)
          {
              txtBookBy.setText("By Client");
          }
          else
          {
              txtBookBy.setText("By Engineer");
              txtBookBy.setText("By Engineer:-"+CommonShare.empMap.get( Integer.parseInt( model.EnterBy)).EmpName);
          }

        }
        catch (Exception ex){}

        try
        {
            txtAmcStatus=findViewById(R.id.txtAmcStatus);
            txtAllocatedEngineer = findViewById(R.id.txtAllocatedEngineer);
            if(model.RecId>0)
            {
                Realm realm=Realm.getDefaultInstance();
                GsonCustomerProduct p=   realm.where(GsonCustomerProduct.class).equalTo("RecId",model.RecId).findFirst();
               // if(p.getEngineerName())
                txtAllocatedEngineer.setText(p.getEngineerName());
                txtAmcStatus.setText(p.getAMCStatus() +"-"+ CommonShare.getDateTime( CommonShare.parseDate( p.getAmcEndDate())).split(" ")[0]);

                long eDate=CommonShare.parseDate(p.getAmcEndDate());
                if(eDate==0)
                {
                    txtAmcStatus.setText(p.getAMCStatus());
                }
                if(eDate>0)
                {
                    if(eDate<System.currentTimeMillis())
                    {
                        txtAmcStatus.setTextColor(Color.RED);
                        txtAmcStatus.setText("No AMC/No WARRANTY" +"-"+ CommonShare.getDateTime( CommonShare.parseDate( p.getAmcEndDate())).split(" ")[0]);
                    }
                }

            }
            else
                {
                    Realm realm=Realm.getDefaultInstance();
                    GsonCustomerProduct p=   realm.where(GsonCustomerProduct.class).equalTo("CustomerId",model.CustomerId).findFirst();
                    txtAllocatedEngineer.setText(p.getEngineerName());
                }



        }
        catch (Exception ex)
        {}

        try
        {
            if(model.ComplaintType!=null && model.ComplaintType.length()>4) {
                findViewById(R.id.lType).setVisibility(View.VISIBLE);
                TextView t=findViewById(R.id.txtType);
                t.setText(model.ComplaintType);
            }
        }
        catch (Exception ex){}
    }
    void otpAlert()
    {
        startActivity(new Intent(this,CustomerComplaintRemark.class));
/*
        AlertDialog.Builder alert=new AlertDialog.Builder(ViewComplaintDetails.this);
        alert.setTitle("View Otp");
        alert.setMessage("Otp for this complaint is '"+model.OTP+"' . Share this otp with our engineer if you are are satisfied with our service." );
        alert.setPositiveButton("Ok",null);
        alert.show();*/

    }
    ArrayList<ComplaintTypeModel> typeList;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        if(item.getItemId()==R.id.actionViewAppUser)
        {
            String url=CommonShare.url+"/Service1.svc/ViewAppUser?ComplaintId="+model.ComplaintId;
            AsyncUtilities utilities=new AsyncUtilities(this,false,url,null,701,this);
            utilities.execute();
        }
        else  if(item.getItemId()==R.id.actionChangeType)
        {
            typeList = CommonShare.getClientComplaintType(this);
            ArrayAdapter adapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1,typeList);
            AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
            alertDialog.setTitle("Select Complaint Type");
            alertDialog.setAdapter(adapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    changeType(typeList.get(i).ComplaintTypeName);
                }
            });
            alertDialog.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            alertDialog.show();
        }
        else  if(item.getItemId()==R.id.actionChangeStatus)
        {
            if((CommonShare.getRole(this).equalsIgnoreCase("Assistance Manager")
                    && CommonShare.getMyEmployeeModel(this).DeptId==3  )  ||
                    CommonShare.getRole(this).equalsIgnoreCase("Admin")
            )
            {
                 startActivity(new Intent(this,ChangeComplaintStatusActivity.class));
            }
            else
            {
                Toast.makeText(convertView, "Unauthorised Access", Toast.LENGTH_SHORT).show();
            }

        }
        return super.onOptionsItemSelected(item);
    }
    void changeType(String  type)
    {
        try {
            String url = CommonShare.urlv1 + "/Service1.svc/ChangeTypeOnComplaint?ComaplintId=" + model.ComplaintId + "&UserId=" + CommonShare.getUserId(this) + "&NewTpe=" +URLEncoder.encode( type,"utf-8");
            AsyncUtilities utilities = new AsyncUtilities(this, false, url, null, 702, this);
            utilities.execute();
        }
        catch (Exception ex){}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(CommonShare.getEmpId(this)>0)
        {
            if(canUserChangeStatus)
            {
                getMenuInflater().inflate(R.menu.complaintmenu1,menu);
            }
            else
            {
                getMenuInflater().inflate(R.menu.complaintmenu, menu);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onComplete(final String str, int requestCode, int responseCode) {
        if(requestCode==1&& responseCode==200)
        {
            Toast.makeText(convertView, "Complaint Closed", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,ViewAllComplaint.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
        else if(requestCode==21 && responseCode==200)
        {
            AlertDialog.Builder alert=new AlertDialog.Builder(this);
            alert.setTitle("Schedule Visit");
            alert.setMessage("Complaint Schedule to engineer.Do you want to schedule visit for  this complaint?");
            alert.setPositiveButton("Schedule Visit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                    try {
                        Gson gson = new Gson();
                                ServerComplaintModel m =gson.fromJson(str,ServerComplaintModel.class);
                                startActivity(
                                        new Intent(ViewComplaintDetails.this, ScheduleVisit.class).putExtra("Complaint",str)
                                       );
                    }
                    catch (Exception ew){}
                }
            });
            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                    startActivity(new Intent(ViewComplaintDetails.this,ComplaintDashboard.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
            });
            alert.setCancelable(false);
            alert.show();
        }
        else if(requestCode==22 && responseCode==200)
        {
             initWork(str);
        }
        else  if(requestCode==701 && responseCode==200)
        {
           //CommonShare.alert(this,str);
            showUserAlert(str);
        }
        else  if(requestCode==702 && responseCode==200)
        {
           // CommonShare.alert(this,str);
            //showUserAlert(str);
            startActivity(new Intent(this,ComplaintDashboard.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
    }

    void schedule(EmployeeModel emp)
    {
        try
        {
            String url = CommonShare.url + "Service1.svc/ScheduleComplaint?EmpId=" + emp.EmpId +
                    "&ComplaintId=" + model.ComplaintId + "&UserId=" + CommonShare.getUserId(this)
                    +"&EmpName="+URLEncoder.encode(emp.EmpName,"utf-8");                   ;

            AsyncUtilities utilities=new AsyncUtilities(this,false,url,"",21,this);
            utilities.execute();
        }
        catch (Exception ex)
        {}

    }
    public  static  boolean isFeedbackGiven=false;
    void initWork(String str)
    {
           try
           {
               JSONObject j=new JSONObject(str);
               Gson gson=new Gson();
               JSONArray array=j.getJSONArray("work");
               JSONArray array1=j.getJSONArray("logs");
              // CommonShare.alert(this,array1.toString());
                       //new JSONArray(str);
               for(int i=0;i<array.length();i++)
               {
                   GsonComplaintWork work=    gson.fromJson(array.getJSONObject(i).toString(),GsonComplaintWork.class);
                   TextView t=new TextView(this);

                   if(work.EmpName==null ||work.EmpName.length()<4 )
                   {
                       try {
                           work.EmpName=   CommonShare.empMap.get(work.EmpId).EmpName;
                       }
                       catch (Exception ex){}


                       t.setText(work.Work + "\n\t" + work.EmpName + "  -" + work.EnterDate+"\n");
                   }
                   else {
                       t.setText(work.Work + "\n\t" + work.EmpName + "  -" + work.EnterDate+"\n");
                   }
                   linearWork.addView(t);
               }


               LinearLayout linearLogs=findViewById(R.id.linearLogs);
               for(int i=0;i<array1.length();i++)
               {
                   linearLogs.setVisibility(View.VISIBLE);
                   TextView t=new TextView(this);
                   JSONObject js=array1.getJSONObject(i);
                   t.setText("Status:- "+js.getString("ComplaintStatus"));
                   String description=js.getString("Description");
                   if(description!=null && !description.equalsIgnoreCase("null")&& description.length()>0)
                   {
                       t.setText(t.getText().toString()+"\n\t"+description);
                   }
                   t.setText(t.getText().toString()+"\n\t\t"+js.getString("EnterBy")+" - "+
                           CommonShare.getDateTime(CommonShare.parseDate(js.getString("LogDate")))  +"\n");
                   linearLogs.addView(t);
                   try {
                       if(js.getString("ComplaintStatus").equalsIgnoreCase("Feedback Given"))
                       {
                           isFeedbackGiven=true;
                       }
                   }
                   catch (Exception ex){}
               }
           }
           catch (Exception ex){}
    }



    ArrayList<CustUser> Ulist=new ArrayList<>();
    void showUserAlert(String str)
    {
        try
        {
            Ulist.clear();
            JSONArray array=new JSONArray(str);
            for(int i=0;i<array.length();i++)
            {
                CustUser u=new CustUser();
                JSONObject j=array.getJSONObject(i);
                u.nm= j.getString("ContactName");
                u.mob= j.getString("MobileNo");
                Ulist.add(u);
            }
            if(Ulist.size()==0)
            {
                AlertDialog.Builder alert=new AlertDialog.Builder(this);
                alert.setTitle("App User List");
                alert.setMessage("No user found in system ,Please Create User first");
                alert.show();

                return;
            }

            ArrayAdapter adapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1,Ulist);
            AlertDialog.Builder alert=new AlertDialog.Builder(this);
            alert.setAdapter(adapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                       Intent intent=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+Ulist.get(i).mob));
                       startActivity(intent);
                }
            });
            alert.setTitle("App User List");
          //  alert.setMessage("Tap to call contact person");
            alert.setPositiveButton("Cancel",null);
            alert.show();


        }
        catch (Exception ex)
        {}
    }
}
class  CustUser
{
    public String nm,mob;
    public  String toString()
    {
        return  nm+"("+mob+")";
    }
}
