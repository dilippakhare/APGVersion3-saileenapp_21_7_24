package com.apgautomation.ui.visit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apgautomation.CameraActivity;
import com.apgautomation.HomePage;
import com.apgautomation.MainActivityLauncher;
import com.apgautomation.R;
import com.apgautomation.controller.VisitModuleController;
import com.apgautomation.model.EmployeeModel;
import com.apgautomation.model.GsonCustomerProduct;
import com.apgautomation.model.GsonQuatationRequestModel;
import com.apgautomation.model.GsonVisitFsr;
import com.apgautomation.model.GsonVisitMaster;
import com.apgautomation.model.GsonVisitMasterNnRealm;
import com.apgautomation.model.GsonVisitPhoto;
import com.apgautomation.model.GsonVisitType;
import com.apgautomation.ui.complaint.SelectProducts;
import com.apgautomation.utility.BitmapUtilities;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.ConnectServer;
import com.apgautomation.utility.syncutilities.VisitSyncUtility;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.otaliastudios.cameraview.BitmapCallback;
import com.apgautomation.utility.serverutility.AsyncUtilities;
import com.apgautomation.utility.serverutility.DownloadUtility;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


public class ProcessVisit extends AppCompatActivity implements DownloadUtility {


    HashMap<Integer,ImageView> map=new HashMap<>();
    private FusedLocationProviderClient fusedLocationClient;


    ArrayList<GsonCustomerProduct>  selectedProductForFSR=new ArrayList<>();
    ArrayList<GsonVisitFsr>  fsrList=new ArrayList<>();
    Button btnSelectProduct;
    CheckBox chkFSR;
    CheckBox chkQuation;
    LinearLayout lFSR ,linearProcess,lQuation;
    Button btnStartVisit,btnGenerateQuatation;
    VisitModuleController controller;
    GsonVisitMaster model;
    ArrayList<EmployeeModel> empList;
    ArrayList<GsonQuatationRequestModel> requestList=new ArrayList<>();

    EditText edRemark;
    Button btnComplete;

    int startpont=500;


    ImageView imgVisitPhtoto;
    LinearLayout lvisitPhoto;

    public  EditText edContactNumber,edContactPerson,edEmailId;
    public LinearLayout linearContact;
    private EditText edComponyAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_visit);
        lvisitPhoto=findViewById(R.id.lvisitPhoto);
        empList=  CommonShare.getQEmployeeList(this);
        controller=new VisitModuleController(this);
        String visitToken=getIntent().getExtras().getString("VisitToken");
        model=controller.getVisitmasterByToken(visitToken);
        btnStartVisit=findViewById(R.id.btnStartVisit);
        imgVisitPhtoto=findViewById(R.id.imgVisitPhtoto);
        chkFSR=findViewById(R.id.chkFSR);
        lFSR=findViewById(R.id.lFSR);
        linearProcess=findViewById(R.id.linearProcess);
        lQuation=findViewById(R.id.lQuation);
        btnGenerateQuatation=findViewById(R.id.btnGenerateQuatation);
        edRemark=findViewById(R.id.edRemark);
        btnComplete=findViewById(R.id.btnComplete);
        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmComplete();
            }
        });
        linearContact=findViewById(R.id.linearContact);
        chkFSR.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         @Override
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
         {
           if(isChecked)
           {
               lFSR.setVisibility(View.VISIBLE);
               btnSelectProduct.setVisibility(View.VISIBLE);
               btnComplete.setText("Inprogress");
           }
           else
           {
               lFSR.setVisibility(View.GONE);
               btnSelectProduct.setVisibility(View.GONE);
               btnComplete.setText("Complete");
           }
         }
         });
        btnSelectProduct=findViewById(R.id.btnSelectProduct);
        btnSelectProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               startActivityForResult(new Intent(ProcessVisit.this, SelectProducts.class).putExtra("GroupId",model.getGroupId()),1);
            }
        });

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
        btnGenerateQuatation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addQuataion();
            }
        });

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Process Visit");


        if(model.getVisitStatus().equalsIgnoreCase("Started"))
        {
            linearProcess.setVisibility(View.VISIBLE);
            btnStartVisit.setVisibility(View.GONE);
            edRemark.setVisibility(View.VISIBLE);
            btnComplete.setVisibility(View.VISIBLE);
            if(true )//getString(R.string.app_name).toUpperCase().contains("SAILEEN")) {
            {
                linearContact.setVisibility(View.VISIBLE);
                String url=CommonShare.url+"Service1.svc/GetLastVisitAddressGroup?GroupId="+model.getGroupId();
                AsyncUtilities utilities=new AsyncUtilities(this,false,url,"",1,this);
                //utilities.hideProgressDialoge();
                utilities.execute();

            }
        }

        btnStartVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (controller.isAnyVisitStarted())
                {
                    AlertDialog.Builder alert=new AlertDialog.Builder(ProcessVisit.this);
                    alert.setTitle("Close started visit");
                    alert.setMessage("First you have to close 'started visit' or 'Inprogress visit'");
                    alert.setPositiveButton("Close started visit first", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    alert.setNegativeButton("Cancel",null);
                    alert.show();
                }
                else
                {

                    AlertDialog.Builder alert=new AlertDialog.Builder(ProcessVisit.this);
                    alert.setTitle("Start Visit");
                    alert.setMessage("Do you want to start this visit ?");
                    alert.setPositiveButton("Start", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            if(locationStr==null)
                            {
                                if(locationCnt>=3)
                                {
                                    AlertDialog.Builder alert=new AlertDialog.Builder(ProcessVisit.this);
                                    alert.setTitle("Location Not Found");
                                    alert.setMessage("Unable to find location, Please Open Map Application to get location.");
                                    alert.setPositiveButton("Open map", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            CommonShare.openMap(ProcessVisit.this);
                                        }
                                    });
                                    alert.setNegativeButton("Cancel",null);
                                    alert.show();
                                }
                                Toast.makeText(ProcessVisit.this, "Unable to find Location", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            model.setStartLocation(locationStr);
                            model.setStartTimeMillisecond(System.currentTimeMillis());
                            model.setIsModified(true);
                            model.setVisitStatus("Started");
                            if(CommonShare.isServiceVisitPhotoEnabled(ProcessVisit.this))
                                lvisitPhoto.setVisibility(View.VISIBLE);
                            controller.saveVisitSchedule(model);
                            linearProcess.setVisibility(View.VISIBLE);
                            btnStartVisit.setVisibility(View.GONE);
                            edRemark.setVisibility(View.VISIBLE);
                            btnComplete.setVisibility(View.VISIBLE);
                            if( true)//getString(R.string.app_name).toUpperCase().contains("SAILEEN")
                             {
                                linearContact.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    alert.setNegativeButton("Cancel",null);
                    alert.show();


                }
            }
        });
        initLocation();
        inItView();

        imgVisitPhtoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ProcessVisit.this, CameraActivity.class), 13131);
            }
        });
    }
    void addQuataion()
    {
        startpont++;

        GsonQuatationRequestModel quotationModel=new GsonQuatationRequestModel();
        quotationModel.VisitToken=model.VisitToken;
        quotationModel.QuatationToken=CommonShare.generateToken(this);
        quotationModel.GroupId=model.GroupId;
        quotationModel.EnterDateMillisecond=System.currentTimeMillis();
        quotationModel.setRequestTag(startpont);

        requestList.add(quotationModel);
        LayoutInflater inflater=getLayoutInflater();
        final View v= inflater .inflate(R.layout.item_quationrequest,null);
        lQuation.addView(v);
        Spinner spAssignTo=v.findViewById(R.id.spAssignTo);
        EditText edRemark=v.findViewById(R.id.edRemark);
        ImageView imgCamera=v.findViewById(R.id.imgCamera);

        map.put(startpont,imgCamera);
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,empList);
        spAssignTo.setAdapter(adapter);


       // imgCamera.setTag();
        v.setTag(quotationModel);
        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder alert=new AlertDialog.Builder(ProcessVisit.this);
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
              //  getImageQuotation(view,0);
                AlertDialog.Builder alert=new AlertDialog.Builder(ProcessVisit.this);
                String[] arr=new String[]{"Capture From Camera","Pick From Gallery"};
                alert.setSingleChoiceItems(
                        new ArrayAdapter(ProcessVisit.this, android.R.layout.simple_list_item_1, arr)
                        , -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(i==0)
                                {
                                    getImageQuotation(v,0);
                                }
                                else  if(i==1) {
                                    getImageQuotation(v,1);
                                }
                                dialogInterface.cancel();
                            }
                        }
                );
                alert.show();
            }
        });

    }
    boolean isQuotationCam=true;
    void getImageQuotation(View view,int id)
    {
        GsonQuatationRequestModel m= (GsonQuatationRequestModel) view.getTag();

        if(id==0) {
            isQuotationCam=true;
            try {

              //  Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            //    File file = new File(getAccessPath() + File.separator + "img.jpg");
             //   intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
               // startActivityForResult(intent, m.getRequestTag());
                //Virat
                startActivityForResult(new Intent(ProcessVisit.this, CameraActivity.class), m.getRequestTag());
            } catch (Exception ex) {
                CommonShare.alert(ProcessVisit.this, ex.toString());
            }
        }
        else {
            isQuotationCam=false;
            Intent in = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(in, m.getRequestTag());
        }
    }
    void inItView()
    {
        Activity convertView=this;
        TextView txtTitle=convertView.findViewById(R.id.txtTitle);
        TextView txtEngName=convertView.findViewById(R.id.txtEngName);
        TextView txtScheduleDate=convertView.findViewById(R.id.txtScheduleDate);
        TextView txtStatus=convertView.findViewById(R.id.txtStatus);
        TextView txtReason=findViewById(R.id.txtReason);
        TextView txtVisitType=findViewById(R.id.txtVisitType);
        txtReason.setText(model.getReasonForVisit());
        txtVisitType.setText(model.getVisitType());
        txtTitle.setText(model.getGroupName());
        txtScheduleDate.setText(CommonShare.convertToDate( CommonShare.parseDate( model.getScheduleDate())));
        txtEngName.setText(model.getEmpName());
        txtStatus.setText(model.getVisitStatus());

        if(model.getVisitStatus().equalsIgnoreCase("Started") || model.getVisitStatus().equalsIgnoreCase("Inprogress-Fsr-Pending") )
        {
            if(CommonShare.isServiceVisitPhotoEnabled(this))
               lvisitPhoto.setVisibility(View.VISIBLE);
            edRemark.setVisibility(View.VISIBLE);
            btnComplete.setVisibility(View.VISIBLE);
            if( true)//getString(R.string.app_name).toUpperCase().contains("SAILEEN")) {
            {
                linearContact.setVisibility(View.VISIBLE);
            }
        }
        try
        {
            edContactPerson=findViewById(R.id.edContactPerson);
            edContactNumber=findViewById(R.id.edContactNumber);
            edEmailId=findViewById(R.id.edEmailId);
            edComponyAddress=findViewById(R.id.edComponyAddress);
        }
        catch (Exception ex){}
    }
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
            AlertDialog.Builder alert=new AlertDialog.Builder(this);
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


    @Override
    protected void onResume() {
        super.onResume();
        callLocationRequest();
    }
    int locationCnt;
    @SuppressLint("MissingPermission")
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
                                ((Activity) ProcessVisit.this).sendBroadcast(i);
                            }
                            catch (Exception ex)
                            {
                            }
                            if(locationStr==null ||locationStr.length()==0)
                            {

                                if(locationCnt>=3)
                                {
                                    androidx.appcompat.app.AlertDialog.Builder alert=new androidx.appcompat.app.AlertDialog.Builder(ProcessVisit.this);
                                    alert.setTitle("Location Not Found");
                                    alert.setMessage("Unable to find location, Please Open Map Application to get location.");
                                    alert.setPositiveButton("Open map", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            CommonShare.openMap(ProcessVisit.this);
                                        }
                                    });
                                    alert.setNegativeButton("Cancel",null);
                                    alert.show();
                                }
                                Toast.makeText(ProcessVisit.this,"Location Not Found",Toast.LENGTH_LONG).show();
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
                   try {
                       if (model.VisitStatus == "Scheduled"  && model.AssigntoEmpId==CommonShare.getEmpId(ProcessVisit.this)) {
                             CommonShare.alert(ProcessVisit.this,"Unable to find Location");
                       }
                   }
                   catch (Exception exception)
                   {}
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
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
    int photoClickImagetag=0;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                        final File bitmapFile = BitmapUtilities.saveToExtenal(thePic, ProcessVisit.this);
                        String localFilePath = bitmapFile.getAbsolutePath();

                        imgVisitPhtoto.setImageBitmap(thePic);
                        imgVisitPhtoto.setTag(localFilePath);


                    }
                });
            }

            return;
        }
        if(requestCode==1 && resultCode==RESULT_OK)
        {
            for(int i=0;i<selectedProductForFSR.size();i++)
            {
                if(selectedProductForFSR.get(i).getRecId()==SelectProducts.visitSelectionProduct.getRecId())
                {
                    Toast.makeText(this, "Allready Selected", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            selectedProductForFSR.add(SelectProducts.visitSelectionProduct);
            final GsonVisitFsr fsrItem=new GsonVisitFsr();
            fsrItem.FSRToken= CommonShare.generateToken(this);
            fsrItem.CustomerProductId=SelectProducts.visitSelectionProduct.getRecId();
            fsrItem.VisitTokenId=model.VisitToken;
            LayoutInflater inflater=getLayoutInflater();
            final View v= inflater .inflate(R.layout.item_fsr,null);
            lFSR.addView(v);
            fsrList.add(fsrItem);

            LinearLayout fsrBottom=v.findViewById(R.id.fsrBottom);
            if(!SelectProducts.visitSelectionProduct.getEquipmentType().toUpperCase().contains("COMPRESSOR"))
            {
                fsrBottom.setVisibility(View.GONE);
            }
            TextView txtProductName=v.findViewById(R.id.  txtProductName);
            TextView txtSerialNumber=v.findViewById(R.id.  txtSerialNumber);
            txtProductName.setText(SelectProducts.visitSelectionProduct.getEquipmentName());
            txtSerialNumber.setText(SelectProducts.visitSelectionProduct.getSerialNumber());
            Spinner fsrType=v.findViewById(R.id.fsrType);
            ArrayList<GsonVisitType>  visitTypes= CommonShare.getVisitTypes(this);
            ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,visitTypes);
            fsrType.setAdapter(adapter);


            ImageView img=v.findViewById(R.id.imgCamera);
            img.setTag( SelectProducts.visitSelectionProduct.getRecId());
            //photoClickImagetag=SelectProducts.visitSelectionProduct.getRecId();

            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    photoClickImagetag=(int)view.getTag();
                            //SelectProducts.visitSelectionProduct.getRecId();

                    if(CommonShare.getMyEmployeeModel(ProcessVisit.this).IsOldCamera) {
                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        File file = new File(getAccessPath() + File.separator + "img.jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                        startActivityForResult(intent, 302);
                    }
                    else {
                        startActivityForResult(new Intent(ProcessVisit.this, CameraActivity.class), 302);
                    }
                }
            });

            v.setTag(SelectProducts.visitSelectionProduct);
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder alert=new AlertDialog.Builder(ProcessVisit.this);
                    alert.setTitle("Delete Item");
                    alert.setMessage("Do you want to delete this item ?");
                    alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            lFSR.removeView(v);
                           // for()
                            selectedProductForFSR.remove(v.getTag());
                            fsrList.remove(fsrItem);
                        }
                    });
                    alert.setNegativeButton("Cancel",null);
                    alert.show();
                    return false;
                }
            });
        }
        else if(requestCode==302)
        {
            try
            {
                if (requestCode == 302 ) {


                    if(CommonShare.getMyEmployeeModel(ProcessVisit.this).IsOldCamera) {

                        File file = new File(getAccessPath() + File.separator + "img.jpg");
                        //  localFilePath = file.getAbsolutePath();

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                        if (bitmap == null)
                            return;
                        int width = bitmap.getWidth();
                        int height = bitmap.getHeight();

                        Log.e("Width height", width + "   " + height);

                        float constant = ((float) (1500 / (float) width));
                        width = 1500;
                        Log.e("Contatnt", constant + "");
                        float m = ((float) height) * constant;
                        height = (int) m;
                        Log.e("new width heith ", width + "   " + height);

                        Bitmap thePic = ThumbnailUtils.extractThumbnail(bitmap, width, height);
                        final File bitmapFile = BitmapUtilities.saveToExtenal(thePic, this);
                        String localFilePath = bitmapFile.getAbsolutePath();
                        //ImageView img= findViewById(  photoClickImagetag);
                        //        img.setImageBitmap(thePic);


                        for (int i = 0; i < fsrList.size(); i++) {
                            if (fsrList.get(i).getCustomerProductId() == photoClickImagetag) {
                                fsrList.get(i).setLocalPath(localFilePath);
                            }
                        }

                        for (int i = 0; i < lFSR.getChildCount(); i++) {
                            try {
                                ImageView img = lFSR.getChildAt(i).findViewWithTag(photoClickImagetag);
                                img.setImageBitmap(thePic);
                            } catch (Exception ex) {
                            }
                        }
                    }
                    else {

                        if (MainActivityLauncher.pictureResult != null) {
                            MainActivityLauncher.pictureResult.toBitmap(new BitmapCallback() {

                                @Override
                                public void onBitmapReady(@Nullable Bitmap bitmap) {
                                    int width = bitmap.getWidth();
                                    int height = bitmap.getHeight();

                                    Log.e("Width height", width + "   " + height);

                                    float constant = ((float) (1400 / (float) width));
                                    width = 1400;
                                    Log.e("Contatnt", constant + "");
                                    float m = ((float) height) * constant;
                                    height = (int) m;
                                    Log.e("new width heith ", width + "   " + height);

                                    Bitmap thePic = ThumbnailUtils.extractThumbnail(bitmap, width, height);
                                    final File bitmapFile = BitmapUtilities.saveToExtenal(thePic, ProcessVisit.this);
                                    String localFilePath = bitmapFile.getAbsolutePath();
                                    //ImageView img= findViewById(  photoClickImagetag);
                                    //        img.setImageBitmap(thePic);


                                    for (int i = 0; i < fsrList.size(); i++) {
                                        if (fsrList.get(i).getCustomerProductId() == photoClickImagetag) {
                                            fsrList.get(i).setLocalPath(localFilePath);
                                        }
                                    }

                                    for (int i = 0; i < lFSR.getChildCount(); i++) {
                                        try {
                                            ImageView img = lFSR.getChildAt(i).findViewWithTag(photoClickImagetag);
                                            img.setImageBitmap(thePic);
                                        } catch (Exception ex) {
                                        }
                                    }


                                }
                            });
                        }
                    }
                }

            }
            catch (Exception ex)
            {}
        }
        else if( requestCode>500)
        {
            try
            {
                File file = null;

                if(isQuotationCam)
                try
                {
                  //file=      new File(getAccessPath() + File.separator + "img.jpg");
                  setQuotationNewCamera(requestCode);
                  return;
                }
                catch (Exception ex){}
                  //  localFilePath = file.getAbsolutePath();
                 if (resultCode != RESULT_OK)
                 {
                     return;
                 }
                if(!isQuotationCam)
                {
                    Uri uri = data.getData();
                    String path = "";
                    try {
                        path = CommonShare.getPath(this, uri);
                    } catch (Exception e) {
                    }

                     file = new File(path);
                }

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                if(bitmap==null)
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
            {
                CommonShare.alert(ProcessVisit.this,ex.toString());
            }
        }
    }

    void  confirmComplete()
    {
        if(!CommonShare.isFsrEnabled(this)) {
            AlertDialog.Builder alert = new AlertDialog.Builder(ProcessVisit.this);
            alert.setTitle("Complete the visit");
            alert.setMessage("Do you want to complete the visit ?");
            alert.setPositiveButton("Complete the visit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //CommonShare.openMap(ProcessVisit.this);
                    complete();
                }
            });
            alert.setNegativeButton("Cancel", null);
            alert.show();
        }
        else {
            complete();
        }
    }
    void complete()
    {

        if(checkValidation())
        {
            if (locationStr == null) {
                if (locationCnt >= 3) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(ProcessVisit.this);
                    alert.setTitle("Location Not Found");
                    alert.setMessage("Unable to find location, Please Open Map Application to get location.");
                    alert.setPositiveButton("Open map", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            CommonShare.openMap(ProcessVisit.this);
                        }
                    });
                    alert.setNegativeButton("Cancel", null);
                    alert.show();
                }
                Toast.makeText(ProcessVisit.this, "Unable to find Location", Toast.LENGTH_SHORT).show();
                return;
            }
            controller.saveFSR(fsrList);
            controller.saveQuatationRequest(requestList);
            model.setEndLocation(locationStr);
            model.setEndTimeMillisecond(System.currentTimeMillis());
            model.setIsModified(true);
            model.setVisitStatus("Complete");
            if(CommonShare.isFsrEnabled(this)  && fsrList.size()>0)
            {
               model.setVisitStatus("Inprogress-Fsr-Pending");
            }
            model.setVisitRemark(edRemark.getText().toString());

            model.setContactName1(edContactPerson.getText().toString().trim());
            model.setContactNo1(edContactNumber.getText().toString().trim());
            model.setEmailId1(edEmailId.getText().toString().trim());
            model.setComponyAddress(edComponyAddress.getText().toString().trim());
            controller.saveVisitSchedule(model);

            try
            {
                GsonVisitPhoto photoModel=new GsonVisitPhoto();
                photoModel.VisitToken=model.getVisitToken();
                photoModel.PhotoToken=CommonShare.generateToken(this);
                photoModel.RecId=0;
                photoModel.LocalPath=imgVisitPhtoto.getTag().toString();
                controller.savePhoto(photoModel);
            }
            catch (Exception ee){}

            if(CommonShare.isFsrEnabled(this)  && fsrList.size()>0)
            {
                callSavingVisit();
                return;
            }

            finish();
            startActivity(new Intent(this, HomePage.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
    }

    boolean checkValidation()
    {//Screw Compressor
        try
        {
            if(CommonShare.isServiceVisitPhotoEnabled(this))
            {
               if(  imgVisitPhtoto.getTag()==null)
               {
                   Toast.makeText(this, "Visit Photo Required", Toast.LENGTH_SHORT).show();
                   return false;
               }
               if(imgVisitPhtoto.getTag().toString().length()<6)
               {
                   Toast.makeText(this, "Visit Photo Required", Toast.LENGTH_SHORT).show();
                   return false;
               }
            }
        }
        catch (Exception ex){}


        try {
            for (int i = 2; i < lFSR.getChildCount(); i++) {
                View v = lFSR.getChildAt(i);
                ImageView im = v.findViewById(R.id.imgCamera);
                EditText editText = v.findViewById(R.id.edFSRNo);
                Spinner fsrType=v.findViewById(R.id.fsrType);
                EditText edRunningHourd = v.findViewById(R.id.edRunningHourd);
                EditText edLoadHourd = v.findViewById(R.id.edLoadHourd);
                EditText edMototStart = v.findViewById(R.id.edMototStart);
                EditText edLoadVolve = v.findViewById(R.id.edLoadVolve);
                EditText edDutyCycle = v.findViewById(R.id.edDutyCycle);

                if(  fsrType.getSelectedItemPosition()==0)
                {
                    Toast.makeText(this, "Please Select FSR TYPE", Toast.LENGTH_SHORT).show();
                    return false;
                }
                int tag = (int) im.getTag();
                for (int j = 0; j < fsrList.size(); j++) {
                    if (fsrList.get(j).getCustomerProductId() == tag) {
                        fsrList.get(j).setFSRNO(editText.getText().toString().trim());
                        fsrList.get(j).setFSRType(fsrType.getSelectedItem().toString());
                        //-------------------------------------------------------------//
                        fsrList.get(j).setLoadHourd(edLoadHourd.getText().toString().trim());
                        fsrList.get(j).setRunningHourd(edRunningHourd.getText().toString().trim());
                        fsrList.get(j).setMototStart(edMototStart.getText().toString().trim());
                        fsrList.get(j).setLoadVolve(edLoadVolve.getText().toString().trim());
                        fsrList.get(j).setDutyCycle(edDutyCycle.getText().toString().trim());
                        //-------------------------------------------------------------//
                        LinearLayout fsrBottom=v.findViewById(R.id.fsrBottom);
                        if (fsrBottom.getVisibility() == View.VISIBLE)
                        {
                            if (edLoadHourd.getText().toString().trim().equalsIgnoreCase("")) {
                                Toast.makeText(this, "Load Hours Required", Toast.LENGTH_SHORT).show();
                                return  false;
                            }
                            if (edRunningHourd.getText().toString().trim().equalsIgnoreCase("")) {
                                Toast.makeText(this, "Running Hours Required", Toast.LENGTH_SHORT).show();
                                return  false;
                            }
                            if (edMototStart.getText().toString().trim().equalsIgnoreCase("")) {
                                Toast.makeText(this, "Mototr Start Required", Toast.LENGTH_SHORT).show();
                                return  false;
                            }
                            if (edLoadVolve.getText().toString().trim().equalsIgnoreCase("")) {
                                Toast.makeText(this, "Load Volve Required", Toast.LENGTH_SHORT).show();
                                return  false;
                            }
                            if (edDutyCycle.getText().toString().trim().equalsIgnoreCase("")) {
                                Toast.makeText(this, "Duty Cycle Required", Toast.LENGTH_SHORT).show();
                                return  false;
                            }
                        }
                        break;
                    }
                }
            }

            for (int i = 2; i < lQuation.getChildCount(); i++) {

                View v = lQuation.getChildAt(i);
                EditText edRemark = v.findViewById(R.id.edRemark);
                Spinner spAssignTo=v.findViewById(R.id.spAssignTo);
                GsonQuatationRequestModel m = (GsonQuatationRequestModel) v.getTag();
                m.setRemark(edRemark.getText().toString());
                m.setEnterByEMpId(CommonShare.getEmpId(this));
                m.setEnterDateMillisecond(System.currentTimeMillis());
                m.AssignToEmpId=empList.get(spAssignTo.getSelectedItemPosition()).EmpId;
                m.setVisitType("Service");

            }

            for (int i = 0; i < fsrList.size(); i++) {
                if(!CommonShare.isFsrEnabled(this) ){
                    if (fsrList.get(i).getLocalPath() == null || fsrList.get(i).getLocalPath().equalsIgnoreCase("")) {
                        Toast.makeText(this, "Select Photo Of FSR", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
                if (fsrList.get(i).getFSRNO() == null || fsrList.get(i).getFSRNO().trim().equalsIgnoreCase("")) {
                    Toast.makeText(this, "Enter FSR No.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            for (int i = 0; i < requestList.size(); i++) {
                if (requestList.get(i).getLocalpath() == null || requestList.get(i).getLocalpath().equalsIgnoreCase("")) {
                    Toast.makeText(this, "Select Photo Of Quotation", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }

            for (int i = 0; i < fsrList.size(); i++) {
                fsrList.get(i).VisitTokenId = model.VisitToken;
                // fsrList.get(i).FSRNO=
            }
    try {
        if (  true)//getString(R.string.app_name).toUpperCase().contains("SAILEEN"))
        {
            if (edContactPerson.getText().toString().trim().length() == 0) {
                Toast.makeText(this, "Contact Person Name is required", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (edContactNumber.getText().toString().trim().length() == 0) {
                Toast.makeText(this, "Contact Number is required", Toast.LENGTH_SHORT).show();
                return false;
            }
            if(edEmailId.getText().toString().trim().length()>0) {
               if( !new CommonShare().checkEmail(edEmailId.getText().toString().trim())) {
                   Toast.makeText(this, "Invalid Email address", Toast.LENGTH_SHORT).show();
                   return false;
               }
            }
            if(edComponyAddress.getText().toString().trim().length()==0) {
                Toast.makeText(this, "Compony Address is required", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }catch (Exception exception){}

            return true;
        }
        catch (Exception ex)
        {
            CommonShare.alert(this,ex.toString());
            return false;
        }

    }


    void setQuotationNewCamera(int requestCode)
    {
        if(MainActivityLauncher.pictureResult!=null) {
            MainActivityLauncher.pictureResult.toBitmap(2000, 2000, new BitmapCallback() {

                @Override
                public void onBitmapReady(@Nullable Bitmap bitmap) {
                    try {
                        if (bitmap == null)
                            return;
                        int width = bitmap.getWidth();
                        int height = bitmap.getHeight();

                        Log.e("Width height", width + "   " + height);

                        float constant = ((float) (800 / (float) width));
                        width = 800;
                        Log.e("Contatnt", constant + "");
                        float m = ((float) height) * constant;
                        height = (int) m;
                        Log.e("new width heith ", width + "   " + height);

                        Bitmap thePic = ThumbnailUtils.extractThumbnail(bitmap, width, height);
                        final File bitmapFile = BitmapUtilities.saveToExtenal(thePic, ProcessVisit.this);
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
                        CommonShare.alert(ProcessVisit.this, ex.toString());
                    }
                }
            });
        }
    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode) {

        try
        {
            if(requestCode==1  && responseCode==200)
            if(!str.toLowerCase().contains("error")){
                {
                    str=str.substring(1,str.length()-2);
                    edComponyAddress.setText(str);
                }
            }
        }
        catch (Exception ex){}
    }

    public void callSavingVisit()
    {
        SaveOnlineVisit v=new SaveOnlineVisit(this);
        v.execute();
    }
    public  class SaveOnlineVisit extends AsyncTask<Void,Void,String>
    {
        VisitSyncUtility utility;
        Context context;
        ProgressDialog pd;
        public SaveOnlineVisit(Context context)
        {
            pd=new ProgressDialog(context);
            pd.setCancelable(false);
            this.context=context;
        }
        String str="";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            utility=new VisitSyncUtility(context);
            utility.saveVisit();
            str ="";
            String url= CommonShare.url+"Service1.svc/GetCurrentVIsitFilter?EmpId="+CommonShare.getEmpId(context)+
                    "&FromDate="+(System.currentTimeMillis()-86400000)+"&ToDate="+(System.currentTimeMillis()+86400000)+"&GroupId="+model.GroupId
                    +"&visitId="+model.VisitId;
            ConnectServer c=new ConnectServer();
            try {
             String   str1=  c.sendGet(url);
                JSONArray arr=new JSONArray(str1);
                str1=arr.getJSONObject(0).toString();
                Gson g=new Gson();
                GsonVisitMasterNnRealm bb=  g.fromJson( str1,GsonVisitMasterNnRealm.class);
                VisitDetails.model=bb;
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            String url2= CommonShare.url+"Service1.svc/GetVisitDetails?VisitId="+model.VisitId;
            try {
                str = c.sendGet(url2);
            }
            catch (Exception ex){}
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            if( utility. isAllVisitSaved()  && str!= null && str.length()>5)
            {
                VisitDetails.str=str;
                ProcessVisit.this.finish();


                startActivity(new Intent(context,VisitDetails.class));
            }
            else
            {
                ProcessVisit.this.callSavingVisit() ;
            }
        }
    }


}
