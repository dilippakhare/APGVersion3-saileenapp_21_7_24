package com.apgautomation.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.apgautomation.HomePage;
import com.apgautomation.model.GsonAskQuery;
import com.apgautomation.model.GsonTodo;
import com.apgautomation.model.ServerModel.GsonSalesVisit;
import com.apgautomation.model.ServerModel.ServerComplaintModel;
import com.apgautomation.model.ServerModel.ServerLeaveDetails;
import com.apgautomation.model.ServerModel.ServerLeaveModel;
import com.apgautomation.ui.communication.CommunicationDetails;
import com.apgautomation.ui.complaint.ViewAllComplaint;
import com.apgautomation.ui.customer.CustomerActivity;
import com.apgautomation.ui.leave.LeaveApplicationDetails;
import com.apgautomation.ui.quotation.QuotationList;
import com.apgautomation.ui.taskmgmt.ui.TaskDetails;
import com.apgautomation.utility.CommonShare;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

import static android.content.ContentValues.TAG;

public class MyFirebaseMessagingService extends FirebaseMessagingService
{

    private NotificationManager mNotificationManager;

    @Override
    public void onNewToken(String token) {
        Log.d("DILIP", "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
       // sendRegistrationToServer(token);
    }



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        try {

            // Check if message contains a data payload.
            if (remoteMessage.getData().size() > 0) {
                Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            /*
            if (/* Check if data needs to be processed by long running job * / true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }
            */
                //  NewMessageNotification.notify(this, remoteMessage.getData().toString(), 1);
                //NewMessageNotification.notify(this,remoteMessage.getNotification().getBody(),1);

                sendNotification(remoteMessage.getData().toString());
            }

            // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {
                Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
                sendNotification(remoteMessage.getNotification().getBody());
                // NewMessageNotification.notify(this,remoteMessage.getNotification().getBody(),1);
            }
            //   NewMessageNotification.notify(this,"Notice",1);
            // Also if you intend on generating your own notifications as a result of a received FCM
            // message, here is where that should be initiated. See sendNotification method below.
        }
        catch (Exception ex){
            //    AlertDialog.Builder alert=new AlertDialog.Builder(c);

        }

    }

    //https://fcm.googleapis.com/fcm/send



    int NOTIFICATION_ID;
    private void sendNotification(String msg)
    {
        try
        {
            JSONObject j=new JSONObject(msg);
            msg=j.getJSONObject("body").toString();

        }
        catch (Exception ex){}
        /*
       try
       {
           msg=msg.replace("GeneralId","\"GeneralId\"");
           msg=msg.replace("MSGCategory","\"MSGCategory\"");
           msg=msg.replace("GeneralName","\"GeneralName\"");
           msg=msg.replace("TypeBody","\"TypeBody\"");

           String[] arr= msg.split("=");
           //------------First Change------------
           String firstChange=  arr[1].split("\"GeneralName")[0];
           String newFisrtString=firstChange;
           newFisrtString=newFisrtString.replace(",","");
           newFisrtString="\""+newFisrtString+"\",";
           msg=msg.replace(firstChange,newFisrtString);

           //------------Second Change------------
           String secondChange=  arr[2].split("\"GeneralId")[0];
           String newSecondString=secondChange;
           newSecondString=newSecondString.replace(",","");
           newSecondString="\""+newSecondString+"\",";
           msg=msg.replace(secondChange,newSecondString);


       }
       catch (Exception ex){}
       */

        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        try
        {
            //JSONObject jsonObject=new JSONObject(msg);
            Gson g=new Gson();
            FCMBody body=g.fromJson(msg,FCMBody.class);
            JSONObject mainJson=new JSONObject(msg);
            String Cate=mainJson.getString("MSGCategory");
            if(Cate.equalsIgnoreCase("Leave") ||Cate.equalsIgnoreCase("Leave Responsible") )
            {
                JSONObject jsonObject = new JSONObject(msg);
                ServerLeaveModel model = g.fromJson(mainJson.getJSONObject("TypeBody").getJSONObject("leaveApplication").toString(), ServerLeaveModel.class);
                try {
                    model.leaveDetails = new ArrayList<>();
                    JSONArray arr = mainJson.getJSONObject("TypeBody").getJSONArray("leaveDetails");
                    for (int j = 0; j < arr.length(); j++)
                    {
                        ServerLeaveDetails mOb = g.fromJson(jsonObject.getJSONObject("leaveApplication").toString(), ServerLeaveDetails.class);
                        model.leaveDetails.add(mOb);
                    }
                } catch (Exception ex) {
                }
                NOTIFICATION_ID = mainJson.getInt("GeneralId");
                PendingIntent contentIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, new Intent(this, LeaveApplicationDetails.class).putExtra("msg", mainJson.getJSONObject("TypeBody").toString()).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP), PendingIntent.FLAG_CANCEL_CURRENT);
                NewMessageNotification.notify(this,mainJson.getString("GeneralName") , mainJson.getString("GeneralName"), NOTIFICATION_ID, contentIntent);
            }
            else  if(Cate.equalsIgnoreCase("Leave Approval")  )
            {
                JSONObject jsonObject = new JSONObject(msg);
                ServerLeaveModel model = g.fromJson(mainJson.getJSONObject("TypeBody").getJSONObject("leaveApplication").toString(), ServerLeaveModel.class);
                try {
                    model.leaveDetails = new ArrayList<>();
                    JSONArray arr = mainJson.getJSONObject("TypeBody").getJSONArray("leaveDetails");
                    for (int j = 0; j < arr.length(); j++)
                    {
                        ServerLeaveDetails mOb = g.fromJson(jsonObject.getJSONObject("leaveApplication").toString(), ServerLeaveDetails.class);
                        model.leaveDetails.add(mOb);
                    }
                } catch (Exception ex) {
                }
                NOTIFICATION_ID = mainJson.getInt("GeneralId");
                PendingIntent contentIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, new Intent(this, LeaveApplicationDetails.class).putExtra("msg", mainJson.getJSONObject("TypeBody").toString()).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP), PendingIntent.FLAG_CANCEL_CURRENT);
                NewMessageNotification.notify(this,mainJson.getString("GeneralName") , mainJson.getString("GeneralName"), NOTIFICATION_ID, contentIntent);
            }
            else  if(Cate.equalsIgnoreCase("Complaint Booking")  )
            {
                JSONObject jsonObject = new JSONObject(msg);
                ServerComplaintModel model = g.fromJson(mainJson.getJSONObject("TypeBody").toString(), ServerComplaintModel.class);
                try {

                } catch (Exception ex) {
                }
                NOTIFICATION_ID = mainJson.getInt("GeneralId");
                PendingIntent contentIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, new Intent(this, ViewAllComplaint.class).putExtra("msg", mainJson.getJSONObject("TypeBody").toString()).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP), PendingIntent.FLAG_CANCEL_CURRENT);
                NewMessageNotification.notify(this,mainJson.getString("GeneralName") , mainJson.getString("GeneralName"), NOTIFICATION_ID, contentIntent);
            }

            else  if(Cate.equalsIgnoreCase("Visit Schedule")  )
            {
                JSONObject jsonObject = new JSONObject(msg);
                 try
                 {
                     ServerComplaintModel model = g.fromJson(mainJson.getJSONObject("TypeBody").toString(), ServerComplaintModel.class);
                     NOTIFICATION_ID = mainJson.getInt("GeneralId");
                     PendingIntent contentIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, new Intent(this, HomePage.class).putExtra("msg", mainJson.getJSONObject("TypeBody").toString()).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP), PendingIntent.FLAG_CANCEL_CURRENT);
                     NewMessageNotification.notify(this,mainJson.getString("GeneralName") , mainJson.getString("GeneralName"), NOTIFICATION_ID, contentIntent);

                 } catch (Exception ex)
                 {
                     try
                     {
                         GsonSalesVisit model = g.fromJson(mainJson.getJSONObject("TypeBody").toString(), GsonSalesVisit.class);
                         NOTIFICATION_ID = mainJson.getInt("GeneralId");
                         PendingIntent contentIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, new Intent(this, HomePage.class).putExtra("msg", mainJson.getJSONObject("TypeBody").toString()).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP), PendingIntent.FLAG_CANCEL_CURRENT);
                         NewMessageNotification.notify(this,mainJson.getString("GeneralName") , mainJson.getString("GeneralName"), NOTIFICATION_ID, contentIntent);


                     }
                     catch (Exception ee){}
                 }
            }
            else  if(Cate.equalsIgnoreCase("Quotation Request")  || Cate.equalsIgnoreCase("Quotation Response")  )
            {
                NOTIFICATION_ID = mainJson.getInt("GeneralId");
                PendingIntent contentIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, new Intent(this, QuotationList.class).putExtra("msg", mainJson.getJSONObject("TypeBody").toString()).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP), PendingIntent.FLAG_CANCEL_CURRENT);
                NewMessageNotification.notify(this, mainJson.getString("GeneralName"), mainJson.getString("GeneralName"), NOTIFICATION_ID, contentIntent);
            }
            else  if(Cate.equalsIgnoreCase("Notice")  )
            {
                JSONObject jsonObject = new JSONObject(msg);

                if(CommonShare.getEmpId(this)>0) {
                    NOTIFICATION_ID = mainJson.getInt("GeneralId");
                    PendingIntent contentIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, new Intent(this, HomePage.class).putExtra("msg", mainJson.getJSONObject("TypeBody").toString()).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP), PendingIntent.FLAG_CANCEL_CURRENT);
                    NewMessageNotification.notify(this, mainJson.getString("GeneralName"), mainJson.getString("GeneralName"), NOTIFICATION_ID, contentIntent);
                }
                else
                {
                    NOTIFICATION_ID = mainJson.getInt("GeneralId");
                    PendingIntent contentIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, new Intent(this, CustomerActivity.class).putExtra("msg", mainJson.getJSONObject("TypeBody").toString()).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP), PendingIntent.FLAG_CANCEL_CURRENT);
                    NewMessageNotification.notify(this, mainJson.getString("GeneralName"), mainJson.getString("GeneralName"), NOTIFICATION_ID, contentIntent);

                }
            }
//
        else  if(Cate.equalsIgnoreCase("To Do")  )
        {
           // JSONObject jsonObject = new JSONObject(msg);
            Gson gson =new Gson();
            GsonTodo todo= gson.fromJson( mainJson.getJSONObject("TypeBody").toString(), GsonTodo.class);
            Realm     realm=Realm.getDefaultInstance();
            RealmResults<GsonTodo> tolist= realm.where(GsonTodo.class).equalTo("Token",todo.getToken()).findAll();
            if(tolist.isEmpty())
            {
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(todo);
                realm.commitTransaction();
            }
            else if (!tolist.isEmpty())
            {
                if(!tolist.first().getModified())
                {
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(todo);
                    realm.commitTransaction();
                }
            }


                NOTIFICATION_ID = mainJson.getInt("GeneralId");
                PendingIntent contentIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, new Intent(this, TaskDetails.class).putExtra("Token",todo.getToken()).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP), PendingIntent.FLAG_CANCEL_CURRENT);
                NewMessageNotification.notify(this, mainJson.getString("GeneralName"), mainJson.getString("GeneralName"), NOTIFICATION_ID, contentIntent);


         }
            else  if(Cate.equalsIgnoreCase("Ask Query")  )
            {
                // JSONObject jsonObject = new JSONObject(msg);
                Gson gson =new Gson();
                GsonAskQuery todo= gson.fromJson( mainJson.getJSONObject("TypeBody").toString(), GsonAskQuery.class);
                Realm     realm=Realm.getDefaultInstance();
                RealmResults<GsonAskQuery> tolist= realm.where(GsonAskQuery.class).equalTo("Token",todo.getToken()).findAll();
                if(tolist.isEmpty())
                {
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(todo);
                    realm.commitTransaction();
                }

                NOTIFICATION_ID = mainJson.getInt("GeneralId");

                PendingIntent contentIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, new Intent(this, CommunicationDetails.class)
                        .putExtra("Token",todo.getToken())
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP), PendingIntent.FLAG_CANCEL_CURRENT);
                NewMessageNotification.notify(this, mainJson.getString("GeneralName"), mainJson.getString("GeneralName"), NOTIFICATION_ID, contentIntent);


            }
        }
        catch (Exception ex)
        {
            Log.d("APG",ex.toString());
        }
    }


}
