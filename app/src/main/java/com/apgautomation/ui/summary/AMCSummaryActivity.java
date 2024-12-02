package com.apgautomation.ui.summary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apgautomation.R;
import com.apgautomation.model.ModelAMCSummary;
import com.apgautomation.model.PieChartBean;
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
import com.natasa.progressviews.LineProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AMCSummaryActivity extends AppCompatActivity implements DownloadUtility {

    LinearLayout linearMain;
    PieChart mChart;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_m_c_summary);
        linearMain=findViewById(R.id.linearMain);
        mChart=findViewById(R.id.chart1);

        String url= CommonShare.urlv1+"service1.svc/amcsummary";
        AsyncUtilities utilities=new AsyncUtilities(this,false,url,null,1,this);
        utilities.execute();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("AMC Summary");


    }

    ArrayList<ModelAMCSummary>  list=new ArrayList<>();
    @Override
    public void onComplete(String str, int requestCode, int responseCode) {
       // CommonShare.alert(this,str);
        if(requestCode==1 && responseCode==200)
        {
                try
                {
                    Gson gson=new Gson();
                    JSONArray array=new JSONArray(str);
                    list.clear();
                    for (int i=0;i<array.length();i++)
                    {
                        JSONObject j=array.getJSONObject(i);
                         list.add( gson.fromJson(j.toString(),ModelAMCSummary.class));
                    }
                    TextView txtTotal=findViewById(R.id.txtTotal);
                    TextView txtDone=findViewById(R.id.txtDone);
                    TextView txtPending=findViewById(R.id.txtPending);
                    txtTotal.setText(((int)list.get(0).getTotal())+"");
                    txtDone.setText((int) ((int) list.get(0).getTotal()- (int)list.get(0).getPending())+"");
                    txtPending.setText(((int)list.get(0).getPending())+"");

                    float percentDone=    ( (float) (list.get(0).getTotal()- (int)list.get(0).getPending())  /
                                            (float)list.get(0).getTotal())                        *100;


                    PieChartBean bean1=new PieChartBean();
                    bean1.MiddleText="AMC";
                    bean1.party="Done";
                  //  bean1.per=(list.get(0).getAmcDone()/(list.get(0).getAmcDone()+list.get(0).getAMCPending()))*100;
                    bean1.per=(percentDone);
                    PieChartBean bean=new PieChartBean();
                    //bean.DataType="Employee Attendance";
                    bean.MiddleText="AMC";
                    bean.party="Pending";
                 //   bean.per=(list.get(0).getAMCPending()/(list.get(0).getAmcDone()+list.get(0).getAMCPending()))*100;
                    bean.per= 100-bean1.per;
                            //(list.get(0).getAMCPending()/((int)list.get(0).getTotal())*100);

                    ArrayList<PieChartBean> list1=new ArrayList<>();
                    list1.add(bean1);
                    list1.add(bean);
                    inIT(list1);

                    LinearLayout l = (LinearLayout) findViewById(R.id.linearMain);
                    LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    for (int i = 0; i < list.size(); i++)
                    {
                      View  convertView = mInflater.inflate(R.layout.item_engamc, null);
                        TextView t1 = (TextView) convertView.findViewById(R.id.textMonth);
                        TextView t2 = (TextView) convertView.findViewById(R.id.txtcondected);
                        TextView t3 = (TextView) convertView.findViewById(R.id.txtAttended);
                        TextView t4 = (TextView) convertView.findViewById(R.id.textPercent);
                        t1.setText(list.get(i).getEngineerName());
                        t2.setText(((int)list.get(i).getAmcDone())+"");
                        t3.setText( (int)list.get(i).getAMCPending() + "");
                        float percentage = (list.get(i).getAmcDone()/ (list.get(i).getAmcDone()+list.get(i).getAMCPending() )) * 100;
                        DecimalFormat df = new DecimalFormat();
                        df.setMaximumFractionDigits(2);
                        t4.setText(df.format(percentage)+ "%");

                        //if((list.get(i).getAmcDone() ==-9999? 0 :list.get(i).ObtainedMarks ) <list.get(i).PassMarks)
                        //    t4.setTextColor(Color.rgb(255,0,0));



                        LineProgressBar lineProgressbar = (LineProgressBar)convertView. findViewById(R.id.line_progressbar);
                        lineProgressbar.setLinearGradientProgress(true);
                        lineProgressbar.setRoundEdgeProgress(true);
                        lineProgressbar.setProgress(percentage);
                        l.addView(convertView);
                    }
                }
                catch (Exception ex)
                {
                    CommonShare.alert( this, ex.toString());
                }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
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
}