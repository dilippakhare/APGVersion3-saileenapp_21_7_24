package com.apgautomation.ui.taskmgmt.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apgautomation.HomePage;
import com.apgautomation.R;
import com.apgautomation.model.GsonToDoDetails;
import com.apgautomation.model.GsonTodo;
import com.apgautomation.utility.CommonShare;
import com.apgautomation.utility.serverutility.AsyncUtilities;
import com.apgautomation.utility.serverutility.DownloadUtility;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmResults;

public class TaskDetails extends AppCompatActivity implements DownloadUtility {

  GsonTodo model;
  Realm realm;
  LinearLayout  linearActivity;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_task_details);
    getSupportActionBar().setHomeButtonEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle("Task Details");
    realm=Realm.getDefaultInstance();
    String token=getIntent().getExtras().getString("Token");
    model= realm.where(GsonTodo.class).equalTo("Token",token).findFirst();

    refresh();
  }
  private void refresh()
  {
    String url= CommonShare.url+"Service1.svc/GetTodotaskDetails?Token="+model.getToken();
    AsyncUtilities utilities=new AsyncUtilities(this,false,url,"",1,this);
    utilities.execute();
  }
  @Override
  protected void onResume() {
    super.onResume();
    init();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    if(model.getEnterById()==CommonShare.getEmpId(this))
    {
      getMenuInflater().inflate(R.menu.menueditdelete,menu);
    }
    return super.onCreateOptionsMenu(menu);
  }

  void  init()
  {
    TextView title,desc,txtStatus,txtDueDate,txtAssignTo,txtCreatedBy;
    Button btnAttchment;
    Button btnClose;
    btnClose=findViewById(R.id.btnClose);
    if(model.getEnterById()==CommonShare.getEmpId(this))
    {
      if(model.getStatus().equalsIgnoreCase("Closed"))
        btnClose.setVisibility(View.GONE);

      btnClose.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          AlertDialog.Builder alert=new AlertDialog.Builder(TaskDetails.this);
          alert.setMessage("Do you want to close this task?");
          alert.setTitle("Close Task");
          alert.setPositiveButton("Close Task", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              realm.beginTransaction();
              model.setStatus("Closed");
              model.setModified(true);
              realm.copyToRealmOrUpdate(model);
              realm.commitTransaction();
              startActivity(new Intent(TaskDetails.this, HomePage.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
          });
          alert.setNegativeButton("No",null);
          alert.show();
        }
      });
    }
    else
    {
      btnClose.setVisibility(View.GONE);
    }
    TextView txtNumber=findViewById(R.id.txtNumber);
    txtNumber.setText(model.getToDoId()+"");
    title=findViewById(R.id.title);
    desc=findViewById(R.id.desc);
    txtStatus=findViewById(R.id.txtStatus);
    txtDueDate=findViewById(R.id.txtDueDate);
    txtAssignTo=findViewById(R.id.txtAssignTo);
    txtCreatedBy=findViewById(R.id.txtCreatedBy);
    btnAttchment=findViewById(R.id.btnAttchment);

    title.setText(model.getTitle());
    desc.setText(model.getDescription());
    txtDueDate.setText(model.getDueDate());
    txtStatus.setText(model.getStatus());

    TextView txtRepeated=findViewById(R.id.txtRepeated);
    TextView txtPrioirty=findViewById(R.id.txtPrioirty);


    String str="";
    try
    {
      for(int i=1;i<model.getEmpids().split(",").length;i++)
      {
        if(str.equalsIgnoreCase(""))
        {
          str= CommonShare.empMap.get( Integer.parseInt(model.getEmpids().split(",")[i])).EmpName;
        }
        else
        {
          str=str+","+CommonShare.empMap.get( Integer.parseInt(model.getEmpids().split(",")[i])).EmpName;
        }
      }
    }
    catch (Exception ex){}
    txtAssignTo.setText(str);
    txtCreatedBy.setText(model.getEnterByName()+" on "+model.getEnterDate());
    if(model.getAttchment()==null || model.getAttchment().length()<5)
    {
      btnAttchment.setVisibility(View.GONE);
    }
    else
    {
      btnAttchment.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(  model.getAttchment().replace("~",CommonShare.url))));
        }
      });
    }

    findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        startActivity(new Intent(TaskDetails.this,Note.class).putExtra("Token",model.getToken()));
      }
    });
    txtPrioirty.setText(model.getPriority());
    txtRepeated.setText(model.isRepeated()+"");
    addDetails();
  }
  void addDetails()
  {
    linearActivity=findViewById(R.id.linearActivity);
    linearActivity.removeAllViews();
    RealmResults<GsonToDoDetails>  list= realm.where(GsonToDoDetails.class).equalTo("Token",model.getToken()).findAll();

    LayoutInflater inflater=getLayoutInflater();
    for (GsonToDoDetails item:list)
    {
      try {
        View v = inflater.inflate(R.layout.item_tododetails, null);
        TextView txtNote, txtMark, txtdate;
        txtNote = v.findViewById(R.id.txtNote);
        txtMark = v.findViewById(R.id.txtMark);
        txtdate = v.findViewById(R.id.txtdate);
        TextView txtBy = v.findViewById(R.id.txtBy);
        if (item.getComplete()) {
          txtMark.setText(item.getEnterBy() + " has marked this task as complete");
        } else {
          txtMark.setVisibility(View.GONE);
        }
        txtNote.setText(item.getTaskDetails());
        txtdate.setText(item.getEnterDate());
        txtBy.setText(item.getEnterBy());
        linearActivity.addView(v);
      }
      catch (Exception ex){}
    }

  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home)
      onBackPressed();
    if(item.getItemId()==R.id.action_edit)
    {
      startActivity(new Intent(this, EditTask.class).putExtra("Token",model.getToken()));

    }
    if(item.getItemId()==R.id.action_delete)
    {
      AlertDialog.Builder alert=new AlertDialog.Builder(TaskDetails.this);
      alert.setMessage("Do you want to delete this task?");
      alert.setTitle("Delete Task");
      alert.setPositiveButton("Delete Task", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {

            String url=CommonShare.url+"Service1.svc/DeleteTodo?Token="+model.getToken();
            AsyncUtilities utilities=new AsyncUtilities(TaskDetails.this,false,url,null,126,TaskDetails.this);
            utilities.execute();
        }
      });
      alert.setNegativeButton("No",null);
      alert.show();

    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onComplete(String str, int requestCode, int responseCode) {

      if(requestCode==1) {
          try {
              Gson gson = new Gson();
              JSONArray array = new JSONArray(str);
              for (int i = 0; i < array.length(); i++) {
                  GsonToDoDetails m = gson.fromJson(array.getJSONObject(i).toString(), GsonToDoDetails.class);
                  realm.beginTransaction();
                  realm.copyToRealmOrUpdate(m);
                  realm.commitTransaction();
              }
              init();
          } catch (Exception ewx) {
          }
      }
      if(requestCode==126)
      {
          if(responseCode==200)
          {
              try
              {
                  JSONObject j=new JSONObject(str.trim());
                  Gson gson=new Gson();
                  GsonTodo t=  gson.fromJson(j.toString(),GsonTodo.class);
                  if(t.getToken()!=null)
                  {
                      realm.beginTransaction();
                      realm.copyToRealmOrUpdate(t);
                      realm.commitTransaction();
                  }
              }
              catch (Exception ex)
              {}
              Toast.makeText(this, "Task Deleted", Toast.LENGTH_SHORT).show();
              startActivity(new Intent(this,TaskMgmt.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
          }
      }
  }
}
