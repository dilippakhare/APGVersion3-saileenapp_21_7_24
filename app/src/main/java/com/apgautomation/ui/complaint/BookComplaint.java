package com.apgautomation.ui.complaint;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.apgautomation.CameraActivity;
import com.apgautomation.MainActivityLauncher;
import com.apgautomation.R;
import com.apgautomation.model.ComplaintModel;
import com.apgautomation.model.ComplaintTypeModel;
import com.apgautomation.model.GSONCustomerMasterBean;
import com.apgautomation.model.GsonCustomerProduct;
import com.apgautomation.model.InterfaceCustomerProduct;
import com.apgautomation.model.ServerModel.ServerCustomerProducts;
import com.apgautomation.model.database.ItemDAOComplaintModel;
import com.apgautomation.utility.BitmapUtilities;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.netrequest.ConnectionDetector;
import com.apgautomation.utility.syncutilities.ComplaintSyncUtility;
import com.otaliastudios.cameraview.BitmapCallback;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class BookComplaint extends AppCompatActivity implements View.OnClickListener {

    public static GSONCustomerMasterBean CustObject;
    EditText edReason;
    Button edEqupment;
    Spinner spnType;
    EditText edContactPerson,edContactNumber;
    ImageView img;

    String localFilePath;
   // String [] arr=new String[]{"Compressor","Reciever","Dryer"};
    ArrayList<ComplaintTypeModel> typeList;
    public static InterfaceCustomerProduct model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model=null;
        setContentView(R.layout.activity_book_complaint);
        img=findViewById(R.id.imgCamera);
        edEqupment=findViewById(R.id.edEqupment);
        edReason=findViewById(R.id.edReason);
        spnType=findViewById(R.id.spnType);
        edContactPerson=findViewById(R.id.edContactPerson);
        edContactNumber=findViewById(R.id.edContactNumber);

        if(CommonShare.getEmpId(this)==0) {
            try {
                edContactPerson.setText(CommonShare.getContactName(this));
                edContactNumber.setText(CommonShare.getContactNumber(this));
            } catch (Exception ex) {
            }
        }
        else
        {

        }

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Book Complaint");
        //CommonShare.alert(this,model.toString());

        typeList=CommonShare.getClientComplaintType(this);
        final ArrayAdapter adapter=new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,typeList);
        spnType.setAdapter(adapter);
        //edEqupment.setText(model.EquipmentName);
        edEqupment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(BookComplaint.this,SelectProducts.class),1);
            }
        });

        findViewById(R.id.btnSubmit).setOnClickListener(this);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert=new AlertDialog.Builder(BookComplaint.this);
                alert.setTitle("Select Option");
                String [] arr=new String[]{"Take Photo From Camera","Browse From Gallery"};
                alert.setItems(arr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                           if(i==1)
                           {
                               Intent inte = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                               startActivityForResult(inte, 301);

                           }
                           else if(i==0)
                           {

                               /*
                               Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                               File file = new File(getAccessPath() + File.separator + "img.jpg");
                               intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                               startActivityForResult(intent, 302);*/
                               startActivityForResult(new Intent(BookComplaint.this, CameraActivity.class), 302);

                           }

                    }
                });
                alert.setPositiveButton("Cancel",null);
                alert.show();
            }
        });
    }
    String getAccessPath()
    {
        String root = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString();
        Log.d("ROOT", root);
        File myDir = new File(root +"/"+ getString(R.string.app_name).replace(" ",""));
        myDir.mkdirs();

        return    myDir.getAbsoluteFile().toString();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

          if(!new ConnectionDetector(this).isConnectingToInternet())
          {
              Toast.makeText(this,"No Internet Connection",Toast.LENGTH_LONG).show();
              return;
          }
        if( edContactNumber.getText().toString().trim().equalsIgnoreCase(""))
        {
            Toast.makeText(this,"Please Enter Your Contact Number",Toast.LENGTH_LONG).show();

        }
        else  if( edContactPerson.getText().toString().trim().equalsIgnoreCase(""))
        {
            Toast.makeText(this,"Please Enter Your Name",Toast.LENGTH_LONG).show();

        }
       else if( edReason.getText().toString().trim().equalsIgnoreCase(""))
       {
           Toast.makeText(this,"Please Enter Your Complaint",Toast.LENGTH_LONG).show();

       }
       else
       {
           AlertDialog.Builder alert=new AlertDialog.Builder(this);
           alert.setTitle("Book Complaint");
           alert.setMessage("Do you want to book complaint ?");
           alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialogInterface, int i) {
                   book();
               }
           });
           alert.setNegativeButton("No",null);
           alert.show();

       }
    }
    void book()
    {
        ComplaintModel cModel=new ComplaintModel();
        cModel.ComplaintDetails=edReason.getText().toString();
        if(CustObject==null)
        {
            cModel.CustomerId = CommonShare.getCustomerId(this);
            cModel.IsBookedByClient=true+"";
        }
        else
        {
            cModel.CustomerId = CustObject.getCustomerId();
            cModel.IsBookedByClient=false+"";
        }
        cModel.ComplaintType=spnType.getSelectedItem().toString();
        try {
            cModel.SelectedEquipment = spnType.getSelectedItem().toString();
            try {
                ServerCustomerProducts scp= (ServerCustomerProducts) model;
                cModel.SerialNumber = scp.SerialNumber;
                cModel.EquipmentName = scp.EquipmentName;
                cModel.EquipmentType = scp.EquipmentType;
                cModel.ModelId = scp.ModelId;
                cModel.ModelName = scp.ModelName;
                cModel.CustomerId = scp.CustomerId;
                cModel.PortNo = scp.PortNo;
                cModel.RecId = scp.RecId;
            }
            catch (Exception ex)
            {}

            try {
                GsonCustomerProduct scp= (GsonCustomerProduct) model;
                cModel.SerialNumber = scp.   getSerialNumber();
                cModel.EquipmentName = scp.getEquipmentName();
                cModel.EquipmentType = scp.getEquipmentType();
                cModel.ModelId =(int) scp.getModelId();
                cModel.ModelName = scp.getModelName();
                cModel.CustomerId = scp.getCustomerId();
                cModel.PortNo = scp.getPortNo();
                cModel.RecId = scp.getRecId();
            }
            catch (Exception ex)
            {}

            cModel.Localpath=localFilePath;
        }
        catch (Exception ex)
        {}

        cModel.EnterBy=CommonShare.getUserId(this)+"";
        cModel.Token=CommonShare.generateToken(this);
        cModel.EnterDate=System.currentTimeMillis();
        cModel.ContactNumber=edContactNumber.getText().toString();
        cModel.ContactPerson=edContactPerson.getText().toString();

        ItemDAOComplaintModel itemDAOComplaintModel=new ItemDAOComplaintModel(this);
        long ln= itemDAOComplaintModel.insertRecord(cModel);
        //CommonShare.alert(this,ln+"");
        ComplaintSyncUtility utility=new ComplaintSyncUtility(this);
        recurrsive();
      //  finish();
      //  Toast.makeText(this,"Complaint Book Successfully",Toast.LENGTH_LONG).show();
       AlertDialog.Builder alert=new AlertDialog.Builder(this);
       alert.setTitle("Complaint Book Successfully");
       alert.setMessage(getString(R.string.app_name)+ " will take attention to your complaint and reply to you shortly ");
       alert.setCancelable(false);
       alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialogInterface, int i) {
               finish();
           }
       });
       alert.show();

    }
    int threadCnt=0;
    void recurrsive()
    {
        threadCnt=0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (threadCnt<5) {
                    try {
                        Thread.sleep(5000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    ComplaintSyncUtility utility = new ComplaintSyncUtility(BookComplaint.this);
                                } catch (Exception ex) {
                                }
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    threadCnt++;
                }
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&& resultCode==RESULT_OK)
        {
            if(model instanceof ServerCustomerProducts)
              edEqupment.setText( ((ServerCustomerProducts)  model).EquipmentName);
            else
                edEqupment.setText( ((GsonCustomerProduct)  model).getEquipmentName());
        }
        if(requestCode==301)
        {

            try {
                Uri uri = data.getData();
                String path = getPath(this, uri);
                File file = new File(path);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);

                int width = bitmap.getWidth();
                int height = bitmap.getHeight();

                Log.e("Width height", width + "   " + height);

                float constant = ((float) (600 / (float) width));
                width = 600;
                Log.e("Contatnt", constant + "");
                float m = ((float) height) * constant;
                height = (int) m;
                Log.e("new width heith ", width + "   " + height);
                
                Bitmap thePic = ThumbnailUtils.extractThumbnail(bitmap, width, height);
                final File bitmapFile = BitmapUtilities.saveToExtenal(thePic, this);
                localFilePath=bitmapFile.getAbsolutePath();
                img.setImageBitmap(thePic);
            }
            catch (Exception exz)
            {}
        }
        if(requestCode==302)
        {
            if(MainActivityLauncher.pictureResult!=null)
            {
                MainActivityLauncher.pictureResult.toBitmap(1000, 1000, new BitmapCallback() {

                    @Override
                    public void onBitmapReady(@Nullable Bitmap bitmap)
                    {
                        int width = bitmap.getWidth();
                        int height = bitmap.getHeight();
                        Log.e("Width height", width + "   " + height);
                        float constant = ((float) (700 / (float) width));
                        width = 700;
                        Log.e("Contatnt", constant + "");
                        float m = ((float) height) * constant;
                        height = (int) m;
                        Log.e("new width heith ", width + "   " + height);
                        Bitmap thePic = ThumbnailUtils.extractThumbnail(bitmap, width, height);
                        final File bitmapFile = BitmapUtilities.saveToExtenal(thePic, BookComplaint.this);
                        localFilePath = bitmapFile.getAbsolutePath();
                        img.setImageBitmap(thePic);
                    }
                });


            }

        }
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


    @Override
    public void finish() {
        super.finish();
        CustObject=null;
    }
}
