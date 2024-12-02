package com.apgautomation.ui.enquiry;






import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;



import com.apgautomation.R;
import com.apgautomation.controller.SyncCustomerControlller;
import com.apgautomation.model.EmployeeModel;
import com.apgautomation.model.GSONCustomerMasterBeanExtends;
import com.apgautomation.model.GsonGroup;
import com.apgautomation.ui.customer.SelectGroup;
import com.apgautomation.ui.salesvisit.CreateNewCustomer;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.AsyncUtilities;
import com.apgautomation.utility.serverutility.DownloadUtility;
import com.apgautomation.utility.serverutility.netrequest.ConnectionDetector;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

public class AddEnquiry extends AppCompatActivity implements DialogInterface.OnMultiChoiceClickListener, DownloadUtility {

    public static EnquiryDTO EditEnquiry;
    public ArrayList<EmployeeModel> empList=new ArrayList<>();
    public ArrayList<String> productList=new ArrayList<String>();
    public ArrayList<String> enquirySendList=new ArrayList<String>();

    public boolean isAdd=true;
    GsonGroup group;
    CheckBox chkIsNewGroup,chkIsNewCustomer;
    Button btnSelectGroup,btnSelectCustomer;
    Spinner spEmpName,spAssignTO;

    Button btnProducts ,btnSave;
    TextView txtProducts,txtDate;

    EditText edContactPerson,edContactNumber,edEnquiryDetails;
    LinearLayout step2;
    Button btnVerify;
    EditText edOrderValue;
    RadioButton rdSYes,rdSNo,rdVYes,rdVNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_enquiry);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Enquiry");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Enquiry");
        empList= CommonShare.getActiveEmployeeList(this);
        txtProducts=findViewById(R.id.txtProducts);
        btnSelectGroup=findViewById(R.id.btnSelectGroup);
        btnSelectCustomer=findViewById(R.id.btnSelectCustomer);
        spEmpName=findViewById(R.id.spEmpName);
        spAssignTO=findViewById(R.id.spAssignTO);

        btnProducts=findViewById(R.id.btnProducts);
        btnProducts=findViewById(R.id.btnProducts);
        btnSave=findViewById(R.id.btnSave);
        chkIsNewGroup=findViewById(R.id.chkIsNewGroup);
        chkIsNewCustomer=findViewById(R.id.chkIsNewCustomer);
        chkIsNewGroup.setChecked(false);
        chkIsNewCustomer.setChecked(false);
        edContactPerson=findViewById(R.id.edContactPerson);
        edContactNumber=findViewById(R.id.edContactNumber);
        edEnquiryDetails=findViewById(R.id.edEnquiryDetails);
        txtDate=findViewById(R.id.txtDate);
        step2=findViewById(R.id.step2);
        step2.setVisibility(View.GONE);
        btnVerify=findViewById(R.id.btnVerify);
        rdSNo=findViewById(R.id.rdSNo);
        rdSYes=findViewById(R.id.rdSYes);
        rdVYes=findViewById(R.id.rdVYes);
        rdVNo=findViewById(R.id.rdVNo);
        edOrderValue=findViewById(R.id.edOrderValue);

        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,empList);
        ArrayAdapter adapter1=new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,CommonShare.getEnquirySentTo(this));
        spAssignTO.setAdapter(adapter1);
        spEmpName.setAdapter(adapter);

        btnSelectGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AddEnquiry.this, SelectGroup.class).putExtra("IsSales",true),1);
            }
        });

        chkIsNewGroup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                if(b)
                {
                    chkIsNewCustomer.setEnabled(false);
                    startActivityForResult(new Intent(AddEnquiry.this,CreateNewCustomer.class).putExtra("IsSync",true),2);
                    btnSelectGroup.setEnabled(false);
                    btnSelectCustomer.setEnabled(false);
                }
                else
                {
                    btnSelectGroup.setEnabled(true);
                    chkIsNewCustomer.setEnabled(true);
                }
                //chkIsNewCustomer.setChecked(b);
            }
        });
        chkIsNewCustomer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {

                    if(!chkIsNewGroup.isChecked()) {
                        startActivityForResult(new Intent(AddEnquiry.this, CreateNewCustomer.class).putExtra("GroupName", btnSelectGroup.getText().toString()).putExtra("IsSync",true), 3);
                        btnSelectCustomer.setEnabled(false);
                    }
                }
                else
                {
                    btnSelectCustomer.setEnabled(true);

                }
            }
        });
        btnSelectCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showCulstomerListAlert();
                //startActivityForResult(new Intent(SalesScheduleVisit.this, SelectGroup.class),1);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert=new AlertDialog.Builder(AddEnquiry.this);
                alert.setMessage("Do you want save enquiry ?");
                alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveEnquiry();
                    }
                });
                alert.setNegativeButton("Cancel",null);
                alert.show();

            }
        });
        btnProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setProductList();
            }
        });

        try{
            boolean isEdit= getIntent().getExtras().getBoolean("IsEdit");
            if(isEdit)
                this.isAdd =false;
        }
        catch (Exception ex){}
    }

    private void setModelView() {
        for(int i=0;i<empList.size();i++)
        {
            if(empList.get(i).EmpId== EditEnquiry.getEmpId())
                spEmpName.setSelection(i);
        }
        ArrayList <EmployeeModel>  emplist1=  CommonShare.getEnquirySentTo(this);
        for(int i=0;i<emplist1.size();i++)
        {
            if(emplist1.get(i).EmpId== EditEnquiry.getEmpId())
                spAssignTO.setSelection(i);
        }
        txtProducts.setText(EditEnquiry.getProductName());
        edContactNumber.setText(EditEnquiry.getContactNo());
        edContactPerson.setText(EditEnquiry.getContactPerson());
        edEnquiryDetails.setText(EditEnquiry.getEnquiryDetails());
        btnSelectGroup.setText(EditEnquiry.getGroupName());
        btnSelectCustomer.setText(EditEnquiry.getCustomerName());
        txtDate.setText(CommonShare.convertToDate(CommonShare.parseDate(EditEnquiry.getEnterDate())));


        edContactNumber.setEnabled(false);
        edContactPerson.setEnabled(false);
        edEnquiryDetails.setEnabled(false);
        btnSelectGroup.setEnabled(false);
        btnSelectCustomer.setEnabled(false);
        spAssignTO.setEnabled(false);
        spEmpName.setEnabled(false);

        Gson gson=new Gson();
        String s=gson.toJson(EditEnquiry);
        step2.setVisibility(View.VISIBLE);
        btnVerify.setVisibility(View.INVISIBLE);
      // CommonShare.alert(AddEnquiry.this,s);
        if(EditEnquiry.getEnquiryVerification()!= null ) {
            if(EditEnquiry.getEnquiryVerification().equalsIgnoreCase("true"))
                 rdVYes.setChecked(true);
            else
                rdVNo.setChecked(true);
            if(EditEnquiry.getOrderStatus().equalsIgnoreCase("true"))
               rdSYes.setChecked(true);
            else
                rdSNo.setChecked(true);

            edOrderValue.setText(EditEnquiry.getOrderValue());

        }

        if(CommonShare.isEnquiryAthurity(this)){
            btnVerify.setVisibility(View.VISIBLE);
            btnVerify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(edOrderValue.getText().toString().equalsIgnoreCase(""))
                    {
                        Toast.makeText(AddEnquiry.this, "Enter Order Value", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(!(rdSNo.isChecked()  ||  rdSYes. isChecked()))
                    {
                        Toast.makeText(AddEnquiry.this, "Select Order Status", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(!(rdVNo.isChecked()  ||  rdVYes. isChecked()))
                    {
                        Toast.makeText(AddEnquiry.this, "Select Enquiry Verification Status", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String url=CommonShare.url+"service1.svc/VerifyEnquiry?Id="
                            +EditEnquiry.getRecId()+"&OrderStatus="+rdSYes.isChecked()+
                            "&OrderValue="+edOrderValue.getText().toString()+"&VerificationStatus="+rdVYes.isChecked()+"&EmpId="+CommonShare.getEmpId(AddEnquiry.this);
                    AsyncUtilities utilities=new AsyncUtilities(AddEnquiry.this,false,url,null,2,AddEnquiry.this);
                    utilities.execute();
                }
            });
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();
        int empId= CommonShare.getEmpId(this);
        if(isAdd) {
            for (int i = 0; i < empList.size(); i++) {
                if (empList.get(i).EmpId == empId)
                    spEmpName.setSelection(i);
            }
        }
        else
        {
            chkIsNewCustomer.setVisibility(View.INVISIBLE);
            chkIsNewGroup.setVisibility(View.INVISIBLE);
            btnProducts.setVisibility(View.GONE);
            txtProducts.setTextColor(Color.BLACK);
            btnSave.setVisibility(View.GONE);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AddEnquiry.this.setModelView();
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    void showCulstomerListAlert()
    {
        SyncCustomerControlller cnt=new SyncCustomerControlller(this);
        final ArrayList<GSONCustomerMasterBeanExtends>  mList=  cnt.getCustomerListByGroupIdNonRelam(group.getGroupId());
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
                btnSelectCustomer.setText(CreateNewCustomer.CustomerName);
            }
        }


    }



    void setProductList()
    {

        String arr[]= CommonShare.getEnquiryProducts(this);
        for (String item:arr) {
            productList.add(item);
        }
        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setTitle("Select Products");
       // alert.setAdapter(new ArrayAdapter(this, android.R.layout.select_dialog_multichoice,productList),new )
        alert.setMultiChoiceItems(arr,null,this);
        alert.setPositiveButton("Select", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.setNegativeButton("cancel",null);
        alert.show();
    }

    public ArrayList<String>  selectedProducts=new ArrayList<>();
    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
           if(isChecked)
               selectedProducts.add(productList.get(which));
           else
               selectedProducts.remove(productList.get(which));

        txtProducts.setText(selectedProducts.toString().replace("[","").replace("]",""));

    }

    void saveEnquiry()
    {
        if(!new ConnectionDetector(this).isConnectingToInternet())
        {
            Toast.makeText(this, "Internet Not Available", Toast.LENGTH_SHORT).show();
            return;
        }
        if(edContactNumber.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Contact number should not empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(edContactPerson.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Contact persone name should not empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(btnSelectGroup.getText().toString().equalsIgnoreCase("Select Group"))
        {
            Toast.makeText(this, "Select Group", Toast.LENGTH_SHORT).show();
            return;
        }
        if(btnSelectCustomer.getText().toString().equalsIgnoreCase("Select Customer"))
        {
            Toast.makeText(this, "Select Customer", Toast.LENGTH_SHORT).show();
            return;
        }
        if(edEnquiryDetails.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Enquiry Details should not empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(txtProducts.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Products Names  should not empty", Toast.LENGTH_SHORT).show();
            return;
        }
        try{
            JSONObject j=new JSONObject();
            j.put("EmpId",empList.get(spEmpName.getSelectedItemPosition()).EmpId);
            j.put("SentTo",empList.get(spAssignTO.getSelectedItemPosition()).EmpId);

            j.put("ContactPerson",edContactPerson.getText().toString());
            j.put("ProductName",txtProducts.getText().toString());
            j.put("ContactNo",edContactNumber.getText().toString());

            j.put("EnquiryDetails",edEnquiryDetails.getText().toString());

            j.put("EnterBy",CommonShare.getEmpId(this));
            int GroupId=0,CustomerId=0;
            if(!chkIsNewGroup.isChecked()) {
                GroupId = group.getGroupId();
            }
            else
            {
                j.put("CustomerName",CreateNewCustomer.CustomerName);
                j.put("GroupName",CreateNewCustomer.GroupName);
                j.put("CustomerAreaId",CreateNewCustomer.AreaId);

            }
            if(!chkIsNewGroup.isChecked() &&!chkIsNewCustomer.isChecked()) {
                CustomerId =((GSONCustomerMasterBeanExtends) btnSelectCustomer.getTag()).CustomerId;
            }
            else {
                j.put("CustomerName",CreateNewCustomer.CustomerName);
                j.put("CustomerAreaId",CreateNewCustomer.AreaId);
            }
            j.put("GroupId",GroupId);
            j.put("CustomerId",CustomerId);
            j.put("GroupId",GroupId);
            j.put("CustomerId",CustomerId);
            j.put("EnterByName",CommonShare.getMyEmployeeModel(this).EmpName);

            Log.d("APG",j.toString());
            AsyncUtilities utilities=new AsyncUtilities(this,true,CommonShare.url+"service1.svc/AddEnquiry",j.toString(),1,this);
            utilities.execute();
        }
        catch (Exception ex){
                CommonShare.alert(this,ex.toString());
        }
    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode) {
             if(responseCode==200)
             {
                 if(requestCode==1)
                 {
                     Toast.makeText(this, "Enquiry Sent", Toast.LENGTH_SHORT).show();
                     finish();
                     startActivity(new Intent(this,EnquiryList.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                 }
                 if(requestCode==2)
                 {
                     Toast.makeText(this, "Enquiry Verified", Toast.LENGTH_SHORT).show();
                     finish();
                     startActivity(new Intent(this,EnquiryList.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                 }
             }
    }
}