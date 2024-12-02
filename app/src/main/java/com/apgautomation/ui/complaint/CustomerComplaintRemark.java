package com.apgautomation.ui.complaint;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.apgautomation.R;
import com.apgautomation.ui.customer.CustomerActivity;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.AsyncUtilities;
import com.apgautomation.utility.serverutility.DownloadUtility;

import java.net.URLEncoder;

public class CustomerComplaintRemark extends AppCompatActivity implements DownloadUtility
{

    RadioButton rdGood,rdExcelent,rdAverage,rdPoor;
    EditText edRemark;
    Button btnSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_complaint_remark);
        rdGood=findViewById(R.id.rdGood);
        rdExcelent=findViewById(R.id.rdExcelent);
        rdAverage=findViewById(R.id.rdAverage);
        rdPoor=findViewById(R.id.rdPoor);
        edRemark=findViewById(R.id.edRemark);
        btnSubmit=findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click();
            }
        });

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Complaint Remarks");
    }

    private void click()
    {
        try {


        String strRemark=edRemark.getText().toString().trim(),rating="";
        strRemark= URLEncoder.encode(strRemark,"UTF-8");
        if(rdAverage.isChecked())
            rating="Average";
            if(rdExcelent.isChecked())
                rating="Excelent";
            if(rdGood.isChecked())
                rating="Good";
            if(rdPoor.isChecked())
                rating="Poor";
        String Url= CommonShare.url+"Service1.svc/SaveCustomerRemarkOnComplaint?ComaplintId="+ViewComplaintDetails.model.ComplaintId+"&remark="+strRemark
                +"&UserId="+CommonShare.getUserId(this)+"&rating="+rating;


           AsyncUtilities utilities = new AsyncUtilities(this, false, Url, null,1, this);
           utilities.execute();
       }
       catch (Exception ex)
       {}

    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode) {
        if(requestCode==1 && responseCode==200)
        {
            Toast.makeText(this, "Remark Saved", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(CustomerComplaintRemark.this, CustomerActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

            /*
            AlertDialog.Builder alert=new AlertDialog.Builder(CustomerComplaintRemark.this);
            alert.setCancelable(false);
            alert.setTitle("View Otp");
            alert.setMessage("Otp for this complaint is '"+ ViewComplaintDetails. model.OTP+"' . Share this otp with our engineer if you are are satisfied with our service." );
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(CustomerComplaintRemark.this, CustomerActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
            });
            alert.show();
            */
        }
    }
}
