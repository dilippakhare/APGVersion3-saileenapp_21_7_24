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
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AppCompatActivity;
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
import com.apgautomation.utility.serverutility.AsyncUtilities;
import com.apgautomation.utility.serverutility.DownloadUtility;
import com.apgautomation.utility.serverutility.netrequest.ConnectionDetector;
import com.apgautomation.utility.serverutility.netrequest.FileUploadClass1;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Calendar;

import io.realm.Realm;

public class EditTask extends AppCompatActivity implements DownloadUtility {


    Button  btnAddEmp,SelectDate,btnAttchment,btnSave;
    EditText edTitle,edDesc;

    RadioButton rdlow,rdmedium,rdhigh;
    CheckBox chkIsRepeated;


    GsonTodo model;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit task");
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
                startActivityForResult(new Intent(EditTask.this, SelectEmployee.class),1);
            }
        });

        btnAttchment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChoice();
            }
        });
        btnAttchment.setVisibility(View.INVISIBLE);
        rdlow=findViewById(R.id.rdlow);
        rdhigh=findViewById(R.id.rdhigh);
        rdmedium=findViewById(R.id.rdmedium);
        chkIsRepeated=findViewById(R.id.chkIsRepeated);

        realm=Realm.getDefaultInstance();
        String token=getIntent().getExtras().getString("Token");
        model= realm.where(GsonTodo.class).equalTo("Token",token).findFirst();
        init();
    }
    void init()
    {
        edTitle.setText(model.getTitle());
        edDesc.setText(model.getDescription());
        empIds=model.getEmpids();
        SelectDate.setText(model.getDueDate());
        chkIsRepeated.setChecked(model.isRepeated());
        try
        {
           int selectedEmp=  Integer.parseInt( empIds.split(",")[1] );
           String anm=  CommonShare.empMap.get(selectedEmp).EmpName;
           btnAddEmp.setText(anm);
        }
        catch (Exception ex)
        {}

        if(model.getPriority().equalsIgnoreCase("High"))
        {
            rdhigh.setChecked(true);
        }
        if(model.getPriority().equalsIgnoreCase("Low"))
        {
            rdlow.setChecked(true);
        }
        if(model.getPriority().equalsIgnoreCase("Medium"))
        {
            rdmedium.setChecked(true);
        }
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
    private void saveChanges()
    {


                GsonTodo todo = realm .copyFromRealm(model);

                todo.setTitle(edTitle.getText().toString().trim());
                todo.setDescription(edDesc.getText().toString().trim());
                todo.setDeleteStatus(false);//.DeleteStatus=false;

                todo.setDueDateMillisecond(CommonShare.ddMMYYDateToLong(SelectDate.getText().toString().replace("/", "-")));
                todo.setDueDate(SelectDate.getText().toString());

                todo.setEmpids(empIds);

                todo.setModified(true);


                todo.setRepeated(chkIsRepeated.isChecked());
                if (rdmedium.isChecked())
                    todo.setPriority(rdmedium.getText().toString());

                if (rdhigh.isChecked())
                    todo.setPriority(rdhigh.getText().toString());

                if (rdlow.isChecked())
                    todo.setPriority(rdlow.getText().toString());


        String url = CommonShare.url + "Service1.svc/SaveTodoTask";


        Gson g=new Gson();
        String p= g.toJson(todo);
        AsyncUtilities utilities=new AsyncUtilities(this,true,url,p,130,this);
        utilities.execute();

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

        if(new ConnectionDetector(EditTask.this).isConnectingToInternet())
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
                                Intent intent = new Intent(EditTask.this, FilePicker.class);
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
            Toast.makeText(EditTask.this, ("Please check internet connection"), Toast.LENGTH_LONG).show();

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
              Toast.makeText(this,SelectEmployee.ids,Toast.LENGTH_LONG).show();

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

    @Override
    public void onComplete(String str, int requestCode, int responseCode) {
        if(responseCode==200)
        {
            startActivity(new Intent(this,TaskMgmt.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
