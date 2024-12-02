package com.apgautomation.ui.taskmgmt.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.apgautomation.FilePicker;
import com.apgautomation.R;
import com.apgautomation.model.GsonTodo;
import com.apgautomation.ui.notifications.SelectEmployee;
import com.apgautomation.utility.BitmapUtilities;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.netrequest.ConnectionDetector;
import com.apgautomation.utility.serverutility.netrequest.FileUploadClass1;
import com.apgautomation.utility.syncutilities.VisitSyncUtility;

import org.json.JSONObject;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Calendar;

import io.realm.Realm;

public class CreateTask extends AppCompatActivity {


    Button  btnAddEmp,SelectDate,btnAttchment,btnSave;
    EditText edTitle,edDesc;

    RadioButton rdlow,rdmedium,rdhigh;
    CheckBox chkIsRepeated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create task");
        btnAddEmp=findViewById(R.id.btnAddEmp);
        SelectDate=findViewById(R.id.SelectDate);
        btnAttchment=findViewById(R.id.btnAttchment);
        edTitle=findViewById(R.id.edTitle);
        edDesc=findViewById(R.id.edDesc);
        linearAssignto=findViewById(R.id.linearAssignto);
        SelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment(SelectDate);
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        btnSave=findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validation())
                {
                    saveChanges();
                }

            }
        });

        btnAddEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(CreateTask.this, SelectEmployee.class),1);
            }
        });

        btnAttchment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChoice();
            }
        });

        rdlow=findViewById(R.id.rdlow);
        rdhigh=findViewById(R.id.rdhigh);
        rdmedium=findViewById(R.id.rdmedium);
        chkIsRepeated=findViewById(R.id.chkIsRepeated);

    }

    private void saveChanges()
    {
        GsonTodo todo=new GsonTodo();
        todo.Attchment=serverPath;
        todo.Token=CommonShare.generateToken(this);
        todo.EnterById=CommonShare.getEmpId(this);
        todo.EnterByName=CommonShare.getMyEmployeeModel(this).EmpName;
        todo.EnterDateMillisecond=System.currentTimeMillis();
        todo.EnterDate=CommonShare.getDateTime(System.currentTimeMillis());
        todo.Title=edTitle.getText().toString().trim();
        todo.Description=edDesc.getText().toString().trim();
        todo.DeleteStatus=false;

        todo.DueDateMillisecond=CommonShare.ddMMYYDateToLong(SelectDate.getText().toString().replace("/","-"));
        todo.DueDate=SelectDate.getText().toString();

        todo.empids=empIds;
        todo.Status="Open";
        todo.setModified(true);

        todo.ReDueDate=todo.DueDate;
        todo.ReDueDateMillisecond=todo.DueDateMillisecond;


        todo.IsRepeated=chkIsRepeated.isChecked();
        if(rdmedium.isChecked())
            todo.setPriority(rdmedium.getText().toString());

        if(rdhigh.isChecked())
            todo.setPriority(rdhigh.getText().toString());

        if(rdlow.isChecked())
            todo.setPriority(rdlow.getText().toString());

        Realm    realm= Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(todo);
        realm.commitTransaction();
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        finish();
        VisitSyncUtility utility=new VisitSyncUtility(this);
        utility.sync();
        startActivity(new Intent(this,TaskMgmt.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    private boolean validation() {
        if(!new ConnectionDetector(this).isConnectingToInternet())
        {
            Toast.makeText(this,"No Internet",Toast.LENGTH_LONG).show();
            return false;
        }
        if(edTitle.getText().toString().trim().equalsIgnoreCase(""))
        {
            Toast.makeText(this,"Title Required",Toast.LENGTH_LONG).show();
            return false;
        }
        if(SelectDate.getText().toString().trim().equalsIgnoreCase("Select Date"))
        {
            Toast.makeText(this,"Due Date Required",Toast.LENGTH_LONG).show();
            return false;
        }

        if(linearAssignto.getChildCount()==1)
        {
            Toast.makeText(this,"Please Assign Task",Toast.LENGTH_LONG).show();
            return false;
        }

        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }


    int  FILE_SELECT_CODE=301;
    void showChoice()
    {

        if(new ConnectionDetector(CreateTask.this).isConnectingToInternet())
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
                                Intent intent = new Intent(CreateTask.this, FilePicker.class);
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
            Toast.makeText(CreateTask.this, ("Please check internet connection"), Toast.LENGTH_LONG).show();

    }

    String empIds="";
    LinearLayout linearAssignto;
    String localPath="";
    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1  && resultCode==RESULT_OK)
        {
              empIds=SelectEmployee.ids;
              //Toast.makeText(this,SelectEmployee.ids,Toast.LENGTH_LONG).show();

              try
              {
                  linearAssignto.removeViewAt(1); linearAssignto.removeViewAt(1); linearAssignto.removeViewAt(1); linearAssignto.removeViewAt(1);
                  linearAssignto.removeViewAt(1); linearAssignto.removeViewAt(1); linearAssignto.removeViewAt(1); linearAssignto.removeViewAt(1);
                  linearAssignto.removeViewAt(1); linearAssignto.removeViewAt(1); linearAssignto.removeViewAt(1); linearAssignto.removeViewAt(1);
                  linearAssignto.removeViewAt(1); linearAssignto.removeViewAt(1); linearAssignto.removeViewAt(1); linearAssignto.removeViewAt(1);
                  linearAssignto.removeViewAt(1); linearAssignto.removeViewAt(1); linearAssignto.removeViewAt(1); linearAssignto.removeViewAt(1);
                  linearAssignto.removeViewAt(1); linearAssignto.removeViewAt(1); linearAssignto.removeViewAt(1); linearAssignto.removeViewAt(1);
                  linearAssignto.removeViewAt(1); linearAssignto.removeViewAt(1); linearAssignto.removeViewAt(1); linearAssignto.removeViewAt(1);
                  linearAssignto.removeViewAt(1); linearAssignto.removeViewAt(1); linearAssignto.removeViewAt(1); linearAssignto.removeViewAt(1);
                  linearAssignto.removeViewAt(1); linearAssignto.removeViewAt(1); linearAssignto.removeViewAt(1); linearAssignto.removeViewAt(1);
                  linearAssignto.removeViewAt(1); linearAssignto.removeViewAt(1); linearAssignto.removeViewAt(1); linearAssignto.removeViewAt(1);
                  linearAssignto.removeViewAt(1); linearAssignto.removeViewAt(1); linearAssignto.removeViewAt(1); linearAssignto.removeViewAt(1);

              }
              catch (Exception ex)
              {}
              try
              {
                  for(int i=1;i<empIds.split(",").length;i++)
                  {
                      TextView textView=new TextView(this);
                      textView.setTag( CommonShare.empMap.get(Integer.parseInt(empIds.split(",")[i])));
                      textView.setText(   CommonShare.empMap.get(Integer.parseInt(empIds.split(",")[i])).EmpName  );
                      linearAssignto.addView(textView);
                  }
              }
              catch (Exception ex)
              {
                  Log.d("Apg",ex.toString());
              }
        }
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
                btnAttchment.setText(localPath);
                btnAttchment.setBackgroundColor(Color.rgb(0,222,0));
            }
        }
    }
    //***************************DatePicker*****************************************************

    @SuppressLint("ValidFragment")
    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        Button  res;

        DatePickerFragment(Button res) {
            this.res = res;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }


        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            Button ed = res;//(Button) getActivity().findViewById(res);
            String dayStr=day+"";
            month=month+1;
            String monthStr=month+"";
            if(dayStr.length()==0)
                dayStr=0+dayStr;
            if(monthStr.length()==0)
                monthStr=0+monthStr;
            ed.setText(dayStr + "/" + monthStr + "/" + year);
        }

    }
}
