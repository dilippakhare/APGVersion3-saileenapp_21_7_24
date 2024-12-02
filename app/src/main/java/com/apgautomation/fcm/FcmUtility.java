package com.apgautomation.fcm;

import android.content.Context;
import android.content.SharedPreferences;


import androidx.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;


import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.DeviceUuidFactory;
import com.apgautomation.utility.serverutility.netrequest.GetServerData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

/*
import bussinesslogic.DeviceUuidFactory;
import common.Common;
import netrequest.GetServerData;
*/


public class FcmUtility
{

    public void callProcedure(Context context)
    {
        FirebaseApp.initializeApp(context);

        checkPlayService(context);
        checkAndGenerate(context);
        if(isFcmKeyGenerated(context))
        {
            if(!isSendTOServer(context))
                 sendToServer(context);
        }
    }


    public  void  checkAndGenerate(Context context)
    {
        if(!isFcmKeyGenerated(context))
        {
            generateFcmId(context);
        }
    }

    public boolean isFcmKeyGenerated(Context context)
    {
        SharedPreferences prefgcm = context.getSharedPreferences("FCM",
                context.MODE_PRIVATE);
        final String gid = prefgcm.getString("fcmid", "no");
        if(gid== null || gid.equals("no"))
            return  false;
        return true;
    }
    public void generateFcmId(final Context  context)
    {
        FirebaseApp.initializeApp(context);
                FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task)
                    {
                        if (!task.isSuccessful())
                        {
                            Log.d("fcm", "getInstanceId failed", task.getException());
                            return;
                        }
                            String token = task.getResult().getToken();
                            Toast.makeText(context, "Subscibe complete", Toast.LENGTH_SHORT).show();
                            SharedPreferences prefgcm = context.getSharedPreferences(
                                    "FCM", context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefgcm.edit();
                            editor.putString("fcmid", token);
                            editor.commit();
                    }
                });
    }
    public String getStringKey(Context context)
    {
        SharedPreferences prefgcm = context.getSharedPreferences("FCM",
                context.MODE_PRIVATE);
         String gid = prefgcm.getString("fcmid", "no");
        return  gid;
    }

    public   boolean isSendTOServer(Context context)
    {

        SharedPreferences prefgcm = context.getSharedPreferences(
                "FCM", context.MODE_PRIVATE);
        String isOK= prefgcm.getString("isSend","");
        if(isOK.equalsIgnoreCase("OK"))
            return true;
        return  false;
    }
    public void sendToServer(final Context context)
    {
        Runnable r=new Runnable() {
            @Override
            public void run()
            {
                sendLogic(context);
            }
        };
        Thread t=new Thread(r);
        t.start();

    }
     void sendLogic(Context context)
     {
         if(CommonShare.getUserId(context)!=0)
         {

             String deviceId=new DeviceUuidFactory(context).getDeviceUuid().toString();
             String regid=getStringKey(context);
             Log.d("FCM","Generetaed");
             String url=CommonShare.url+"Service1.svc/Upd_UserMasterForGCMIdAndDeviceId?UserId="+CommonShare.getUserId(context)+"&GCMId="+regid+"&DeviceId="+deviceId;                    ;
             Log.d("FCM","Generetaed");

             try
             {
                 if (new GetServerData().downloadUrl(url).contains("Record Updated"))
                 {
                     SharedPreferences prefgcm = context.getSharedPreferences(
                             "FCM", context.MODE_PRIVATE);
                     prefgcm.edit().putString("isSend","OK").commit();
                 }
             }
             catch (Exception ex)
             {
             }
         }


     }

    public void clearSendToserver(Context context)
    {
        SharedPreferences prefgcm = context.getSharedPreferences(
                "FCM", context.MODE_PRIVATE);
      //  String isOK= prefgcm.getString("isSend","");
        prefgcm.edit().putString("isSend","").commit();
    }

    private void checkPlayService(Context context) {
        int isGPSAvailable = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);

      //  Toast.makeText(context,"isGPSAvailable " + isGPSAvailable,Toast.LENGTH_SHORT).show();

       if(isGPSAvailable!= ConnectionResult.SUCCESS)
       {
          // Common.alert(context,"Please The version of the Google Play services installed on this device");
       }

        switch (isGPSAvailable)
        {
            case ConnectionResult.API_UNAVAILABLE:
                //API is not available
                break;
            case ConnectionResult.NETWORK_ERROR:
                //Network error while connection
                break;
            case ConnectionResult.RESTRICTED_PROFILE:
                //Profile is restricted by google so can not be used for play services
                break;
            case ConnectionResult.SERVICE_MISSING:
                //service is missing
                break;
            case ConnectionResult.SIGN_IN_REQUIRED:
                //service available but user not signed in
                break;   case ConnectionResult.SERVICE_INVALID:
            //  The version of the Google Play services installed on this device is not authentic
            break;
            case ConnectionResult.SUCCESS:
                break;
        }
    }
}
