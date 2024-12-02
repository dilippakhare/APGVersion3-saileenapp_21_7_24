package com.apgautomation.ui.notifications;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.apgautomation.FilePicker;
import com.apgautomation.R;
import com.apgautomation.ui.customer.SelectGroup;
import com.apgautomation.utility.BitmapUtilities;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.AsyncUtilities;
import com.apgautomation.utility.serverutility.DownloadUtility;
import com.apgautomation.utility.serverutility.netrequest.ConnectionDetector;
import com.apgautomation.utility.serverutility.netrequest.FileUploadClass1;

import org.json.JSONObject;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Calendar;

public class CreateNotice extends AppCompatActivity implements DownloadUtility {

    EditText edTitle,edDescription;
    Spinner spinnerResponsible;
    Button btnBrowse,btnDate,btnSubmit;
    RadioButton rd1,rd2,rd3,rd4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notice);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Notice");

        edTitle=findViewById(R.id.edTitle);
        edDescription=findViewById(R.id.edDescription);
        spinnerResponsible=findViewById(R.id.spinnerResponsible);
        btnBrowse=findViewById(R.id.btnBrowse);
        btnDate=findViewById(R.id.btnDate);
        btnSubmit=findViewById(R.id.btnSubmit);
        rd1=findViewById(R.id.rd1);
        rd2=findViewById(R.id.rd2);
        rd3=findViewById(R.id.rd3);
        rd4=findViewById(R.id.rd4);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
        btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                            showChoice();
            }
        });
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment(btnDate);
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
        rd3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    startActivityForResult(new Intent(CreateNotice.this,SelectEmployee.class),900);
                }
            }
        });
        rd4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    startActivityForResult(new Intent(CreateNotice.this,SelectGroup.class),901);
                }
            }
        });
    }

    void save()
    {
        if(edTitle.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Notice Title Required", Toast.LENGTH_SHORT).show();
            return;
        }
        if(edDescription.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Description Required", Toast.LENGTH_SHORT).show();
            return;
        }
        if(btnDate.getText().toString().equalsIgnoreCase("Select Date"))
        {
            Toast.makeText(this, "Expiry Date Required", Toast.LENGTH_SHORT).show();
            return;
        }
        try
        {
          JSONObject j=new JSONObject();
            j.put("Topic",edTitle.getText().toString());
            j.put("Details",edDescription.getText().toString());
            j.put("Attachment",serverPath);

            j.put("ExpiryDate",CommonShare.ddMMYYDateToLong(btnDate.getText().toString().replace("/","-")));
            j.put("EnterBy",CommonShare.getUserId(this));

            j.put("DisplayToAll",rd1.isChecked());
            j.put("DisplayToCustomer",rd2.isChecked());

            if(rd3.isChecked() || rd4.isChecked())
            {
                if(rd3.isChecked())
                {
                    j.put("DisplayToSpecific",rd3.isChecked());
                    j.put("EmpIds",SelectEmployee.ids);
                }
                if(rd4.isChecked())
                {
                    j.put("EmpIds",","+ SelectGroup.selectedGroup.getGroupId()+",");
                }
            }

            AsyncUtilities utilities=new AsyncUtilities(this,true,CommonShare.url+"Service1.svc/CreateNotice",j.toString(),1,this);
            utilities.execute();

        }
        catch (Exception ex)
        {}

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode) {

        if(requestCode==1 && responseCode==200)
        {
            Toast.makeText(this, "Notice Created", Toast.LENGTH_SHORT).show();
            finish();

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


   int  FILE_SELECT_CODE=301;
    void showChoice()
    {

        if(new ConnectionDetector(CreateNotice.this).isConnectingToInternet())
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
                                Intent intent = new Intent(CreateNotice.this, FilePicker.class);
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
            Toast.makeText(CreateNotice.this, ("Please check internet connection"), Toast.LENGTH_LONG).show();

    }


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
                btnBrowse.setText(localPath);
                btnBrowse.setBackgroundColor(Color.rgb(0,222,0));
            }
        }
    }
}
