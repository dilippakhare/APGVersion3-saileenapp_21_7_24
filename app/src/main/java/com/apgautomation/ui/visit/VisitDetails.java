package com.apgautomation.ui.visit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apgautomation.HomePage;
import com.apgautomation.R;
import com.apgautomation.controller.VisitModuleController;
import com.apgautomation.model.GsonVisitMaster;
import com.apgautomation.model.GsonVisitMasterNnRealm;
import com.apgautomation.model.GsonVisitPhoto;
import com.apgautomation.model.GsonVisitType;
import com.apgautomation.ui.SignatureActivity;
import com.apgautomation.ui.ViewImage;
import com.apgautomation.ui.WebViewactivity;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.AsyncUtilities;
import com.apgautomation.utility.serverutility.DownloadUtility;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.Realm;

public class VisitDetails extends AppCompatActivity implements DownloadUtility {

    public static GsonVisitMasterNnRealm model;
    public static String str;
    LinearLayout lFSR,lQuation;
    Activity convertView;
    Button btnComplete;
    ImageView imgVisitPhtoto;
    LinearLayout linearVisit;
    LinearLayout linearContact;
    public  EditText edContactNumber,edContactPerson,edEmailId;
    private EditText edComponyAddress;
    ArrayList<String> fsrList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_details);
        btnComplete=findViewById(R.id.btnComplete);
        linearVisit=findViewById(R.id.linearVisit);
        linearVisit.setVisibility(View.GONE);
        linearContact=findViewById(R.id.linearContact);
        imgVisitPhtoto=findViewById(R.id.imgVisitPhtoto);
        edComponyAddress=findViewById(R.id.edComponyAddress);
        convertView=this;
        try
        {
                init();
        }
        catch (Exception ex)
        {

        }
       // CommonShare.alert(this,str);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Visit Details");
        CommonShare.hideatInItInputBoard(this);
        CommonShare.hideSoftKeyBord(this);
        if(model.VisitStatus .equalsIgnoreCase("Inprogress-Fsr-Pending")  && model.AssigntoEmpId ==CommonShare.getEmpId(this))
        {
            btnComplete.setVisibility(View.VISIBLE);
            btnComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url=CommonShare.url+"Service1.svc/CompleteTheVisit?token="+model.getVisitToken();
                    AsyncUtilities utilities=new AsyncUtilities(VisitDetails.this,false,url,"",2,VisitDetails.this);
                   // utilities.hideProgressDialoge();
                    utilities.execute();

                }
            });
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
    ArrayList<ImageView> imagList=new ArrayList<>();
    void init()
    {
        TextView txtTitle=convertView.findViewById(R.id.txtTitle);
        TextView txtEngName=convertView.findViewById(R.id.txtEngName);
        TextView txtScheduleDate=convertView.findViewById(R.id.txtScheduleDate);
        TextView txtStatus=convertView.findViewById(R.id.txtStatus);
        TextView txtRemark=convertView.findViewById(R.id.txtRemark);
        TextView txtStartTime=convertView.findViewById(R.id.txtStartTime);
        TextView txtEndTime=convertView.findViewById(R.id.txtEndTime);

        edContactPerson=findViewById(R.id.edContactPerson);
        edContactNumber=findViewById(R.id.edContactNumber);
        edEmailId=findViewById(R.id.edEmailId);


        txtTitle.setText(model.getGroupName());
        txtScheduleDate.setText(CommonShare.convertToDate( CommonShare.parseDate( model.getScheduleDate())));
        txtEngName.setText(model.getEmpName());
        txtStatus.setText(model.getVisitStatus());
        txtRemark.setText(model.getVisitRemark());

        try {
            txtStartTime.setText(CommonShare.getDateTime(  CommonShare.parseDate( model.getStartTime())));
            if(txtStartTime.getText().toString().contains("1970"))
            {
                txtStartTime.setText("");
            }
        }
        catch (Exception ex){}
        try {
            txtEndTime.setText(CommonShare.getDateTime(  CommonShare.parseDate(  model.getEndTime())));
            if(txtEndTime.getText().toString().contains("1970"))
            {
                txtEndTime.setText("");
            }
        }
        catch (Exception ex){}


        lFSR=findViewById(R.id.lFSR);
        lQuation=findViewById(R.id.lQuation);
        try
        {
            LayoutInflater inflater=getLayoutInflater();
            JSONObject jsonObject=new JSONObject(str);
            for(int i=0;i<jsonObject.getJSONArray("VisitFsr").length();i++)
            {
                JSONObject j=jsonObject.getJSONArray("VisitFsr").getJSONObject(i);
                View v= inflater .inflate(R.layout.item_fsr,null);
                lFSR.addView(v);
                try {
                    if(CommonShare.isFsrEnabled(this)) {
                        Button btnFsrDetails = v.findViewById(R.id.btnFsrDetails);
                        ImageView download= v.findViewById(R.id.download);
                        btnFsrDetails.setVisibility(View.VISIBLE);
                        download.setVisibility(View.VISIBLE);
                        fsrList.add(j.getString("FSRToken"));
                        if (j.getString("FSRToken") != null && j.getString("FSRToken").length() > 5) {
                            btnFsrDetails.setTag(j.getString("FSRToken"));
                            btnFsrDetails.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String t = (String) v.getTag();
                                    WebViewactivity.token = t;
                                    startActivity(new Intent(VisitDetails.this, WebViewactivity.class));
                                }
                            });
                            download.setTag(j.getString("FSRToken"));
                            download.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String t = (String) v.getTag();
                                    //WebViewactivity.token = t;
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://174.141.238.216/FSRAPG/#/print?id="+t)));
                                }
                            });
                        }
                    }
                }
                catch (Exception e){

                }

                EditText edFSRNo=v.findViewById(R.id.edFSRNo);
                edFSRNo.setEnabled(false);
                edFSRNo.setText(j.getString("FSRNO"));
                TextView txtProductName=v.findViewById(R.id.  txtProductName);
                TextView txtSerialNumber=v.findViewById(R.id.  txtSerialNumber);
                txtProductName.setText(j.getString("EquipmentName"));
                txtSerialNumber.setText(j.getString("SerialNumber"));

                //------------------------------------------//
                try {
                    LinearLayout fsrBottom = v.findViewById(R.id.fsrBottom);
                    fsrBottom.setVisibility(View.GONE);
                    if (j.getString("RunningHourd").length()>0  && !j.getString("RunningHourd").equalsIgnoreCase("null") ){
                        fsrBottom.setVisibility(View.VISIBLE);
                        EditText edRunningHourd = v.findViewById(R.id.edRunningHourd);
                        EditText edLoadHourd = v.findViewById(R.id.edLoadHourd);
                        EditText edMototStart = v.findViewById(R.id.edMototStart);
                        EditText edLoadVolve = v.findViewById(R.id.edLoadVolve);
                        EditText edDutyCycle = v.findViewById(R.id.edDutyCycle);
                        edRunningHourd.setText(j.getString("RunningHourd"));edRunningHourd.setEnabled(false);
                        edLoadHourd.setText(j.getString("LoadHourd"));edLoadHourd.setEnabled(false);
                        edMototStart.setText(j.getString("MototStart"));edMototStart.setEnabled(false);
                        edLoadVolve.setText(j.getString("LoadVolve"));edLoadVolve.setEnabled(false);
                        edDutyCycle.setText(j.getString("DutyCycle"));edDutyCycle.setEnabled(false);
                    }
                }
                catch (Exception e){}
                //-----------------------------------------//

                Spinner fsrType=v.findViewById(R.id.fsrType);
                ArrayList<GsonVisitType>  visitTypes= CommonShare.getVisitTypes(this);
                ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,visitTypes);
                fsrType.setAdapter(adapter);

                try
                {
                    for(int fi=0;fi<visitTypes.size();fi++)
                    {
                         if(visitTypes.get(fi).toString().equalsIgnoreCase(j.getString("FSRType")))
                         {
                             fsrType.setSelection(fi);
                             fsrType.setEnabled(false);
                             break;
                         }
                    }
                }
                catch (Exception ex){}

                ImageView img=v.findViewById(R.id.imgCamera);
                img.setTag(j.getInt("RecId"));
                imagList.add(img);
                if(model.getVisitStatus().equalsIgnoreCase("Inprogress-Fsr-Pending "))
                {
                       img.setBackgroundResource(R.drawable.custsign);
                }
                try
                {
                    final String url=j.getString("FSRAttachment").replace("~",CommonShare.url1);
                   // CommonShare.alert(this,url);
                    int res=R.drawable.loading;

                        if(model.getVisitStatus().equalsIgnoreCase("Inprogress-Fsr-Pending"))
                        {
                           // img.setBackgroundResource(R.drawable.custsign);
                            res=R.drawable.custsign;
                        }
                    Picasso.get().load(  url).placeholder(res).into(img);
                    img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                          /*  if(model.getVisitStatus().equalsIgnoreCase("Inprogress-Fsr-Pending") || url.length()<5) {
                                try {
                                    SignatureActivity.FsrId= j.getInt("RecId");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                startActivity(new Intent(VisitDetails.this, SignatureActivity.class));
                                return;
                            } */
                            ViewImage.url=url;
                            startActivity(new Intent(VisitDetails.this, ViewImage.class));
                        }
                    });
                }
                catch (Exception ex){}
            }
        }
        catch (Exception ex)
        {}

        try {
            LayoutInflater inflater = getLayoutInflater();
            JSONObject jsonObject = new JSONObject(str);
            for (int i = 0; i < jsonObject.getJSONArray("VisitQuotation").length(); i++) {
                JSONObject j = jsonObject.getJSONArray("VisitQuotation").getJSONObject(i);
                final View v= inflater .inflate(R.layout.item_quationrequest1,null);
                lQuation.addView(v);
                TextView spAssignTo=v.findViewById(R.id.spAssignTo);
                EditText edRemark=v.findViewById(R.id.edRemark);
                ImageView imgCamera=v.findViewById(R.id.imgCamera);
                spAssignTo.setText(j.getString("AssignedToName"));
                edRemark.setText(j.getString("Remark"));
                    edRemark.setEnabled(false);
                try
                {
                    final String url=j.getString("Attachment").replace("~", CommonShare.url1 /*CommonShare.url*/);

                    Picasso.get().load(  url).placeholder(R.drawable.loading).into(imgCamera);
                    imgCamera.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ViewImage.url=url;
                            startActivity(new Intent(VisitDetails.this, ViewImage.class));
                        }
                    });
                }
                catch (Exception ex){}
            }
        }
        catch (Exception ex )
        {}

        try
        {
          if(model.getStartLocation().length()>4)
          {
              if(CommonShare.getEmpId(this)==1 ||CommonShare.getEmpId(this)==3 ||CommonShare.getEmpId(this)==2)
              {
                  findViewById(R.id.linearLocation).setVisibility(View.VISIBLE);
                  findViewById(R.id.txtStartLocation).setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          CommonShare.openMapWithGeo(VisitDetails.this,"Start Location",model.getStartLocation());

                      }
                  });
                  findViewById(R.id.txtEndLocation).setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          CommonShare.openMapWithGeo(VisitDetails.this,"End Location",model.getEndLocation());
                      }
                  });
              }
          }
        }
        catch (Exception ex)
        {}

        try
        {
            if(CommonShare.isServiceVisitPhotoEnabled(this)) {
                linearVisit.setVisibility(View.VISIBLE);

                VisitModuleController controller = new VisitModuleController(this);
                ArrayList<GsonVisitPhoto> listtt = controller.getVisitPhoto(model.getVisitToken());
                if(listtt.size()>0) {
                    String url1 = listtt.get(0).getPhoto().replace("~", CommonShare.url);
                    Picasso.get().load(url1).placeholder(R.drawable.loading).into(imgVisitPhtoto);
                    if (url1.length() > 10) {
                        linearVisit.setVisibility(View.VISIBLE);
                        imgVisitPhtoto.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ViewImage.url = url1;
                                startActivity(new Intent(VisitDetails.this, ViewImage.class));
                            }
                        });
                    }
                }
                else
                {
                    if(model.VisitStatus.equalsIgnoreCase("Complete")|| model.VisitStatus.equalsIgnoreCase("Closed"))
                    {
                        String url=CommonShare.url+"Service1.svc/getVisitPhoto?token="+model.getVisitToken();
                        AsyncUtilities utilities=new AsyncUtilities(this,false,url,"",1,this);
                        utilities.hideProgressDialoge();
                        utilities.execute();



                    }
                }
            }

        }
        catch (Exception ex)
        {}

        if( true )//getString(R.string.app_name).toUpperCase().contains("SAILEEN")) {
        {
            linearContact.setVisibility(View.VISIBLE);
            try {
                if (model.getContactName1() != null) {
                    edContactPerson.setText(model.getContactName1());
                    edContactPerson.setEnabled(false);
                }
                if (model.getContactNo1().length() > 5) {
                    edContactNumber.setText(model.getContactNo1());
                    edContactNumber.setEnabled(false);
                }
                if (model.getEmailId1().length() > 5) {
                    edEmailId.setText(model.getEmailId1());
                    edEmailId.setEnabled(false);
                }
                edComponyAddress.setText(model.getComponyAddress());
            }
            catch (Exception ex){}
        }



    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode) {
       if(requestCode == 1) {
           try {
               JSONObject j = new JSONObject(str);
               String url = j.getString("Photo");
               String url1 = url.replace("~", CommonShare.url);
               Picasso.get().load(url1).placeholder(R.drawable.loading).into(imgVisitPhtoto);

               imgVisitPhtoto.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       ViewImage.url = url1;
                       startActivity(new Intent(VisitDetails.this, ViewImage.class));
                   }
               });
           } catch (Exception ex) {
           }
       }
       else if(requestCode == 2 )
       {
           if(responseCode != 200)
           {
               Toast.makeText(this, "Error while submitting", Toast.LENGTH_SHORT).show();
               return;
           }
           if(str.contains("Success")) {
               CommonShare.alert(this, str);
               Realm realm=Realm.getDefaultInstance();
               GsonVisitMaster cp= realm.where(GsonVisitMaster.class).equalTo("VisitId",model.VisitId).findFirst();


               realm.beginTransaction();
               cp.setVisitStatus("Complete");
               realm.copyToRealmOrUpdate(cp);
               realm.commitTransaction();

               AlertDialog.Builder alert =new AlertDialog.Builder(this);
               alert.setTitle("Visit Complete");
               alert.setMessage("Do u want to send FSR Details to customer");
               alert.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                        String extraMessage="Open below link to view FSR";
                       try {
                           int i=1;
                           for (String t:fsrList
                                ) {
                               extraMessage=extraMessage+"\n\n"+ " FSR-"+i+" :- http://174.141.238.216/FSRAPG/#/print?id="+t;

                               //  extraMessage=extraMessage+"\n\n"+ Html.fromHtml("<a href='http://174.141.238.216/FSRAPG/#/print?id="+t+"'> FSR-"+i+"</a>");
                              i++;
                           }

                       }
                       catch (Exception ex){}
                       Intent sendIntent = new Intent();
                       sendIntent.setAction(Intent.ACTION_SEND);
                       sendIntent.putExtra(Intent.EXTRA_TEXT, extraMessage);
                       sendIntent.setType("text/plain");

                       Intent shareIntent = Intent.createChooser(sendIntent, null);
                       startActivity(shareIntent);
                       finish();
                       //startActivity(new Intent(VisitDetails.this, HomePage.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                   }
               });
               alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {


                       startActivity(new Intent(VisitDetails.this, HomePage.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                   }
               });
               alert.setCancelable(false);
               alert.show();

           }
           else
           {
              CommonShare.alert(VisitDetails.this,str);
           }

       }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            for (ImageView i : imagList
            ) {
                if (i.getTag().equals(SignatureActivity.FsrId)) {
                    String url = SignatureActivity.serverpath.replace("~", CommonShare.url);
                    Picasso.get().load(url).placeholder(R.drawable.loading).into(i);
                }
            }
        }
        catch (Exception ex)
        {}
    }
}
