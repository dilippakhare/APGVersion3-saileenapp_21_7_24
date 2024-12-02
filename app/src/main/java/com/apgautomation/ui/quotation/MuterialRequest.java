 package com.apgautomation.ui.quotation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.apgautomation.R;
import com.apgautomation.controller.SyncCustomerProductControlller;
import com.apgautomation.model.EmployeeModel;
import com.apgautomation.model.GsonCustomerProduct;
import com.apgautomation.model.ModelMuterialRequestList;
import com.apgautomation.ui.complaint.SelectProducts;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.AsyncUtilities;
import com.apgautomation.utility.serverutility.DownloadUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

 public class MuterialRequest extends AppCompatActivity implements DownloadUtility {

    public static ModelMuterialRequestList editModel;
    int mode=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muterial_request);
        try
        {
              mode=getIntent().getExtras().getInt("mode");
        }
        catch (Exception ex){}
        initView(this);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    RadioButton rdHigh,rdLow,rdMedium   ,rdTrial,rdCFF,rdLoadBasis;
    EditText edAmount,edReason,edPDetails;
    Button btnRequest;

    LinearLayout linearSection1,linearSection2,linearSection3;

    RadioButton rdSaction1,rdSaction1NOt;
    EditText edNote1;
    TextView spiner1;
    TextView txtSaction1;
    Button btnSection1;

    RadioButton rdSaction2,rdSaction2NOt;
    EditText edNote2;
    TextView spiner2;
    Button btnSection2;
     TextView txtSaction2;

    RadioButton rdSaction3,rdSaction3NOt;
    EditText edNote3;
    TextView spiner3;
    Button btnSection3;
    TextView txtSaction3;

     ArrayList<EmployeeModel> section1List=new ArrayList<>();
     ArrayList<EmployeeModel> section2List=new ArrayList<>();
     ArrayList<EmployeeModel> section3List=new ArrayList<>();

    void initView(MuterialRequest convertView)
    {
        linearSection1=findViewById(R.id.linearSection1);
        rdSaction1=findViewById(R.id.rdSaction1); rdSaction1NOt=findViewById(R.id.rdSaction1NOt);
        edNote1=findViewById(R.id.edNote1);
        spiner1=findViewById(R.id.spiner1);
        btnSection1=findViewById(R.id.btnSection1);
        linearSection2=findViewById(R.id.linearSection2);
        rdSaction2=findViewById(R.id.rdSaction2); rdSaction2NOt=findViewById(R.id.rdSaction2NOt);
        edNote2=findViewById(R.id.edNote2);
        spiner2=findViewById(R.id.spiner2);
        btnSection2=findViewById(R.id.btnSection2);
        linearSection3=findViewById(R.id.linearSection3);
        rdSaction3=findViewById(R.id.rdSaction3); rdSaction3NOt=findViewById(R.id.rdSaction3NOt);
        edNote3=findViewById(R.id.edNote3);
        spiner3=findViewById(R.id.spiner3);
        btnSection3=findViewById(R.id.btnSection3);
        linearSection1.setVisibility(View.GONE);
        linearSection2.setVisibility(View.GONE);
        linearSection3.setVisibility(View.GONE);



        TextView txtName,txtPort,txtType,txtSerial,txtAmcStart,txtAmcEnd;
        TextView  txtBy=findViewById(R.id.txtBy);
        txtSaction1=findViewById(R.id.txtSaction1);
        txtSaction2=findViewById(R.id.txtSaction2);
        txtSaction3=findViewById(R.id.txtSaction3);

        txtName=convertView.findViewById(R.id.txtName);
        txtPort=convertView.findViewById(R.id.txtPort);
        txtType=convertView.findViewById(R.id.txtType);

        txtSerial=convertView.findViewById(R.id.txtSerial);
        txtAmcStart=convertView.findViewById(R.id.txtAmcStart);
        txtAmcEnd=convertView.findViewById(R.id.txtAmcEnd);
        SyncCustomerProductControlller scp=new SyncCustomerProductControlller(this);
        GsonCustomerProduct model =null;
               // scp.getProductWithProductId(SelectProducts.visitSelectionProduct.RecId);
      //  if (list.get(position) instanceof ServerCustomerProducts)
       //     model = (ServerCustomerProducts) list.get(position);
        if(mode==0) {
          model=  scp.getProductWithProductId(SelectProducts.visitSelectionProduct.RecId);
            txtBy.setText(CommonShare.getMyEmployeeModel(this).EmpName);
        }
        else
        {
            model=  scp.getProductWithProductId((int)editModel.getProductId());
            txtBy.setText(CommonShare.empMap.get((int)editModel.getEmpId()).EmpName);
        }

        txtAmcStart.setText(CommonShare.convertToDate(CommonShare.parseDate(model.AmcStartDate)));
        txtAmcEnd.setText(CommonShare.convertToDate(CommonShare.parseDate(model.AmcEndDate)));
        txtName.setText(model.EquipmentName);
        txtType.setText(model.EquipmentType);

        txtPort.setText(model.PortNo);
        txtSerial.setText(model.SerialNumber);
        TextView txtCustomer = convertView.findViewById(R.id.txtCustomer);
        txtCustomer.setText(model.CustomerName+"("+model.GroupName+")");

        edPDetails=findViewById(R.id.edPDetails);


        rdHigh=findViewById(R.id.rdHigh);
        rdLow=findViewById(R.id.rdLow);
        rdMedium=findViewById(R.id.rdMedium);

        rdTrial=findViewById(R.id.rdTrial);
        rdLoadBasis=findViewById(R.id.rdLoadBasis);
        rdCFF=findViewById(R.id.rdCFF);

        edReason=findViewById(R.id.edReason);
        edAmount=findViewById(R.id.edAmount);

        btnRequest=findViewById(R.id.btnRequest);
        if(mode!=0)
        {
            btnRequest.setVisibility(View.GONE);
        }
        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        submitRequest();
            }
        });

        if(mode==1)
        {
            edAmount.setText(editModel.getAmount()+"");
            edReason.setText(editModel.getReason());

            try
            {
                if(editModel.ProductDetails!=null && editModel.ProductDetails.length()>0 && !editModel.ProductDetails.equalsIgnoreCase("null"))
                   edPDetails.setText(editModel.ProductDetails);
            }
            catch (Exception ex){}
            if(editModel.getRiskLevel().equalsIgnoreCase("high"))
            {
                rdHigh.setChecked(true);
            }
            else if(editModel.getRiskLevel().equalsIgnoreCase("low"))
            {
                rdLow.setChecked(true);
            }
            else if(editModel.getRiskLevel().equalsIgnoreCase("medium"))
            {
                rdMedium.setChecked(true);
            }

            if(editModel.getBasis().equalsIgnoreCase("CCF"))
            {
                rdCFF.setChecked(true);
            }
            else if(editModel.getBasis().equalsIgnoreCase("Trial"))
            {
                rdTrial.setChecked(true);
            }
            else if(editModel.getBasis().equalsIgnoreCase("LoadBasis"))
            {
                rdLoadBasis.setChecked(true);
            }
           // CommonShare.getComplaintStatusList()
            linearSection1.setVisibility(View.VISIBLE);

            initFirstSection();
            initSeconSection();
            initThirdSection();

           /*
            if(  true )//editModel.getSanction1Status()==null ||editModel.getSanction1Status().equalsIgnoreCase(""))
            {
               initFirstSection();

            }
            else
            {
                edNote1.setText(editModel.getSanction1Note());
                edNote1.setEnabled(false);
                if(editModel.getSanction1Status().equalsIgnoreCase("Approved"))
                {
                    rdSaction1.setChecked(true);
                    txtSaction1.setText("Approved By "+CommonShare.empMap.get(Integer.parseInt(editModel.getSanction1EmpId())).EmpName);
                    txtSaction1.setTextColor(Color.GREEN);
                }
                else
                {
                    txtSaction1.setText("Rejected By "+CommonShare.empMap.get(Integer.parseInt(editModel.getSanction1EmpId())).EmpName);
                    rdSaction1NOt.setChecked(false);
                    txtSaction1.setTextColor(Color.RED);
                }
                rdSaction1.setEnabled(false);
                rdSaction1NOt.setEnabled(false);


                if(editModel.getSanction2Status()==null ||editModel.getSanction2Status().equalsIgnoreCase(""))
                {

                }
                else
                {
                    linearSection2.setVisibility(View.VISIBLE);
                    edNote2.setText(editModel.getSanction2Note());
                    edNote2.setEnabled(false);
                    if(editModel.getSanction2Status().equalsIgnoreCase("Approved"))
                    {
                        rdSaction2.setChecked(true);
                        txtSaction2.setText("Approved By "+CommonShare.empMap.get(Integer.parseInt(editModel.getSanction2EmpId())).EmpName);
                        txtSaction2.setTextColor(Color.GREEN);
                    }
                    else
                    {
                        txtSaction2.setText("Rejected By "+CommonShare.empMap.get(Integer.parseInt(editModel.getSanction2EmpId())).EmpName);
                        rdSaction2NOt.setChecked(true);
                        txtSaction2.setTextColor(Color.RED);
                    }
                    rdSaction2.setEnabled(false);
                    rdSaction2NOt.setEnabled(false);



                }

            }


            */


        }
    }

    void initFirstSection()
    {
        String EmpName="";
        ArrayList<EmployeeModel>  empList=CommonShare.getSectionList(this,1);
        for (EmployeeModel m:empList)
        {
            if(EmpName.equalsIgnoreCase(""))
                EmpName=m.EmpName;
            else
                EmpName=EmpName+","+m.EmpName;
        }

        if( editModel.getSanction1Status()==null ||editModel.getSanction1Status().equalsIgnoreCase("")) {
            txtSaction1.setText("Pending From:- " + "");
            spiner1.setText(EmpName);
            if (CommonShare.isResposibleSectionPersion(this, 1)) {
                btnSection1.setVisibility(View.VISIBLE);
                btnSection1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edNote1.getText().toString().equalsIgnoreCase("")) {
                            Toast.makeText(MuterialRequest.this, "Note Required", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!(rdSaction1.isChecked() || rdSaction1NOt.isChecked())) {
                            Toast.makeText(MuterialRequest.this, "Status Required", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String status = rdSaction1.isChecked() ? "Approved" : "Rejected";
                        String url = CommonShare.url + "Service1.svc/approveMuterial1?status=" + status + "&empid=" + CommonShare.getEmpId(MuterialRequest.this)
                                + "&note=" + CommonShare.getEncodeString(edNote1.getText().toString()) + "&requestid=" + (int) editModel.getRequestId();
                        AsyncUtilities utilities = new AsyncUtilities(MuterialRequest.this, false, url, null, 2, MuterialRequest.this);
                        utilities.execute();
                    }
                });
            }
        }
        else
        {
            edNote1.setText(editModel.getSanction1Note());
            edNote1.setEnabled(false);
            if(editModel.getSanction1Status().equalsIgnoreCase("Approved"))
            {
                rdSaction1.setChecked(true);
                txtSaction1.setText("Approved By "+CommonShare.empMap.get(Integer.parseInt(editModel.getSanction1EmpId())).EmpName);
                txtSaction1.setTextColor(Color.GREEN);
            }
            else
            {
                txtSaction1.setText("Rejected By "+CommonShare.empMap.get(Integer.parseInt(editModel.getSanction1EmpId())).EmpName);
                rdSaction1NOt.setChecked(false);
                txtSaction1.setTextColor(Color.RED);
            }
            rdSaction1.setEnabled(false);
            rdSaction1NOt.setEnabled(false);
        }

      //  if(editModel.getSanction3Status()===)
    }

    void initSeconSection()
    {
        linearSection2.setVisibility(View.VISIBLE);
        String EmpName="";
        ArrayList<EmployeeModel>  empList=CommonShare.getSectionList(this,2);
        for (EmployeeModel m:empList)
        {
            if(EmpName.equalsIgnoreCase(""))
                EmpName=m.EmpName;
            else
                EmpName=EmpName+","+m.EmpName;
        }
        if(editModel.getSanction2Status()==null ||editModel.getSanction2Status().equalsIgnoreCase("")) {

            txtSaction2.setText("Pending From:- " + "");
            spiner2.setText(EmpName);

            if (CommonShare.isResposibleSectionPersion(this, 2)) {
                btnSection2.setVisibility(View.VISIBLE);
                btnSection2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edNote2.getText().toString().equalsIgnoreCase("")) {
                            Toast.makeText(MuterialRequest.this, "Note Required", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!(rdSaction2.isChecked() || rdSaction2NOt.isChecked())) {
                            Toast.makeText(MuterialRequest.this, "Status Required", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String status = rdSaction2.isChecked() ? "Approved" : "Rejected";
                        String url = CommonShare.url + "Service1.svc/approveMuterial2?status=" + status + "&empid=" + CommonShare.getEmpId(MuterialRequest.this)
                                + "&note=" + CommonShare.getEncodeString(edNote2.getText().toString()) + "&requestid=" + (int) editModel.getRequestId();
                        AsyncUtilities utilities = new AsyncUtilities(MuterialRequest.this, false, url, null, 2, MuterialRequest.this);
                        utilities.execute();
                    }
                });
            }
        }
        else
        {
            edNote2.setText(editModel.getSanction2Note());
            edNote2.setEnabled(false);
            if(editModel.getSanction2Status().equalsIgnoreCase("Approved"))
            {
                rdSaction2.setChecked(true);
                txtSaction2.setText("Approved By "+CommonShare.empMap.get(Integer.parseInt(editModel.getSanction2EmpId())).EmpName);
                txtSaction2.setTextColor(Color.GREEN);
            }
            else
            {
                txtSaction2.setText("Rejected By "+CommonShare.empMap.get(Integer.parseInt(editModel.getSanction2EmpId())).EmpName);
                rdSaction2NOt.setChecked(true);
                txtSaction2.setTextColor(Color.RED);
            }
            rdSaction2.setEnabled(false);
            rdSaction2NOt.setEnabled(false);
        }
    }

    void initThirdSection()
    {
        linearSection3.setVisibility(View.VISIBLE);
        if(getString(R.string.app_name).contains("APG")) {
            if (editModel.getSanction3Status() == null || editModel.getSanction3Status().equalsIgnoreCase("")) {

                String EmpName = "";
                ArrayList<EmployeeModel> empList = CommonShare.getSectionList(this, 3);
                for (EmployeeModel m : empList) {
                    if (EmpName.equalsIgnoreCase(""))
                        EmpName = m.EmpName;
                    else
                        EmpName = EmpName + "," + m.EmpName;
                }
                txtSaction3.setText("Pending From:- " + "");
                spiner3.setText(EmpName);
                if (CommonShare.isResposibleSectionPersion(this, 3)) {
                    btnSection3.setVisibility(View.VISIBLE);
                    btnSection3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (edNote3.getText().toString().equalsIgnoreCase("")) {
                                Toast.makeText(MuterialRequest.this, "Note Required", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (!(rdSaction3.isChecked() || rdSaction3NOt.isChecked())) {
                                Toast.makeText(MuterialRequest.this, "Status Required", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            String status = rdSaction3.isChecked() ? "Approved" : "Rejected";
                            String url = CommonShare.url + "Service1.svc/approveMuterial3?status=" + status + "&empid=" + CommonShare.getEmpId(MuterialRequest.this)
                                    + "&note=" + CommonShare.getEncodeString(edNote3.getText().toString()) + "&requestid=" + (int) editModel.getRequestId();
                            AsyncUtilities utilities = new AsyncUtilities(MuterialRequest.this, false, url, null, 2, MuterialRequest.this);
                            utilities.execute();
                        }
                    });
                }
            } else {
                edNote3.setText(editModel.getSanction3Note());
                edNote3.setEnabled(false);
                if (editModel.getSanction3Status().equalsIgnoreCase("Approved")) {
                    rdSaction3.setChecked(true);
                    txtSaction3.setText("Approved By " + CommonShare.empMap.get(Integer.parseInt(editModel.getSanction3EmpId())).EmpName);
                    txtSaction3.setTextColor(Color.GREEN);
                } else {
                    txtSaction3.setText("Rejected By " + CommonShare.empMap.get(Integer.parseInt(editModel.getSanction3EmpId())).EmpName);
                    rdSaction3NOt.setChecked(true);
                    txtSaction3.setTextColor(Color.RED);
                }
                rdSaction3.setEnabled(false);
                rdSaction3NOt.setEnabled(false);
                linearSection3.setVisibility(View.VISIBLE);

            }
        }
        else
        {
            linearSection3.setVisibility(View.GONE);
        }
    }

    private void submitRequest() {
        if(edAmount.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Amount Required", Toast.LENGTH_SHORT).show();
            return;
        }
        if(edReason.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Reason Required", Toast.LENGTH_SHORT).show();
            return;
        }
        if(rdHigh.isChecked() || rdLow.isChecked() || rdMedium.isChecked())
        {

        }
        else
        {
            Toast.makeText(this, "Select Risk Level", Toast.LENGTH_SHORT).show();
            return;
        }
        if(rdCFF.isChecked() || rdLoadBasis.isChecked() || rdTrial.isChecked())
        {

        }
        else
        {
            Toast.makeText(this, "Select Requirement Type", Toast.LENGTH_SHORT).show();
            return;
        }
        String RiskLevel="",Basis="";
        if(rdHigh.isChecked())
            RiskLevel="High";
        if(rdLow.isChecked())
            RiskLevel="Low";
        if(rdMedium.isChecked())
            RiskLevel="Medium";

        if(rdTrial.isChecked())
            Basis="Trial";
        if(rdCFF.isChecked())
            Basis="CCF";
        if(rdLoadBasis.isChecked())
            Basis="LoadBasis";

        JSONObject j=new JSONObject( );
        try {
            j.put("EmpId",CommonShare.getEmpId(this));
            j.put("ProductId",SelectProducts.visitSelectionProduct.RecId);
            j.put("CustomerId",SelectProducts.visitSelectionProduct.CustomerId);
            j.put("GroupId",SelectProducts.visitSelectionProduct.GroupId);

            j.put("Amount",Integer.parseInt(edAmount.getText().toString()));
            j.put("Reason", edReason.getText().toString() );
            j.put("RiskLevel",RiskLevel);
            j.put("Basis",Basis);
            j.put("ProductDetails",edPDetails.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url=CommonShare.url+"Service1.svc/makeMuterialRequest";
        AsyncUtilities utilities=new AsyncUtilities(this,true,url,j.toString(),1,this);
        utilities.execute();
    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode) {
     //   CommonShare.alert(this,str);
        if(responseCode==200)
        {
            if(requestCode==1)
            {
                Toast.makeText(this, "Record Saved", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(this,MuteilaRequestList.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
            else  if(requestCode==2)
            {
                startActivity(new Intent(this,MuteilaRequestList.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }

        }
    }
}