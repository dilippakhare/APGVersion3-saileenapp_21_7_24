package com.apgautomation.ui.quotation;

import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.apgautomation.R;
import com.apgautomation.model.GsonQuotationRequestModel;
import com.apgautomation.ui.ViewImage;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.AsyncUtilities;
import com.apgautomation.utility.serverutility.DownloadUtility;
import com.squareup.picasso.Picasso;

public class QuotationDetails extends AppCompatActivity implements DownloadUtility {

    ImageView img;
    public static GsonQuotationRequestModel model;
    TextView txtTitle,txtRemark,txtAssignTo;
    Button btnResponse;
    TextView  txtResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation_details);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Quotation Details");

        txtAssignTo=findViewById(R.id.txtAssignTo);
        txtTitle=findViewById(R.id.txtTitle);
        txtRemark=findViewById(R.id.txtRemark);
        img=findViewById(R.id.img);
        try
        {
            String url=model.getAttachment().replace("~", CommonShare.url1);

            Picasso.get().load(  url).placeholder(R.drawable.loading).into(img);
            if(url.length()>10) {
                img.setOnClickListener(new View.OnClickListener()

                {
                    @Override
                    public void onClick(View view)
                    {
                        ViewImage.url = model.getAttachment().replace("~", CommonShare.url1);
                        startActivity(new Intent(QuotationDetails.this, ViewImage.class));
                    }
                });
            }

        }
        catch (Exception ex)
        {}

        txtTitle.setText(model.toString());
        try {
            txtAssignTo.setText(model.AssignedToName);//CommonShare.empMap.get(model.getEnterDate()).EmpName);
        }
        catch (Exception ex)
        {}
        btnResponse=findViewById(R.id.btnResponse);
        try
        {
            txtRemark.setText(model.getRemark());


        }
        catch (Exception ex)
        {}


        if(model.AssignToEmpId==CommonShare.getEmpId(this))
        {
            btnResponse.setVisibility(View.VISIBLE);
            btnResponse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alert=new AlertDialog.Builder(QuotationDetails.this);
                    alert.setMessage("Do you have given this quotation ?");
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                                confirm();
                        }
                    });
                    alert.setNegativeButton("No",null);
                    alert.show();

                }
            });
        }
        txtResponse=findViewById(R.id.txtResponse);
        try
        {
             if(model.getResponseRemark()!=null && model.getResponseRemark().equalsIgnoreCase("Given"))
             {
                 txtResponse.setText("Quotation Given on "+ CommonShare.getDateTime( CommonShare.parseDate(model.getResponseDate())));
             }
             if(model.getResponseRemark().equalsIgnoreCase("given"))
                btnResponse.setVisibility(View.GONE);
        }
        catch (Exception ex)
        {}
        try
        {
            TextView TxtRequestBy  =findViewById(R.id.TxtRequestBy);
            TxtRequestBy.setText(  CommonShare.empMap.get((int)model.getEnterByEMpId()).EmpName);
        }
        catch (Exception xx)
        {}
    }
    void confirm()
    {
        String url= CommonShare.url+ "Service1.svc/ResposeQuotation?EmpId="+CommonShare.getEmpId(QuotationDetails.this)+"&QuotationId="+((int)model.getQuotationId())+"&text=Given";
        AsyncUtilities utilities=new AsyncUtilities(QuotationDetails.this,false,url,"",1,QuotationDetails.this);
        utilities.execute();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onComplete(String str, int requestCode, int responseCode) {

        if(responseCode==200)
        {
            finish();
            startActivity(new Intent(this,QuotationList.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
    }
}
