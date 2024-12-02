package com.apgautomation.ui.communication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apgautomation.R;
import com.apgautomation.model.GsonAskQuery;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.syncutilities.VisitSyncUtility;

import io.realm.Realm;
import io.realm.RealmResults;

public class CommunicationDetails extends AppCompatActivity {

   Realm realm;
    LinearLayout linearMain;
    String Token;
    GsonAskQuery lastModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication_details);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Ask Query");
        linearMain=findViewById(R.id.linearMain);
        Token=getIntent().getExtras().getString("Token");
        realm=Realm.getDefaultInstance();
        lastModel= realm.where(GsonAskQuery.class).equalTo("Token",Token).findFirst();
        setView();
        try
        {
            if(CommonShare.getEmpId(this)==lastModel.getToId())
            {
                realm.beginTransaction();
                lastModel.setReed(true);
                lastModel.setModified(true);
                realm.copyToRealmOrUpdate(lastModel);
                realm.commitTransaction();
                VisitSyncUtility utility=new VisitSyncUtility(this);
                utility.sync();
            }
        }
        catch (Exception ex)
        {}
        while(true)
        {

            RealmResults<GsonAskQuery> cp= realm.where(GsonAskQuery.class).equalTo("RefferenceToken",lastModel.getToken()).findAll();
            if(cp.size()==0)
            {
                Button b=new Button(this);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        b.setBackground(getDrawable(R.drawable.button_selector));
                        b.setTextColor(Color.WHITE);
                    }
                }
                b.setText("Reply");
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(CommunicationDetails.this,AskQueryReply.class).putExtra("lasttoken",lastModel.getToken()));
                    }
                });
                if(lastModel.getEnterById()!=CommonShare.getEmpId(this))
                   linearMain.addView(b);
                break;
            }
            else
            {
                lastModel=cp.first();
                setView();
            }
        }


    }
    void setView()
    {
        LayoutInflater inflater=getLayoutInflater();
        View v=inflater.inflate(R.layout.item_communicationdetails,null);
        linearMain.addView(v);
        TextView txtName=  v.findViewById(R.id.txtName);
        TextView txtMsg=  v.findViewById(R.id.txtMsg);
        Button btnAttchment=  v.findViewById(R.id.btnAttchment);
        txtName.setText(lastModel.getEnterByName()+"-    on "+lastModel.getEnterDate());
        txtMsg.setText(lastModel.getMsg());
        if(lastModel.getAttachment()!=null && lastModel.getAttachment().length()>5)
        {
            btnAttchment.setTag(lastModel.getAttachment().replace("~",  CommonShare.url));
            btnAttchment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    CommonShare.openFile(view.getTag().toString(),CommunicationDetails.this);
                }
            });
        }
        else
        {
            btnAttchment.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
