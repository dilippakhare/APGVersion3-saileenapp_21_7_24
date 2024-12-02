package com.apgautomation.ui.attendance;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apgautomation.R;
import com.apgautomation.model.AttendanceStatusBean;
import com.apgautomation.model.ServerModel.EmployeeAttendanceListModel;
import com.apgautomation.ui.ViewImage;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.AsyncUtilities;
import com.apgautomation.utility.serverutility.DownloadUtility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewAttenadnceDetails extends AppCompatActivity implements DownloadUtility {

    LinearLayout linearStart,linearEnd;

    TextView txtName,txtStatus,txtAttendanceDate,txtIsTa ,
             txtStartKm ,txtStartLocation ,txtStartTime,
            txtEndKm,txtEndLocation,txtEndTime  ,txtCurrentStatus;
    ImageView imgStartPhoto,imgEndPhoto;
    void initUi()
    {
        linearStart=findViewById(R.id.linearStart);
        linearEnd=findViewById(R.id.linearEnd);
        txtName=findViewById(R.id.txtName);

        txtStatus=findViewById(R.id.txtStatus);
        txtAttendanceDate=findViewById(R.id.txtAttendanceDate);
        txtIsTa=findViewById(R.id.txtIsTa);

        txtStartKm=findViewById(R.id.txtStartKm);
        txtStartLocation=findViewById(R.id.txtStartLocation);
        txtStartTime=findViewById(R.id.txtStartTime);

        txtEndKm=findViewById(R.id.txtEndKm);
        txtEndLocation=findViewById(R.id.txtEndLocation);
        txtEndTime=findViewById(R.id.txtEndTime);
        txtCurrentStatus=findViewById(R.id.txtCurrentStatus);

        imgStartPhoto=findViewById(R.id.imgStartPhoto);
        imgEndPhoto=findViewById(R.id.imgEndPhoto);
    }

    void intItModel()
    {
        txtName.setText(model.EmpName);
        txtStatus.setText(model.StatusName);
        txtAttendanceDate.setText( ""+CommonShare.getDateTime( CommonShare.parseDate(model.AttendanceDate) ));
        linearStart.setVisibility(View.INVISIBLE);
        if(model.IsTaApplicable)
        {
            linearStart.setVisibility(View.VISIBLE);
            txtStartKm.setText( model.StartKm+"" );
            txtStartLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CommonShare.openMapWithGeo(ViewAttenadnceDetails.this,"Start Location",model.StartLocation);
                }
            });
            try
            {
                String[] arr=  CommonShare.getDateTime( CommonShare.parseDate(model.StartTime) ).split(" ");
                txtStartTime.setText( arr[1] +" "+arr[2]);

                final String url= model.StartPhotoPath.replace("~",CommonShare.url1);
                Picasso.get().load(url).into(imgStartPhoto);
                if(url.length()>5)
                {
                    imgStartPhoto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ViewImage.url=url;
                            startActivity(new Intent(ViewAttenadnceDetails.this, ViewImage.class));
                        }
                    });
                }

            }
            catch (Exception ex){}
            //imgStartPhoto.
            if(model.EndKm>0)
            {
                linearEnd.setVisibility(View.VISIBLE);
                txtEndKm.setText(model.EndKm+"");
                try {
                    txtEndTime.setText("" + CommonShare.getDateTime(CommonShare.parseDate(model.EndTime)).split(" ")[1]+ " "+CommonShare.getDateTime(CommonShare.parseDate(model.EndTime)).split(" ")[2]  );

                    if(txtEndTime.getText().toString().contains("1970"))
                    {
                        txtEndTime.setText("");
                    }
                }
                catch (Exception ex){}
                txtEndLocation.  setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CommonShare.openMapWithGeo(ViewAttenadnceDetails.this,"Start Location",model.StartLocation);
                    }
                });

                try
                {
                    final String url= model.EndPhotoPath.replace("~",CommonShare.url1);
                    Picasso.get().load(url).into(imgEndPhoto);

                    if(url.length()>5)
                    {
                        imgEndPhoto.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ViewImage.url=url;
                                startActivity(new Intent(ViewAttenadnceDetails.this, ViewImage.class));
                            }
                        });
                    }
                }
                catch (Exception ex){}
            }
            else
            {
                linearEnd.setVisibility(View.INVISIBLE);
            }
        }
        else
        {
            txtIsTa.setText("No");
            linearStart.setVisibility(View.INVISIBLE);
            linearEnd.setVisibility(View.INVISIBLE);
        }
        try
        {
            if (model.CurrentStatus.equalsIgnoreCase("Started")) {
                txtCurrentStatus.setText("Day Started");
            }
            if (model.CurrentStatus.equalsIgnoreCase("Closed")) {
                txtCurrentStatus.setText("Day Closed");
            }
        }
        catch (Exception ex){}


        if(model.EmpId==CommonShare.getEmpId(this))
        {
            txtEndLocation.setVisibility(View.INVISIBLE);
            txtStartLocation.setVisibility(View.INVISIBLE);
        }
    }
    public static EmployeeAttendanceListModel model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attenadnce_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("View Attendance");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initUi();
        intItModel();

        statusList   = CommonShare.getAttendanceStatusList(this);
    }
   public   ArrayList<AttendanceStatusBean> statusList;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(CommonShare.empMap.get(model.EmpId).MangerId==CommonShare.getEmpId(this)  || CommonShare.getRole(this).equalsIgnoreCase("admin"))
        {
            if(CommonShare.isAttendanceVerification(this))
            {
                if(!getString(R.string.app_name).contains("APG"))
                {
                    if( CommonShare.getRole(this).equalsIgnoreCase("admin"))
                          getMenuInflater().inflate(R.menu.menuedit, menu);
                }
                else
                {
                    if(CommonShare.getUserId(this)==2)
                        getMenuInflater().inflate(R.menu.menuedit, menu);
                }
            }
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }
        if(item.getItemId()==R.id.actionEdit)
        {
            verifyAttendance();
        }
        return super.onOptionsItemSelected(item);
    }

    private void verifyAttendance()
    {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();


        DialogFragment newFragment = MyDialogFragment.newInstance();
        newFragment.show(ft, "dialog");
    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode) {

        if(requestCode==1 &&responseCode==200)
        {
             finish();
             startActivity(new  Intent(this,EmployeeAttendanceList.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
    }


    public static class MyDialogFragment extends DialogFragment {


        /**
         * Create a new instance of MyDialogFragment, providing "num"
         * as an argument.
         */
        static MyDialogFragment newInstance() {
            MyDialogFragment f = new MyDialogFragment();
            return f;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }
        EditText edStartKm,endKM;
        Spinner sp;
        Button btnSave,btnCancel;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.item_fragmentdialouge, container, false);
            ViewAttenadnceDetails act= (ViewAttenadnceDetails) getActivity();
            ArrayAdapter adapter=new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,act.statusList);
            sp=    v.findViewById(R.id.spinner);
            edStartKm=v.findViewById(R.id.edStartKm);
            endKM=v.findViewById(R.id.endKM);
            sp.setAdapter(adapter);
            for (int i=0;i<act.statusList.size();i++)
            {
                 if(act.model.StatusId==act.statusList.get(i).StatusId)
                 {
                     sp.setSelection(i);
                 }
            }
            try
            {
                if(    model.StartKm >0)
                {
                      edStartKm.setText(model.StartKm+"");
                }
                if(    model.EndKm >0)
                {
                    endKM.setText(model.EndKm+"");
                }
            }
            catch (Exception ex){}
            btnCancel=v.findViewById(R.id.btnCancel);
            btnSave=v.findViewById(R.id.btnSave);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   act.   finish();
                }
            });
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (edStartKm.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(act, "Enter  KM", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (endKM.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(act, "Enter  KM", Toast.LENGTH_LONG).show();
                        return;
                    }
                    int intendKM = Integer.parseInt(endKM.getText().toString());
                    int intStartKm = Integer.parseInt(edStartKm.getText().toString());
                    if(intStartKm>intendKM  || intendKM-intStartKm>500)
                    {
                        Toast.makeText(act, "Enter Valid KM", Toast.LENGTH_LONG).show();
                        return;
                    }
                    ViewAttenadnceDetails act= (ViewAttenadnceDetails) getActivity();
                    int NewStatusId=act.statusList.get(sp.getSelectedItemPosition()).StatusId;
                    String url= CommonShare.url+"Service1.svc/verifyAttendance?AttendanceId="+model.AttendanceId+"&NewStatusId="+NewStatusId+"&EnterById="+CommonShare.getEmpId(act)+"&StartKm="+edStartKm.getText().toString()+"&EndKm="+endKM.getText().toString();
                    AsyncUtilities utilities=new AsyncUtilities(act,false,url,"",1,act);
                    utilities.execute();
                }
            });
            return v;
        }
    }
}
