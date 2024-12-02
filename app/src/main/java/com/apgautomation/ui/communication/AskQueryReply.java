package com.apgautomation.ui.communication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apgautomation.FilePicker;
import com.apgautomation.R;
import com.apgautomation.model.EmployeeModel;
import com.apgautomation.model.GsonAskQuery;
import com.apgautomation.utility.BitmapUtilities;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.netrequest.ConnectionDetector;
import com.apgautomation.utility.serverutility.netrequest.FileUploadClass1;
import com.apgautomation.utility.syncutilities.VisitSyncUtility;

import org.json.JSONObject;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;

import io.realm.Realm;

public class AskQueryReply extends AppCompatActivity implements View.OnClickListener {

    Button btnAttachment,btnSubmit;
    EditText ed;
    Spinner spinerUser;
    String lasttoken;
    GsonAskQuery lastModel;
    ArrayList<EmployeeModel>  list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_query);
        lasttoken=getIntent().getExtras().getString("lasttoken");
        Realm realm=Realm.getDefaultInstance();
        lastModel= realm.where(GsonAskQuery.class).equalTo("Token",lasttoken).findFirst();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Ask Query");
        EmployeeModel m=new EmployeeModel();
        m.EmpId=lastModel.getEnterById();
        m.EmpName=lastModel.getEnterByName();
        list.add(m);
        spinerUser=findViewById(R.id.spinerUser);
        ed=findViewById(R.id.ed);
        btnAttachment=findViewById(R.id.btnAttachment);
        btnSubmit=findViewById(R.id.btnSubmit);

        btnAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChoice();
            }
        });

        btnSubmit.setOnClickListener(this);



        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,list);
        spinerUser.setAdapter(adapter);

        TextView t=findViewById(R.id.lable);
        t.setText("Reply");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    void showChoice()
    {

        if(new ConnectionDetector(AskQueryReply.this).isConnectingToInternet())
        {

            AlertDialog.Builder alert=new AlertDialog.Builder(this);
            alert.setTitle("Select Attachment From");
            String[] arr=  new String[]  {"Image","Video","Audio","File"};
            alert.setSingleChoiceItems( arr, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if(which==1)
                            {
                                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(i, FILE_SELECT_CODE);
                            }
                            else  if(which==0)
                            {
                                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(i, FILE_SELECT_CODE);
                            }
                            else if(which==2)
                            {
                                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(i, FILE_SELECT_CODE);
                            }
                            else if(which==3)
                            {
                            /*Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                           // intent.setType("* / *");
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            startActivityForResult(intent, 26);*/
                                Intent intent = new Intent(AskQueryReply.this, FilePicker.class);
                                startActivityForResult(intent, 26);
                            }
                            dialog.dismiss();
                        }
                    }
            );
            alert.show();
            ///  showFileChooser();
        }
        else
            Toast.makeText(AskQueryReply.this, ("Please check internet connection"), Toast.LENGTH_LONG).show();

    }


    int  FILE_SELECT_CODE=301;
    String localPath="";
    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==26 && data.hasExtra(FilePicker.EXTRA_FILE_PATH))
        {
            File selectedFile = new File
                    (data.getStringExtra(FilePicker.EXTRA_FILE_PATH));
            localPath=selectedFile.getAbsolutePath();
            filePre();
            // perform(path);
        }
        if ( requestCode==FILE_SELECT_CODE &&   resultCode == RESULT_OK)
        {
            try
            {
                Uri uri = data.getData();
                localPath = getPath(this, uri);
                File file = new File(localPath);
                if(isImageFile(file)) {
                    localPath = CompressImage(localPath);
                }
                filePre();
            }
            catch(Exception e)
            {}
        }

        if(requestCode==900  && resultCode==RESULT_OK)
        {

        }
        if(requestCode==901  && resultCode==RESULT_OK)
        {

        }

    }
    private boolean isImageFile(File file)
    {
        String[] okFileExtensions =  new String[] {"jpg", "png", "gif","jpeg"};
        for (String extension : okFileExtensions)
        {
            if (file.getName().toLowerCase().endsWith(extension))
            {
                return true;
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    private String CompressImage(String path) {

        File file = new File(path);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);

        //int numBytesByRow = bitmap.getRowBytes() * bitmap.getHeight();
        int file_size = Integer.parseInt(String.valueOf(file.length()/1024));
        //int numBytesByCount = bitmap.getByteCount();

        if(file_size>(550)) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            float constant = ((float) (1000f / (float) width));
            width = 1000;
            float m = ((float) height) * constant;
            height = (int) m;

            Bitmap thePic = ThumbnailUtils.extractThumbnail(bitmap, width, height);
            File bitmapFile = BitmapUtilities.saveToExtenal(thePic, this);
            int file_ = Integer.parseInt(String.valueOf(bitmapFile.length()/1024));
            return bitmapFile.getAbsolutePath();
        }
        return file.getAbsolutePath();
    }


    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    String serverPath="";
    ProgressDialog pd;
    void filePre()
    {
        pd=new ProgressDialog(this);
        pd.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                uploadFile();
            }
        }).start();

    }
    void uploadFile()
    {

        try {
            FileUploadClass1 uploadClass1 = new FileUploadClass1();
            String str=  uploadClass1.checkinstream("", localPath, CommonShare.url + "UploadService.svc/UploadImage", 1, "notice");
            JSONObject j=new JSONObject(str);
            serverPath=j.getString("serverpath");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    filePost();
                }
            });
        }
        catch (Exception ex) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    filePost();
                }
            });
        }
    }

    void filePost()
    {
        if(pd.isShowing())
            pd.dismiss();
        if(serverPath!=null)
        {
            if(serverPath.length()>5)
            {
                btnAttachment.setText(localPath);
                btnAttachment.setBackgroundColor(Color.rgb(0,222,0));
            }
        }
    }

    @Override
    public void onClick(View view) {
        if(ed.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Enter your query", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!new ConnectionDetector(this).isConnectingToInternet())
        {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
            return;
        }


        GsonAskQuery query=new GsonAskQuery();
        query.Attachment=serverPath;
        query.DeleteStatus=false;
        query.EnterById=CommonShare.getEmpId(this);
        query.EnterByName=CommonShare.getMyEmployeeModel(this).EmpName;
        query.EnterDate=CommonShare.getDateTime(System.currentTimeMillis());
        query.EnterDateMillisecond=System.currentTimeMillis();
        query.IsModified=true;
        query.IsParentMsg=false;
        query.IsReed=false;
        query.ToId=((EmployeeModel)spinerUser.getSelectedItem()).EmpId;
        query.Msg="Reply:- "+ed.getText().toString();
        query.ToName=((EmployeeModel)spinerUser.getSelectedItem()).EmpName;
        query.Token=CommonShare.generateToken(this);
        query.RefferenceToken=lastModel.getToken();

        Realm realm=Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(query);
        realm.commitTransaction();
        realm.close();
        VisitSyncUtility utility=new VisitSyncUtility(this);
        utility.sync();
        startActivity(new Intent(this, Communication.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
}
