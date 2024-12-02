package com.apgautomation.ui.activities;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.apgautomation.BuildConfig;
import com.apgautomation.R;
import com.apgautomation.controller.AttendanceController;
import com.apgautomation.controller.SaleVisitController;
import com.apgautomation.controller.VisitModuleController;
import com.apgautomation.model.AttendanceModel;
import com.apgautomation.model.EmployeeModel;
import com.apgautomation.model.GsonVisitMaster;
import com.apgautomation.model.ServerModel.GsonSalesVisit;
import com.apgautomation.model.database.ItemDAOAttendanceModel;
import com.apgautomation.ui.attendance.EmployeeAttendanceList;
import com.apgautomation.ui.attendance.EmployeeRecentAttenance;
import com.apgautomation.ui.attendance.MarkAttendance;
import com.apgautomation.ui.complaint.ViewComplaint;
import com.apgautomation.ui.leave.LeaveApplicationsLiistForManager;
import com.apgautomation.ui.salesvisit.SalesVisitDetails;
import com.apgautomation.ui.salesvisit.SalesVisitPendingList;
import com.apgautomation.ui.visit.ProcessVisit;
import com.apgautomation.ui.visit.VisitCustomerList;
import com.apgautomation.ui.visit.VisitList;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.AsyncUtilities;
import com.apgautomation.utility.serverutility.DownloadUtility;
import com.apgautomation.utility.serverutility.netrequest.ConnectionDetector;
import com.apgautomation.utility.syncutilities.VisitSyncUtility;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivitiesFragment extends Fragment implements DownloadUtility {
    VisitSyncUtility syncUtility;

    public ActivitiesFragment() {
        // Required empty public constructor
    }


    @BindView(R.id.btnMarkAttendance) Button btnMarkAttendance;
    @BindView(R.id.txtDate)
    TextView txtDate;

    @BindView(R.id.txtStatus)
    TextView txtStatus;

    @BindView(R.id.txtCurrentStatus)
    TextView txtCurrentStatus;
   //@BindView(R.id.button1) Button button1;
    //Button btnMarkAttendance;
    @BindView(R.id.btnViewEmpAttendance)
    Button btnViewEmpAttendance;

    @BindView(R.id.btnRecentAttendance)
    Button btnRecentAttendance;

    @BindView(R.id.btnViewEmpLeaveApplication)
    Button btnViewEmpLeaveApplication;

    @BindView(R.id.btnViewScheduleVisit)
    Button btnViewScheduleVisit;

    @BindView(R.id.linearStarted)
    LinearLayout linearStarted;

    @BindView(R.id.btnStartedVisit)
    Button btnStartedVisit;
    @BindView(R.id.txtGroupName)
    TextView  txtGroupName;

    @BindView(R.id.imgMyAttendance)
    ImageView imgMyAttendance;
    @BindView(R.id.imgRecent)
    ImageView imgRecent;
    @BindView(R.id.imgLeave)
    ImageView imgLeave;
    @BindView(R.id.lA)
    LinearLayout lA;
    @BindView(R.id.lB)
    LinearLayout lB;
    @BindView(R.id.lC)
    LinearLayout lC;




    CardView cardComplaint;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // CommonShare.alert(getActivity(),"Act Frag");
        View v=inflater.inflate(R.layout.fragment_activities, container, false);
        ButterKnife.bind(this, v);
        cardComplaint=v.findViewById(R.id.cardComplaint);
        // ButterKnife.inject(this, v);

       // btnMarkAttendance=v.findViewById(R.id.btnMarkAttendance);
/*
        btnMarkAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
*/
        initView();
        txtDate.setText(CommonShare.convertToDate(System.currentTimeMillis()));


        ItemDAOAttendanceModel item=new ItemDAOAttendanceModel(getActivity());
         ArrayList<AttendanceModel> list= item.getRecentAttendance();

        /* String ss="";
         for (int i=0;i<list.size();i++)
         {
            ss= list.get(i).AttendanceDate+"\n";
         }
         CommonShare.alert(getActivity(),ss+"\n\n"+list.toString());
*/
        return v;
    }
    public void refreshAttendance()
    {
        initView();

    }
    void initView()
    {

        AttendanceController controller=new AttendanceController(getActivity());
        AttendanceModel model= controller.getTodaysAttendanceToken();
        if(model==null)
        {
            txtStatus.setText("Not Marked");
            txtStatus.setTextColor(Color.RED);

        }
        else
        {
            txtStatus.setText(CommonShare.getSelectedStatusTextById( model.StatusId,getActivity()));
            txtStatus.setTextColor(Color.GREEN);
            btnMarkAttendance.setText("Edit Attendance");
            if(model.CurrentStatus.equalsIgnoreCase("Closed"))
            {
                btnMarkAttendance.setText("View Attendance");
            }
            if(model.CurrentStatus.equalsIgnoreCase("Started"))
            {
                txtCurrentStatus.setText("Day Started");
            }
            else if(model.CurrentStatus.equalsIgnoreCase("Closed"))
            {
                txtCurrentStatus.setText("Day Closed");
            }
        }
        if(model!=null)
        {
            try
            {
                /*
                Gson g=new Gson();
                String jsonStr= g.toJson(model);
                AsyncUtilities utilities=new AsyncUtilities(getActivity(),true,CommonShare.url+"Service1.svc/SaveAttendance",jsonStr,1,this);
                utilities.execute();*/
            }
            catch (Exception ex){}
        }


        if(CommonShare.getTeamList(getActivity()).size()==0)
        {
            btnViewEmpAttendance.setVisibility(View.GONE);
            btnViewEmpLeaveApplication.setVisibility(View.GONE);

            lB.setVisibility(View.GONE);
            lC.setVisibility(View.GONE);
        }

        if(CommonShare.getRole(getActivity()).equalsIgnoreCase("Admin") ||  CommonShare.getEmpId(getActivity())==9 || CommonShare.getEmpId(getActivity())==53 )
        {
            lB.setVisibility(View.VISIBLE);
            lC.setVisibility(View.VISIBLE);
            btnViewEmpAttendance.setVisibility(View.VISIBLE);
            btnViewEmpLeaveApplication.setVisibility(View.VISIBLE);

        }


        viewLastDayAttendance();


        lA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click2();
            }
        });
        lB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click1();
            }
        });
        lC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click3();
            }
        });

    }

    ScrollView mScrollView;
    @Override
    public void onResume() {
        super.onResume();
        EmployeeModel m= CommonShare.getMyEmployeeModel(getActivity());
        if(m.DeptId==3)
        {
            VisitModuleController controller = new VisitModuleController(getActivity());
            if (controller.getStaretedVisit() != null) {
                txtGroupName.setText("Visit Started at " + controller.getStaretedVisit().getGroupName());
                linearStarted.setVisibility(View.VISIBLE);
            } else {
                linearStarted.setVisibility(View.GONE);
            }

            String url=CommonShare.url+"Service1.svc/GetServiceEmpSummary?EmpId="+m.EmpId;
            AsyncUtilities utilities=new AsyncUtilities(getActivity(),false,url,null,2701,this);
            utilities.hideProgressDialoge();
            utilities.execute();
        }
        if(m.DeptId==2) {
            SaleVisitController controller1 = new SaleVisitController(getActivity());
            GsonSalesVisit vObj = controller1.getStarteVisit();
            if (vObj != null) {
                txtGroupName.setText("Visit Started at " + vObj.getVGroupName());
                linearStarted.setVisibility(View.VISIBLE);
            } else {
                linearStarted.setVisibility(View.GONE);
            }
        }



        mScrollView=getView().findViewById(R.id.mScrollView);
      //  mScrollView.setSmoothScrollingEnabled(true);
        /*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isAdded()) {
                    // Scroll 1000px down
                    mScrollView.smoothScrollTo(0, 1000);
                }
            }
        }, 1000);*/
    }

    boolean viewLastDayAttendance()
    {
        AttendanceController controller=new AttendanceController(getActivity());
        AttendanceModel model1= controller.getLastDayAttendanceToken();

        if(model1!=null)
        {
            if(model1.CurrentStatus!=null  && !model1.CurrentStatus.equalsIgnoreCase("Closed"))
            {
                AlertDialog.Builder alert =new AlertDialog.Builder(getActivity());
                alert.setTitle("Last day attendance");
                alert.setMessage("Last day attendance is not closed ,Please close last day attendance .");
                alert.setPositiveButton("Close Last day attendance", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getActivity(), MarkAttendance.class).putExtra("IsLastDay",true));
                    }
                });
                alert.setCancelable(false);
                alert.show();

                return false;
            }
        }
        return true;
    }



    @OnClick(R.id.btnMarkAttendance)
    void click()
    {
        // TODO ...
     if(viewLastDayAttendance())
     {
         if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
         {
             androidx.appcompat.app.AlertDialog.Builder  alert=new androidx.appcompat.app.AlertDialog.Builder(getActivity());

             alert.setTitle("Location Permission Not Given");
             alert.setMessage("Location Permission Not Given.Please Give Location.");
             alert.setPositiveButton("Open Permission Setting", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialogInterface, int i) {
                     Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID));
                     startActivity(intent);
                 }
             });
             alert.setNegativeButton("No",null);
             alert.show();
             return;

         }

         if(new ConnectionDetector(getActivity()).isConnectingToInternet()) {
             startActivity(new Intent(getActivity(), MarkAttendance.class).putExtra("IsLastDay", false));
         }
         else
         {
             AlertDialog.Builder alert=new AlertDialog.Builder(getActivity());
             alert.setTitle("Internet Not Available");
             alert.setMessage("Please make sure that internet is on .");
             alert.setPositiveButton("Check Internet", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialogInterface, int i) {
                     try {
                         Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                        // intent.setClassName("com.android.phone", "com.android.phone.NetworkSetting");
                         startActivity(intent);
                     }
                     catch (Exception ex)
                     {}
                 }
             });
             alert.setNegativeButton("Cancel",null);
             alert.show();
         }
     }
    }


    @OnClick(R.id.btnViewEmpAttendance)
    void click1()
    {
        Intent i= new Intent(getActivity(), EmployeeAttendanceList.class);
        startActivity(i);
    }
    @OnClick(R.id.btnRecentAttendance)
    void click2()
    {
        Intent i= new Intent(getActivity(), EmployeeRecentAttenance.class);
        startActivity(i);
    }
    @OnClick(R.id.btnViewEmpLeaveApplication)
    void click3()
    {
        Intent i= new Intent(getActivity(), LeaveApplicationsLiistForManager.class);
        startActivity(i);
    }
    @OnClick(R.id.btnViewScheduleVisit)
    void click4()
    {
        syncUtility=new VisitSyncUtility(getActivity());
        if(!new ConnectionDetector(getActivity()).isConnectingToInternet())
        {
            Toast.makeText(getActivity(), "Please Turn on your Internet", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!syncUtility.isPeningZeoVisit()) {
            EmployeeModel m = CommonShare.getMyEmployeeModel(getActivity());
            if (m.DeptId == 3) {
                Intent i = new Intent(getActivity(), VisitList.class);
                startActivity(i);
            } else if (m.DeptId == 2) {
                Intent i = new Intent(getActivity(), SalesVisitPendingList.class);
                startActivity(i);
            }
        }
        else
        {
            startProgress();
        }
    }
    @OnClick(R.id.btnStartedVisit)
    void btnStartedVisitOnClick()
    {
        EmployeeModel m= CommonShare.getMyEmployeeModel(getActivity());
        if(m.DeptId==3)
        {
            VisitModuleController controller = new VisitModuleController(getActivity());
            GsonVisitMaster vistMaster = controller.getStaretedVisit();
            if (vistMaster != null) {
                startActivity(new Intent(getActivity(), ProcessVisit.class).putExtra("VisitToken", vistMaster.getVisitToken()));
            }
        }
        else  if(m.DeptId==2)
        {
            SaleVisitController controller=new SaleVisitController(getActivity());
            GsonSalesVisit vObj= controller.getStarteVisit();
            if(vObj!=null)
            {
                startActivity(new Intent(getActivity(), SalesVisitDetails.class).putExtra("VisitToken",vObj.getVisitTokenId()));
            }

        }
    }
    @Override
    public void onComplete(String str, int requestCode, int responseCode)
    {
       // Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
        if(requestCode==2701 && responseCode==200)
        {
            try
            {
                cardComplaint.setVisibility(View.VISIBLE);
                Button btnPending =getView().findViewById(R.id.btnPending);TextView badge_notification_1=getView().findViewById(R.id.badge_notification_1);
                Button btnExcced=getView().findViewById(R.id.btnExcced) ;TextView badge_notification_2=getView().findViewById(R.id.badge_notification_2);
                Button btnVisitScedule=getView().findViewById(R.id.btnVisitScedule) ;TextView badge_notification_3=getView().findViewById(R.id.badge_notification_3);
                Button btnPndingCheckUp =getView().findViewById(R.id.btnPndingCheckUp);TextView badge_notification_4=getView().findViewById(R.id.badge_notification_4);

                JSONObject j=new JSONObject(str);
                int PendingComplaint= j.getInt("PendingComplaint");
                badge_notification_1.setText(PendingComplaint+"");
                int ExcedeCnt= j.getInt("ExcedeCnt");
                badge_notification_2.setText(ExcedeCnt+"");
                int ScheduleVisittCnt= j.getInt("ScheduleVisitCnt");
                badge_notification_3.setText(ScheduleVisittCnt+"");
                int PendingChekup= j.getInt("PendingCheckup");
                badge_notification_4.setText(PendingChekup+"");

                btnPndingCheckUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getActivity(), VisitCustomerList.class));
                    }
                });

                btnVisitScedule.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       click4();
                    }
                });

                btnPending.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getActivity(), ViewComplaint.class).putExtra("IsEmp",true));
                    }
                });
                btnExcced.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getActivity(), ViewComplaint.class).putExtra("IsEmp",true).putExtra("IsExceeded",true));
                    }
                });
            }
            catch (Exception ex)
            {}
        }
    }

    @Override
    public void onStop() {
        super.onStop();

    }


    ProgressDialog pd;
    int starterCount=0;
    public void startProgress() {

        if(pd!=null && pd.isShowing())
            pd.hide();

            pd = new ProgressDialog(getActivity());
            pd.setMessage("Visit Scheduling pending... Please Wait");
            pd.setCancelable(false);
            pd.show();
            starterCount++;
            checkDialogue();
    }

    void checkDialogue()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pd.hide();
                        }
                    });
                   // startProgress();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
