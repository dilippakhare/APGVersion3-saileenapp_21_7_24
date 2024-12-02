package com.apgautomation.ui.salesvisit;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.apgautomation.R;
import com.apgautomation.model.AreaMaster;
import com.apgautomation.utility.CommonShare;

import java.util.ArrayList;

public class CreateNewCustomer extends AppCompatActivity implements View.OnClickListener {

    EditText edGroup,edCustomer;
    Spinner spArea;
    Button btnSave;
    ArrayList<AreaMaster> areas=new ArrayList<>();
    boolean IsSync=false;
    public  static  String GroupName,CustomerName;
    public static int AreaId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_customer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        edGroup=findViewById(R.id.edGroup);
        edCustomer=findViewById(R.id.edCustomer);
        spArea=findViewById(R.id.spArea);

        btnSave=findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        areas= CommonShare.getArea(this);
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,areas);
        spArea.setAdapter(adapter);

        try
        {
          String groupName= getIntent().getExtras().getString("GroupName");
          if(!groupName.equalsIgnoreCase("Select Group"))
          {
              edGroup.setEnabled(false);
              edGroup.setText(groupName);
          }
        }
        catch (Exception ex)
        {}
        try
        {
            IsSync=getIntent().getExtras().getBoolean("IsSync");
        }
        catch (Exception ex){}
    }

    @Override
    public void onClick(View view) {

        if(edGroup.getText().toString().trim().equals(""))
        {
            Toast.makeText(this,"Enter Group",Toast.LENGTH_LONG).show();
            return;
        }
        if(edCustomer.getText().toString().trim().equals(""))
        {
            Toast.makeText(this,"Enter Customer Name",Toast.LENGTH_LONG).show();
            return;
        }

                 GroupName = edGroup.getText().toString();
                 CustomerName = edCustomer.getText().toString();
                 AreaId = ((AreaMaster) spArea.getSelectedItem()).AreaId;
                 setResult(RESULT_OK);
                 finish();

    }
}
