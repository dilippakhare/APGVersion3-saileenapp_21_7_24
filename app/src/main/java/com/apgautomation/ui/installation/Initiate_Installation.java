package com.apgautomation.ui.installation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apgautomation.R;
import com.apgautomation.controller.SyncCustomerControlller;
import com.apgautomation.model.EmployeeModel;
import com.apgautomation.model.GSONCustomerMasterBeanExtends;
import com.apgautomation.model.GsonCustomerProduct;
import com.apgautomation.model.GsonGroup;
import com.apgautomation.ui.complaint.SelectProducts;
import com.apgautomation.ui.customer.SelectGroup;
import com.apgautomation.ui.salesvisit.CreateNewCustomer;
import com.apgautomation.utility.BitmapUtilities;
import com.apgautomation.utility.CommonShare;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

public class Initiate_Installation extends AppCompatActivity {

    Button btnSelectGroup,btnPODate,btnSelectCustomer,btnExpectedDate;
    Spinner spAssignTO;
    GsonGroup group;
    ArrayList<EmployeeModel> teamList;
    Button btnSelectProduct,btnInitiate;
    LinearLayout mainProductList;
    Button btnBrouse;
    private File file;


    EditText  edContactname,edContactNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initiate__installation);
        btnBrouse=findViewById(R.id.btnBrouse);
        btnSelectGroup=findViewById(R.id.btnSelectGroup);
        btnSelectCustomer=findViewById(R.id.btnSelectCustomer);
        btnPODate=findViewById(R.id.btnPODate);
        btnExpectedDate=findViewById(R.id.btnExpectedDate);
        spAssignTO=findViewById(R.id.spAssignTO);
        btnSelectProduct=findViewById(R.id.btnSelectProduct);
        mainProductList=findViewById(R.id.mainProductList);
        btnInitiate=findViewById(R.id.btnInitiate);

        edContactNo=findViewById(R.id.edContactNo);
        edContactname=findViewById(R.id.edContactname);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("New Intallation");

        btnExpectedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment(btnExpectedDate);
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
        btnPODate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment(btnPODate);
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        btnSelectGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(new Intent(Initiate_Installation.this, SelectGroup.class).putExtra("IsSales",false),1);
            }
        });
        btnSelectCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showCulstomerListAlert();
                //startActivityForResult(new Intent(SalesScheduleVisit.this, SelectGroup.class),1);
            }
        });


        ArrayList<EmployeeModel> tList= CommonShare.getEmployeeList(this);
        teamList=new ArrayList<>();
        for (EmployeeModel t:tList )
        {
            if(!t.DeleteStatus)
            {
                teamList.add(t);
            }
        }
        //teamList=
        //  CommonShare.getTeamListPlusOwn(this);

        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,teamList);
        spAssignTO.setAdapter(adapter);

        btnSelectProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SelectProducts.mGroupId= (int) ((GSONCustomerMasterBeanExtends)btnSelectCustomer.getTag()).getGroupId();
                SelectProducts.mCustomerID= ((GSONCustomerMasterBeanExtends)btnSelectCustomer.getTag()).getCustomerId();
                Intent intent=new Intent(Initiate_Installation.this, SelectProducts.class);
                intent.putExtra("type","Installation");
                startActivityForResult(intent,5);
            }
        });

        btnInitiate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveInstallation();
            }
        });

        btnBrouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(in,6);
            }
        });

    }

    private void saveInstallation() {
        initiate();
    }

    void initiate()
    {
        if(btnExpectedDate.getText().toString().equalsIgnoreCase("Select Date"))
        {
            Toast.makeText(this, "Select Expected Date", Toast.LENGTH_SHORT).show();
            return;
        }
        if(btnPODate.getText().toString().equalsIgnoreCase("Select Date"))
        {
            Toast.makeText(this, "Select PO Date", Toast.LENGTH_SHORT).show();
            return;
        }
        if(pList.size()==0)
        {
            Toast.makeText(this, "Select Product to be Installation", Toast.LENGTH_SHORT).show();
            return;
        }

        if(btnSelectCustomer.getText().toString().equalsIgnoreCase("Select Customer"))
        {
            Toast.makeText(this, "Select Customer", Toast.LENGTH_SHORT).show();
            return;
        }

        if(edContactname.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Enter Contact name", Toast.LENGTH_SHORT).show();
            return;
        }
        if(edContactNo.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Enter Contact Number", Toast.LENGTH_SHORT).show();
            return;
        }

        
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
    void showCulstomerListAlert()
    {
        SyncCustomerControlller cnt=new SyncCustomerControlller(this);
        final ArrayList<GSONCustomerMasterBeanExtends> mList=  cnt.getCustomerListByGroupIdNonRelam(group.getGroupId());
        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setTitle("Select Customer");
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,mList);
        alert.setSingleChoiceItems(adapter, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                btnSelectCustomer.setText(mList.get(i).CustomerName);
                btnSelectCustomer.setTag(mList.get(i));
                dialogInterface.dismiss();
            }
        });
        alert.show();
    }
  ArrayList<GsonCustomerProduct> pList=new ArrayList< GsonCustomerProduct  >();
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1) {
            if (resultCode == RESULT_OK) {
                group = SelectGroup.selectedGroup;
                btnSelectGroup.setText(group.getGroupName());
            }
        }
        if(requestCode==2) {
            if (resultCode == RESULT_OK)
            {
                group=null;
                // group = SelectGroup.selectedGroup;
                btnSelectGroup.setText(CreateNewCustomer.GroupName);
                btnSelectCustomer.setText(CreateNewCustomer.CustomerName);
            }
        }
        if(requestCode==3)
        {
            if (resultCode == RESULT_OK)
            {
                /*
                if(!chkIsNewGroup.isChecked()) {
                    group = SelectGroup.selectedGroup;
                    btnSelectGroup.setText(group.getGroupName());
                }
                else  if(chkIsNewGroup.isChecked())
                {
                    btnSelectCustomer.setText(CreateNewCustomer.CustomerName);
                }
                */
                btnSelectCustomer.setText(CreateNewCustomer.CustomerName);
            }
        }
        if(requestCode==5) {
            if (resultCode == RESULT_OK) {
                pList.add(SelectProducts.visitSelectionProduct);
                setProductList();
            }
        }

        if(requestCode==6)
        {
            if(resultCode != RESULT_OK)
                return;

                Uri uri = data.getData();
                String path = "";
                try {
                    path = CommonShare.getPath(this, uri);
                } catch (Exception e) {
                }

                file = new File(path);


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

            
        }

    }
    void setProductList()
    {

        mainProductList.removeAllViews();
        for (int i=0;i<pList.size();i++)
        {
            GsonCustomerProduct item =pList.get(i);
            TextView t=new TextView( this);
            t.setText((i+1)+  ". Type :-"+ item.getEquipmentType() +"("+item.ModelName+")");
            mainProductList.addView(t);
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

            long ScheduleDateLong=CommonShare.ddMMYYDateToLong(ed.getText().toString().replace("/","-"));

            final Calendar c = Calendar.getInstance();
            int tyear = c.get(Calendar.YEAR);
            int tmonth = c.get(Calendar.MONTH);
            int tday = c.get(Calendar.DAY_OF_MONTH);
            long TodayDateLong=CommonShare.ddMMYYDateToLong(
                    tday+"-"+(tmonth+1)+"-"+tyear);

            // CommonShare.alert(getContext(),  ScheduleDateLong+":"+TodayDateLong+"::"+(ScheduleDateLong-TodayDateLong)+"");


        }

    }


}