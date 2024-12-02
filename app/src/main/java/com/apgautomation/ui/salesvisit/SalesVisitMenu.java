package com.apgautomation.ui.salesvisit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.apgautomation.R;
import com.apgautomation.model.SubmenuModel;
import com.apgautomation.ui.adapter.SubMenuAdapter;
import com.apgautomation.utility.serverutility.netrequest.ConnectionDetector;
import com.apgautomation.utility.syncutilities.VisitSyncUtility;

import java.util.ArrayList;

public class SalesVisitMenu extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView listview;
    ArrayList<SubmenuModel> list=new ArrayList();

    VisitSyncUtility syncUtility;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_menu);
        listview=findViewById(R.id.listview);

        SubmenuModel model=new SubmenuModel();
        model.SubmenuName="Schedule Visit";
        list.add(model);
        model=new SubmenuModel();
        model.SubmenuName="Pending Visit";
        list.add(model);

        model=new SubmenuModel();
        model.SubmenuName="View All Visit";
        list.add(model);


        SubMenuAdapter adapter=new SubMenuAdapter(this,R.layout.item_submenu,list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(this);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Visit");

        syncUtility=new VisitSyncUtility(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    ProgressDialog pd;
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        if(!new ConnectionDetector(this).isConnectingToInternet())
        {
            Toast.makeText(this, "Please Turn on your Internet", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!syncUtility.isPeningZeoVisit()) {
            if (list.get(i).SubmenuName.equalsIgnoreCase("Schedule Visit")) {
                startActivity(new Intent(this, SalesScheduleVisit.class));
            } else if (list.get(i).SubmenuName.equalsIgnoreCase("Pending Visit")) {
                startActivity(new Intent(this, SalesVisitPendingList.class));
            } else if (list.get(i).SubmenuName.equalsIgnoreCase("View All Visit")) {
                startActivity(new Intent(this, SalesVisitAllList.class));
                //Toast.makeText(this, "Coming Soon ", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
           // syncUtility.sync();
            startProgress();

        }

    }

    int starterCount=0;
    public void startProgress() {
        syncUtility.sync();
        if(pd!=null && pd.isShowing())
            pd.hide();

            pd = new ProgressDialog(this);
            pd.setMessage("Visit Scheduling pending... Please Wait");
            pd.setCancelable(false);
            pd.show();
            starterCount++;
            checkDialogue();
    }

    void checkDialogue()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pd.hide();
                        }
                    });
                    //startProgress();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
