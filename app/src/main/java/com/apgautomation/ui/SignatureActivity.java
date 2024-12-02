package com.apgautomation.ui;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import com.apgautomation.model.GsonVisitFsr;
import com.apgautomation.utility.BitmapUtilities;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.ConnectServer;
import com.apgautomation.utility.serverutility.netrequest.FileUploadClass1;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.apgautomation.R;

import org.json.JSONObject;

import java.io.File;

import io.realm.Realm;

public class SignatureActivity extends AppCompatActivity {
    com.github.gcacace.signaturepad.views.SignaturePad signature_pad;
    ImageView img;
    public static String serverpath;
    public static int FsrId=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        serverpath ="";
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Signature saved", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                img.setImageBitmap( signature_pad.getSignatureBitmap());
                FileUploadAsync uploadAsync=new FileUploadAsync();
                uploadAsync.execute();
            }
        });
        findViewById(R.id.fab1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signature_pad.clear();;
                img.setImageBitmap( signature_pad.getSignatureBitmap());
            }
        });

        signature_pad=findViewById(R.id.signature_pad);
        img=findViewById(R.id.img);


    }


    String saveSign()
    {
        final File bitmapFile = BitmapUtilities.saveToExtenal(signature_pad.getSignatureBitmap(), SignatureActivity.this);
        String localFilePath = bitmapFile.getAbsolutePath();
        try {
            FileUploadClass1 f = new FileUploadClass1();
            String res = f.checkinstream("", localFilePath, CommonShare.url + "UploadService.svc/UploadImage", 1, "customersign");
            JSONObject j = new JSONObject(res);
            serverpath =  j.getString("serverpath");

            String url= CommonShare.url+"Service1.svc/updateCustomerSign?RecId="+FsrId+"&path="+serverpath;
            ConnectServer c=new ConnectServer();
            String str=   c.sendGet(url);

            if(str.contains("Success") )
            {
            return  "Success";
            }
            return  "Fail";
        }
        catch (Exception ex){}
        return "Fail";
    }

    class  FileUploadAsync extends AsyncTask
    {
       ProgressDialog pd;
       String s="";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(SignatureActivity.this);
            pd.setTitle("Saving Signature....");
            pd.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
          s=  saveSign();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            pd.dismiss();
            if(s.equalsIgnoreCase("Success")){

                Realm realm=Realm.getDefaultInstance();
                GsonVisitFsr cp= realm.where(GsonVisitFsr.class).equalTo("RecId",FsrId).findFirst();


                realm.beginTransaction();
                cp.setFSRAttachment(serverpath);
                realm.copyToRealmOrUpdate(cp);
                realm.commitTransaction();
                Toast.makeText(SignatureActivity.this, "Signature Saved", Toast.LENGTH_SHORT).show();
                finish();
            }
            else
            {
                CommonShare.alert(SignatureActivity.this,"Unable to save signature");
            }

        }
    }
}