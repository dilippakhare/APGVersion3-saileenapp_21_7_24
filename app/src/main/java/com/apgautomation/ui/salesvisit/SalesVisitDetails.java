package com.apgautomation.ui.salesvisit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apgautomation.CameraActivity;
import com.apgautomation.HomePage;
import com.apgautomation.MainActivityLauncher;
import com.apgautomation.R;
import com.apgautomation.controller.SaleVisitController;
import com.apgautomation.controller.VisitModuleController;
import com.apgautomation.model.EmployeeModel;
import com.apgautomation.model.GsonQuatationRequestModel;
import com.apgautomation.model.GsonVisitPhoto;
import com.apgautomation.model.ServerModel.GsonSalesVisit;
import com.apgautomation.ui.ViewImage;
import com.apgautomation.utility.BitmapUtilities;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.AsyncUtilities;
import com.apgautomation.utility.serverutility.DownloadUtility;
import com.apgautomation.utility.serverutility.netrequest.ConnectionDetector;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.otaliastudios.cameraview.BitmapCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import io.realm.Realm;

public class SalesVisitDetails extends AppCompatActivity implements DownloadUtility {

    Realm realm;
    SaleVisitController controller;
    VisitModuleController visitModuleController;

    GsonSalesVisit model;
    LinearLayout linearDetails,lQuation,linearClose;
    String VisitToken;
    ArrayList<GsonQuatationRequestModel> requestList=new ArrayList<>();
    HashMap<Integer,ImageView> map=new HashMap<>();
    ArrayList<EmployeeModel> empList;
    private CheckBox chkQuation;
    private FusedLocationProviderClient fusedLocationClient;

    public  EditText edContactNumber,edContactPerson,edEmailId;
    private EditText edComponyAddress;

    ImageView imgVisitPhtoto;
    LinearLayout lvisitPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_visit_details);
        lvisitPhoto=findViewById(R.id.lvisitPhoto);
        imgVisitPhtoto=findViewById(R.id.imgVisitPhtoto);
        controller=new SaleVisitController(this);
        visitModuleController=new VisitModuleController(this);
        empList=  CommonShare.getQEmployeeList(this);
        realm=Realm.getDefaultInstance();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Visit Details");
        CommonShare.hideatInItInputBoard(this);
        CommonShare.hideSoftKeyBord(this);

        VisitToken = getIntent().getExtras().getString("VisitToken");

        model=controller.getVisitObjectByToken(VisitToken);
        linearDetails=findViewById(R.id.linearDetails);
        linearClose=findViewById(R.id.linearClose);
        lQuation=findViewById(R.id.lQuation);
        init();
        initLocation();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(android.R.id.home==item.getItemId())
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(CommonShare.getEmpId(this)==model.getAssignToId()  && !model.getVisitStatus().equalsIgnoreCase("Closed"))
        {
            AlertDialog.Builder alert=new AlertDialog.Builder(this);
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SalesVisitDetails.super.onBackPressed();
                }
            });
            alert.setNegativeButton("No",null);
            alert.setMessage("Do you want to go back ?");
            alert.setTitle("Go back");
            alert.show();
        }
        else
        {
            super.onBackPressed();
        }
        //
    }

    Button btnStart,btnReScheduleDate,btnGenerateQuatation,btnClose;

    EditText edWork,edRemark,edFollowup;
    CheckBox chkDone,chkPending,chkReschedule;
    RadioButton rdHot,rdWarm,rdCold;
    void init()
    {
        edWork=findViewById(R.id.edWork);
        edRemark=findViewById(R.id.edRemark);
        edFollowup=findViewById(R.id.edFollowup);
        rdHot=findViewById(R.id.rdHot);
        rdWarm=findViewById(R.id.rdWarm);
        rdCold=findViewById(R.id.rdCold);

        chkDone=findViewById(R.id.chkDone);
        chkPending=findViewById(R.id.chkPending);
        chkReschedule=findViewById(R.id.chkReschedule);
        chkReschedule.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               if(!model.getVisitStatus().equalsIgnoreCase("Closed")) {
                   DialogFragment newFragment = new DatePickerFragment(btnReScheduleDate);
                   newFragment.show(getSupportFragmentManager(), "datePicker");
               }
            }
        });

        Activity convertView=this;
        TextView txtTitle=convertView.findViewById(R.id.txtTitle);
        TextView txtEngName=convertView.findViewById(R.id.txtEngName);
        TextView txtScheduleDate=convertView.findViewById(R.id.txtScheduleDate);
        TextView txtStatus=convertView.findViewById(R.id.txtStatus);
        TextView txtCustomer=convertView.findViewById(R.id.txtCustomer);
        TextView txtScheduleBy=convertView.findViewById(R.id.txtScheduleBy);
        TextView txtVisitType=convertView.findViewById(R.id.txtVisitType);
        edContactPerson=findViewById(R.id.edContactPerson);
        edContactNumber=findViewById(R.id.edContactNumber);
        edEmailId=findViewById(R.id.edEmailId);
        edComponyAddress=findViewById(R.id.edComponyAddress);
        try
        {
            txtScheduleBy.setText(   model.getEnterByName());
            txtVisitType.setText(model.getVisitType());
        }
        catch (Exception ex){}
        btnReScheduleDate=findViewById(R.id.btnReScheduleDate);
        btnGenerateQuatation=findViewById(R.id.btnGenerateQuatation);
        btnClose=findViewById(R.id.btnClose);
        btnGenerateQuatation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddQuotaion();
            }
        });
        btnReScheduleDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment(btnReScheduleDate);
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        txtTitle.setText(model.getVGroupName());
        txtScheduleDate.setText(CommonShare.convertToDate(model.getScheduleDateMillisecond()));
        txtEngName.setText(model.getAssignToName());
        txtStatus.setText(model.getVisitStatus());
        txtCustomer.setText(model.getVCustomerName());

        if(txtStatus.getText().toString().equalsIgnoreCase("Started"))
        {
            txtStatus.setTextColor(Color.GREEN);
            if(CommonShare.isSalesVisitPhotoEnabled(this))
            {
                lvisitPhoto.setVisibility(View.VISIBLE);
                imgVisitPhtoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(new Intent(SalesVisitDetails.this, CameraActivity.class), 13131);

                    }
                });
            }
        }
        try
        {
            GsonSalesVisit employeeModelObj=  model;
            if(employeeModelObj.getAssignToId()==employeeModelObj.getEnterById() )
            {
                Date date = new Date(   (model.getEnteredDateMillisecond()));
                Date today=new Date( CommonShare.parseDate( model.getScheduleDate()));

                long difference=  (date.getTime()-today.getTime()) / (24 * 60 * 60 * 1000); //date.compareTo(today);
                if(date.getDay()==today.getDay()  &&  date.getMonth()==today.getMonth() && date.getYear()==today.getYear())
                    if(date.getHours()>9)
                    {
                        //  msg=msg+"\nThis visit will be marked as unplanned.Try to schedule visit one day before.";
                        txtStatus.setText(txtStatus.getText().toString()+"(Unplanned Visit)");
                    }


            }

        }
        catch (Exception ex)
        {
            Log.d("com.apgautomation",ex.toString());
        }
        btnStart=findViewById(R.id.btnStart);
        if(model.getAssignToId()==CommonShare.getEmpId(this))
        {
            if (model.getVisitStatus().equalsIgnoreCase("Schedule"))
            {
                findViewById(R.id.linearStart).setVisibility(View.VISIBLE);
                btnStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder alert=new AlertDialog.Builder(SalesVisitDetails.this);
                        alert.setTitle("Start Visit");
                        alert.setMessage("Do you want to start this visit ?");
                        alert.setPositiveButton("Start", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startVisit();
                            }
                        });
                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        alert.show();
                    }
                });
            }
            if (model.getVisitStatus().equalsIgnoreCase("Started")) {
                findViewById(R.id.linearStart).setVisibility(View.GONE);
                linearClose.setVisibility(View.VISIBLE);
                btnClose.setVisibility(View.VISIBLE);
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder alert=new AlertDialog.Builder(SalesVisitDetails.this);
                        alert.setTitle("Close Visit");
                        alert.setMessage("Do you want to close this visit ?");
                        alert.setPositiveButton("Close Visit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                closeVisit();
                            }
                        });
                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        alert.show();
                    }
                });
            }
        }

        //------------  if Started  -----//
        if(!model.getVisitStatus().equalsIgnoreCase("Schedule"))
        {
            linearDetails.setVisibility(View.VISIBLE);
        }
        if(!model.getVisitStatus().equalsIgnoreCase("Started")) {
            if (getString(R.string.app_name).toUpperCase().contains("SAILEEN")) {

                String url = CommonShare.url + "Service1.svc/GetLastVisitAddress?CustomerId=" + model.getCustomerId();
                AsyncUtilities utilities = new AsyncUtilities(this, false, url, "", 2, this);
                //utilities.hideProgressDialoge();
                utilities.execute();

            }
        }

        chkQuation=findViewById(R.id.chkQuation);
        chkQuation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    lQuation.setVisibility(View.VISIBLE);
                    btnGenerateQuatation.setVisibility(View.VISIBLE);
                }
                else {
                    lQuation.setVisibility(View.GONE);
                    btnGenerateQuatation.setVisibility(View.INVISIBLE);
                }
            }
        });


        if(model.getVisitStatus().equalsIgnoreCase("Closed"))
        {
            linearDetails.setVisibility(View.VISIBLE);

            edRemark.setEnabled(false);
            edWork.setEnabled(false);
            edFollowup.setEnabled(false);
            chkReschedule.setEnabled(false);
            chkPending.setEnabled(false);
            chkDone.setEnabled(false);
            chkQuation.setEnabled(false);

            edFollowup.setText(model.getFollowupPendingPoint());
            edWork.setText(model.getWorkDetails());
            edRemark.setText(model.getRemark());
            btnGenerateQuatation.setEnabled(false);
            btnClose.setVisibility(View.GONE);
            btnStart.setVisibility(View.GONE);

            chkDone.setChecked(model.isCallDone());
            chkPending.setChecked(model.isCallPending());
            chkReschedule.setChecked(model.isCallReSchedule());
            if(model.getRescheduleDateMilliSecond()>0)
            {
              btnReScheduleDate.setText  (CommonShare.convertToDate(model.getScheduleDateMillisecond()));
            }
            try {
                if (model.getCallPriority().equalsIgnoreCase("Hot"))
                    rdHot.setChecked(true);
                if (model.getCallPriority().equalsIgnoreCase("Cold"))
                    rdCold.setChecked(true);
                if (model.getCallPriority().equalsIgnoreCase("Warm"))
                    rdWarm.setChecked(true);
            }
            catch (Exception ex)
            {}
            try
            {
                if(model.getContactName1()!=null)
                {
                    edContactPerson.setText(model.getContactName1());
                    edContactPerson.setEnabled(false);
                }
                if(model.getContactNo1().length()>5)
                {
                    edContactNumber.setText(model.getContactNo1());
                    edContactNumber.setEnabled(false);
                }
                if(model.getEmailId1().length()>5)
                {
                    edEmailId.setText(model.getEmailId1());
                    edEmailId.setEnabled(false);
                }
                if(model.getComponyAddress().length()>0)
                {
                    edComponyAddress.setText(model.getComponyAddress());
                    edComponyAddress.setEnabled(false);
                }
            }
            catch (Exception ex){}
            try
            {
                try {
                    LayoutInflater inflater = getLayoutInflater();
                    ArrayList<GsonQuatationRequestModel> qList=  visitModuleController.getGsonQuoationByVisitToken(model.getVisitTokenId());
                    for (int i = 0; i < qList.size(); i++) {
                        final View v= inflater .inflate(R.layout.item_quationrequest1,null);
                        lQuation.addView(v);
                        TextView spAssignTo=v.findViewById(R.id.spAssignTo);
                        EditText edRemark=v.findViewById(R.id.edRemark);
                        ImageView imgCamera=v.findViewById(R.id.imgCamera);
                        spAssignTo.setText(   CommonShare.empMap.get(qList.get(i).getAssignToEmpId()).EmpName );
                        edRemark.setText(qList.get(i).getRemark());
                        edRemark.setEnabled(false);
                        try
                        {
                            final String url=qList.get(i).getAttachment().replace("~",CommonShare.url1);
                            Picasso.get().load(  url).placeholder(R.drawable.loading).into(imgCamera);
                            imgCamera.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ViewImage.url=url;
                                    startActivity(new Intent(SalesVisitDetails.this, ViewImage.class));
                                }
                            });
                        }
                        catch (Exception ex){}
                    }
                }
                catch (Exception ex )
                {}

            }
            catch (Exception ex)
            {}

            try {
               if(CommonShare.isSalesVisitPhotoEnabled(this))
               {
                   try {
                       VisitModuleController controller = new VisitModuleController(this);
                       ArrayList<GsonVisitPhoto> listtt = controller.getVisitPhoto(model.getVisitTokenId());
                       String url1 = listtt.get(0).getPhoto().replace("~", CommonShare.url1);
                       Picasso.get().load(url1).placeholder(R.drawable.loading).into(imgVisitPhtoto);
                       if (url1.length() > 10) {
                           lvisitPhoto.setVisibility(View.VISIBLE);
                           // lvisitPhoto.setVisibility(View.VISIBLE);
                           imgVisitPhtoto.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View v) {
                                   ViewImage.url = url1;
                                   startActivity(new Intent(SalesVisitDetails.this, ViewImage.class));
                               }
                           });
                       }
                   }
                   catch (Exception ex) {
                       if (model.VisitStatus.equalsIgnoreCase("Closed")) {
                           String url = CommonShare.url + "Service1.svc/getVisitPhoto?token=" + model.getVisitTokenId();
                           AsyncUtilities utilities = new AsyncUtilities(this, false, url, "", 1, SalesVisitDetails.this);
                           utilities.hideProgressDialoge();
                           utilities.execute();

                       }
                   }
               }
            }
            catch (Exception ex){}
        }

        try {
            if (CommonShare.getEmpId(this) == 1 || CommonShare.getEmpId(this) == 3 || CommonShare.getEmpId(this) == 2) {
                findViewById(R.id.linearLocation).setVisibility(View.VISIBLE);
                findViewById(R.id.txtViewLocation).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CommonShare.openMapWithGeo(SalesVisitDetails.this, "Start Location", model.getStartLocation());

                    }
                });

            }
        }
        catch (Exception ex){}
    }

    private void closeVisit()
    {
        if(!new ConnectionDetector(this).isConnectingToInternet())
        {
            Toast.makeText(this,"Check Internet Connection",Toast.LENGTH_LONG).show();
            return;
        }
        if(locationStr==null || locationStr.length()<3)
        {
            Toast.makeText(this,"Location Not Found",Toast.LENGTH_LONG).show();
            callLocationRequest();
            return;
        }

        try
        {
            if(CommonShare.isServiceVisitPhotoEnabled(this))
            {
                if(  imgVisitPhtoto.getTag()==null)
                {
                    Toast.makeText(this, "Visit Photo Required", Toast.LENGTH_SHORT).show();
                    return ;
                }
                if(imgVisitPhtoto.getTag().toString().length()<6)
                {
                    Toast.makeText(this, "Visit Photo Required", Toast.LENGTH_SHORT).show();
                    return ;
                }
            }
        }
        catch (Exception ex){}

       if(edWork.getText().toString().trim().equalsIgnoreCase(""))
       {
           Toast.makeText(this, "Enter your work", Toast.LENGTH_SHORT).show();
           return;
       }
       if(edRemark.getText().toString().trim().equalsIgnoreCase(""))
       {
            Toast.makeText(this, "Enter your remark", Toast.LENGTH_SHORT).show();
            return;
       }
       if(chkDone.isChecked() || chkReschedule.isChecked() || chkPending.isChecked())
       {
           if(chkReschedule.isChecked())
           {
               if(btnReScheduleDate.getText().toString().equalsIgnoreCase("Select Date"))
               {
                   Toast.makeText(this, "Re-Schedule Date Required", Toast.LENGTH_SHORT).show();
                   return;
               }
           }
       }
       else
       {
           Toast.makeText(this, "Call Status Required", Toast.LENGTH_SHORT).show();
           return;
       }

       //---------------------------------------------------------------------------------------------//
        if(requestList.size()>0)
        {
            for (GsonQuatationRequestModel qModel:requestList)
            {
                if(qModel.getLocalpath()==null || qModel.getLocalpath().length()<5)
                {
                    Toast.makeText(this, "Quotation Photo Required", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
        if(getString(R.string.app_name).toUpperCase().contains("SAILEEN"))
        {
            if(edContactPerson.getText().toString().trim().length()==0)
            {
                Toast.makeText(this, "Contact Person Name is required", Toast.LENGTH_SHORT).show();
                return;
            }
            if(edContactNumber.getText().toString().trim().length()==0)
            {
                Toast.makeText(this, "Contact Number is required", Toast.LENGTH_SHORT).show();
                return;
            }
            if(edComponyAddress.getText().toString().trim().length()==0)
            {
                Toast.makeText(this, "Compony Address is required", Toast.LENGTH_SHORT).show();
                return;
            }

            if(edEmailId.getText().toString().trim().length()>0) {
                if( !new CommonShare().checkEmail(edEmailId.getText().toString().trim())) {
                    Toast.makeText(this, "Invalid Email address", Toast.LENGTH_SHORT).show();
                    return ;
                }
            }
        }
        realm.beginTransaction();
        if(rdHot.isChecked())
            model.setCallPriority("Hot");
        if(rdCold.isChecked())
            model.setCallPriority("Cold");
        if(rdWarm.isChecked())
            model.setCallPriority("Warm");

        model.setWorkDetails(edWork.getText().toString().trim());
        model.setRemark(edRemark.getText().toString().trim());
        model.setFollowupPendingPoint(edFollowup.getText().toString().trim());
        model.setCallDone(chkDone.isChecked());
        model.setCallPending(chkPending.isChecked());
        model.setCallReSchedule(chkReschedule.isChecked());
        //----Date to Millisecond----
        model.setRescheduleDateMilliSecond(   CommonShare.ddMMYYDateToLong(btnReScheduleDate.getText().toString().replace("/","-")));

        model.setModified(true);
        model.setEndTimeMillisecond(System.currentTimeMillis());

        model.setVisitStatus("Closed");
        model.setEndLocation(locationStr);

        model.setContactName1(edContactPerson.getText().toString().trim());
        model.setContactNo1(edContactNumber.getText().toString().trim());
        model.setEmailId1(edEmailId.getText().toString().trim());
        model.setEndLocation(locationStr);
        model.setComponyAddress(edComponyAddress.getText().toString().trim());

        realm.commitTransaction();

        for (int i = 2; i < lQuation.getChildCount(); i++) {

            View v = lQuation.getChildAt(i);
            EditText edRemark = v.findViewById(R.id.edRemark);
            Spinner spAssignTo=v.findViewById(R.id.spAssignTo);
            GsonQuatationRequestModel m = (GsonQuatationRequestModel) v.getTag();
            m.setRemark(edRemark.getText().toString());
            m.setEnterByEMpId(CommonShare.getEmpId(this));
            m.setEnterDateMillisecond(System.currentTimeMillis());
            m.AssignToEmpId=empList.get(spAssignTo.getSelectedItemPosition()).EmpId;

        }

        visitModuleController.saveQuatationRequest(requestList);



        try
        {
            VisitModuleController controllerV=new VisitModuleController(this);
            GsonVisitPhoto photoModel=new GsonVisitPhoto();
            photoModel.VisitToken=model.getVisitTokenId();
            photoModel.PhotoToken=CommonShare.generateToken(this);
            photoModel.RecId=0;
            photoModel.LocalPath=imgVisitPhtoto.getTag().toString();
            controllerV.savePhoto(photoModel);
        }
        catch (Exception ee){}


        Toast.makeText(this, "Visit Close Successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, HomePage.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

    }

    void startVisit()
    {
        SaleVisitController controller1 = new SaleVisitController(this);
        GsonSalesVisit vObj = controller1.getStarteVisit();
        if (vObj != null)
        {
            androidx.appcompat.app.AlertDialog.Builder alert=new androidx.appcompat.app.AlertDialog.Builder(SalesVisitDetails.this);
            alert.setTitle("Close started visit");
            alert.setMessage("First you have to close started visit");
            alert.setPositiveButton("Close started visit first", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            alert.setNegativeButton("Cancel",null);
            alert.show();
            return;
        }

        if(locationStr==null || locationStr.length()<3)
        {
            Toast.makeText(this,"Location Not Found",Toast.LENGTH_LONG).show();
            callLocationRequest();
            return;
        }
        if(!new ConnectionDetector(this).isConnectingToInternet())
        {
            Toast.makeText(this,"Check Internet Connection",Toast.LENGTH_LONG).show();
            return;
        }
        realm.beginTransaction();
        model.setVisitStatus("Started");
        model.setStartLocation(locationStr);
        model.setStartTimeMillisecond(System.currentTimeMillis());
        model.setModified(true);
        realm.copyToRealmOrUpdate(model);
        realm.commitTransaction();
        model=controller.getVisitObjectByToken(VisitToken);
        init();

    }
    int startpont=500;
    void AddQuotaion()
    {
        startpont++;
        int gid= model.getGroupId();
        GsonQuatationRequestModel quotationModel=new GsonQuatationRequestModel();
        quotationModel.VisitToken=model.getVisitTokenId();
        quotationModel.QuatationToken=CommonShare.generateToken(this);
        quotationModel.GroupId=gid;
        quotationModel.EnterDateMillisecond=System.currentTimeMillis();
        quotationModel.setRequestTag(startpont);
        quotationModel.setVisitType("Sales");


        LayoutInflater inflater=getLayoutInflater();
        final View v= inflater .inflate(R.layout.item_quationrequest,null);
        lQuation.addView(v);
        Spinner spAssignTo=v.findViewById(R.id.spAssignTo);
        EditText edRemark=v.findViewById(R.id.edRemark);
        ImageView imgCamera=v.findViewById(R.id.imgCamera);
        map.put(startpont,imgCamera);
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,empList);
        spAssignTo.setAdapter(adapter);

         requestList.add(quotationModel);
        // imgCamera.setTag();
        v.setTag(quotationModel);
        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                androidx.appcompat.app.AlertDialog.Builder alert=new androidx.appcompat.app.AlertDialog.Builder(SalesVisitDetails.this);
                alert.setTitle("Delete Item");
                alert.setMessage("Do you want to delete this item ?");
                alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        lQuation.removeView(v);
                        //selectedProductForFSR.remove(v.getTag());
                        requestList.remove(v.getTag());
                    }
                });
                alert.setNegativeButton("Cancel",null);
                alert.show();
                return false;
            }
        });

        imgCamera.setTag(quotationModel);
        imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    GsonQuatationRequestModel m= (GsonQuatationRequestModel) view.getTag();
                    /*
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    File file = new File(getAccessPath() + File.separator + "img.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                    startActivityForResult(intent, m.getRequestTag()); */

                    startActivityForResult(new Intent(SalesVisitDetails.this, CameraActivity.class), m.getRequestTag());
                }
                catch (Exception ex)
                {
                    CommonShare.alert(SalesVisitDetails.this,ex.toString());
                }
            }
        });

    }

    String getAccessPath()
    {
        String root = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString();
        Log.d("ROOT", root);
        File myDir = new File(root +"/"+ getString(R.string.app_name).replace(" ",""));
        myDir.mkdirs();

        return    myDir.getAbsoluteFile().toString();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==13131  )
        {
            if(MainActivityLauncher.pictureResult!=null) {
                MainActivityLauncher.pictureResult.toBitmap(new BitmapCallback() {

                    @Override
                    public void onBitmapReady(@Nullable Bitmap bitmap) {
                        int width = bitmap.getWidth();
                        int height = bitmap.getHeight();

                        Log.e("Width height", width + "   " + height);

                        float constant = ((float) (500 / (float) width));
                        width = 500;
                        Log.e("Contatnt", constant + "");
                        float m = ((float) height) * constant;
                        height = (int) m;
                        Log.e("new width heith ", width + "   " + height);

                        Bitmap thePic = ThumbnailUtils.extractThumbnail(bitmap, width, height);
                        final File bitmapFile = BitmapUtilities.saveToExtenal(thePic, SalesVisitDetails.this);
                        String localFilePath = bitmapFile.getAbsolutePath();

                        imgVisitPhtoto.setImageBitmap(thePic);
                        imgVisitPhtoto.setTag(localFilePath);


                    }
                });
            }

            return;
        }

         if(requestCode>500)
        {
            /*
            try
            {
                File file = new File(getAccessPath() + File.separator + "img.jpg");
                //  localFilePath = file.getAbsolutePath();

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);

                int width = bitmap.getWidth();
                int height = bitmap.getHeight();

                Log.e("Width height", width + "   " + height);

                float constant = ((float) (700 / (float) width));
                width = 700;
                Log.e("Contatnt", constant + "");
                float m = ((float) height) * constant;
                height = (int) m;
                Log.e("new width heith ", width + "   " + height);

                Bitmap thePic = ThumbnailUtils.extractThumbnail(bitmap, width, height);
                final File bitmapFile = BitmapUtilities.saveToExtenal(thePic, this);
                String  localFilePath = bitmapFile.getAbsolutePath();
                //ImageView img= findViewById(  photoClickImagetag);
                //        img.setImageBitmap(thePic);


                for(int i=0;i<requestList.size();i++)
                {
                    if(requestList.get(i).equals(    (map.get(requestCode)).getTag()))
                    {
                        requestList.get(i).setLocalpath(localFilePath);
                    }
                }


                //ImageView img=  findViewById(map.get(requestCode));
                map.get(requestCode).setImageBitmap(thePic);


            }
            catch (Exception ex)
            {}*/
            if(MainActivityLauncher.pictureResult!=null)
            {
                setQuotationNewCamera(requestCode);
                return;
            }

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



    //-------------------------  Location-------------------------------

    String locationStr;
    void initLocation()
    {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean enabled1 = service
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        //  Settings.ACTION_LOCATION_SOURCE_SETTINGS
        if (enabled  || enabled1)
        {

        }
        else
        {
            androidx.appcompat.app.AlertDialog.Builder alert=new androidx.appcompat.app.AlertDialog.Builder(this);
            alert.setTitle("Location Off");
            alert.setMessage("Dou you want to on location");
            alert.setPositiveButton("Open Location Setting", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);

                }
            });
            alert.show();
        }
        callLocationRequest();
    }
    int locationCnt;
    void callLocationRequest()
    {
        locationCnt++;
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.

                        if(location==null)
                        {
                            //
                            try {
                                Intent i = new Intent();
                                i.setAction("ConnectGoogle");
                                ((Activity) SalesVisitDetails.this).sendBroadcast(i);
                            }
                            catch (Exception ex)
                            {
                            }
                            if(locationStr==null ||locationStr.length()==0)
                            {
                                locationCnt++;
                                if(locationCnt>=3)
                                {
                                    androidx.appcompat.app.AlertDialog.Builder alert=new androidx.appcompat.app.AlertDialog.Builder(SalesVisitDetails.this);
                                    alert.setTitle("Location Not Found");
                                    alert.setMessage("Unable to find location, Please Open Map Application to get location.");
                                    alert.setPositiveButton("Open map", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            CommonShare.openMap(SalesVisitDetails.this);
                                        }
                                    });
                                    alert.setNegativeButton("Cancel",null);
                                    alert.show();
                                }
                                Toast.makeText(SalesVisitDetails.this,"Location Not Found",Toast.LENGTH_LONG).show();
                                return;
                            }
                        }

                        if (location != null) {
                            locationStr=location.getLatitude()+","+location.getLongitude();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                //Toast.makeText(MarkAttendance.this,e.toString(),Toast.LENGTH_LONG).show();

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        callLocationRequest();
    }

    void setQuotationNewCamera(int requestCode)
    {
        if(MainActivityLauncher.pictureResult!=null) {
            MainActivityLauncher.pictureResult.toBitmap(1000, 1000, new BitmapCallback() {

                @Override
                public void onBitmapReady(@Nullable Bitmap bitmap) {
                    try {
                        if (bitmap == null)
                            return;
                        int width = bitmap.getWidth();
                        int height = bitmap.getHeight();

                        Log.e("Width height", width + "   " + height);

                        float constant = ((float) (700 / (float) width));
                        width = 700;
                        Log.e("Contatnt", constant + "");
                        float m = ((float) height) * constant;
                        height = (int) m;
                        Log.e("new width heith ", width + "   " + height);

                        Bitmap thePic = ThumbnailUtils.extractThumbnail(bitmap, width, height);
                        final File bitmapFile = BitmapUtilities.saveToExtenal(thePic, SalesVisitDetails.this);
                        String localFilePath = bitmapFile.getAbsolutePath();
                        //ImageView img= findViewById(  photoClickImagetag);
                        //        img.setImageBitmap(thePic);


                        for (int i = 0; i < requestList.size(); i++) {
                            if (requestList.get(i).equals((map.get(requestCode)).getTag())) {
                                requestList.get(i).setLocalpath(localFilePath);
                            }
                        }


                        //ImageView img=  findViewById(map.get(requestCode));
                        map.get(requestCode).setImageBitmap(thePic);


                    } catch (Exception ex) {
                        CommonShare.alert(SalesVisitDetails.this, ex.toString());
                    }
                }
            });
        }
    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode) {


        if(requestCode==1)
        try
        {
            JSONObject j=new JSONObject(str);
            String url=  j.getString("Photo");
            String url1 = url.replace("~", CommonShare.url1);
            Picasso.get().load(url1).placeholder(R.drawable.loading).into(imgVisitPhtoto);
            lvisitPhoto.setVisibility(View.VISIBLE);
            imgVisitPhtoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ViewImage.url=url1;
                    startActivity(new Intent(SalesVisitDetails.this, ViewImage.class));
                }
            });
        }
        catch (Exception ex)
        {}

        try
        {
            if(requestCode==2  && responseCode==200)
                if(!str.toLowerCase().contains("error")){
                    {
                        str=str.substring(1,str.length()-2);
                        edComponyAddress.setText(str);
                    }
                }
        }
        catch (Exception ex){}
    }
}


