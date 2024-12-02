package com.apgautomation.utility.serverutility;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;


/**
 * Created by bt18 on 08/08/2016.
 */
public class AsyncParsingUtilities extends AsyncTask<Void,Void,String>
{
    DownloadUtility objDownloadUtility;
    ConnectServer connectServer;
    Context context;
    boolean isPost;
    int requestCode;
    String url,paylod;
    ProgressDialog pd;
    ResponseBody responseBody;
    boolean progressVisibility=true;
    ParsingUtilities parsingUtilities;
    private boolean cancableDialoge=false;

    public AsyncParsingUtilities(Context context, boolean isPost, String url, String paylod,
                                 int requestCode, DownloadUtility objDownloadUtility,ParsingUtilities parsingUtilities)
    {
        this.context=context;
        this.url=url;
        this.isPost=isPost;
        this.paylod=paylod;
        connectServer=new ConnectServer();
        this.objDownloadUtility=objDownloadUtility;
        this.requestCode=requestCode;
        this.parsingUtilities=parsingUtilities;
    }

    public void setProgressDialoaugeVisibility(boolean visibility)
    {
             progressVisibility=visibility;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(progressVisibility)
        {
            pd = new ProgressDialog(context);
            pd.setMessage(("Loading")+"...");

            pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    AsyncParsingUtilities.this.cancel(true);
                    connectServer.closeConnection();
                }
            });

            pd.setCancelable(cancableDialoge);
            pd.show();
        }
    }

    @Override
    protected String doInBackground(Void... params)
    {

        String str="";
        if(isPost)
        {
           try
           {
                 responseBody=connectServer.performPostCallJson(url, paylod);
                         //connectServer.performPostCallJsonForStream(url, paylod);

             // if(responseBody!=null)
                // str=responseBody.getResponseString();
               parsingUtilities.parseStreamingData(responseBody.stream);
           }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            try
            {

                responseBody= connectServer.getDataSteram(url);
               // String  result=new GetServerData().downloadUrl(url);
              /*  {
                    if (responseBody == null || responseBody.stream == null) {
                        responseBody = new ResponseBody(400, "");
                    } else {
                        responseBody = new ResponseBody(200, "");

                    }
                }*/
              // ModuleSync sync=new ModuleSync(context);
                        //(ModuleSync) objDownloadUtility;
                //sync.parseStream(responseBody.stream);
                parsingUtilities.parseStreamingData(responseBody.stream);

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
        try
        {
            pd.dismiss();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        objDownloadUtility.onComplete(responseBody.getResponseString(),requestCode,responseBody.responseCode);
    }


    public void setCancableDialoge(boolean cancableDialoge)
    {
        this.cancableDialoge = cancableDialoge;
    }


}
