package com.apgautomation.utility.serverutility;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;


/**
 * Created by bt18 on 08/08/2016.
 */
public class AsyncUtilities extends AsyncTask<Void,Void,String>
{
    DownloadUtility objDownloadUtility;
    ConnectServer connectServer;
    Context context;
    boolean isPost;
    int requestCode;
    String url,paylod;
    ProgressDialog pd;
    ResponseBody responseBody;
    boolean autoCanclable=true;
    public void setAutoCancleable(boolean autoCanclable)
    {
         this.autoCanclable=autoCanclable;
    }

    public AsyncUtilities(Context context,boolean isPost,String url,String paylod,
                          int requestCode,DownloadUtility objDownloadUtility)
    {
        this.context=context;
        this.url=url;
        this.isPost=isPost;
        this.paylod=paylod;
        connectServer=new ConnectServer();
        this.objDownloadUtility=objDownloadUtility;
        this.requestCode=requestCode;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd=new ProgressDialog(context);
        pd.setMessage(("Loading")+"...");
        pd.setCanceledOnTouchOutside(autoCanclable);

        if(!hideProgressDialoge)
           pd.show();

        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                try {
                    AsyncUtilities.this.cancel(true);
                    connectServer.closeConnection();
                }
                catch (Exception e)
                {
                  Log.e("OMI" , e.toString());
                }
            }
        });
        String ssssss="";
        //Common.alert(context,url+"-*****-"+paylod);

    }

    @Override
    protected String doInBackground(Void... params)
    {

        String str="";
        if(isPost)
        {
           try
           {


                responseBody= connectServer.performPostCallJson(url, paylod);
             // if(responseBody!=null)
                // str=responseBody.getResponseString();
           }
            catch (Exception e){}
        }
        else
        {
            try {
                responseBody= connectServer.getData(url);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s)
    {
        super.onPostExecute(s);
        //Common.alert(context,responseBody.responseString+"-"+responseBody.responseCode);
        try {
            if(pd.isShowing())
            pd.dismiss();
          //  Common.alert(context,s);
            objDownloadUtility.onComplete(responseBody.getResponseString(),requestCode,responseBody.responseCode);
        }
        catch (Exception e)
        {
            Log.e("Log",""+e.toString());
        }

    }

  boolean hideProgressDialoge=false;
    public void hideProgressDialoge() {
        hideProgressDialoge=true;
    }
}
