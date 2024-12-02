package com.apgautomation.ui.quotation;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.apgautomation.CameraActivity;
import com.apgautomation.HomePage;
import com.apgautomation.MainActivityLauncher;
import com.apgautomation.R;
import com.apgautomation.model.EmployeeModel;
import com.apgautomation.model.GsonGroup;
import com.apgautomation.model.GsonQuatationRequestModel;
import com.apgautomation.ui.customer.SelectGroup;
import com.apgautomation.utility.BitmapUtilities;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.netrequest.ConnectionDetector;
import com.otaliastudios.cameraview.BitmapCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import io.realm.Realm;

public class GenrateQuotationRequest extends AppCompatActivity {

    HashMap<Integer,ImageView> map=new HashMap<>();
    LinearLayout lQuation;
    ArrayList<EmployeeModel> empList;
    static  int GroupId;
    ArrayList<GsonQuatationRequestModel> requestList=new ArrayList<>();
    Button btnSelectGroup;
    Button butnSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genrate_quotation_request);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Quotation Request");
        lQuation=findViewById(R.id.lQuation);
        empList=  CommonShare.getQEmployeeList(this);
        AddQuotaion(GroupId);
        btnSelectGroup=findViewById(R.id.btnSelectGroup);
        btnSelectGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(GenrateQuotationRequest.this, SelectGroup.class).putExtra("IsSales",true),1);
            }
        });
        butnSubmit=findViewById(R.id.butnSubmit);
        butnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                          saveRecord();
            }
        });
    }
    void saveRecord()
    {
        if(group==null)
        {

            Toast.makeText(this, "Please Select Group", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!new ConnectionDetector(this).isConnectingToInternet())
        {
            Toast.makeText(this, "Internet Connection Not available", Toast.LENGTH_SHORT).show();
            return;
        }
        if(requestList.size()>0)
        {
            for (GsonQuatationRequestModel qModel:requestList)
            {
                if(qModel.getLocalpath()==null || qModel.getLocalpath().length()<5)
                {
                    Toast.makeText(this, "Quotation Photo Required", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }

        for (int i = 0; i < lQuation.getChildCount(); i++) {

            View v = lQuation.getChildAt(i);
            EditText edRemark = v.findViewById(R.id.edRemark);
            EditText edNumber = v.findViewById(R.id.edNumber);
            Spinner spAssignTo=v.findViewById(R.id.spAssignTo);
            GsonQuatationRequestModel m = (GsonQuatationRequestModel) v.getTag();
            m.setRemark(edRemark.getText().toString());
            m.setEnterByEMpId(CommonShare.getEmpId(this));
            m.setEnterDateMillisecond(System.currentTimeMillis());
            m.AssignToEmpId=empList.get(spAssignTo.getSelectedItemPosition()).EmpId;
            m.GroupId=group.getGroupId();

            try
            {
             //   m.setQuotatationNo(Integer.parseInt(edNumber.getText().toString()));
            }
            catch (Exception ex){}

        }
        Realm realm=Realm.getDefaultInstance();
        for (GsonQuatationRequestModel obj :requestList )
        {
            obj.setModified(true);
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(obj);
            realm.commitTransaction();
        }

        Toast.makeText(this, "Quotation Saved", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, HomePage.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    int startpont=500;
    void AddQuotaion(int GroupId)
    {
        startpont++;
        int gid= GroupId;
        GsonQuatationRequestModel quotationModel=new GsonQuatationRequestModel();
        quotationModel.VisitToken="";
        quotationModel.QuatationToken= CommonShare.generateToken(this);
        quotationModel.GroupId=gid;
        quotationModel.EnterDateMillisecond=System.currentTimeMillis();
        quotationModel.setRequestTag(startpont);
        quotationModel.setVisitType("Seperate");


        LayoutInflater inflater=getLayoutInflater();
        final View v= inflater .inflate(R.layout.item_quationrequest,null);
        lQuation.addView(v);
        Spinner spAssignTo=v.findViewById(R.id.spAssignTo);
        EditText edRemark=v.findViewById(R.id.edRemark);
        ImageView imgCamera=v.findViewById(R.id.imgCamera);
        map.put(startpont,imgCamera);
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,empList);
        spAssignTo.setAdapter(adapter);

        requestList.add(quotationModel);
        // imgCamera.setTag();
        v.setTag(quotationModel);
        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                androidx.appcompat.app.AlertDialog.Builder alert=new androidx.appcompat.app.AlertDialog.Builder(GenrateQuotationRequest.this);
                alert.setTitle("Delete Item");
                alert.setMessage("Do you want to delete this item ?");
                alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        lQuation.removeView(v);
                        //selectedProductForFSR.remove(v.getTag());
                        requestList.remove(v.getTag());
                    }
                });
                alert.setNegativeButton("Cancel",null);
                alert.show();
                return false;
            }
        });

        imgCamera.setTag(quotationModel);
        imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert=new AlertDialog.Builder(GenrateQuotationRequest.this);
                String[] arr=new String[]{"Capture From Camera","Pick From Gallery"};
                alert.setSingleChoiceItems(
                        new ArrayAdapter(GenrateQuotationRequest.this, android.R.layout.simple_list_item_1, arr)
                        , -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(i==0)
                                {
                                    getImageQuotation(v,0);
                                }
                                else  if(i==1) {
                                    getImageQuotation(v,1);
                                }
                                dialogInterface.cancel();
                            }
                        }
                );
                alert.show();
            }
        });

    }

    boolean isQuotationCam=true;
    void getImageQuotation(View view,int id)
    {
        GsonQuatationRequestModel m= (GsonQuatationRequestModel) view.getTag();

        if(id==0) {
            isQuotationCam=true;
            try
            {
                /*
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                File file = new File(getAccessPath() + File.separator + "img.jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(intent, m.getRequestTag());*/
                startActivityForResult(new Intent(GenrateQuotationRequest.this, CameraActivity.class), m.getRequestTag());

            } catch (Exception ex) {
                CommonShare.alert(this, ex.toString());
            }
        }
        else {
            isQuotationCam=false;
            Intent in = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(in, m.getRequestTag());
        }
    }



    String getAccessPath()
    {
       // getExternalStoragePublicDirectory
        String root = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString();
        Log.d("ROOT", root);
        File myDir = new File(root +"/"+ getString(R.string.app_name).replace(" ",""));
        myDir.mkdirs();

        return    myDir.getAbsoluteFile().toString();
    }

    GsonGroup group;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1) {
            if (resultCode == RESULT_OK) {
                group = SelectGroup.selectedGroup;
                btnSelectGroup.setText(group.getGroupName());
            }
        }
        if( requestCode>500)
        {
            try
            {
                File file = null;

                if(isQuotationCam)
                    try
                    {
                     /*   file=      new File(//Environment.getExternalStorageDirectory()
                                getAccessPath()
                                + File.separator + "img.jpg"); */
                        setQuotationNewCamera(requestCode);
                        return;

                    }
                    catch (Exception ex){}
                //  localFilePath = file.getAbsolutePath();
                if(resultCode != RESULT_OK)
                     return;
                if(!isQuotationCam)
                {
                    Uri uri = data.getData();
                    String path = "";
                    try {
                        path = CommonShare.getPath(this, uri);
                    } catch (Exception e) {
                    }

                    file = new File(path);
                }       //  localFilePath = file.getAbsolutePath();

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);

                int width = bitmap.getWidth();
                int height = bitmap.getHeight();

                Log.e("Width height", width + "   " + height);

                float constant = ((float) (650 / (float) width));
                width = 650;
                Log.e("Contatnt", constant + "");
                float m = ((float) height) * constant;
                height = (int) m;
                Log.e("new width heith ", width + "   " + height);

                Bitmap thePic = ThumbnailUtils.extractThumbnail(bitmap, width, height);
                final File bitmapFile = BitmapUtilities.saveToExtenal(thePic, this);
                String  localFilePath = bitmapFile.getAbsolutePath();
                //ImageView img= findViewById(  photoClickImagetag);
                //        img.setImageBitmap(thePic);


                for(int i=0;i<requestList.size();i++)
                {
                    if(requestList.get(i).equals(    (map.get(requestCode)).getTag()))
                    {
                        requestList.get(i).setLocalpath(localFilePath);
                    }
                }


                //ImageView img=  findViewById(map.get(requestCode));
                map.get(requestCode).setImageBitmap(thePic);


            }
            catch (Exception ex)
            {

                CommonShare.alert(this, ex.toString());

            }
        }

    }

    void setQuotationNewCamera(int requestCode)
    {
        if(MainActivityLauncher.pictureResult!=null) {
            MainActivityLauncher.pictureResult.toBitmap(1000, 1000, new BitmapCallback() {

                @Override
                public void onBitmapReady(@Nullable Bitmap bitmap) {
                    try {
                        if (bitmap == null)
                            return;
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
                        final File bitmapFile = BitmapUtilities.saveToExtenal(thePic, GenrateQuotationRequest.this);
                        String localFilePath = bitmapFile.getAbsolutePath();
                        //ImageView img= findViewById(  photoClickImagetag);
                        //        img.setImageBitmap(thePic);


                        for (int i = 0; i < requestList.size(); i++) {
                            if (requestList.get(i).equals((map.get(requestCode)).getTag())) {
                                requestList.get(i).setLocalpath(localFilePath);
                            }
                        }


                        //ImageView img=  findViewById(map.get(requestCode));
                        map.get(requestCode).setImageBitmap(thePic);


                    } catch (Exception ex) {
                        CommonShare.alert(GenrateQuotationRequest.this, ex.toString());
                    }
                }
            });
        }
    }
}
