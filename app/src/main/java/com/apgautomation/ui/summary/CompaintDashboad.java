package com.apgautomation.ui.summary;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apgautomation.R;
import com.apgautomation.model.PieChartBean;
import com.apgautomation.model.TodaysSummaryModel;
import com.apgautomation.model.VisitSummaryModel;
import com.apgautomation.ui.adapter.VisitSummaryAdapter;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.AsyncUtilities;
import com.apgautomation.utility.serverutility.DownloadUtility;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;


public class CompaintDashboad extends Fragment implements DownloadUtility, DatePickerDialog.OnDateSetListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    public CompaintDashboad() {
        // Required empty public constructor
    }


    ArrayList<VisitSummaryModel>   visitSummaryList=new ArrayList<>();


    Calendar myCalendar = Calendar.getInstance();
    Button btnDate;

    PieChart mChart;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_compaint_dashboad, container, false);
        btnDate=v.findViewById(R.id.btnDate);
        mChart=v.findViewById(R.id.chart1);
        Date date=new Date(System.currentTimeMillis());
        btnDate.setText(date.getDate()+"/"+(date.getMonth()+1)+"/"+(1900+date.getYear()));
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new DatePickerDialog(getActivity(), CompaintDashboad.this, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();


            }
        });
        init(v);
        String url=CommonShare.url+"Service1.svc/GetAMCDetails?&EmpId="+CommonShare.getEmpId(getActivity())+"&selectDate="+System.currentTimeMillis();

        //CommonShare.url+"Service1.svc/getTodaysSummary?&EmpId="+CommonShare.getEmpId(getActivity())+"&selectDate="+System.currentTimeMillis();
        AsyncUtilities utilities=new AsyncUtilities(getActivity(),false,url,"",1,this);
        utilities.execute();
        return v;
    }

    void callRequest()
    {
        long selectedDate=0l;
        try
        {
            selectedDate= CommonShare.ddMMYYDateToLong(btnDate.getText().toString().replace("/","-"));
            String url= CommonShare.url+"Service1.svc/GetAMCDetails?&EmpId="+CommonShare.getEmpId(getActivity())+"&selectDate="+selectedDate;
            AsyncUtilities utilities=new AsyncUtilities(getActivity(),false,url,"",1,this);
            utilities.execute();
        }
        catch (Exception ex){}
    }
    void callSummaryReport(int requestCode)
    {
        long selectedDate=0l;
        try
        {
            selectedDate= CommonShare.ddMMYYDateToLong(btnDate.getText().toString().replace("/","-"));
            String url= CommonShare.url+"Service1.svc/GetPlannedSummary?&EmpId="+CommonShare.getEmpId(getActivity())+"&selectDate="+selectedDate;
            AsyncUtilities utilities=new AsyncUtilities(getActivity(),false,url,"",requestCode,this);
            utilities.execute();
        }
        catch (Exception ex){}
    }

    // TODO: Rename method, update argument and hook method into UI event

    LinearLayout linearMin;

    TextView txtTodaysBookComplaint,txtTodaysSolved,txtTodaysNotSchedule,txtTodaysBreakDown,
            txtServiceVisit,txtFsr,txtSalesVisit,txtQuotation,
            txtTotalComplaints,txtSolved,txtPending,txtUnderObservation,txtNotSchedule,txtPendingCheckup;
    Button txtAMCSummary;
    void init(View v)
    {
        txtTodaysBookComplaint=v.findViewById(R.id.txtTodaysBookComplaint);
        txtTodaysSolved=v.findViewById(R.id.txtTodaysSolved);
        txtTodaysNotSchedule=v.findViewById(R.id.txtTodaysNotSchedule);
        txtTodaysBreakDown=v.findViewById(R.id.txtTodaysBreakDown);

        txtServiceVisit=v.findViewById(R.id.txtServiceVisit);
        txtFsr=v.findViewById(R.id.txtFsr);
        txtSalesVisit=v.findViewById(R.id.txtSalesVisit);
        txtQuotation=v.findViewById(R.id.txtQuotation);

        txtTotalComplaints=v.findViewById(R.id.txtTotalComplaints);
        txtSolved=v.findViewById(R.id.txtSolved);
        txtPending=v.findViewById(R.id.txtPending);
        txtUnderObservation=v.findViewById(R.id.txtUnderObservation);

        txtNotSchedule=v.findViewById(R.id.txtNotSchedule);
        txtPendingCheckup=v.findViewById(R.id.txtPendingCheckup);
        linearMin=v.findViewById(R.id.linearMin);
        linearMin.setVisibility(View.GONE);

        txtAMCSummary=v.findViewById(R.id.txtAMCSummary);

        TextView  txtSalesSummary=v.findViewById(R.id.txtSalesSummary);
        TextView  txtServiceSummary=v.findViewById(R.id.txtServiceSummary);
        txtServiceSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callSummaryReport(3);
            }
        });
        txtSalesSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callSummaryReport(2);
            }
        });
        txtAMCSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),AMCSummaryActivity.class));
            }
        });
       v. findViewById(R.id.linearSales).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callSummaryReport(2);
            }
        });
       v.  findViewById(R.id.linearService).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callSummaryReport(3);
            }
        });
    }



    @Override
    public void onComplete(String str, int requestCode, int responseCode) {
           try
           {

              if(responseCode==200   && requestCode==1)
              {
                  JSONObject jj=new JSONObject(str);
                 Gson g=new Gson();
                 TodaysSummaryModel model=g.fromJson(jj.getJSONObject("TodaysSummary").toString(),TodaysSummaryModel.class);
                 linearMin.setVisibility(View.VISIBLE);
                 txtTodaysBookComplaint.setText((int)model.getBookedComplaint()+"");
                 txtTodaysSolved.setText((int)model.getSolvedComplaint()+"");
                 txtTodaysNotSchedule.setText((int)model.getNotSchedledComplaint()+"");
                 txtTodaysBreakDown.setText((int)model.getBreakdownComplaint()+"");

                 txtServiceVisit.setText((int)model.getServiceVisit()+"");
                 txtFsr.setText((int)model.getFSRCnt()+"");
                 txtSalesVisit.setText((int)model.getSalesVisit()+"");
                 txtQuotation.setText((int)model.getQuotationCnt()+"");

                 txtTotalComplaints.setText((int)model.getTotalComplaint()+"");
                 txtSolved.setText((int)model.getTotalSolvedComplint()+"");
                 txtPending.setText((int)model.getTotalPendingComplaint()+"");
                  txtUnderObservation.setText(model.UnderObservation+"");

                 txtNotSchedule.setText((int)model.getTotalNotSchedledComplaint()+"");
                 txtPendingCheckup.setText((int)model.UnderCCF+"");

                 linearMin.setVisibility(View.VISIBLE);
                 try {
                     float donePer=((float) model.AmcDone/ (float) (model.AmcDone+model.AMCPending))*100;
                     //setChart(donePer, (100-donePer));
                     mChart.setBackgroundColor(Color.WHITE);

                     setChart(jj.getJSONArray("AMCTypeSummary").toString());
                     mChart.setBackgroundColor(Color.WHITE);
                 }
                 catch (Exception ex){}
             }

               if(responseCode==200   && requestCode==3)
               {
                   //CommonShare.alert(getActivity(),str);
                   visitSummaryList.clear();
                   try
                   {
                     Gson gson=new Gson();
                       JSONArray array=new JSONArray(str);
                       for(int i=0;i<array.length();i++)
                       {
                           VisitSummaryModel m=gson.fromJson(array.getJSONObject(i).toString(),VisitSummaryModel.class);
                           if(m.DeptId==3)
                             visitSummaryList.add(m);

                       }
                   }
                   catch (Exception ex)
                   {}
                   AlertDialog.Builder alert=new AlertDialog.Builder(getContext());
                   alert.setTitle("Visit Summary List");
                   VisitSummaryAdapter adapter=new VisitSummaryAdapter(getContext(), android.R.layout.simple_list_item_1,visitSummaryList);
                   adapter.isFsrShow =1;
                   alert.setAdapter(adapter, new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {

                       }
                   });
                   alert.setPositiveButton("Close",null);
                   alert.show();

                   // CommonShare.alert(getContext(),str);
               }
               if(responseCode==200   && requestCode==2)
               {
                 //  CommonShare.alert(getContext(),str);
                   visitSummaryList.clear();
                   try
                   {
                       Gson gson=new Gson();
                       JSONArray array=new JSONArray(str);
                       for(int i=0;i<array.length();i++)
                       {
                           VisitSummaryModel m=gson.fromJson(array.getJSONObject(i).toString(),VisitSummaryModel.class);
                           if(m.DeptId==2)
                           visitSummaryList.add(m);

                       }
                   }
                   catch (Exception ex)
                   {}
                   AlertDialog.Builder alert=new AlertDialog.Builder(getContext());
                   alert.setTitle("Visit Summary List");
                   VisitSummaryAdapter adapter=new VisitSummaryAdapter(getContext(), android.R.layout.simple_list_item_1,visitSummaryList);
                   alert.setAdapter(adapter, new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {

                       }
                   });
                   alert.setPositiveButton("Close",null);
                   alert.show();
               }
           }
           catch (Exception ex)
           {}

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth)
    {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        btnDate.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
        callRequest();
    }




    //--------------------  Pie chart----------------------
    void setChart(float donePer,float pendingPer)
    {
        PieChartBean bean1=new PieChartBean();
        bean1.MiddleText="AMC";
        bean1.party="Done";
        bean1.per=donePer;
        PieChartBean bean=new PieChartBean();
        //bean.DataType="Employee Attendance";
        bean.MiddleText="AMC";
        bean.party="Pending";
        bean.per=pendingPer;

        ArrayList<PieChartBean> list1=new ArrayList<>();
        list1.add(bean1);
        list1.add(bean);
        inIT(list1);
    }

    void setChart(String str)
    {
        try
        {
            ArrayList<PieChartBean> list1=new ArrayList<>();
            JSONArray array=new JSONArray(str);
            for(int i=0;i<array.length();i++)
            {
                JSONObject j=array.getJSONObject(i);
                PieChartBean bean1=new PieChartBean();
                bean1.MiddleText="AMC";
                bean1.party=j.getString("AmcStatus");
                bean1.per=j.getInt("Cnt");
                list1.add(bean1);
            }
            inIT(list1);
        }
        catch (Exception ex){}
        /*
        PieChartBean bean1=new PieChartBean();
        bean1.MiddleText="AMC";
        bean1.party="Done";
        bean1.per=donePer;
        PieChartBean bean=new PieChartBean();
        //bean.DataType="Employee Attendance";
        bean.MiddleText="AMC";
        bean.party="Pending";
        bean.per=pendingPer;

        ArrayList<PieChartBean> list1=new ArrayList<>();
        list1.add(bean1);
        list1.add(bean);*/

    }
    void inIT(ArrayList<PieChartBean> list1)
    {

        mChart.setUsePercentValues(true);
        mChart.setDescription("");
        mChart.setExtraOffsets(5, 10, 5, 5);



        mChart.setDragDecelerationFrictionCoef(0.95f);

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);
        setData(list1);
        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);
        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
    }
    private void setData(ArrayList<PieChartBean> list)
    {
        float mult = 100;
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();
        for(int i=0;i<list.size();i++)
        {
            yVals1.add(new Entry(list.get(i).per, i));
            xVals.add(list.get(i).party);
        }
        try {
            mChart.setDescription("");
            //  (int)(list.get(0).per + list.get(1).per)+" Condcted Lectures "+"\n"+
            //  (int)(list.get(0).per) + " Attended Lectures out of "+(int)(list.get(0).per + list.get(1).per) );
        }
        catch (Exception e){}
        mChart.setCenterText(generateCenterSpannableText(list.get(0).MiddleText));
        PieDataSet dataSet = new PieDataSet(yVals1, list.get(0).DataType);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors
        ArrayList<Integer> colors = new ArrayList<Integer>();
       /* for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
*/
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        // data.setValueTypeface(tf);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }
    private SpannableString generateCenterSpannableText(String middleText) {

        SpannableString s = new SpannableString(middleText);
        return s;
    }

    //--------------------Pie Chart------------------------
}
