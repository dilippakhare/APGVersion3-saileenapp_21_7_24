package com.example.agecalculator;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;

public class MyWorker extends Worker {
    Context context;
    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context=context;

    }

    @NonNull
    @Override
    public Result doWork() {
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 1,
                new Intent(getApplicationContext(), MainActivity.class).
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),
                PendingIntent.FLAG_CANCEL_CURRENT);
        try {
            String str = calculate();
            if(Build.VERSION.SDK_INT>=26){
                str = calculate1();
            }
            SimpleDateFormat formatter = new SimpleDateFormat("hh");
             Date date = new Date();
             String hrStr= formatter.format(date);
             if(hrStr.equalsIgnoreCase("12")||hrStr.equalsIgnoreCase("9")|| hrStr.equalsIgnoreCase("10")||hrStr.equalsIgnoreCase("11"))
                 NewNotificationMessage.notify(getApplicationContext(), str, str, 1, contentIntent);
        }
        catch (Exception ex){}
        return Result.success();
    }

    String  calculate()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();

        SharedPreferences sh=this.context.getSharedPreferences("b", this.context.MODE_PRIVATE);
         Date d1=new Date(Integer.parseInt(sh.getString("y","")),Integer.parseInt(sh.getString("m","")),Integer.parseInt(sh.getString("d","")));

         return findDifference(d1.getYear()+"-"+d1.getMonth()+"-"+d1.getDay()+" 00:00:00",
                 ( formatter.format(date)).split("-")[2] +"-"+( formatter.format(date)).split("-")[1]+"-"+( formatter.format(date)).split("-")[0]+" 00:00:00"
                 );
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    String calculate1()
    {
        SharedPreferences sh=this.context.getSharedPreferences("b", this.context.MODE_PRIVATE);
        LocalDate startDate
                = LocalDate.of(Integer.parseInt(sh.getString("y","")),Integer.parseInt(sh.getString("m","")),Integer.parseInt(sh.getString("d","")));

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();

        LocalDate endDate
                = LocalDate.of(
                Integer.parseInt( formatter.format(date).split("-")[2]),
                Integer.parseInt( formatter.format(date).split("-")[1]),
                Integer.parseInt( formatter.format(date).split("-")[0])
        );


        return findDifference1(startDate,endDate);
    }
    String  findDifference(String start_date,
                   String end_date)
    {

        // SimpleDateFormat converts the
        // string format to date object
        SimpleDateFormat sdf
                = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");

        // Try Block
        try {

            Date d1 = sdf.parse(start_date);
            Date d2 = sdf.parse(end_date);

            long time_difference = d2.getTime() - d1.getTime();
            // Calucalte time difference in days
            long days_difference = (time_difference / (1000*60*60*24)) % 365;
            // Calucalte time difference in years
            long years_difference = (time_difference / (1000l*60*60*24*365));

            return  "You are "+years_difference+" years "+days_difference+" days old";
        }
        catch (ParseException e) {
            e.printStackTrace();
            return  "";
        }


    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    String
    findDifference1(LocalDate start_date,
                    LocalDate end_date)
    {

        // find the period between
        // the start and end date
        Period diff
                = Period
                .between(start_date,
                        end_date);

        // Print the date difference
        // in years, months, and days
        System.out.print(
                "Difference "
                        + "between two dates is: ");

        // Print the result
       return (
            "You are "+   diff.getYears()+" years,"+diff.getMonths()+" months"
                        + " and "+diff.getDays()+" days "

                );
    }
}
