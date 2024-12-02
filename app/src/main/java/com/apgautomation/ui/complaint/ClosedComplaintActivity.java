package com.apgautomation.ui.complaint;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apgautomation.HomePage;
import com.apgautomation.R;
import com.apgautomation.model.GSONCloseComplaint;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.netrequest.ConnectionDetector;
import com.apgautomation.utility.syncutilities.VisitSyncUtility;

import io.realm.Realm;

public class ClosedComplaintActivity extends AppCompatActivity implements View.OnClickListener {


    Button btnClose;
    EditText edOtp,edSolution;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closed_complaint);
        btnClose=findViewById(R.id.btnClose);
        edSolution=findViewById(R.id.edSolution);
        edOtp=findViewById(R.id.edOtp);

        btnClose.setOnClickListener(this);

        try {
            if (ViewComplaintDetails.isFeedbackGiven) {
                edOtp.setText(ViewComplaintDetails.model.OTP);
                edOtp.setEnabled(false);
            }
        }
        catch (Exception ex){}
    }

    @Override
    public void onClick(View view)
    {
        if(edSolution.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Solution Required", Toast.LENGTH_SHORT).show();
            return;
        }
        if(edOtp.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(this, "OTP Required", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!edOtp.getText().toString().equalsIgnoreCase(ViewComplaintDetails.model.OTP))
        {
            Toast.makeText(this, "OTP Not Matched", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!new ConnectionDetector(this).isConnectingToInternet())
        {
            Toast.makeText(this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
            return;
        }

        GSONCloseComplaint m=new GSONCloseComplaint();

        m.ClosedBy= CommonShare.getEmpId(this);
        m.Token=ViewComplaintDetails.model.Token;
        m.Solution=edSolution.getText().toString();
        m.IsModified=true;
        m.MatchTimeMillisecond=System.currentTimeMillis();

        Realm realm=Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(m);
        realm.commitTransaction();
        VisitSyncUtility utility=new VisitSyncUtility(this);
        utility.sync();

        startActivity(new Intent(this, HomePage.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

    }
}
